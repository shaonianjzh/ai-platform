package com.shaonian.project.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaonian.project.mapper.UserModelMapper;
import com.shaonian.project.model.dto.usermodel.UserModelQueryRequest;
import com.shaonian.project.model.entity.UserModel;
import com.shaonian.project.model.vo.UserModelVO;
import com.shaonian.project.service.UserModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 少年
* @description 针对表【user_model】的数据库操作Service实现
* @createDate 2023-08-27 12:04:15
*/
@Service
public class UserModelServiceImpl extends ServiceImpl<UserModelMapper, UserModel>
    implements UserModelService{

    @Resource
    private UserModelMapper userModelMapper;

    @Override
    public Page<UserModelVO> pageUserModelVO(Page<UserModelVO> page, UserModelQueryRequest userModelQueryRequest) {
        return page.setRecords(userModelMapper.pageUserModelVO(page,userModelQueryRequest));
    }
}




