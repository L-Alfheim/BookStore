package com.twt.bookstore.dto.response;

/**
 * 所有访问返回的基本框架
 * 成功 200 + "success" + data
 * 错误 101 + "cause" + null
 */
public record BaseResponse<T>(Integer code,
                             String message,
                             T data) {
    
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(200, "success", null);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, "success", data);
    }
    
    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(200, message, data);
    }
    
    public static <T> BaseResponse<T> error(Integer code, String message) {
        return new BaseResponse<>(code, message, null);
    }
}