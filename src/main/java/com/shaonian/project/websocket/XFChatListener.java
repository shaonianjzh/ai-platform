package com.shaonian.project.websocket;

import com.google.gson.Gson;
import com.shaonian.project.service.ChatModelService;
import com.unfbx.sparkdesk.entity.AIChatRequest;
import com.unfbx.sparkdesk.entity.AIChatResponse;
import com.unfbx.sparkdesk.entity.Usage;
import com.unfbx.sparkdesk.listener.ChatListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.Session;

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
    private Long chatId;

    private static ChatModelService chatModelService;

    @Autowired
    public void setChatModelService(ChatModelService chatModelService) {
        XFChatListener.chatModelService = chatModelService;
    }

    private Gson gson=new Gson();


    public XFChatListener(AIChatRequest aiChatRequest, Session session) {
        super(aiChatRequest);
        this.session = session;
    }

    public XFChatListener(AIChatRequest aiChatRequest, Session session, Long chatId) {
        super(aiChatRequest);
        this.session = session;
        this.chatId = chatId;
    }

    /**
     * 异常回调
     * @param aiChatResponse
     */
    @SneakyThrows
    @Override
    public void onChatError(AIChatResponse aiChatResponse) {
        log.warn(String.valueOf(aiChatResponse));
    }

    /**
     * 输出回调
     * @param aiChatResponse
     */
    @Override
    @SneakyThrows
    public void onChatOutput(AIChatResponse aiChatResponse) {
        log.info("AI返回的数据 content:{}",aiChatResponse);
        session.getBasicRemote().sendText(gson.toJson(aiChatResponse.getPayload().getChoices().getText()));
//        ChatModel chatModel = new ChatModel();
//        chatModel.setId(chatId);
//        chatModelService.save(chatModel);
    }

    /**
     *会话结束回调
     */
    @Override
    public void onChatEnd() {
        log.info("当前会话结束了");
        log.info("将AI回答的信息存入数据库{}",chatId);
    }

    /**
     *会话结束 获取token使用信息回调
     */
    @Override
    public void onChatToken(Usage usage) {
        log.info("token 信息:{}",usage);
    }
}
