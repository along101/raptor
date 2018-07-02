package com.ppdai.framework.raptor.spring.client;

import com.ppdai.framework.raptor.proto.HelloReply;
import com.ppdai.framework.raptor.proto.HelloRequest;
import com.ppdai.framework.raptor.proto.MoreService;
import com.ppdai.framework.raptor.proto.Simple;
import com.ppdai.framework.raptor.spring.TestApplication;
import com.ppdai.framework.raptor.spring.annotation.RaptorClient;
import feign.Client;
import feign.Request;
import feign.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author yinzuolong
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
@Import(RaptorClientTest.TestConfig.class)
public class RaptorClientTest {

    @RaptorClient
    private Simple simple;

    @RaptorClient
    private MoreService moreService;

    @Test
    public void testSimple() {
        HelloRequest helloRequest = new HelloRequest();
        helloRequest.setName("ppdai");
        HelloReply reply = simple.sayHello(helloRequest);
        Assert.assertEquals("ppdai123", reply.getRequest().getName());
    }

    @Test
    public void testMoreService() {
        HelloRequest helloRequest = new HelloRequest();
        helloRequest.setName("ppdai");
        HelloReply reply = moreService.testPost1(helloRequest);
        Assert.assertEquals("ppdai123", reply.getRequest().getName());
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public Client createTestClient() {
            return new Client() {
                @Override
                public Response execute(Request request, Request.Options options) throws IOException {
                    return Response.builder()
                            .body("{\"request\":{\"name\":\"ppdai123\"}}", StandardCharsets.UTF_8)
                            .status(200)
                            .headers(new HashMap<>())
                            .build();
                }
            };
        }
    }
}
