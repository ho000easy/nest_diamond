package com.nest.diamond.mapper.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: zy
 */
@Configuration
@EnableTransactionManagement
@Data
public class NestDiamondDataSourceConfig {

    @Value("${nest_diamond.jdbc.user}")
    private String nest_diamond_userName;

    @Value("${nest_diamond.jdbc.pwd}")
    private String nest_diamond_password;

    @Value("${nest_diamond.jdbc.url}")
    private String nest_diamond_url;

    @Value("${nest_diamond.jdbc.maxActive}")
    private Integer nest_diamond_maxActive;

    @Value("${nest_diamond.jdbc.maxWaitMills}")
    private Integer nest_diamond_maxWaitMills;

    @Value("${nest_diamond.jdbc.initialSize}")
    private Integer nest_diamond_initialSize;

    @Value("${nest_diamond.password.encrypt}")
    private String nest_diamond_isEncrypt;



}
