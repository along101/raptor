package com.ppdai.framework.raptor.spring.client.httpclient;

import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yinzuolong
 */
@Configuration
@EnableConfigurationProperties({RaptorHttpClientProperties.class})
@ConditionalOnMissingBean(CloseableHttpClient.class)
@ConditionalOnProperty(name = "raptor.httpclient", havingValue = "apache", matchIfMissing = true)
public class RaptorHttpClientConfiguration {

    @Autowired
    private RaptorHttpClientProperties httpClientProperties;

    @Autowired(required = false)
    private RegistryBuilder registryBuilder;

    private ScheduledExecutorService connectionManagerSchedule;

    private CloseableHttpClient httpClient;

    @Bean
    @ConditionalOnMissingBean(ApacheHttpClientConnectionManagerFactory.class)
    public ApacheHttpClientConnectionManagerFactory connectionManagerFactory() {
        return new ApacheHttpClientConnectionManagerFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApacheHttpClientFactory apacheHttpClientFactory() {
        return new ApacheHttpClientFactory();
    }

    @Bean
    @ConditionalOnMissingBean(HttpClientConnectionManager.class)
    public HttpClientConnectionManager connectionManager(
            ApacheHttpClientConnectionManagerFactory connectionManagerFactory) {
        final HttpClientConnectionManager connectionManager = connectionManagerFactory
                .newConnectionManager(httpClientProperties.isDisableSslValidation(), httpClientProperties.getMaxConnections(),
                        httpClientProperties.getMaxConnectionsPerRoute(),
                        httpClientProperties.getTimeToLive(),
                        httpClientProperties.getTimeToLiveUnit(), registryBuilder);

        CustomizableThreadFactory customizableThreadFactory = new CustomizableThreadFactory("RaptorHttpClient.connectionManager.schedule");
        customizableThreadFactory.setDaemon(true);
        connectionManagerSchedule = new ScheduledThreadPoolExecutor(1, customizableThreadFactory);
        connectionManagerSchedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                connectionManager.closeExpiredConnections();
            }
        }, 30000, httpClientProperties.getConnectionTimerRepeat(), TimeUnit.MILLISECONDS);
        return connectionManager;
    }

    @Bean
    public CloseableHttpClient httpClient(ApacheHttpClientFactory httpClientFactory,
                                          HttpClientConnectionManager httpClientConnectionManager) {
        this.httpClient = httpClientFactory.createHttpClient(httpClientConnectionManager, httpClientProperties);
        return this.httpClient;
    }

    @PreDestroy
    public void destroy() throws Exception {
        if (connectionManagerSchedule != null) {
            connectionManagerSchedule.shutdownNow();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }

}
