package com.shaonian.project.websocket;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.mapper.UserMapper;
import com.shaonian.project.model.entity.ChatModel;
import com.shaonian.project.model.entity.User;
import com.shaonian.project.model.entity.UserModel;
import com.shaonian.project.model.enums.DomainEnum;
import com.shaonian.project.service.ChatModelService;
import com.shaonian.project.service.RedisLimiter;
import com.shaonian.project.service.UserModelService;
import com.unfbx.sparkdesk.SparkDeskClient;
import com.unfbx.sparkdesk.entity.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author 少年
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/chat/user/{userId}/{chatModelId}")
public class WebSocketServer {


    //在线总数
    private static int onlineCount;

    private Session session;


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 模型id
     */
    private Long modelId;


    private static SparkDeskClient sparkDeskClient;

    private static StringRedisTemplate stringRedisTemplate;

    private static ChatModelService chatModelService;

    private static UserModelService userModelService;

    private static RedisLimiter redisLimiter;

    private static UserMapper userMapper;


    @Autowired
    public void setService(SparkDeskClient sparkDeskClient, StringRedisTemplate stringRedisTemplate, ChatModelService chatModelService, UserMapper userMapper,UserModelService userModelService,RedisLimiter redisLimiter) {
        WebSocketServer.sparkDeskClient = sparkDeskClient;
        WebSocketServer.stringRedisTemplate = stringRedisTemplate;
        WebSocketServer.chatModelService = chatModelService;
        WebSocketServer.userMapper = userMapper;
        WebSocketServer.userModelService = userModelService;
        WebSocketServer.redisLimiter = redisLimiter;
    }

    /**
     *用来存放每个客户端对应的WebSocketServer对象
     */
    private static ConcurrentHashMap<Long,WebSocketServer> webSocketMap = new ConcurrentHashMap<Long,WebSocketServer>();


    /**
     * 建立连接
     * @param session
     * @param userId
     */
    @OnOpen
    @SneakyThrows
    public void onOpen(Session session, @PathParam("userId") Long userId,@PathParam("chatModelId") Long chatModelId) {
        this.session = session;
        this.userId = userId;
        this.modelId = chatModelId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
        } else {
            webSocketMap.put(userId, this);
            addOnlineCount();
        }
        log.info("[连接ID:{}] 建立连接, 当前连接数:{}", this.userId, getOnlineCount());
    }

    /**
     * 断开连接
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            subOnlineCount();
        }
        log.info("[连接ID:{}] 断开连接, 当前连接数:{}", userId, getOnlineCount());
    }

    /**
     * 发送错误
     * @param session
     * @param error
     */
    @OnError
    @SneakyThrows
    public void onError(Session session, Throwable error) {
        log.info("[连接ID:{}] 错误原因:{}", this.userId, error.getMessage());
        session.getBasicRemote().sendText(error.getMessage());
    }

    /**
     * 接收到客户端消息
     * @param msg
     */
    @OnMessage
    @SneakyThrows
//    @Transactional(rollbackFor = Exception.class)
    public void onMessage(String msg) {
        log.info("[连接ID:{}] 收到消息:{}", this.userId, msg);
        //校验用户的合法性，是否有调用次数
        User user = validateUser(this.userId);
        //限流
        redisLimiter.doRateLimit(String.valueOf(userId));

        //TODO 消息队列异步处理,优化

        //根据chatModelId获取模型预设
        ChatModel chatModel = chatModelService.getById(modelId);
        String prompt = chatModel.getPrompt();

        //从缓存中获取最近的聊天记录
        List<Text> textList = new ArrayList<>();
        //本地缓存
        //String messagesContext = (String) LocalCache.CACHE.get(userId + ":" + modelId);
        //查询redis缓存历史对话记录
        String messagesContext = stringRedisTemplate.opsForValue().get(userId+":"+modelId);
        log.info("发送的数据：{}",messagesContext);
        if(StringUtils.isNotBlank(messagesContext)){
            textList = JSONUtil.toList(messagesContext, Text.class);
            if (textList.size() >= 5) {
                textList = textList.subList(0, 5);
                System.out.println(textList);
            }
        }else{
            textList.add(Text.builder().role(Text.Role.USER.getName()).content(prompt).build());
        }
        textList.add(Text.builder().role(Text.Role.USER.getName()).content(msg).build());

        //生成用户每次的对话id
        Snowflake snowflake = IdUtil.getSnowflake(1,1);
        long userModelId = snowflake.nextId();

        //调用AI接口
        sparkDeskClient.chat(new XFChatListener(getAIChatRequest(this.userId.toString(), 0.3, textList),session,userModelId));

        //将每次的对话信息存入数据库
        UserModel userModel = new UserModel();
        userModel.setId(userModelId)
                .setUserId(userId).setModelId(modelId)
                .setChatData(msg).setStatus("running");
        userModelService.save(userModel);


        //从数据库中查询最近5次对话记录更新缓存
        QueryWrapper<UserModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        queryWrapper.eq("modelId",modelId);
        queryWrapper.orderByDesc("createTime");
        List<UserModel> userModels = userModelService.page(new Page<>(1, 5), queryWrapper).getRecords();
        List<Text> historyText = getTextList(userModels);
        if(CollectionUtil.isNotEmpty(historyText)){
            historyText.set(0,Text.builder().role(Text.Role.USER.getName()).content(prompt).build());
        }

        //缓存最近10条对话记录  重新设置过期时间为三天
        stringRedisTemplate.opsForValue().set(userId+":"+modelId,JSONUtil.toJsonStr(historyText));
        stringRedisTemplate.expire(String.valueOf(userId),3, TimeUnit.DAYS);
        //本地缓存
        //LocalCache.CACHE.put(userId+":"+modelId,JSONUtil.toJsonStr(historyText));
    }


    /**
     * 根据用户对话记录拼接 Text
     * @param userModels
     * @return
     */
    private List<Text> getTextList(List<UserModel> userModels){
        List<Text> textList = new ArrayList<>();
        for(UserModel userModel:userModels){
            textList.add(Text.builder().role(Text.Role.USER.getName()).content(userModel.getChatData()).build());
            textList.add(Text.builder().role(Text.Role.ASSISTANT.getName()).content(userModel.getGenResult()).build());
        }
        return textList;
    }


    /**
     * 校验用户合法性
     * @param userId
     */
    private User validateUser(Long userId){
        User user = userMapper.selectById(this.userId);
        if(user==null){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"不是本平台用户，禁止访问");
        }
        if(user.getCallNum()<=0){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"调用次数不够，请充值");
        }
        return user;
    }

    /**
     * 获取当前连接数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 当前连接数加一
     */
    public static synchronized void addOnlineCount() {
        if(onlineCount>50){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"当前在线人数过多，请稍后访问");
        }
        WebSocketServer.onlineCount++;
    }

    /**
     * 当前连接数减一
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }


    /**
     * 构造AIChatRequest
     * @param UUID   每个用户的id，用于区分不同用户
     * @param temperature 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高
     * @param textList
     * @return
     */
    public AIChatRequest getAIChatRequest(String UUID, double temperature, List<Text> textList){
        InHeader header = InHeader.builder().uid(UUID).appid(sparkDeskClient.getAppid()).build();
        Parameter parameter = Parameter.builder().chat(Chat.builder().domain(DomainEnum.GENERAL.getName()).maxTokens(2048).temperature(temperature).build()).build();
        InPayload payload = InPayload.builder().message(Message.builder().text(textList).build()).build();
        return AIChatRequest.builder().header(header).parameter(parameter).payload(payload).build();
    }
}
