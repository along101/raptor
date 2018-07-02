package com.ppdai.framework.raptor.spring.integration;

import com.ppdai.framework.raptor.exception.RaptorException;
import com.ppdai.framework.raptor.proto.HelloRequest;
import com.ppdai.framework.raptor.proto.MoreService;
import com.ppdai.framework.raptor.spring.TestApplication;
import com.ppdai.framework.raptor.spring.annotation.RaptorClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;

/**
 * @author yinzuolong
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RaptorExceptionTest {

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("server.port", String.valueOf(SocketUtils.findAvailableTcpPort()));
    }

    @AfterClass
    public static void afterClass() {
        System.clearProperty("server.port");
    }

    @RaptorClient
    private MoreService moreService;

    @Test
    public void testException() {
        HelloRequest request = new HelloRequest();
        request.setName("error");
        try {
            moreService.testGet2(request);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof RaptorException);
            Assert.assertTrue(e.getMessage().contains("error"));
        }
    }

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
}
