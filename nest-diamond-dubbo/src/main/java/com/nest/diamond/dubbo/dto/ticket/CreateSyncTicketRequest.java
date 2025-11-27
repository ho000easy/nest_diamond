package com.nest.diamond.dubbo.dto.ticket;

import com.nest.diamond.dubbo.dto.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CreateSyncTicketRequest extends BaseDTO {

    @NotBlank(message = "工单名称不能为空")
    private String name;

    @NotBlank(message = "工单编号不能为空")
    private String ticketNo;

    @NotEmpty(message = "空投项目名称不能为空")
    private String airdropName;

    @NotEmpty(message = "空投项目地址不能为空")
    private List<String> addressList;

    @NotNull(message = "开始时间不能为空")
    private Date startTime;
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    private String remark;

    @NotEmpty(message = "申请人不能为空")
    private String applicant;
}