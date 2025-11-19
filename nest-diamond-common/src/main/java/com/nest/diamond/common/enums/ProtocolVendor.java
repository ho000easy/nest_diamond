package com.nest.diamond.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ProtocolVendor implements IEnum<String> {
    DEFI("DEFI"),
    NFT("NFT"),
    DID("DID"),
    BRIDGE("BRIDGE"),
    WALLET("WALLET"),
    GAMEFI("GAMEFI"),
    INFRA("INFRA");

    @EnumValue
    private String name;

    ProtocolVendor(String name){
        this.name = name;
    }

    public static List<String> names(){
        return Arrays.stream(ProtocolVendor.values()).map(ProtocolVendor::name).collect(Collectors.toList());
    }

    @Override
    public String getValue() {
        return name();
    }
}
