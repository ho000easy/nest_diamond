package com.nest.diamond.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum VMType {
    EVM("EVM"),
    SOLANA("SOLANA"),
    STARK_NET("STARK_NET"),
    COSMOS("COSMOS"),
    ZKSYNC_LITE("ZKSYNC_LITE"),

    ;

    @EnumValue
    private String name;

    VMType(String name){
        this.name = name;
    }

    public static List<String> names(){
        return Arrays.stream(VMType.values()).map(VMType::name).collect(Collectors.toList());
    }
}
