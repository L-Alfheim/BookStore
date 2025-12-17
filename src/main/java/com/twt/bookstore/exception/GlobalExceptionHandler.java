package com.twt.bookstore.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.twt.bookstore.dto.response.BaseResponse;

// 全局异常处理器
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Void> handle(BusinessException e) {
        String detailMessage = (e.getCause() != null) ? e.getCause().getMessage() : "No underlying cause";
        log.warn("ServiceException caught: ErrorCode={}, Class={}, Message={}, Detail={}", 
                 e.getErrorCode(), e.getClass().getSimpleName(), e.getMessage(), detailMessage);
        if(e.getErrorCode() == 101) {
            return BaseResponse.error(404, e.getMessage());
        }else {
            return BaseResponse.error(500, "please contact admin");
        }
    }

    @ExceptionHandler(SqlException.class)
    public BaseResponse<Void> handle(SqlException e) {
        String detailMessage = (e.getCause() != null) ? e.getCause().getMessage() : "No underlying cause";
        log.warn("ServiceException caught: ErrorCode={}, Class={}, Message={}, Detail={}", 
                 e.getErrorCode(), e.getClass().getSimpleName(), e.getMessage(), detailMessage);
        if(e.getErrorCode() == 101) {
            return BaseResponse.error(404, e.getMessage());
        }else {
            return BaseResponse.error(500, "please contact admin");
        }
    }

    @ExceptionHandler(JwtSecurityException.class)
    public BaseResponse<Void> handle(JwtSecurityException e) {
        String detailMessage = (e.getCause() != null) ? e.getCause().getMessage() : "No underlying cause";
        log.warn("ServiceException caught: ErrorCode={}, Class={}, Message={}, Detail={}", 
                 e.getErrorCode(), e.getClass().getSimpleName(), e.getMessage(), detailMessage);
        if(e.getErrorCode() == 101) {
            return BaseResponse.error(404, e.getMessage());
        }else {
            return BaseResponse.error(500, "please contact admin");
        }
    }
    
    @ExceptionHandler(ServiceException.class)
    public BaseResponse<Void> handle(ServiceException e) {
        String detailMessage = (e.getCause() != null) ? e.getCause().getMessage() : "No underlying cause";
        log.warn("ServiceException caught: ErrorCode={}, Class={}, Message={}, Detail={}", 
                 e.getErrorCode(), e.getClass().getSimpleName(), e.getMessage(), detailMessage);
        if(e.getErrorCode() == 101) {
            return BaseResponse.error(404, e.getMessage());
        }else {
            return BaseResponse.error(500, "please contact admin");
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) 
    public BaseResponse<Void> handleValidationException(MethodArgumentNotValidException e) {
        return BaseResponse.error(400, "Method argument not valid");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) 
    public BaseResponse<Void> handle(HttpMessageNotReadableException e) {
        return BaseResponse.error(400, "json syntax error");
    }
    
    @ExceptionHandler(Exception.class)
    public BaseResponse<Void> handleOther(Exception e) {
        log.warn("ServiceException caught: happen at {}, Message= {}", e.getClass(), e.getMessage() + e.getCause().getMessage());
        return BaseResponse.error(500, "Internal server error");
    }
}
