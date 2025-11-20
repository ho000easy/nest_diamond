package com.nest.diamond.dubbo.dto;

import lombok.Data;

@Data
public class SignMessageRequest extends AbstractSignRequest{
    private String messageHex;
    private Boolean needToHash;
}
