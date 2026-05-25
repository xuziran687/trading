package com.longan;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan({"com.longan.user.mapper","com.longan.goods.mapper","com.longan.order.mapper","com.longan.chat.mapper","com.longan.common.mapper"})
public class LonganApexApplication {

    // 手动声明log变量
    private static final Logger log = LoggerFactory.getLogger(LonganApexApplication.class);
    public static void main(String[] args) {

        SpringApplication.run(LonganApexApplication.class, args);
        log.info("启动成功");
    }

}
