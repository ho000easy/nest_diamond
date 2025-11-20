package com.nest.diamond.dubbo.api;

import com.nest.diamond.dubbo.dto.*;
import jakarta.validation.Valid;

public interface EthSignDubboService {
    SignRawTransactionResponse signRawTransaction(@Valid SignRawTransactionRequest signRawTransactionRequest);

    SignEip712MessageResponse signEip712Message(@Valid SignEip712MessageRequest signEip712MessageRequest);

    SignPrefixedMessageResponse signPrefixedMessage(@Valid SignPrefixedMessageRequest signPrefixedMessageRequest);
}
