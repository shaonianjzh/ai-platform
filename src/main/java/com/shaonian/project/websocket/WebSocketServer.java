package com.shaonian.project.websocket;

import cn.hutool.json.JSONUtil;
import com.shaonian.project.model.entity.ChatModel;
import com.shaonian.project.model.enums.DomainEnum;
import com.shaonian.project.service.ChatModelService;
import com.shaonian.project.service.UserService;
import com.unfbx.sparkdesk.SparkDeskClient;
import com.unfbx.sparkdesk.entity.*;
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

    private Long userId;

    private Long chatModelId;


    private static SparkDeskClient sparkDeskClient;

    private static StringRedisTemplate stringRedisTemplate;

    private static ChatModelService chatModelService;

    private static UserService userService;

    @Autowired
    public void setService(SparkDeskClient sparkDeskClient, StringRedisTemplate stringRedisTemplate, ChatModelService chatModelService, UserService userService) {
        WebSocketServer.sparkDeskClient = sparkDeskClient;
        WebSocketServer.stringRedisTemplate = stringRedisTemplate;
        WebSocketServer.chatModelService = chatModelService;
        WebSocketServer.userService = userService;
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
    public void onOpen(Session session, @PathParam("userId") Long userId,@PathParam("chatModelId") Long chatModelId) {
        this.session = session;
        this.userId = userId;
        this.chatModelId = chatModelId;
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
    public void onError(Session session, Throwable error) {
        log.info("[连接ID:{}] 错误原因:{}", this.userId, error.getMessage());
        error.printStackTrace();
    }

    /**
     * 接收到客户端消息
     * @param msg
     */
    @OnMessage
    public void onMessage(String msg) {
        log.info("[连接ID:{}] 收到消息:{}", this.userId, msg);
        //TODO 限流
        // TODO 校验用户的合法性，是否有调用次数
        //TODO 消息队列异步处理,优化

        //根据chatModelId获取模型预设
        ChatModel chatModel = chatModelService.getById(chatModelId);
        String prompt = chatModel.getPrompt();

        //从缓存中获取最近的聊天记录
        List<Text> textList = new ArrayList<>();
        //查询redis缓存最近15条的历史消息
        String messagesContext = stringRedisTemplate.opsForValue().get(userId+":"+chatModelId);
        if(StringUtils.isNotBlank(messagesContext)){
            textList = JSONUtil.toList(messagesContext, Text.class);
            if (textList.size() >= 10) {
                textList = textList.subList(1, 10);
            }
        }else{
            textList.add(Text.builder().role(Text.Role.USER.getName()).content(prompt).build());
        }
        textList.add(Text.builder().role(Text.Role.USER.getName()).content(msg).build());
        //调用AI接口
        sparkDeskClient.chat(new XFChatListener(getAIChatRequest(this.userId.toString(), 0.3, textList),session,chatModelId));

        //存入缓存中对话记录  重新设置过期时间为三天
        stringRedisTemplate.opsForValue().set(userId+":"+chatModelId,JSONUtil.toJsonStr(textList));
        stringRedisTemplate.expire(String.valueOf(userId),3, TimeUnit.DAYS);

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
