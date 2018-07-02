package com.ppdai.framework.raptor.spring.service;

import com.ppdai.framework.raptor.rpc.RaptorServiceInterceptor;
import com.ppdai.framework.raptor.spring.converter.RaptorMessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Import({RaptorHandlerMappingPostProcessor.class,
        RaptorHandlerAdapterPostProcessor.class,
        RaptorHandlerMethodProcessor.class})
@Configuration
public class RaptorServiceAutoConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private RaptorMessageConverter raptorMessageConverter;

    @Autowired
    private ObjectProvider<List<RaptorServiceInterceptor>> handlerInterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //先增加RaptorContextInitHandlerInterceptor，其他拦截器之前先初始化RaptorContext
        registry.addInterceptor(new RaptorContextInitHandlerInterceptor());

        List<RaptorServiceInterceptor> handlerInterceptorList = handlerInterceptors.getIfAvailable();
        if (CollectionUtils.isEmpty(handlerInterceptorList)) {
            return;
        }
        handlerInterceptorList.sort(new AnnotationAwareOrderComparator());
        for (RaptorServiceInterceptor raptorServiceInterceptor : handlerInterceptorList) {
            registry.addInterceptor(new RaptorHandlerInterceptorAdapter(raptorServiceInterceptor));
        }
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new RaptorHandlerExceptionResolver(raptorMessageConverter));
    }

}
