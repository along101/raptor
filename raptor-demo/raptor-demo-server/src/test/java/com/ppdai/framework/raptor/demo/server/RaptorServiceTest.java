package com.ppdai.framework.raptor.demo.server;

import com.ppdai.framework.raptor.proto.HelloReply;
import com.ppdai.framework.raptor.proto.HelloRequest;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootServer.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RaptorServiceTest {

    @Autowired
    private ServerProperties serverProperties;

    private RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("server.port", String.valueOf(SocketUtils.findAvailableTcpPort()));
    }

    @AfterClass
    public static void afterClass() {
        System.clearProperty("server.port");
    }

    @Test
    public void testServer() throws Exception {
        String url = "http://localhost:" + serverProperties.getPort() + "/raptor/com.ppdai.framework.raptor.proto.Simple/sayHello";
        HelloRequest request = new HelloRequest();
        request.setName("ppdai");
        HelloReply reply = restTemplate.postForObject(url, request, HelloReply.class);
        System.out.println(reply.getRequest().getName());
        Assert.assertTrue(reply.getMessage().contains("Hello ppdai"));
    }

}
