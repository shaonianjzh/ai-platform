package com.shaonian.project.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.mapper.ChatModelMapper;
import com.shaonian.project.model.entity.ChatModel;
import com.shaonian.project.service.ChatModelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 少年
* @description 针对表【chat_model】的数据库操作Service实现
* @createDate 2023-08-27 12:04:26
*/
@Service
public class ChatModelServiceImpl extends ServiceImpl<ChatModelMapper, ChatModel>
    implements ChatModelService{

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
}




