// src/main/java/com/nest/diamond/dubbo/dto/RpcResult.java
package com.nest.diamond.dubbo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RpcResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success = true;
    private String message = "成功";
    private T data;
    private Date timestamp = new Date();

    // 成功
    public static <T> RpcResult<T> success(T data) {
        RpcResult<T> result = new RpcResult<>();
        result.setData(data);
        return result;
    }

    public static RpcResult<Void> success() {
        return success(null);
    }

    // 失败
    public static RpcResult<Void> fail(String message) {
        RpcResult<Void> result = new RpcResult<>();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }
}