package com.foodback.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})

public class BlockbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlockbackApplication.class, args);
    }

}
