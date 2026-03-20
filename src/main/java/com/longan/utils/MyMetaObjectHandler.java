package com.longan.utils;

// 导入MyBatis-Plus的自动填充核心接口
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
// MyBatis的元对象（用于操作实体类字段）
import org.apache.ibatis.reflection.MetaObject;
// 交给Spring容器管理，让MyBatis-Plus能扫描到这个处理器
import org.springframework.stereotype.Component;

import java.time.LocalDateTime; // 用于处理时间字段（JDK8+推荐）
import java.util.UUID; // 生成唯一标识，用于默认昵称

/**
 * MyBatis-Plus 字段自动填充处理器
 * 作用：在执行insert/update操作时，自动填充指定字段（无需手动set）
 */
@Component // 关键：必须加@Component，否则Spring无法扫描到这个处理器
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作时的自动填充逻辑
     * 触发时机：调用mapper.insert()/service.save()时执行
     * @param metaObject 元对象：封装了要插入的实体类对象和字段信息
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 1. 自动填充nickname字段（插入时如果未手动设置，则填充默认值）
        this.strictInsertFill(
                metaObject,          // 元对象：传递MyBatis的元对象参数
                "nickname",          // 要填充的字段名（必须和实体类的属性名一致，驼峰命名）
                String.class,        // 字段类型：和实体类中nickname的类型匹配
                // 填充值：默认昵称 = "新用户" + 随机UUID（去掉横线后截取前16位，避免过长）
                "新用户"+UUID.randomUUID()
                        .toString()       // UUID转字符串（格式：xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx）
                        .replace("-", "") // 去掉UUID中的横线
                        .substring(0, 16) // 截取前16位，防止昵称过长
        );

        // 2. 自动填充createTime字段（插入时填充当前时间）
        this.strictInsertFill(
                metaObject,          // 元对象
                "createTime",        // 实体类中的创建时间字段名（驼峰）
                LocalDateTime.class, // 字段类型：LocalDateTime（推荐替代Date）
                LocalDateTime.now()  // 填充值：当前系统时间
        );

        // 3. 自动填充updateTime字段（插入时也填充当前时间）
        this.strictInsertFill(
                metaObject,          // 元对象
                "updateTime",        // 实体类中的更新时间字段名（驼峰）
                LocalDateTime.class, // 字段类型
                LocalDateTime.now()  // 填充值：当前系统时间
        );
    }

    /**
     * 更新操作时的自动填充逻辑
     * 触发时机：调用mapper.update()/service.update()时执行
     * @param metaObject 元对象：封装了要更新的实体类对象和字段信息
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 自动填充updateTime字段（更新时覆盖为当前时间）
        this.strictUpdateFill(
                metaObject,          // 元对象
                "updateTime",        // 要填充的更新时间字段名
                LocalDateTime.class, // 字段类型
                LocalDateTime.now()  // 填充值：更新操作时的当前时间
        );
    }
}
