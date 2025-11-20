package com.nest.diamond.dubbo.dto;

import lombok.Data;

@Data
public class SignEip712MessageRequest extends AbstractSignRequest{
    private String messageHex;
}
