package com.nest.diamond.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum RpcVendor implements IEnum<String> {
    INFURA("INFURA"),
    ALCHEMY("ALCHEMY"),
    METAMASK("METAMASK"),
    ARGENT("ARGENT"),
    BRAAVOS("BRAAVOS")
    ;

    @EnumValue
    private String name;

    RpcVendor(String name){
        this.name = name;
    }

    public static List<String> names(){
        return Arrays.stream(RpcVendor.values()).map(RpcVendor::name).collect(Collectors.toList());
    }

    @Override
    public String getValue() {
        return name();
    }
}
