package com.nest.diamond.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum WalletGenerateType {
    SINGLE_SEED("SINGLE_SEED"),
    MULTI_SEED("MULTI_SEED"),
    MULTI_PRIVATE_KEY("MULTI_PRIVATE_KEY"),
    MULTI_ADDRESS("MULTI_ADDRESS");

    @EnumValue
    private String name;

    WalletGenerateType(String name){
        this.name = name;
    }
    public static List<String> names(){
        return Arrays.stream(WalletGenerateType.values()).map(WalletGenerateType::name).collect(Collectors.toList());
    }
}
