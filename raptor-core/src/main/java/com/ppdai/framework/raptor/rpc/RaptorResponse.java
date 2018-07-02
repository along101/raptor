package com.ppdai.framework.raptor.rpc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RaptorResponse implements Serializable {
    private static final long serialVersionUID = 4281186647291615871L;

    private int code = -1;
    private Object value;
    private Throwable exception;
    private String requestId;

    private Map<String, String> attachments = new HashMap<>();

    public RaptorResponse() {
    }

    public RaptorResponse(String requestId) {
        this.requestId = requestId;
    }

    public Object getValue() {
        if (exception != null) {
            throw new RuntimeException(exception);
        }
        return value;
    }

    public void setAttachment(String key, String value) {
        if (this.attachments == null) {
            this.attachments = new HashMap<>();
        }
        this.attachments.put(key, value);
    }

}
