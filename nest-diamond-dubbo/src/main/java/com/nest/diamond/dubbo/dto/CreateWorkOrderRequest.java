package com.nest.diamond.dubbo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CreateWorkOrderRequest {

    @NotBlank(message = "工单名称不能为空")
    private String name;

    @NotNull(message = "空投操作ID不能为空")
    private Long airdropOperationId;

    @NotBlank(message = "空投操作名称不能为空")
    private String airdropOperationName;

    @NotNull(message = "空投项目ID不能为空")
    private Long airdropId;

    @NotNull(message = "起始序列不能为空")
    private Integer startSequence;

    @NotNull(message = "结束序列不能为空")
    private Integer endSequence;

    @NotNull(message = "开始时间不能为空")
    private Date startTime;
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    @NotEmpty(message = "至少选择一个合约实例")
    private List<ContractInstanceRef> contractInstanceRefs;

    private String remark;

    @NotEmpty(message = "申请人不能为空")
    private String applicant;
}