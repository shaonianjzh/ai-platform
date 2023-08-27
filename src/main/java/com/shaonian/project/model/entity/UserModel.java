package com.shaonian.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user_model
 */
@TableName(value ="user_model")
@Data
public class UserModel implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 模型id
     */
    private Long modelId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户聊天内容
     */
    private String chatData;

    /**
     * AI回复结果
     */
    private String genResult;

    /**
     * 调用状态 wait/running/succeed/failed

     */
    private String status;

    /**
     * 执行信息
     */
    private String execMessage;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}