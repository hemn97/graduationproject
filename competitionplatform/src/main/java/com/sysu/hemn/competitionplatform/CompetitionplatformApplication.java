package com.sysu.hemn.competitionplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.sysu.hemn.competitionplatform.mapper"})
public class CompetitionplatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompetitionplatformApplication.class, args);
    }

}
