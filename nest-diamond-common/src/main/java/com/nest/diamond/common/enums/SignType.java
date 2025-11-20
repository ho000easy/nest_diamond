package com.nest.diamond.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 签名类型
 */
@Getter
public enum SignType {

    ETH_TRANSACTION("ETH_TRANSACTION", "ETH交易签名"),
    SOLANA_TRANSACTION("SOLANA_TRANSACTION", "Solana交易签名"),
    ETH_MESSAGE("ETH_MESSAGE", "ETH文本签名(personal_sign)"),
    ETH_EIP712("ETH_EIP712", "ETH EIP712结构化签名");

    @EnumValue
    private final String code;

    @JsonValue
    private final String desc;

    SignType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}