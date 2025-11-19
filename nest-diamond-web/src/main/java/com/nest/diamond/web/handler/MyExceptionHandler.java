package com.nest.diamond.web.handler;

import com.google.common.collect.Lists;
import com.nest.diamond.model.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = Exception.class)
	@ResponseBody
    public ApiResult exceptionHandler(Exception e) {
		log.error("程序运行发生异常", e);
        return ApiResult.fail(e.getMessage());
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseBody
    public ApiResult exceptionHandler(DuplicateKeyException e) {
        log.error("程序运行发生异常", e);
        return ApiResult.fail("数据库唯一键约束冲突 " + e.getCause().getMessage());
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ApiResult exceptionHandler(BindException ex) {
        List<String> errorMsgList = Lists.newArrayList();
        ex.getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errorMsgList.add(errorMessage);
        });

        return ApiResult.fail(String.join("\r\n", errorMsgList));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ApiResult handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errorMsgList = Lists.newArrayList();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errorMsgList.add(errorMessage);
        });
        List<String> distinctMsgs = errorMsgList.stream()
                .distinct()
                .collect(Collectors.toList());
        return ApiResult.fail(String.join("\r\n", distinctMsgs));
    }
}
