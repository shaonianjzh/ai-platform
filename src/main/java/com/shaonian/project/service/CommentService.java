package com.shaonian.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaonian.project.model.dto.comment.CommentQueryRequest;
import com.shaonian.project.model.entity.Comment;
import com.shaonian.project.model.vo.CommentVO;

/**
* @author 少年
* @description 针对表【comment】的数据库操作Service
* @createDate 2023-08-27 12:04:23
*/
public interface CommentService extends IService<Comment> {


    /**
     * 根据用户账号分页获取评论信息
     * @param commentQueryRequest
     * @return
     */
    Page<CommentVO> pageCommentVO(Page<CommentVO> page,CommentQueryRequest commentQueryRequest);

}
