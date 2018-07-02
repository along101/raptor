package com.ppdai.framework.raptor.benchmark.client.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppdai.framework.raptor.proto.HelloReply;
import com.ppdai.framework.raptor.proto.MoreService;
import com.ppdai.framework.raptor.proto.Simple;
import com.ppdai.framework.raptor.spring.annotation.RaptorClient;
import feign.Client;
import feign.Request;
import feign.Response;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SpringBootApplication
@Configuration
public class ClientMockApplication {

    @Getter
    @RaptorClient
    private Simple simple;

    public static void main(String[] args) {
        SpringApplication.run(ClientMockApplication.class, args);
    }

    @Bean
    public Client createMockClient() {
        return new MockClient();
    }

    public static class MockClient implements Client {

        @Getter
        @Setter
        private String message = "ppdai";

        private ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

        @Override
        public Response execute(Request request, Request.Options options) throws IOException {
            HelloReply reply = new HelloReply();
            reply.setMessage(message);
            String body = objectMapper.writeValueAsString(reply);
            return Response.builder()
                    .body(body, StandardCharsets.UTF_8)
                    .status(200)
                    .headers(new HashMap<>())
                    .build();
        }
    }
}
