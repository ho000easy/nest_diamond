package com.nest.diamond.dubbo.dto.sign;

import lombok.Data;

@Data
public class SignEip712MessageResponse extends AbstractSignResponse{
    private String signature;
}
