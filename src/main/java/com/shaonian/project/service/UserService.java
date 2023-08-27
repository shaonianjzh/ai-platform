package com.shaonian.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shaonian.project.common.BaseResponse;
import com.shaonian.project.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author 少年
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param email 邮箱
     * @param code 邮箱验证码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String email,String code);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    BaseResponse sendCode(String email);
}
