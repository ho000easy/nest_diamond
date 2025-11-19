package com.nest.diamond.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MethodIdVendor implements IEnum<String> {
    LP_POOL("LP_POOL"),
    SWAP("SWAP")
    ;

    @EnumValue
    private String name;

    MethodIdVendor(String name){
        this.name = name;
    }

    public static List<String> names(){
        return Arrays.stream(MethodIdVendor.values()).map(MethodIdVendor::name).collect(Collectors.toList());
    }

    @Override
    public String getValue() {
        return name();
    }
}
