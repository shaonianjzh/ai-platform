package com.shaonian.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.project.annotation.AuthCheck;
import com.shaonian.project.common.BaseResponse;
import com.shaonian.project.common.DeleteRequest;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.common.ResultUtils;
import com.shaonian.project.constant.UserConstant;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.model.dto.usermodel.UserModelQueryRequest;
import com.shaonian.project.model.entity.User;
import com.shaonian.project.model.entity.UserModel;
import com.shaonian.project.model.vo.UserModelVO;
import com.shaonian.project.service.UserModelService;
import com.shaonian.project.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 少年
 */
@RestController
@RequestMapping("/userModel")
public class UserModelController {

    @Resource
    private UserModelService userModelService;

    @Resource
    private UserService userService;



    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteChatModel(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserModel oldUserModel = userModelService.getById(id);
        if (oldUserModel == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldUserModel.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userModelService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserModelVO>> listUserModelVOByPage(@RequestBody UserModelQueryRequest userModelQueryRequest){
        if (userModelQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userModelQueryRequest.getCurrent();
        long pageSize = userModelQueryRequest.getPageSize();
        // 限制爬虫
        if (pageSize > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Page<UserModelVO> userModelVoPage = userModelService.pageUserModelVO(new Page<>(current,pageSize),userModelQueryRequest);

        return ResultUtils.success(userModelVoPage);

    }
}
