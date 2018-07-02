package com.ppdai.framework.raptor.benchmark.server.okhttp;

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
public class OkhttpRaptorServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OkhttpRaptorServerApplication.class, args);
    }
}
