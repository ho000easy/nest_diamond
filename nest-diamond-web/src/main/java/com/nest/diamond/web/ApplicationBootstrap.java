package com.nest.diamond.web;


import com.nest.diamond.web.checker.ApplicationStartCheck;
import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;

@SpringBootApplication(scanBasePackages = "com.nest.diamond")
@EnableDubbo(scanBasePackages = "com.nest.diamond")
public class ApplicationBootstrap {

    @SneakyThrows
    public static void main(String[] args) {
        initialize();
        TestingServer server = new TestingServer(2181);
        server.start();

        ApplicationStartCheck.isValidationSuccessful(args);

        SpringApplication.run(ApplicationBootstrap.class, args);
    }


    private static void initialize() {
        // 修改dubbo注册的IP为本机

        try {
            Field f = NetUtils.class.getDeclaredField("HOST_ADDRESS");
            f.setAccessible(true);
            f.set(null, "127.0.0.1");
            System.out.println("⚠️  [Dubbo Host Override] 强制 Host 设置为 127.0.0.1");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to override Dubbo HOST_ADDRESS", e);
        }
    }
}

