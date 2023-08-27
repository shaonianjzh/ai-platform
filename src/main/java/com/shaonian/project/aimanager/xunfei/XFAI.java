package com.shaonian.project.aimanager.xunfei;

import com.unfbx.sparkdesk.SparkDeskClient;
import com.unfbx.sparkdesk.constant.SparkDesk;
import com.unfbx.sparkdesk.entity.*;
import lombok.Data;
import okhttp3.WebSocket;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * 讯飞AI
 * @author 少年
 */
@ConfigurationProperties(prefix = "ai.xunfei")
@Component
@Data
public class XFAI {

    private String appId;

    private String apiKey;

    private String apiSecret;

    /**
     * AI 对话
     */
    public void doChat(){
        SparkDeskClient client = SparkDeskClient.builder()
                .host(SparkDesk.SPARK_API_HOST_WS_V2_1)
                .appid(appId)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();

        //构建请求参数s
        InHeader header = InHeader.builder().uid(UUID.randomUUID().toString().substring(0, 10)).appid("44419ec1").build();
        Parameter parameter = Parameter.builder().chat(Chat.builder().domain("generalv2").maxTokens(2048).temperature(0.3).build()).build();
        List<Text> text = new ArrayList<>();
        text.add(Text.builder().role(Text.Role.USER.getName()).content("使用md文档格式写出一个三行三列的表格，表头包含：姓名，性别，爱好。数据随机即可。").build());
        InPayload payload = InPayload.builder().message(Message.builder().text(text).build()).build();
        AIChatRequest aiChatRequest = AIChatRequest.builder().header(header).parameter(parameter).payload(payload).build();

        WebSocket chat = client.chat(new XFChatListener(aiChatRequest));

    }

}
