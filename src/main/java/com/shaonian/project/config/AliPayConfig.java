package com.shaonian.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 少年
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {
    private String appId;

    private String privateKey;

    private String publicKey;

    private String returnUrl;

    private String notifyUrl;

    private String signType;

    private String charset;

    private String gatewayUrl;
}
