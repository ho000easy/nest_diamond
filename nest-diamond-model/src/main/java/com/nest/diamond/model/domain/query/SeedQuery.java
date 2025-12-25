package com.nest.diamond.model.domain.query;

import com.nest.diamond.common.enums.WalletGenerateType;
import com.nest.diamond.common.enums.WalletVendor;
import lombok.Data;

@Data
public class SeedQuery {
    private String seedPrefix;
    private WalletGenerateType walletGenerateType;
    private WalletVendor walletVendor;
}
