package com.shaonian.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author 少年
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String email;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String code;
}
