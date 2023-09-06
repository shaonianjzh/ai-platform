package com.shaonian.project.model.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户更新请求
 *
 * @author 少年
 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

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
     * 是否删除
     */
    private Integer isDelete;

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
    @JsonFormat(pattern="yyyy-MM-dd" ,timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expireTime;

    /**
     * vip类型
     */
    private String vipType;

    private static final long serialVersionUID = 1L;
}