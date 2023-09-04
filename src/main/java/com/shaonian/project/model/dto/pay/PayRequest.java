package com.shaonian.project.model.dto.pay;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 少年
 */

@Data
public class PayRequest implements Serializable {


    /**
     * 充值类型
     */
    private String type;

    /**
     * 充值金额
     */
    private Double money;

    private static final long serialVersionUID = 1L;


}
