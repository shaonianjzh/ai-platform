package com.shaonian.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户创建请求
 *
 * @author 少年
 */
@Data
public class UserAddRequest implements Serializable {


    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;


    /**
     * 用户角色：user/admin/vip/ban
     */
    private String userRole;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 接口调用次数 默认有50次
     */
    private Integer callNum;

    /**
     * 到期时间
     */
    private Date expireTime;

    /**
     * vip类型
     */
    private String vipType;

    private static final long serialVersionUID = 1L;
}