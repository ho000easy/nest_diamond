package com.nest.diamond.web;


import com.nest.diamond.web.checker.ApplicationStartCheck;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.nest.diamond")
public class ApplicationBootstrap {

    public static void main(String[] args) {
        System.out.println("执行启动函数");
        ApplicationStartCheck.isValidationSuccessful(args);

        SpringApplication.run(ApplicationBootstrap.class, args);
    }

}

