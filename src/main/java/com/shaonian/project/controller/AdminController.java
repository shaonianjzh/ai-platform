package com.shaonian.project.controller;


import com.shaonian.project.common.BaseResponse;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.common.ResultUtils;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.model.dto.user.AdminLoginRequest;
import com.shaonian.project.model.entity.User;
import com.shaonian.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * 管理员接口
 * @author 少年
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public BaseResponse login(@RequestBody AdminLoginRequest adminLoginRequest, HttpServletRequest request){
        if(adminLoginRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String adminAccount = adminLoginRequest.getUsername();
        String adminPassword = adminLoginRequest.getPassword();
        if(StringUtils.isAnyBlank(adminAccount,adminPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码错误");
        }
        User user = userService.userLogin(adminAccount, adminPassword,request);
        return ResultUtils.success(user);
    }

}
