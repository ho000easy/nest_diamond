package com.nest.diamond.dubbo.dto;

import lombok.Data;

@Data
public class SignRawTransactionResponse extends AbstractSignResponse{

    private String signedRawTransaction;     // 已签名的完整交易（0x开头，可直接广播）

    private String txHash;                   // 交易hash

}