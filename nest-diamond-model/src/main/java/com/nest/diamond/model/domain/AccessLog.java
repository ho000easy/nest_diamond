package com.nest.diamond.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("access_log")
public class AccessLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    // 模块名称 (对应注解上的 value)
    private String module;

    // 类名.方法名
    private String methodName;

    // 请求IP
    private String ipAddress;

    // 请求参数 (JSON)
    private String requestParams;

    // 响应结果 (JSON)
    private String responseData;

    // 执行耗时 (毫秒)
    private Long executionTime;

    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Date modifyTime;
}