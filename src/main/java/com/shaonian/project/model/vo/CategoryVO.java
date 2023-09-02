package com.shaonian.project.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 少年
 */
@Data
public class CategoryVO implements Serializable {

    private Integer value;


    /**
     * 模型名称
     */
    private String label;

    private static final long serialVersionUID = 1L;

}
