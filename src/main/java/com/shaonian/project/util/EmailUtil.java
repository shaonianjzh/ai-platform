package com.shaonian.project.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 少年
 */
@Component
public class EmailUtil {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Resource
    private JavaMailSender javaMailSender;

    private static final String subject = "AI平台验证码";

    public void sendCode(String email,String code){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject(subject);
        message.setText("您好，您注册AI平台的验证码为 "+code+" 请再五分钟内验证。若不是本人注册，请忽略此消息");
        javaMailSender.send(message);
    }
}
