package com.nest.diamond.dubbo.dto.sync;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SyncAccountRequest implements Serializable {
    @NotEmpty(message = "账户列表不能为空")
    private List<AccountRef> accounts;
}
