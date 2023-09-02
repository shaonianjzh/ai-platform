package com.shaonian.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.project.model.entity.Comment;
import com.shaonian.project.model.vo.CommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 少年
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2023-08-27 12:04:23
* @Entity com.shaonian.project.model.entity.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {


    /**
     * 分页获取评论信息
     * @param page
     * @param userAccount
     * @return
     */
    List<CommentVO> pageCommentVO(Page<CommentVO> page, @Param("userAccount") String  userAccount);
}




