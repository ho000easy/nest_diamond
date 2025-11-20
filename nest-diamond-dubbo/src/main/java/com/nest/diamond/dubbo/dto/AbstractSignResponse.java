package com.nest.diamond.dubbo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AbstractSignResponse {
    private Date signTime;
    private Long bizOrderId; //业务订单号
    private Long airdropOperationId; //空投操作ID
    private String remark;

}
