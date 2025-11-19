package com.nest.diamond.model.bo;

import com.nest.diamond.model.domain.AirdropItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirdropItemExtend extends AirdropItem {
    private String privateKey;
    private String seed;
    private Long seedId;

    private String seedPrefix;
}
