package com.ppdai.framework.raptor.spring.client;

import com.ppdai.framework.raptor.annotation.RaptorInterface;
import com.ppdai.framework.raptor.spring.annotation.RaptorClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

public class RaptorClientPostProcessor implements BeanPostProcessor {
    @Autowired
    private RaptorClientRegistry raptorClientRegistry;
    @Autowired
    private ObjectProvider<List<RaptorClientFactory>> raptorClientFactories;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = ClassUtils.getUserClass(bean);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                RaptorClient reference = field.getAnnotation(RaptorClient.class);
                if (reference != null) {
                    ReflectionUtils.makeAccessible(field);
                    if (field.get(bean) != null) {
                        //字段已经初始化
                        continue;
                    }
                    Object value = getClientProxy(field.getType());
                    if (value != null) {
                        field.set(bean, value);
                    } else {
                        throw new BeanInitializationException(String.format("Can not find Object %s.", bean.getClass().getName()));
                    }
                }
            } catch (Exception e) {
                throw new BeanInitializationException(String.format("Failed to build remote service reference at filed %s in class %s",
                        field.getName(), bean.getClass().getName()), e);
            }
        }
        return bean;
    }

    private Object getClientProxy(Class<?> interfaceClass) {
        RaptorInterface raptorInterface = AnnotationUtils.findAnnotation(interfaceClass, RaptorInterface.class);
        if (raptorInterface == null) {
            return null;
        }
        Object clientProxy = this.raptorClientRegistry.get(interfaceClass);
        if (clientProxy != null) {
            return clientProxy;
        }
        List<RaptorClientFactory> factories = this.raptorClientFactories.getIfAvailable();
        for (RaptorClientFactory raptorClientFactory : factories) {
            if (raptorClientFactory.support(interfaceClass)) {
                clientProxy = raptorClientFactory.create(interfaceClass);
                this.raptorClientRegistry.registerClientProxy(interfaceClass, clientProxy);
                break;
            }
        }
        return clientProxy;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
