package com.shaonian.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaonian.project.model.dto.usermodel.UserModelQueryRequest;
import com.shaonian.project.model.entity.UserModel;
import com.shaonian.project.model.vo.UserChatVO;
import com.shaonian.project.model.vo.UserModelVO;

import java.util.List;

/**
* @author 少年
* @description 针对表【user_model】的数据库操作Service
* @createDate 2023-08-27 12:04:15
*/
public interface UserModelService extends IService<UserModel> {

    /**
     * 分页查询用户对话记录
     * @param page
     * @param userModelQueryRequest
     * @return
     */
    Page<UserModelVO> pageUserModelVO(Page<UserModelVO> page, UserModelQueryRequest userModelQueryRequest);

    /**
     * 获取模型调用数量
     * @return
     */
    List<UserChatVO> getModelCount();

}
