package com.shaonian.project.config;

import com.unfbx.sparkdesk.SparkDeskClient;
import com.unfbx.sparkdesk.constant.SparkDesk;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 讯飞AI
 *
 * @author 少年
 */
@ConfigurationProperties(prefix = "ai.xunfei")
@Component
@Data
public class XFAIConfig {

    private String appId;

    private String apiKey;

    private String apiSecret;


    @Bean
    public SparkDeskClient sparkDeskClient() {
        return SparkDeskClient.builder()
                .host(SparkDesk.SPARK_API_HOST_WS_V1_1)
                .appid(appId)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();
    }

}
