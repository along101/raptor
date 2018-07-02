package com.ppdai.framework.raptor.spring.utils;

import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

/**
 * @author yinzuolong
 */
public class RaptorHandlerUtils {

    public static boolean isRaptorService(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String interfaceName = RaptorInterfaceUtils.getInterfaceName(ClassUtils.getUserClass(handlerMethod.getBean()), handlerMethod.getMethod());
            return !StringUtils.isEmpty(interfaceName);
        }
        return false;
    }
}
