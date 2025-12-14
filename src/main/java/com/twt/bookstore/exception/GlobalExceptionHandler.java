package com.twt.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.twt.bookstore.dto.response.BaseResponse;

// 全局异常处理器
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<Void> handle(BusinessException e) {
        if(e.getErrorCode() == 101) {
            return BaseResponse.error(404, e.getMessage());
        }else {
            return BaseResponse.error(404, "please contact admin");
        }
    }

    @ExceptionHandler(SqlException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // HTTP 404
    public BaseResponse<Void> handle(SqlException e) {
        if(e.getErrorCode() == 101) {
            return BaseResponse.error(404, e.getMessage());
        }else {
            return BaseResponse.error(404, "please contact admin");
        }
    }

    @ExceptionHandler(JwtSecurityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<Void> handle(JwtSecurityException e) {
        if(e.getErrorCode() == 101) {
            return BaseResponse.error(404, e.getMessage());
        }else {
            return BaseResponse.error(404, "please contact admin");
        }
    }
    
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<Void> handle(ServiceException e) {
        if(e.getErrorCode() == 101) {
            return BaseResponse.error(404, e.getMessage());
        }else {
            return BaseResponse.error(404, "please contact admin");
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<Void> handleOther(Exception e) {
        return BaseResponse.error(500, "Internal server error");
    }
}
