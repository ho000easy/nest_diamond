package com.nest.diamond.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.nest.diamond.common.enums.MethodIdVendor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2023-08-16
 */
@Data
@TableName("contract_instance_function")
public class ContractInstanceFunction implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long contractInstanceId;
    private String contractAddress;

    private String contractName;

    private String protocolName;

    private String name;

    private String functionMethodId;

    private MethodIdVendor functionMethodType;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;

}
