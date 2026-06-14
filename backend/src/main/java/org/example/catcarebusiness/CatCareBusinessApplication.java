package org.example.catcarebusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("org.example.catcarebusiness.mapper")
public class CatCareBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatCareBusinessApplication.class, args);
    }

}
