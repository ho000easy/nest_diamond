package com.nest.diamond.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum AccountType {
    CUSTODY("CUSTODY"),
    NON_CUSTODY("NON_CUSTODY");

    @EnumValue
    private String name;

    AccountType(String name){
        this.name = name;
    }

}
