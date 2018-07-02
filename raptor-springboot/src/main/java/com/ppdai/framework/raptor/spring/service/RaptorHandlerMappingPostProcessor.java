package com.ppdai.framework.raptor.spring.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 后置处理器，解决springMVC接口方法参数的注解无法被继承的问题。
 *
 * @author yinzuolong
 */
public class RaptorHandlerMappingPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (RequestMappingHandlerMapping.class.isAssignableFrom(ClassUtils.getUserClass(bean))) {
            RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) bean;
            Map<RequestMappingInfo, HandlerMethod> maps = mapping.getHandlerMethods();
            for (HandlerMethod handlerMethod : maps.values()) {
                MethodParameter[] parameters = handlerMethod.getMethodParameters();
                for (int i = 0; i < parameters.length; i++) {
                    MethodParameter parameter = parameters[i];
                    Annotation[] parameterAnnotation = parameter.getParameterAnnotations();
                    Method superMethod = findInterfaceMethod(parameter.getMethod());
                    if (superMethod == null) {
                        continue;
                    }
                    Annotation[][] superMethodParameterAnnotations = superMethod.getParameterAnnotations();
                    Annotation[] superParameterAnnotation = superMethodParameterAnnotations[parameter.getParameterIndex()];
                    Annotation[] newParameterAnnotation = new Annotation[parameterAnnotation.length + superParameterAnnotation.length];
                    System.arraycopy(parameterAnnotation, 0, newParameterAnnotation, 0, parameterAnnotation.length);
                    System.arraycopy(superParameterAnnotation, 0, newParameterAnnotation, parameterAnnotation.length, superParameterAnnotation.length);
                    MethodParameter newParameter = new MethodParameter(parameter) {
                        /**
                         * 覆盖父类方法，返回合并接口注解的数组
                         * @return
                         */
                        @Override
                        public Annotation[] getParameterAnnotations() {
                            return newParameterAnnotation;
                        }
                    };
                    parameters[i] = newParameter;

                }
            }
        }
        return bean;
    }

    /**
     * 找到方法实现的接口方法
     * @param method
     * @return
     */
    private Method findInterfaceMethod(Method method) {
        Set<Class<?>> interfaces = ClassUtils.getAllInterfacesForClassAsSet(method.getDeclaringClass());
        for (Class<?> superInterface : interfaces) {
            Method superMethod = null;
            try {
                superMethod = superInterface.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                //忽略异常
            }
            if (superMethod != null) {
                return superMethod;
            }
        }
        return null;
    }

}
