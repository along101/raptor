package com.ppdai.framework.raptor.spring.client.feign;

import com.ppdai.framework.raptor.spring.utils.HttpHeadersUtils;
import feign.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yinzuolong
 */
public class FeignResponseAdapter implements ClientHttpResponse {

    private final Response response;

    public FeignResponseAdapter(Response response) {
        this.response = response;
    }

    @Override
    public HttpStatus getStatusCode() throws IOException {
        return HttpStatus.valueOf(this.response.status());
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return this.response.status();
    }

    @Override
    public String getStatusText() throws IOException {
        return this.response.reason();
    }

    @Override
    public void close() {
        try {
            this.response.body().close();
        } catch (IOException ex) {
            // Ignore exception on close...
        }
    }

    @Override
    public InputStream getBody() throws IOException {
        return this.response.body().asInputStream();
    }

    @Override
    public HttpHeaders getHeaders() {
        return HttpHeadersUtils.getHttpHeaders(this.response.headers());
    }

}