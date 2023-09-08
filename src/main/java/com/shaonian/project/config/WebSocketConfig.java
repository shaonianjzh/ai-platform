package com.shaonian.project.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author 少年
 */
@Configuration
public class WebSocketConfig {
    /**
     * 这个bean的注册,用于扫描带有@ServerEndpoint的注解成为websocket,如果你使用外置的tomcat就不需要该配置文件
     */
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }
}