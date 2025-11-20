package com.nest.diamond.dubbo.dto.sign;

import lombok.Data;

@Data
public class SignPrefixedMessageRequest extends AbstractSignRequest {
    private String message;
}
