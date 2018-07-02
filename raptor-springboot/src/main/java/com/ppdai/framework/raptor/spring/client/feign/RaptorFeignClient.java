package com.ppdai.framework.raptor.spring.client.feign;

import com.ppdai.framework.raptor.common.RaptorConstants;
import com.ppdai.framework.raptor.rpc.RaptorContext;
import com.ppdai.framework.raptor.rpc.RaptorResponse;
import com.ppdai.framework.raptor.utils.UrlUtils;
import feign.Client;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @author yinzuolong
 */
@Slf4j
public class RaptorFeignClient implements Client {

    public static final String NAME_REQUEST_TIME = "raptor-client-http-time";
    public static final String NAME_HTTP_URI = "raptor-client-http-uri";
    public static final String NAME_HTTP_METHOD = "raptor-client-http-method";

    private final Client client;

    public RaptorFeignClient(Client client) {
        this.client = client;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        Response response = null;
        Exception ex = null;
        long start = System.nanoTime();
        try {
            preHandle(request, options);
            response = client.execute(request, options);
            postHandle(request, response);
            return response;
        } catch (Exception e) {
            ex = e;
            if (e instanceof IOException) {
                throw (IOException) e;
            }
            throw new RuntimeException("Request execute error.", ex);
        } finally {
            long cost = System.nanoTime() - start;
            afterCompletion(request, response, ex, cost);
        }
    }

    protected void preHandle(Request request, Request.Options options) throws Exception {
        //设置url和method到Context中，方便其他地方取
        RaptorContext.getContext().putAttribute(NAME_HTTP_URI, UrlUtils.getUri(request.url()));
        RaptorContext.getContext().putAttribute(NAME_HTTP_METHOD, request.method());
    }

    protected void postHandle(Request request, Response response) throws Exception {
        RaptorResponse raptorResponse = RaptorContext.getContext().getResponse();
        raptorResponse.setCode(response.status());
    }

    protected void afterCompletion(Request request, Response response, Exception ex, long cost) {
        setResponseHeader(response);
        RaptorContext.getContext().putAttribute(NAME_REQUEST_TIME, cost);
    }

    protected void setResponseHeader(Response response) {
        if (response != null) {
            RaptorResponse raptorResponse = RaptorContext.getContext().getResponse();
            Map<String, Collection<String>> headers = response.headers();
            for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
                String value = entry.getValue().iterator().next();
                //设置头到RaptorContext response中
                raptorResponse.setAttachment(entry.getKey(), value);

                //responseAttachment，传递Response头
                if (entry.getKey().toLowerCase().startsWith(RaptorConstants.HEADER_TRACE_PREFIX)) {
                    RaptorContext.getContext().putResponseAttachment(entry.getKey(), value);
                }
            }
        }
    }
}
