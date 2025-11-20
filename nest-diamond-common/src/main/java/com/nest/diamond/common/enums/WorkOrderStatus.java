package com.nest.diamond.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum WorkOrderStatus {

    PENDING("PENDING", "待审批"),
    APPROVED("APPROVED", "通过"),
    REJECTED("REJECTED", "拒绝");

    @EnumValue
    private final String code;

//    @JsonValue
    private final String desc;

    WorkOrderStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static List<String> names(){
        return Arrays.stream(WorkOrderStatus.values()).map(WorkOrderStatus::name).collect(Collectors.toList());
    }
}