package com.ppdai.framework.raptor.spring;

import com.ppdai.framework.raptor.spring.service.SimpleImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author yinzuolong
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = {SimpleImpl.class})
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
