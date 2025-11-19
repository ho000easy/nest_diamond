package com.nest.diamond.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nest.diamond.common.enums.WalletGenerateType;
import com.nest.diamond.common.enums.WalletVendor;
import com.nest.diamond.common.serializer.LineSeparatedStringToListDeserializer;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AddSeedReq {
    @JsonDeserialize(using = LineSeparatedStringToListDeserializer.class)
    private List<String> seedWordsList;

    @JsonDeserialize(using = LineSeparatedStringToListDeserializer.class)
    private List<String> privateKeyList;

    @JsonDeserialize(using = LineSeparatedStringToListDeserializer.class)
    private List<String> addressList;

    private Integer count;
    @NotNull(message = "钱包供应商不能为空")
    private WalletVendor walletVendor;

    @NotNull(message = "钱包生成类型不能为空")
    private WalletGenerateType walletGenerateType;

    private Boolean isReserveSeed = false;

}
