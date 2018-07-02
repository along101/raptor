package com.ppdai.framework.raptor.spring.service;

import com.ppdai.framework.raptor.spring.TestApplication;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
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

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author yinzuolong
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ServiceExceptionTest {
    @Autowired
    private ServerProperties serverProperties;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("server.port", String.valueOf(SocketUtils.findAvailableTcpPort()));
    }

    @AfterClass
    public static void afterClass() {
        System.clearProperty("server.port");
    }

    @Test
    public void testException() throws Exception {
        String url = "http://localhost:" + serverProperties.getPort() + "/more/get2?name=error";
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpUriRequest get = RequestBuilder.get(url)
                .addHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                .build();

        HttpResponse response = httpClient.execute(get);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        response.getEntity().writeTo(bos);
        String result = new String(bos.toByteArray(), StandardCharsets.UTF_8);
        System.out.println(result);
        Assert.assertTrue(result.contains("error!"));
    }


    @Test
    public void testRaptorException() throws Exception {
        String url = "http://localhost:" + serverProperties.getPort() + "/more/get2?name=RaptorException";
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpUriRequest get = RequestBuilder.get(url)
                .addHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                .build();

        HttpResponse response = httpClient.execute(get);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        response.getEntity().writeTo(bos);
        String result = new String(bos.toByteArray(), StandardCharsets.UTF_8);
        System.out.println(result);
        Assert.assertTrue(result.contains("RaptorException!"));
        Assert.assertTrue(result.contains("{\"a\":\"b\"}"));
    }
}
