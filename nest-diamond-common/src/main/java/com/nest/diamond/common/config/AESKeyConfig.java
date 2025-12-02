package com.nest.diamond.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AESKeyConfig {
    @Value("${nest_diamond.aes.check}")
    private Boolean isAesCheck;
    @Value("${nest_diamond.aes.key}")
    private String aesKey;
}
