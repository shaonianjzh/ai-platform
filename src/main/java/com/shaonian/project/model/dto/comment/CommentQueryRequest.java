package com.shaonian.project.model.dto.comment;

import com.shaonian.project.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 少年
 */
@Data
public class CommentQueryRequest extends PageRequest implements Serializable {
    /**
     * 用户账号
     */
    private String userAccount;


    private static final long serialVersionUID = 1L;
}
