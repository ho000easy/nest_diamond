package com.nest.diamond.common.util;

import lombok.SneakyThrows;
import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtil {

    @SneakyThrows
    public static void cleanDOCommonField(Object bean){
        PropertyUtils.setProperty(bean, "id", null);
        PropertyUtils.setProperty(bean, "createTime", null);
        PropertyUtils.setProperty(bean, "modifyTime", null);
    }
}
