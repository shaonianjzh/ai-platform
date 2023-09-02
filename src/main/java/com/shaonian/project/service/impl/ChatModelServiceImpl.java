package com.shaonian.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.constant.CommonConstant;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.mapper.ChatModelMapper;
import com.shaonian.project.model.dto.chatmodel.ChatModelQueryRequest;
import com.shaonian.project.model.entity.ChatModel;
import com.shaonian.project.model.entity.User;
import com.shaonian.project.service.ChatModelService;
import com.shaonian.project.service.UserService;
import com.shaonian.project.util.SqlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author 少年
* @description 针对表【chat_model】的数据库操作Service实现
* @createDate 2023-08-27 12:04:26
*/
@Service
public class ChatModelServiceImpl extends ServiceImpl<ChatModelMapper, ChatModel>
    implements ChatModelService{

    @Resource
    private UserService userService;

    @Override
    public void validChatModel(ChatModel chatModel, boolean b) {
        if(chatModel==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = chatModel.getName();
        String prompt = chatModel.getPrompt();
        String img = chatModel.getImg();
        Integer categoryId = chatModel.getCategoryId();

        if(StringUtils.isBlank(name)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"模型名称不能为空");
        }
        if(StringUtils.isNotBlank(prompt)&&prompt.length()>1000){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"prompt 字数超过限制");
        }
        if(categoryId==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请选择模型的分类");
        }
    }
    @Override
    public QueryWrapper<ChatModel> getQueryWrapper(ChatModelQueryRequest chatModelQueryRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (chatModelQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String name = chatModelQueryRequest.getName();
        Integer categoryId = chatModelQueryRequest.getCategoryId();
        Integer isOpen = chatModelQueryRequest.getIsOpen();
        String sortField = chatModelQueryRequest.getSortField();
        String sortOrder = chatModelQueryRequest.getSortOrder();
        QueryWrapper<ChatModel> queryWrapper = new QueryWrapper<>();


        queryWrapper.eq(isOpen!=null,"isOpen", isOpen);
        //普通用户不能查看未公开的模型
        if(loginUser.getUserRole().equals("user")) {
            queryWrapper.eq("isOpen",1);
        }
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(categoryId!=null, "categoryId", categoryId);

        queryWrapper.orderBy(SqlUtil.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




