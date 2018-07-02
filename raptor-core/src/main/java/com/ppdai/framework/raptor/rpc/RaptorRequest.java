package com.ppdai.framework.raptor.rpc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class RaptorRequest implements Serializable {

    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] arguments;
    private Map<String, String> attachments = new HashMap<>();

    public void setAttachment(String key, String value) {
        this.attachments.put(key, value);
    }

    @Override
    public String toString() {
        return "requestId=" + this.getRequestId()
                + " interface=" + this.getInterfaceName()
                + " method=" + this.getMethodName();
    }

}
