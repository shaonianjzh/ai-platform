package com.shaonian.project.model.dto.payorder;

import com.shaonian.project.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 少年
 */
@Data
public class PayOrderQueryRequest extends PageRequest implements Serializable {

    private String userAccount;

    private static final long serialVersionUID = 1L;
}
