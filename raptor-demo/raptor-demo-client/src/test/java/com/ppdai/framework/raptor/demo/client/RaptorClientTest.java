package com.ppdai.framework.raptor.demo.client;

import com.ppdai.framework.raptor.proto.HelloReply;
import com.ppdai.framework.raptor.proto.HelloRequest;
import com.ppdai.framework.raptor.proto.MoreService;
import com.ppdai.framework.raptor.proto.Simple;
import com.ppdai.framework.raptor.spring.annotation.RaptorClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootClient.class)
public class RaptorClientTest {

    @RaptorClient
    private Simple simple;

    @RaptorClient
    private MoreService moreService;

    @Test
    public void testClient() {
        HelloRequest helloRequest = new HelloRequest();
        helloRequest.setName("ppdai");
        HelloReply reply = simple.sayHello(helloRequest);
        Assert.assertTrue(StringUtils.startsWithIgnoreCase(reply.getMessage(), "Hello"));
    }

    @Test
    public void getGet1() {
        HelloRequest helloRequest = new HelloRequest();
        helloRequest.setName("ppdai");
        HelloReply reply = moreService.testGet1(helloRequest);
        Assert.assertEquals("testGet1", reply.getMessage());
    }

    @Test
    public void testGet2() {
        HelloRequest helloRequest = new HelloRequest();
        helloRequest.setName("ppdai");
        HelloReply reply = moreService.testGet2(helloRequest);
        Assert.assertEquals("testGet2", reply.getMessage());
    }

    @Test
    public void testPost1() {
        HelloRequest helloRequest = new HelloRequest();
        helloRequest.setName("ppdai");
        HelloReply reply = moreService.testPost1(helloRequest);
        Assert.assertEquals("testPost1", reply.getMessage());
    }

    @Test
    public void testPut1() {
        HelloRequest helloRequest = new HelloRequest();
        helloRequest.setName("ppdai");
        HelloReply reply = moreService.testPut1(helloRequest);
        Assert.assertEquals("testPut1", reply.getMessage());
    }

    @Test
    public void testDelete1() {
        HelloRequest helloRequest = new HelloRequest();
        helloRequest.setName("ppdai");
        HelloReply reply = moreService.testDelete1(helloRequest);
        Assert.assertEquals("testDelete1", reply.getMessage());
    }
}
