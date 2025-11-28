package com.nest.diamond.web;


import com.nest.diamond.web.checker.ApplicationStartCheck;
import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nest.diamond")
@EnableDubbo(scanBasePackages = "com.nest.diamond")
public class ApplicationBootstrap {

    @SneakyThrows
    public static void main(String[] args) {
        TestingServer server = new TestingServer(2181);
        server.start();

        ApplicationStartCheck.isValidationSuccessful(args);

        SpringApplication.run(ApplicationBootstrap.class, args);
    }

}

