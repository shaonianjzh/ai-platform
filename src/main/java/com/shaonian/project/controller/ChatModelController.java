package com.shaonian.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.project.annotation.AuthCheck;
import com.shaonian.project.common.BaseResponse;
import com.shaonian.project.common.DeleteRequest;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.common.ResultUtils;
import com.shaonian.project.constant.UserConstant;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.model.dto.chatmodel.ChatModelAddRequest;
import com.shaonian.project.model.dto.chatmodel.ChatModelQueryRequest;
import com.shaonian.project.model.dto.chatmodel.ChatModelUpdateRequest;
import com.shaonian.project.model.entity.ChatModel;
import com.shaonian.project.model.entity.User;
import com.shaonian.project.service.ChatModelService;
import com.shaonian.project.service.UserService;
import com.shaonian.project.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 模型接口
 *
 * @author 少年
 */
@RestController
@RequestMapping("/chatModel")
@Slf4j
public class ChatModelController {

    @Resource
    private ChatModelService chatModelService;

    @Resource
    private UserService userService;

    @Resource
    private FileUtil fileUtil;

    // region 增删改查

    /**
     * 创建
     *
     * @param chatModelAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChatModel(ChatModelAddRequest chatModelAddRequest, HttpServletRequest request, MultipartFile file) {
        if(file==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"图片不能为空");
        }
//        上传文件
        String imgUrl = fileUtil.upload(file);
        if (chatModelAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChatModel chatModel = new ChatModel();
        BeanUtils.copyProperties(chatModelAddRequest, chatModel);
        // 校验
        chatModelService.validChatModel(chatModel, true);
        User loginUser = userService.getLoginUser(request);

        QueryWrapper<ChatModel> wrapper = new QueryWrapper();
        wrapper.eq("name",chatModelAddRequest.getName());
        ChatModel one = chatModelService.getOne(wrapper);
        if(one != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"模型名称已存在");
        }
        chatModel.setUserId(loginUser.getId());
//        chatModel.setImg(imgUrl);
        boolean result = chatModelService.save(chatModel);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newChatModelId = chatModel.getId();
        return ResultUtils.success(newChatModelId);
    }

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
        ChatModel oldChatModel = chatModelService.getById(id);
        if (oldChatModel == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldChatModel.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = chatModelService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param chatModelUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateChatModel(ChatModelUpdateRequest chatModelUpdateRequest, HttpServletRequest request,MultipartFile file) {
        User user = userService.getLoginUser(request);
        String imgUrl = null;
        ChatModel chatModel = new ChatModel();
        if(file!=null){
            imgUrl = fileUtil.upload(file);
            chatModel.setImg(imgUrl);
        }
        if (chatModelUpdateRequest == null || chatModelUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        BeanUtils.copyProperties(chatModelUpdateRequest, chatModel);
        // 参数校验
        chatModelService.validChatModel(chatModel, false);
        long id = chatModelUpdateRequest.getId();
        // 判断是否存在
        ChatModel oldChatModel = chatModelService.getById(id);
        if (oldChatModel == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldChatModel.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = chatModelService.updateById(chatModel);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<ChatModel> getChatModelById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChatModel chatModel = chatModelService.getById(id);
        return ResultUtils.success(chatModel);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param chatModelQueryRequest
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<List<ChatModel>> listChatModel(@RequestBody ChatModelQueryRequest chatModelQueryRequest,HttpServletRequest request) {
        if (chatModelQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<ChatModel> chatModelList = chatModelService
                .list(chatModelService.getQueryWrapper(chatModelQueryRequest,request));
        return ResultUtils.success(chatModelList);
    }

    /**
     * 分页获取列表
     *
     * @param chatModelQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ChatModel>> listChatModelByPage(@RequestBody ChatModelQueryRequest chatModelQueryRequest, HttpServletRequest request) {
        if (chatModelQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChatModel chatModelQuery = new ChatModel();
        BeanUtils.copyProperties(chatModelQueryRequest, chatModelQuery);
        long current = chatModelQueryRequest.getCurrent();
        long size = chatModelQueryRequest.getPageSize();
        // content 需支持模糊搜索
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<ChatModel> chatModelPage = chatModelService.page(new Page<>(current, size),
                chatModelService.getQueryWrapper(chatModelQueryRequest,request));
        return ResultUtils.success(chatModelPage);
    }

    // endregion
}
