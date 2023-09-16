package com.shaonian.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 少年
 */
@Configuration
@ConfigurationProperties(prefix = "jwt.config")
@Data
public class JwtConfig {
    /**
     * jwt 加密 key，默认值：shaoniancode
     */
    private String key;

    /**
     * jwt 过期时间，默认值：600000 {@code 三天}.
     */
    private Long expireTime;
}
