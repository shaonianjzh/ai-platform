package com.shaonian.project.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 少年
 */
@Data
public class CommentVO implements Serializable {

    /**
     *id
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;


    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数量
     */
    private Integer upvote;

    /**
     *评论时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
