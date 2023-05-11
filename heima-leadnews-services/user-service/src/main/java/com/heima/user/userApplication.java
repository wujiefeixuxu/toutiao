package com.heima.user;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.heima.user.mapper")
public class userApplication {
    public static void main(String[] args) {
        SpringApplication.run(userApplication.class, args);
    }
    @Bean
    PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
