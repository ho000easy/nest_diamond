package com.nest.diamond.dubbo.enums;

import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum TicketStatusDTO implements Serializable {

    PENDING("PENDING", "待审批"),
    APPROVED("APPROVED", "通过"),
    REJECTED("REJECTED", "拒绝");

    private final String code;

//    @JsonValue
    private final String desc;

    TicketStatusDTO(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static List<String> names(){
        return Arrays.stream(TicketStatusDTO.values()).map(TicketStatusDTO::name).collect(Collectors.toList());
    }
}