package com.twt.bookstore.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Service类异常
 * 204 后端内部异常
 */
@Setter
@Getter
public class ServiceException extends Exception{
    private int errorCode;

    /**
     * 全参构造器
     * @param errorCode
     * @param message
     * @param e
     */
    public ServiceException(int errorCode, String message, Throwable e) {
        super(message, e);
        this.errorCode = errorCode;
    }

    public ServiceException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
