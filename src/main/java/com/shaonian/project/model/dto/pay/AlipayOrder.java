package com.shaonian.project.model.dto.pay;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author 少年
 */
@Data
@Accessors(chain = true)
public class AlipayOrder {

    /**
     * 用户订单号，必填
     */
    private String out_trade_no;

    /**
     * 订单名称，必填
     */
    private String subject;

    /**
     * 订单总金额，必填
     */
    private BigDecimal total_amount;

    /**
     * 销售产品码，必填，注：目前电脑支付场景下仅支持FAST_INSTANT_TRADE_PAY，所以可以写死
     */
    private final String product_code = "FAST_INSTANT_TRADE_PAY";
}
