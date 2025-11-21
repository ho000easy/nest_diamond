package com.nest.diamond.web;


import com.nest.diamond.web.checker.ApplicationStartCheck;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nest.diamond")
@EnableDubbo(scanBasePackages = "com.nest.diamond")
public class ApplicationBootstrap {

    public static void main(String[] args) {
        System.out.println("执行启动函数");
        ApplicationStartCheck.isValidationSuccessful(args);

        SpringApplication.run(ApplicationBootstrap.class, args);
    }

}

