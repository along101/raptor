package com.ppdai.framework.raptor.spring.client.feign;

import com.ppdai.framework.raptor.annotation.RaptorMessage;
import com.ppdai.framework.raptor.spring.converter.RaptorMessageConverter;
import com.ppdai.framework.raptor.spring.utils.HttpHeadersUtils;
import com.ppdai.framework.raptor.spring.utils.RaptorMessageUtils;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author yinzuolong
 */
public class RaptorMessageEncoder implements Encoder {

    private RaptorMessageConverter raptorMessageConverter;

    public RaptorMessageEncoder(RaptorMessageConverter raptorMessageConverter) {
        this.raptorMessageConverter = raptorMessageConverter;
    }

    @Override
    public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
        Class<?> requestType = requestBody.getClass();
        if (AnnotationUtils.findAnnotation(requestType, RaptorMessage.class) != null) {
            if ("GET".equalsIgnoreCase(request.method())) {
                try {
                    Map<String, String> map = RaptorMessageUtils.transferMessageToMap(requestBody);
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        request.query(entry.getKey(), entry.getValue());
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Transfer requestBody to query string error.", e);
                }
            } else {
                FeignRequestOutputMessage outputMessage = new FeignRequestOutputMessage(request);
                try {
                    raptorMessageConverter.write(requestBody,  MediaType.APPLICATION_JSON, outputMessage);
                    request.body(outputMessage.body(), StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    throw new EncodeException("Error converting request body", ex);
                }
                request.headers(HttpHeadersUtils.getHeaders(outputMessage.getHeaders()));
            }
        } else {
            throw new RuntimeException("Can't encode requestBody, bodyType must be RaptorMessage.");
        }
    }
}
