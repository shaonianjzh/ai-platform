package com.shaonian.project.model.dto.chatmodel;

import com.shaonian.project.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author 少年
 */
@Data
public class ChatModelQueryRequest extends PageRequest implements Serializable {


    /**
     * 模型名称
     */
    private String name;


    /**
     * 分类
     */
    private Integer categoryId;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 是否公开
     */

    private Integer isOpen;

    private static final long serialVersionUID = 1L;
}