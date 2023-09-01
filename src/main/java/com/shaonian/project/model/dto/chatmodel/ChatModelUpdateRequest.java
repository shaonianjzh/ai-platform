package com.shaonian.project.model.dto.chatmodel;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @author 少年
 * @TableName product
 */
@Data
public class ChatModelUpdateRequest implements Serializable {

    /**
     *
     */
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
     * 分类

     */
    private Integer categoryId;


    /**
     * 是否公开
     */
    private Integer isOpen;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}