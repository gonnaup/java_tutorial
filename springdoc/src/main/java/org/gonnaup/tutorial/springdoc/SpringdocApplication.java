package org.gonnaup.tutorial.springdoc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;

/**
 * @author gonnaup
 * @version created at 2023/9/1 下午8:39
 */
@MapperScan(basePackages = "org.gonnaup.tutorial.springdoc.mapper", annotationClass = Repository.class)
@SpringBootApplication
public class SpringdocApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringdocApplication.class, args);
    }
}
