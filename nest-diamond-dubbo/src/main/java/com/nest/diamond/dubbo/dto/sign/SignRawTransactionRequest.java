package com.nest.diamond.dubbo.dto.sign;

import lombok.Data;

@Data
public class SignRawTransactionRequest extends AbstractSignRequest {
    private Long chainId;
    private TransactionDTO transactionDTO;
}
