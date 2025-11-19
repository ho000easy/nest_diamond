package com.nest.diamond.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static volatile boolean isInit = false;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.isInit = true;
    }

    public static ApplicationContext getApplicationContext(){
        if(!isInit){
            throw new IllegalStateException("spring容器未初始化");
        }
        return applicationContext;
    }
}
