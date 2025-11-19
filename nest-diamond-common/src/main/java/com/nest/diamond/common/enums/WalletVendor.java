package com.nest.diamond.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum WalletVendor {
    METAMASK("METAMASK"),
    ARGENT_X("ARGENT_X"),
    BRAAVOS("BRAAVOS"),
    PHANTOM("PHANTOM"),
    SUBSTRATE("SUBSTRATE"),
    MULTI_CHAIN("MULTI_CHAIN"),
    CUSTODY("CUSTODY");

    @EnumValue
    private String name;

    WalletVendor(String name){
        this.name = name;
    }
    public static List<String> names(){
        return Arrays.stream(WalletVendor.values()).map(WalletVendor::name).collect(Collectors.toList());
    }
}
