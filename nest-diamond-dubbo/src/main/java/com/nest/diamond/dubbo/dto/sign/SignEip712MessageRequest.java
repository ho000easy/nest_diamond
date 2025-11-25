package com.nest.diamond.dubbo.dto.sign;

import lombok.Data;

@Data
public class SignEip712MessageRequest extends AbstractSignRequest{
//    private String messageHex;
    private String jsonMessage;
    private String domainSeparatorHex;
}
