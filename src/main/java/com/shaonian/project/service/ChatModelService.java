package com.shaonian.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaonian.project.model.dto.chatmodel.ChatModelQueryRequest;
import com.shaonian.project.model.entity.ChatModel;

import javax.servlet.http.HttpServletRequest;

/**
* @author 少年
* @description 针对表【chat_model】的数据库操作Service
* @createDate 2023-08-27 12:04:26
*/
public interface ChatModelService extends IService<ChatModel> {

    void validChatModel(ChatModel chatModel, boolean b);

    /**
     * 获取查询条件
     * @param chatModelQueryRequest
     * @return
     */
    QueryWrapper<ChatModel> getQueryWrapper(ChatModelQueryRequest chatModelQueryRequest, HttpServletRequest request);
}
