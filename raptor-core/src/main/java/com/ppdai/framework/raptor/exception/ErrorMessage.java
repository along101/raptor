package com.ppdai.framework.raptor.exception;

import com.ppdai.framework.raptor.annotation.RaptorField;

import java.util.Map;

/**
 * @author yinzuolong
 */
public class ErrorMessage {

    @RaptorField(
            fieldType = "int32",
            order = 1,
            name = "code"
    )
    private int code;

    @RaptorField(
            fieldType = "string",
            order = 2,
            name = "message"
    )
    private String message;

    @RaptorField(
            fieldType = "string",
            keyType = "string",
            order = 3,
            name = "attachments"
    )
    private Map<String, String> attachments;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments;
    }
}
