package com.danceflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.danceflow.mapper")
public class DanceFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(DanceFlowApplication.class, args);
    }
}
