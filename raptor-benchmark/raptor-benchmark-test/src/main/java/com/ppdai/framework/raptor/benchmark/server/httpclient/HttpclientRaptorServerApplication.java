package com.ppdai.framework.raptor.benchmark.server.httpclient;

import com.ppdai.framework.raptor.benchmark.server.SimpleImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author yinzuolong
 */
@SpringBootApplication
@Configuration
@Import(SimpleImpl.class)
public class HttpclientRaptorServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpclientRaptorServerApplication.class, args);
    }
}
