package com.ppdai.framework.raptor.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yinzuolong
 */
@Getter
@Setter
public class RaptorException extends RuntimeException {

    private int code;
    private String message;
    private Map<String, String> attachments = new HashMap<>();

    public RaptorException(ErrorMessage errorMessage) {
        super();
        this.code = errorMessage.getCode();
        this.message = errorMessage.getMessage();
        this.attachments = errorMessage.getAttachments();
    }

    public RaptorException(String message) {
        super(message);
        this.message = message;
    }

    public RaptorException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public RaptorException(int code, String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = code;
    }

    public RaptorException(int code, String message, Map<String, String> attachments, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = code;
        if (attachments != null) {
            this.attachments = attachments;
        }
    }

    public RaptorException putAttachment(String name, String value) {
        if (this.attachments != null) {
            this.attachments.put(name, value);
        }
        return this;
    }
}
