package com.ppdai.framework.raptor.spring.client.httpclient;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author yinzuolong
 */
public class ApacheHttpClientFactory {

    public CloseableHttpClient createHttpClient(HttpClientConnectionManager httpClientConnectionManager,
                                             RaptorHttpClientProperties httpClientProperties) {

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(httpClientProperties.getConnectionTimeout())
                .setSocketTimeout(httpClientProperties.getReadTimeout())
                .setRedirectsEnabled(httpClientProperties.isFollowRedirects())
                .build();

        HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(httpClientProperties.getRetryCount(),
                httpClientProperties.isRequestSentRetryEnabled());

        return HttpClientBuilder.create().disableContentCompression()
                .disableCookieManagement()
                .useSystemProperties()
                .setRetryHandler(retryHandler)
                .setConnectionManager(httpClientConnectionManager)
                .setDefaultRequestConfig(defaultRequestConfig).build();
    }
}
