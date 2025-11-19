package com.nest.diamond.model.vo;

import lombok.Data;

@Data
public class ApiResult<T>{
    private T data;
    private Boolean isSuccess;
//    private String code;
    private String message;

    public static <T> ApiResult<T> success(T data){
        ApiResult<T> apiResult = new ApiResult<T>();
        apiResult.setIsSuccess(true);
        apiResult.setData(data);
        return apiResult;
    }

    public static ApiResult<Void> success(){
        ApiResult<Void> apiResult = new ApiResult<Void>();
        apiResult.setIsSuccess(true);
        return apiResult;
    }

    public static <T> ApiResult<T> fail(String message){
        ApiResult<T> apiResult = new ApiResult<T>();
        apiResult.setIsSuccess(false);
        apiResult.setMessage(message);
        return apiResult;
    }
}
