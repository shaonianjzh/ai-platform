package com.shaonian.project.exception;

import com.shaonian.project.common.ErrorCode;

/**
 * @author 少年
 */
public class ThrowUtil {

    public static void throwIf(boolean condition,RuntimeException exception){
        if(condition){
            throw exception;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
