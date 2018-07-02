package com.ppdai.framework.raptor.demo.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppdai.framework.raptor.proto.HelloRequest;
import com.ppdai.framework.raptor.spring.converter.RaptorJacksonMessageConverter;
import com.ppdai.framework.raptor.spring.utils.RaptorMessageUtils;
import feign.RequestTemplate;
import org.junit.Test;

import java.net.URLDecoder;
import java.util.Map;

/**
 * @author yinzuolong
 */
public class SerializeTest {

    @Test
    public void test() throws Exception {
        HelloRequest request = DemoMessageBuilder.getTestRequest();
        ObjectMapper objectMapper = new RaptorJacksonMessageConverter().getObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        System.out.println(json);

        Map<String, String> map = RaptorMessageUtils.transferMessageToMap(request);
        RequestTemplate requestTemplate = new RequestTemplate();
        map.forEach(requestTemplate::query);

        System.out.println(URLDecoder.decode(requestTemplate.queryLine(),"UTF-8"));

    }

}
