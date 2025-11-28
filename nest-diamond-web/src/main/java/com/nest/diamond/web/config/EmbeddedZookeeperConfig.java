package com.nest.diamond.web.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.test.TestingServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Slf4j
//@Configuration
public class EmbeddedZookeeperConfig {

    /**
     * 启动一个内嵌 Zookeeper。
     * 
     * destroyMethod = "close" 保证 Spring 关机时把 ZK 一起关掉。
     */
    @Bean(destroyMethod = "close")
    @Order(Ordered.HIGHEST_PRECEDENCE)  // 尽量靠前
    public TestingServer embeddedZookeeper() throws Exception {
        int port = 2181; // 固定端口，Dubbo 直接连这个
        TestingServer server = new TestingServer(port, true);
        log.info("Embedded Zookeeper started at 127.0.0.1:{}", port);
        return server;
    }
}
