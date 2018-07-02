package com.ppdai.framework.raptor.spring.service;

import com.ppdai.framework.raptor.spring.TestApplication;
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

/**
 * @author yinzuolong
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
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
    public void testService() {
        String url = "http://localhost:" + serverProperties.getPort() + "/more?name=ppdai";
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);
        Assert.assertTrue(response.contains("ppdai"));
    }
}
