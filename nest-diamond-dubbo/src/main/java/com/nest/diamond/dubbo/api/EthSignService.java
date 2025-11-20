package com.nest.diamond.dubbo.api;

import com.nest.diamond.dubbo.dto.*;
import jakarta.validation.Valid;

public interface EthSignService {
    SignRawTransactionResponse signRawTransaction(@Valid SignRawTransactionRequest signRawTransactionRequest);

    SignMessageResponse signMessage(@Valid SignMessageRequest signMessageRequest);

    SignPrefixedMessageResponse signPrefixedMessage(@Valid SignPrefixedMessageRequest signPrefixedMessageRequest);
}
