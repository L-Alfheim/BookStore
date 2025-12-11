package com.twt.bookstore.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 事务异常
 * 这里的异常都要汇报前端
 * 理论上需要汇报前端的异常都在Service重新抛出BusinessException，errorCode 101
 */
@Setter
@Getter
public class BusinessException extends Exception {
    private int errorCode;

    /**
     * 全参构造器
     * @param errorCode
     * @param message
     * @param e
     */
    public BusinessException(int errorCode, String message, Throwable e) {
        super(message, e);
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
