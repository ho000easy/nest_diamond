package com.nest.diamond.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum TicketType {

    SIGN("SIGN", "签名"),
    ACCOUNT_SYNC("ACCOUNT_SYNC", "账户同步");

    @EnumValue
    private final String code;

//    @JsonValue
    private final String desc;

    TicketType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static List<String> names(){
        return Arrays.stream(TicketType.values()).map(TicketType::name).collect(Collectors.toList());
    }
}