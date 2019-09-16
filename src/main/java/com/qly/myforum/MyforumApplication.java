package com.qly.myforum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qly.myforum.mapper")
public class MyforumApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyforumApplication.class, args);
    }

}
