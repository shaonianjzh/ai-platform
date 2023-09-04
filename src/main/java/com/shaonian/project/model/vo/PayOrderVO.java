package com.shaonian.project.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 少年
 */
@Data
public class PayOrderVO {

    /**
     *
     */
    private Long id;

    /**
     * 交易号
     */
    private String tradeId;

    /**
     * 用户id
     */
    private String  userAccount;

    /**
     * 订单名称
     */
    private String type;

    /**
     * 支付金额
     */
    private BigDecimal payMoney;

    /**
     * 订单状态 未支付 已支付
     */
    private String status;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 订单结束时间
     */
    private Date endTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    private static final long serialVersionUID = 1L;
}
