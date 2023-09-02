package com.shaonian.project.model.dto.chatmodel;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author 少年
 * @TableName product
 */
@Data
public class ChatModelAddRequest implements Serializable {

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
     * 是否公开
     */
    private Integer isOpen;


    private static final long serialVersionUID = 1L;
}