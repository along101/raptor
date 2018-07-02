package com.ppdai.framework.raptor.spring.client.httpclient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@ConfigurationProperties(prefix = "raptor.httpclient")
public class RaptorHttpClientProperties {
    public static final boolean DEFAULT_DISABLE_SSL_VALIDATION = false;
    public static final int DEFAULT_MAX_CONNECTIONS = 200;
    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;
    public static final long DEFAULT_TIME_TO_LIVE = 900L;
    public static final TimeUnit DEFAULT_TIME_TO_LIVE_UNIT = TimeUnit.SECONDS;
    public static final boolean DEFAULT_FOLLOW_REDIRECTS = false;
    public static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 30000;
    public static final int DEFAULT_CONNECTION_TIMER_REPEAT = 10000;
    public static final int DEFAULT_RETRY_COUNT = 0;
    public static final boolean DEFAULT_RETRY_ENABLE = false;


    private boolean disableSslValidation = DEFAULT_DISABLE_SSL_VALIDATION;
    private int maxConnections = DEFAULT_MAX_CONNECTIONS;
    private int maxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;
    private long timeToLive = DEFAULT_TIME_TO_LIVE;
    private TimeUnit timeToLiveUnit = DEFAULT_TIME_TO_LIVE_UNIT;
    private boolean followRedirects = DEFAULT_FOLLOW_REDIRECTS;
    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private int readTimeout = DEFAULT_SOCKET_TIMEOUT;
    private int connectionTimerRepeat = DEFAULT_CONNECTION_TIMER_REPEAT;
    private int retryCount = DEFAULT_RETRY_COUNT;
    private boolean requestSentRetryEnabled = DEFAULT_RETRY_ENABLE;
}
