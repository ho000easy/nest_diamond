package com.nest.diamond.dubbo.api;

import com.nest.diamond.dubbo.dto.RpcResult;
import com.nest.diamond.dubbo.dto.sign.*;
import jakarta.validation.Valid;

public interface EthSignDubboService {
    RpcResult<SignRawTransactionResponse> signRawTransaction(@Valid SignRawTransactionRequest signRawTransactionRequest);

    RpcResult<SignEip712MessageResponse> signEip712Message(@Valid SignEip712MessageRequest signEip712MessageRequest);

    RpcResult<SignPrefixedMessageResponse> signPrefixedMessage(@Valid SignPrefixedMessageRequest signPrefixedMessageRequest);
}
