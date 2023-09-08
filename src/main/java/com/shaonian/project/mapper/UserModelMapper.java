package com.shaonian.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.project.model.dto.usermodel.UserModelQueryRequest;
import com.shaonian.project.model.entity.UserModel;
import com.shaonian.project.model.vo.UserChatVO;
import com.shaonian.project.model.vo.UserModelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 少年
* @description 针对表【user_model】的数据库操作Mapper
* @createDate 2023-08-27 12:04:15
* @Entity com.shaonian.project.model.entity.UserModel
*/
public interface UserModelMapper extends BaseMapper<UserModel> {


    /**
     * 分页获取用户调用记录
     * @param page
     * @param userModelQueryRequest
     * @return
     */
    List<UserModelVO> pageUserModelVO(Page<UserModelVO> page,@Param("request") UserModelQueryRequest userModelQueryRequest);

    /**
     * 获取模型及对应的调用数量
     * @return
     */
    List<UserChatVO> getModelCount();
}




