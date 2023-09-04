package com.shaonian.project.model.enums;

/**
 * @author 少年
 *
 *
 * 支付订单状态枚举
 */
public enum PayOrderStatusEnum {

    PAID("已支付"),
    UNPAID("未支付");

    PayOrderStatusEnum(String status) {
        this.status = status;
    }

    private String status;

    public String getStatus() {
        return status;
    }
}
