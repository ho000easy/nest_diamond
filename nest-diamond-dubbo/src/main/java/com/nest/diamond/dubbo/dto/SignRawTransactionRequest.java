package com.nest.diamond.dubbo.dto;

import lombok.Data;
import org.web3j.crypto.RawTransaction;

@Data
public class SignRawTransactionRequest extends AbstractSignRequest {
    private Long chainId;
    private RawTransaction rawTransaction;
}
