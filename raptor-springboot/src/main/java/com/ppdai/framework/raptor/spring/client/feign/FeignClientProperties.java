package com.ppdai.framework.raptor.spring.client.feign;

import feign.Contract;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties("raptor.client")
public class FeignClientProperties {

    private String defaultConfig = "default";

    private Map<String, RaptorClientConfiguration> config = new HashMap<>();

    @Setter
    @Getter
    public static class RaptorClientConfiguration {

        private String url;

        private Logger.Level loggerLevel;

        private Integer connectTimeout;

        private Integer readTimeout;

        private Class<Retryer> retryer;

        private Class<ErrorDecoder> errorDecoder;

        private List<Class<RequestInterceptor>> requestInterceptors;

        private Class<Decoder> decoder;

        private Class<Encoder> encoder;

        private Class<Contract> contract;

    }

}
