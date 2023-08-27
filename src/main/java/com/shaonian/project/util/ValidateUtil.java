package com.shaonian.project.util;


import org.apache.commons.lang3.StringUtils;

/**
 * @author 少年
 */
public class ValidateUtil {

    /**
     * 邮箱验证规则
     */
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(?:\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$";

    /**
     * 身份证校验规则（2代）
     */
    private static final String ID_CARD_PATTERN = "^[1-9]\\d{5}(?:18|19|20)\\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\\d|30|31)\\d{3}[0-9Xx]$";

    /**
     * 手机号校验规则
     */
    public static final String PHONE_PATTERN = "^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[189]))\\d{8}$";



    public static Boolean validateEmail(String email){
        if(StringUtils.isBlank(email)){
            return false;
        }
        return email.matches(EMAIL_PATTERN);
    }

    public static Boolean validatePhone(String phone){
        if(StringUtils.isBlank(phone)){
            return false;
        }
        return phone.matches(PHONE_PATTERN);
    }

    public static Boolean validateIdCard(String idCard){
        if(StringUtils.isBlank(idCard)){
            return false;
        }
        return idCard.matches(ID_CARD_PATTERN);
    }
}
