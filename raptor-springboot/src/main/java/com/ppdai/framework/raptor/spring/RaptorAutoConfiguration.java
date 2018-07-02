package com.ppdai.framework.raptor.spring;

import com.ppdai.framework.raptor.spring.converter.RaptorJacksonMessageConverter;
import com.ppdai.framework.raptor.spring.converter.RaptorMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yinzuolong
 */
@Configuration
public class RaptorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RaptorMessageConverter createRaptorMessageConverter() {
        return new RaptorJacksonMessageConverter();
    }
}
