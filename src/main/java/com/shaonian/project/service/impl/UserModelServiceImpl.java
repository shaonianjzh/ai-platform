package com.shaonian.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaonian.project.model.entity.UserModel;
import com.shaonian.project.service.UserModelService;
import com.shaonian.project.mapper.UserModelMapper;
import org.springframework.stereotype.Service;

/**
* @author 少年
* @description 针对表【user_model】的数据库操作Service实现
* @createDate 2023-08-27 12:04:15
*/
@Service
public class UserModelServiceImpl extends ServiceImpl<UserModelMapper, UserModel>
    implements UserModelService{

}




