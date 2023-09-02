package com.shaonian.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaonian.project.model.dto.usermodel.UserModelQueryRequest;
import com.shaonian.project.model.entity.UserModel;
import com.shaonian.project.model.vo.UserModelVO;

/**
* @author 少年
* @description 针对表【user_model】的数据库操作Service
* @createDate 2023-08-27 12:04:15
*/
public interface UserModelService extends IService<UserModel> {

    Page<UserModelVO> pageUserModelVO(Page<UserModelVO> page, UserModelQueryRequest userModelQueryRequest);
}
