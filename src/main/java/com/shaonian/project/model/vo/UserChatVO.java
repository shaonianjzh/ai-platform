package com.shaonian.project.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 少年
 */
@Data
@AllArgsConstructor
public class UserChatVO {

    /**
     * 单词
     */
    private String name;

    /**
     * 数量
     */
    private Long value;
}
