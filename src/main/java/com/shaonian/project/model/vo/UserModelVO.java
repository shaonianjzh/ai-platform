package com.shaonian.project.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 少年
 */
@Data
public class UserModelVO implements Serializable {

    private String id;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户聊天数据
     */
    private String  chatData;

    /**
     * AI生成结果
     */
    private String genResult;

    /**
     * 执行状态
     */
    private String status;


    /**
     * 执行信息
     */
    private String execMessage;

    /**
     * 创建时间
     */
    private String createTime;

    private static final long serialVersionUID = 1L;
}
