package com.longan.utils;

// 导入MyBatis-Plus的自动填充核心接口

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * MyBatis-Plus 字段自动填充处理器
 * 作用：在执行insert/update操作时，自动填充指定字段（无需手动set）
 */
@Component // 关键：必须加@Component，否则Spring无法扫描到这个处理器
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作时的自动填充逻辑
     * 触发时机：调用mapper.insert()/service.save()时执行
     *
     * @param metaObject 元对象：封装了要插入的实体类对象和字段信息
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动生成用户名：user + 时间戳后8位 + 随机4位
        if (metaObject.hasSetter("username") && getFieldValByName("username", metaObject) == null) {
            String username = "user" + System.currentTimeMillis() % 100000000
                    + String.format("%04d", (int)(Math.random() * 10000));
            this.setFieldValByName("username", username, metaObject);
        }

        if (metaObject.hasSetter("createTime")) {
            this.setFieldValByName("createTime", nowForType(metaObject, "createTime"), metaObject);
        }
        if (metaObject.hasSetter("updateTime")) {
            this.setFieldValByName("updateTime", nowForType(metaObject, "updateTime"), metaObject);
        }

        // nickname 可以保留 strict 逻辑，因为如果用户自己传了昵称，我们不应该覆盖它
        if (metaObject.hasSetter("nickname") && getFieldValByName("nickname", metaObject) == null) {
            String randomNick = "新用户" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
            this.setFieldValByName("nickname", randomNick, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter("updateTime")) {
            this.setFieldValByName("updateTime", nowForType(metaObject, "updateTime"), metaObject);
        }
    }

    private Object nowForType(MetaObject metaObject, String fieldName) {
        Class<?> type = metaObject.getSetterType(fieldName);
        if (type == LocalDate.class) {
            return LocalDate.now();
        } else if (type == LocalDateTime.class) {
            return LocalDateTime.now();
        }
        return new Date();
    }
}
