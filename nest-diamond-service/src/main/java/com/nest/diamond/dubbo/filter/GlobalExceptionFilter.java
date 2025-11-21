package com.nest.diamond.dubbo.filter;

import com.nest.diamond.dubbo.dto.RpcResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

@Activate(group = CommonConstants.PROVIDER, value = "globalException", order = -30000)  // 优先级最低，确保最后执行
public class GlobalExceptionFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            Result result = invoker.invoke(invocation);
            if (result.hasException()) {
                return handleException(result.getException(), invocation);
            }
            return result;
        } catch (Throwable e) {
            return handleException(e, invocation);
        }
    }

    private Result handleException(Throwable e, Invocation invocation) {
        log.error("Dubbo Provider 全局异常拦截: {}", e.getMessage(), e);
        RpcResult<Void> rpcResult = RpcResult.fail(ExceptionUtils.getRootCauseMessage(e));
        if(e instanceof ConstraintViolationException){
            Set<ConstraintViolation<?>> constraintViolationExceptionSet = ((ConstraintViolationException) e).getConstraintViolations();
            List<String> errorMessageList = constraintViolationExceptionSet.stream().map(ConstraintViolation::getMessage).toList();
            rpcResult = RpcResult.fail(String.join(",", errorMessageList));
        }

        // 关键！必须返回 AsyncRpcResult
        AsyncRpcResult asyncResult = AsyncRpcResult.newDefaultAsyncResult(invocation);
        asyncResult.setValue(rpcResult);
        return asyncResult;
    }
}