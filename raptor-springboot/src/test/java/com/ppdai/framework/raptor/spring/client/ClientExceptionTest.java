package com.ppdai.framework.raptor.spring.client;

import com.ppdai.framework.raptor.common.RaptorConstants;
import com.ppdai.framework.raptor.exception.RaptorException;
import com.ppdai.framework.raptor.proto.HelloRequest;
import com.ppdai.framework.raptor.proto.MoreService;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yinzuolong
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
@Import(ClientExceptionTest.TestConfig.class)
public class ClientExceptionTest {

    @RaptorClient
    private MoreService moreService;

    @Test
    public void testRaptorException() {
        HelloRequest request = new HelloRequest();
        request.setName("RaptorException");
        try {
            moreService.testGet2(request);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof RaptorException);
            RaptorException raptorException = (RaptorException) e;
            Assert.assertTrue(raptorException.getMessage().contains("RaptorException"));
            Assert.assertEquals("b", raptorException.getAttachments().get("a"));
        }
    }


    @TestConfiguration
    public static class TestConfig {
        @Bean
        public Client createTestClient() {
            return new Client() {
                @Override
                public Response execute(Request request, Request.Options options) throws IOException {
                    Map<String, Collection<String>> headers = new HashMap<>();
                    headers.put(RaptorConstants.HEADER_ERROR, Arrays.asList("true"));
                    return Response.builder()
                            .body("{\"code\":500,\"message\":\"RaptorException\",\"attachments\":{\"a\":\"b\"}}", StandardCharsets.UTF_8)
                            .status(500)
                            .headers(headers)
                            .build();
                }
            };
        }
    }
}
