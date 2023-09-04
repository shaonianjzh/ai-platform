package com.shaonian.project.model.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author 少年
 */
public enum RechargeTypeEnum {
    THREE_DAYS_MEMBER("三天会员",9.9),
    MONTHLY_MEMBER("月会员",14.9),
    YEARLY_MEMBER("年会员",100.0),
    ONE_HUNDRED_COUNT_RECHARGE("100点数",9.9),
    FIVE_HUNDRED_COUNT_RECHARGE("500点数",29.9),
    ONE_THOUSAND_COUNT_RECHARGE("1000点数",89.9);


    private String type;
    private Double value;

    RechargeTypeEnum(String type, Double value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Double getValue() {
        return value;
    }

    /**
     * 根据type获取枚举值
     * @param type
     * @return
     */
    public static RechargeTypeEnum getEnumByType(String type){
        if(StringUtils.isBlank(type)){
            return null;
        }
        for(RechargeTypeEnum rechargeTypeEnum: RechargeTypeEnum.values()){
            if(rechargeTypeEnum.getType().equals(type)){
                return rechargeTypeEnum;
            }
        }
        return null;
    }
}
