package com.nest.diamond.dubbo.dto.sign;

import lombok.Data;

@Data
public class SignPrefixedMessageResponse extends AbstractSignResponse{
    private String signature;
}
