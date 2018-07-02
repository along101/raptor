package com.ppdai.framework.raptor.rpc;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yinzuolong
 */
@Getter
@Setter
public class RaptorContext {

    private Map<Object, Object> attributes = new ConcurrentHashMap<>();
    private Map<String, String> requestAttachments = new ConcurrentHashMap<>();
    private Map<String, String> responseAttachments = new ConcurrentHashMap<>();
    private RaptorRequest request;
    private RaptorResponse response;

    private static final ThreadLocal<RaptorContext> CONTEXT = new ThreadLocal<RaptorContext>() {
        @Override
        protected RaptorContext initialValue() {
            return new RaptorContext();
        }
    };

    public static RaptorContext getContext() {
        return CONTEXT.get();
    }

    public void putAttribute(Object key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(Object key) {
        return attributes.get(key);
    }

    public void putRequestAttachment(String key, String value) {
        requestAttachments.put(key, value);
    }

    public String getRequestAttachment(String key) {
        return requestAttachments.get(key);
    }

    public void putResponseAttachment(String key, String value) {
        responseAttachments.put(key, value);
    }

    public String getResponseAttachment(String key) {
        return responseAttachments.get(key);
    }

}
