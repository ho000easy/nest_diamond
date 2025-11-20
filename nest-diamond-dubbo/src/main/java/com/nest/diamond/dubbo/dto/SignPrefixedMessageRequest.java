package com.nest.diamond.dubbo.dto;

import lombok.Data;

@Data
public class SignPrefixedMessageRequest extends AbstractSignRequest {
    private String message;
}
