package com.shaonian.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author 少年
 * @TableName pay_order
 */
@TableName(value ="pay_order")
@Data
@Accessors(chain = true)
public class PayOrder implements Serializable {
    /**
     * 
     */
    @TableId(type=IdType.ASSIGN_ID)
    private Long id;

    /**
     * 交易号
     */
    private String tradeId;

    /**
     * 用户id
     */
    private Long userId;

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

    /**
     * 
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}