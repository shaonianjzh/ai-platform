package com.shaonian.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName chat_model
 */
@TableName(value ="chat_model")
@Data
public class ChatModel implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 模型预设(提示语)
     */
    private String prompt;

    /**
     * 图标路径
     */
    private String img;

    /**
     * 分类

     */
    private Integer categoryId;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 是否公开
     */
    private Integer isOpen;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}