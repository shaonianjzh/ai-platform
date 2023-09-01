package com.shaonian.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 少年
 */
@Data
public class AdminLoginRequest implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;

    private String username;

    private String password;
}
