package com.shaonian.project.model.dto.usermodel;

import com.shaonian.project.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 少年
 */
@Data
public class UserModelQueryRequest extends PageRequest implements Serializable {

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 模型id
     */
    private Long modelId;

    /**
     * 用户id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
