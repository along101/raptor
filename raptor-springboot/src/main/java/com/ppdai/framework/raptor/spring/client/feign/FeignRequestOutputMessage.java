package com.ppdai.framework.raptor.spring.client.feign;

import com.ppdai.framework.raptor.spring.utils.HttpHeadersUtils;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author yinzuolong
 */
public class FeignRequestOutputMessage implements HttpOutputMessage {
    private HttpHeaders httpHeaders;
    private ByteArrayOutputStream buf;

    public FeignRequestOutputMessage(RequestTemplate request) {
        this.httpHeaders = HttpHeadersUtils.getHttpHeaders(request.headers());
        this.buf = new ByteArrayOutputStream();
    }

    @Override
    public OutputStream getBody() throws IOException {
        return this.buf;
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }

    public byte[] body() {
        return this.buf.toByteArray();
    }
}