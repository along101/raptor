package com.ppdai.framework.raptor.spring.client.feign;

import com.ppdai.framework.raptor.common.RaptorConstants;
import com.ppdai.framework.raptor.exception.ErrorMessage;
import com.ppdai.framework.raptor.exception.RaptorException;
import com.ppdai.framework.raptor.spring.converter.RaptorMessageConverter;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author yinzuolong
 */
public class RaptorErrorDecoder implements ErrorDecoder {

    private RaptorMessageConverter raptorMessageConverter;

    public RaptorErrorDecoder(RaptorMessageConverter raptorMessageConverter) {
        this.raptorMessageConverter = raptorMessageConverter;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        Collection<String> raptorErrorHeaders = response.headers().get(RaptorConstants.HEADER_ERROR);
        String raptorError = raptorErrorHeaders.iterator().hasNext() ? raptorErrorHeaders.iterator().next() : null;
        if ("true".equals(raptorError)) {
            try {
                ErrorMessage errorMessage = (ErrorMessage) raptorMessageConverter.read(ErrorMessage.class, new FeignResponseInputMessage(response));
                return new RaptorException(errorMessage);
            } catch (IOException e) {
                return new RaptorException(getResponseBodyString(response));
            }
        } else {
            return new RaptorException(getResponseBodyString(response));
        }
    }

    private String getResponseBodyString(Response response) {
        try {
            return StreamUtils.copyToString(response.body().asInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "error read response body. " + e.getMessage();
        }
    }
}
