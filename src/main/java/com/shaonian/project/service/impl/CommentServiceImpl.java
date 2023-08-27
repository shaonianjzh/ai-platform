package com.shaonian.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaonian.project.model.entity.Comment;
import com.shaonian.project.service.CommentService;
import com.shaonian.project.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author 少年
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2023-08-27 12:04:23
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




