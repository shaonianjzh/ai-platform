package com.shaonian.project.websocket;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shaonian.project.model.entity.User;
import com.shaonian.project.model.entity.UserModel;
import com.shaonian.project.service.UserModelService;
import com.shaonian.project.service.UserService;
import com.shaonian.project.util.SpringContextUtils;
import com.unfbx.sparkdesk.entity.AIChatRequest;
import com.unfbx.sparkdesk.entity.AIChatResponse;
import com.unfbx.sparkdesk.entity.Text;
import com.unfbx.sparkdesk.entity.Usage;
import com.unfbx.sparkdesk.listener.ChatListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 少年
 */
@Slf4j
public class XFChatListener extends ChatListener {

    /**
     * 用户会话
     */
    private Session session;

    /**
     * 每次对话的唯一id
     */
    private Long userModelId;


    private static StringBuffer str = new StringBuffer();


    private static UserModelService userModelService;

    private static UserService userService;



    public XFChatListener(AIChatRequest aiChatRequest, Session session, Long userModelId) {
        super(aiChatRequest);
        this.session = session;
        this.userModelId = userModelId;
        userModelService = SpringContextUtils.getBean(UserModelService.class);
        userService = SpringContextUtils.getBean(UserService.class);
    }

    /**
     * 异常回调
     * @param aiChatResponse
     */
    @SneakyThrows
    @Override
    public void onChatError(AIChatResponse aiChatResponse) {
        log.warn(String.valueOf(aiChatResponse));

        UserModel userModel = new UserModel();
        userModel.setId(userModelId).setStatus("failed").setExecMessage(aiChatResponse.getPayload().getChoices().getText().toString());
        userModelService.updateById(userModel);
    }

    /**
     * 输出回调
     * @param aiChatResponse
     */
    @Override
    @SneakyThrows
    public void onChatOutput(AIChatResponse aiChatResponse) {
        log.info("AI返回的数据 content:{}",aiChatResponse);
        List<Text> text = aiChatResponse.getPayload().getChoices().getText();
        //发送给前端
        session.getBasicRemote().sendText(JSONUtil.toJsonStr(text));
        //收集AI返回的结果
        str.append(text.stream().map(Text::getContent).collect(Collectors.toList()));

    }

    /**
     *会话结束回调
     */
    @Override
    public void onChatEnd() {
        log.info("当前会话结束了");
        log.info("将AI回答的信息存入数据库{}",userModelId);

        //会话结束，在数据库中更新本次对话记录，并清理缓存结果
        UserModel userModel = new UserModel();
        userModel.setId(userModelId).setStatus("success").setGenResult(str.toString()).setExecMessage("执行成功");
        userModelService.updateById(userModel);

        //调用成功 次数减一
        UserModel userModel1 = userModelService.getById(userModelId);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",userModel1.getUserId());
        updateWrapper.setSql("callNum = callNum-1");
        userService.update(updateWrapper);

        //清理缓存
        str.setLength(0);
    }

    /**
     *会话结束 获取token使用信息回调
     */
    @Override
    public void onChatToken(Usage usage) {
        log.info("token 信息:{}",usage);
    }
}
