package com.shaonian.project.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.exception.BusinessException;
import com.shaonian.project.mapper.CommentMapper;
import com.shaonian.project.model.dto.comment.CommentQueryRequest;
import com.shaonian.project.model.entity.Comment;
import com.shaonian.project.model.vo.CommentVO;
import com.shaonian.project.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 少年
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2023-08-27 12:04:23
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Resource
    private CommentMapper commentMapper;

    @Override
    public Page<CommentVO> pageCommentVO(Page<CommentVO> page,CommentQueryRequest commentQueryRequest) {
        if(commentQueryRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return page.setRecords(commentMapper.pageCommentVO(page,commentQueryRequest.getUserAccount()));
    }
}




