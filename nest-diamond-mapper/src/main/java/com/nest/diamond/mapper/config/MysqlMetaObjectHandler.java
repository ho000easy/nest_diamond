/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.nest.diamond.mapper.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 公共字段自动更新
 *
 * @author zy
 */
public class MysqlMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 在插入的时候自动填充 createDate
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("modifyTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 在插入的时候自动更新 modifyDate
        this.setFieldValByName("modifyTime", new Date(), metaObject);// 不允许手动设置更新时间
//        this.setFieldValByName("createDate", null, metaObject);// createTime创建只有不允许更改
    }
}

