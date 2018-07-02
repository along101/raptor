package com.ppdai.framework.raptor.spring.client.feign;

import com.ppdai.framework.raptor.spring.utils.HttpHeadersUtils;
import feign.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yinzuolong
 */
public class FeignResponseInputMessage implements HttpInputMessage {

    private Response response;

    public FeignResponseInputMessage(Response response) {
        this.response = response;
    }

    @Override
    public InputStream getBody() throws IOException {
        return response.body().asInputStream();
    }

    @Override
    public HttpHeaders getHeaders() {
        return HttpHeadersUtils.getHttpHeaders(response.headers());
    }
}
