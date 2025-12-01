package com.nest.diamond.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.nest.diamond.common.annos.LogMask;
import com.nest.diamond.common.config.AESEncryptHandler;
import com.nest.diamond.common.enums.WalletVendor;
import lombok.Getter;
import lombok.Setter;
import com.nest.diamond.common.enums.AccountType;

import java.util.Date;

@Getter
@Setter
@TableName(autoResultMap = true)
public class Account {
    @TableId(type = IdType.AUTO)
    private Long id;

    private AccountType type;
    private String address;

    @LogMask
    @TableField(typeHandler = AESEncryptHandler.class)
    private String privateKey;

    private String publicKey;

    private Long seedId;

    @LogMask
    @TableField(typeHandler = AESEncryptHandler.class)
    private String seed;
    private String seedPrefix;

    private Integer hdIndex;

    private WalletVendor walletVendor;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}
