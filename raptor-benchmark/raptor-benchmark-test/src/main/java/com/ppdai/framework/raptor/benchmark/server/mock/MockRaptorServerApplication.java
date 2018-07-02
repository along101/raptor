package com.ppdai.framework.raptor.benchmark.server.mock;

import com.ppdai.framework.raptor.benchmark.server.SimpleImpl;
import com.ppdai.framework.raptor.benchmark.server.httpclient.HttpclientRaptorServerApplication;
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
public class MockRaptorServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpclientRaptorServerApplication.class, args);
    }
}
