package com.twt.bookstore.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * mapper异常类
 * errorCode 错误码101表示需要返回前端具体信息，202表示后端内部错误
 */
@Setter
@Getter
public class SqlException extends Exception {
    
    private Throwable originalError;
    private int errorCode;
    
    /**
     * 构造异常 
     * @param errorCode 错误码
     * @param message 信息
     */
    public SqlException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.originalError = null;
    }
    
    /**
     * 构造异常
     * @param message Debug信息
     * @param cause 异常原因，原始异常传递
     */
    public SqlException(String message, Throwable cause) {
        super(message, cause);
        this.originalError = cause;
    }

    public SqlException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.originalError = cause;
        this.errorCode = errorCode;
    }
    
    /**
     * 以字符串形式返回错误信息
     */
    @Override
    public String toString() {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append("SqlException{");
        errorMsg.append("message='").append(getMessage()).append("'");

        //异常抛出位置
        if (originalError != null) {
            errorMsg.append(", originalError=").append(originalError.getClass().getSimpleName())
              .append(": ").append(originalError.getMessage());
        }

        errorMsg.append("}");
        return errorMsg.toString();
    }
}
