package com.ppdai.framework.raptor.benchmark.springmvc.httpclient;

import com.ppdai.framework.raptor.benchmark.springmvc.SimpleController;
import com.ppdai.framework.raptor.spring.RaptorAutoConfiguration;
import com.ppdai.framework.raptor.spring.client.RaptorClientAutoConfiguration;
import com.ppdai.framework.raptor.spring.service.RaptorServiceAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author yinzuolong
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {RaptorAutoConfiguration.class, RaptorClientAutoConfiguration.class, RaptorServiceAutoConfiguration.class})
@Import(SimpleController.class)
public class HttpclientSpringMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpclientSpringMvcApplication.class, args);
    }

}
