package com.ppdai.framework.raptor.spring.client;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端代理Registry
 */
public class RaptorClientRegistry {

    private Map<String, Object> clientCache = new ConcurrentHashMap<>();

    public void registerClientProxy(Class<?> interfaceClass, Object proxy) {
        Assert.notNull(proxy, "proxy object can't be null.");
        String cacheKey = getCacheKey(interfaceClass.getName());
        clientCache.put(cacheKey, proxy);
    }

    public Object get(Class<?> interfaceClass) {
        return clientCache.get(getCacheKey(interfaceClass.getName()));
    }

    public Map<String, Object> getAllRegistered() {
        return Collections.unmodifiableMap(clientCache);
    }

    private String getCacheKey(String interfaceName) {
        return interfaceName;
    }
}
