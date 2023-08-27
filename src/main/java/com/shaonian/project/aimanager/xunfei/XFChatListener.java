package com.shaonian.project.aimanager.xunfei;

import com.unfbx.sparkdesk.entity.AIChatRequest;
import com.unfbx.sparkdesk.entity.AIChatResponse;
import com.unfbx.sparkdesk.entity.Usage;
import com.unfbx.sparkdesk.listener.ChatListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 少年
 */
@Slf4j
public class XFChatListener extends ChatListener {

    public XFChatListener(AIChatRequest aiChatRequest) {
        super(aiChatRequest);
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
    public void onChatOutput(AIChatResponse aiChatResponse) {
        log.info("content:{}",aiChatResponse);
    }

    /**
     *会话结束回调
     */
    @Override
    public void onChatEnd() {
        log.info("当前会话结束了");
    }

    /**
     *会话结束 获取token使用信息回调
     */
    @Override
    public void onChatToken(Usage usage) {
        log.info("token 信息:{}",usage);
    }
}
