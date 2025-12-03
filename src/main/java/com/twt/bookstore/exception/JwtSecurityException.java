package com.twt.bookstore.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * JWT 验证异常类，继承自Exception
 * 必须在service里手动回滚
 * JWT签发验证时会抛出这个异常
 * errorCode 错误码101错误需要回报前端，203表示后端内部错误
 */
@Setter
@Getter
public class JwtSecurityException extends Exception {

    private int errorCode;

    /**
     * 全参数构造
     * @param errorCode 错误码 203表示后端内部错误，101错误需要回报前端
     * @param message 错误信息
     * @param e Throwable 原始异常
     */
    public JwtSecurityException(int errorCode, String message, Throwable e) {
        super(message, e);
        this.errorCode = errorCode;
    }
}
