package com.nest.diamond.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.nest.diamond.common.enums.RpcVendor;
import com.nest.diamond.common.enums.VMType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class BlockChain {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long chainId;
    private String chainName;
    private String rpcNode;
    private String wssNode;
    private String blockExplorerUrl;

    private Boolean isSupportEip1559;
    private Boolean isSupportWeb3j;

    private Boolean isL2;
    private Boolean isTestnet;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private RpcVendor rpcVendor;
//    private IpProxyVendor ipProxyVendor;
    private VMType vmType;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}
