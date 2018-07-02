package com.ppdai.framework.raptor.spring.client.feign;

import com.ppdai.framework.raptor.annotation.RaptorMessage;
import com.ppdai.framework.raptor.spring.converter.RaptorMessageConverter;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author yinzuolong
 */
public class RaptorMessageDecoder implements Decoder {
    private RaptorMessageConverter raptorMessageConverter;

    public RaptorMessageDecoder(RaptorMessageConverter raptorMessageConverter) {
        this.raptorMessageConverter = raptorMessageConverter;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        if (type instanceof Class && AnnotationUtils.findAnnotation((Class) type, RaptorMessage.class) != null) {
            return raptorMessageConverter.read((Class) type, new FeignResponseAdapter(response));
        }
        throw new RuntimeException("Can't decode response, Return Type must be RaptorMessage.");
    }
}
