package com.ppdai.framework.raptor.benchmark.client.integration;

import com.ppdai.framework.raptor.benchmark.client.SimpleImpl;
import com.ppdai.framework.raptor.proto.MoreService;
import com.ppdai.framework.raptor.proto.Simple;
import com.ppdai.framework.raptor.spring.annotation.RaptorClient;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SimpleImpl.class)
public class ClientIntegrationApplication {

    @Getter
    @RaptorClient
    private Simple simple;

    @Getter
    @RaptorClient
    private MoreService moreService;

    public static void main(String[] args) {
        SpringApplication.run(ClientIntegrationApplication.class, args);
    }

}
