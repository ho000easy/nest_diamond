package com.nest.diamonad.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//@TestPropertySource(properties = "app.scheduling.enable=false")
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = "org.example")
//@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass=true)
public class TestApplication {
    static{
        System.setProperty("java.awt.headless", "false");
        System.setProperty("app.scheduling.enable", "true");
    }
}
