package com.shaonian.project.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.project.annotation.AuthCheck;
import com.shaonian.project.common.BaseResponse;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.common.ResultUtils;
import com.shaonian.project.constant.UserConstant;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.exception.ThrowUtil;
import com.shaonian.project.model.dto.comment.CommentQueryRequest;
import com.shaonian.project.model.vo.CommentVO;
import com.shaonian.project.service.CommentService;
import com.shaonian.project.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 少年
 */
@RestController
@RequestMapping("/comment")
public class CommentController {


    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    /**
     * 删除评论
     *
     * @param id
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteUser(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = commentService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 分页获取评论列表
     *
     * @param commentQueryRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/list/page")
    public BaseResponse<Page<CommentVO>> listUserByPage(@RequestBody CommentQueryRequest commentQueryRequest, HttpServletRequest request) {
        if (commentQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();

        //防止爬虫
        ThrowUtil.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<CommentVO> commentVOPage = commentService.pageCommentVO(new Page<>(current,size),commentQueryRequest);
        return ResultUtils.success(commentVOPage);
    }
}
