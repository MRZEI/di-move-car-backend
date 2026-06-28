package com.dimovecar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.dimovecar.mapper")
@EnableCaching
@EnableScheduling
public class DiMoveCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiMoveCarApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  DI挪车后端服务启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
