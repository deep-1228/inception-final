package com.stanzaliving.inception.error;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Data
@ToString(callSuper = true)
public class WFException extends RuntimeException {
    private static final long serialVersionUID = -4075250719863628707L;

    private final String message;
    private final String code;
    private final Throwable error;

    public WFException() {
        this(StringUtils.EMPTY, StringUtils.EMPTY, null);
    }

    public WFException(String message) {
        this(message, StringUtils.EMPTY, null);
    }

    public WFException(Throwable cause) {
        this(StringUtils.EMPTY, StringUtils.EMPTY, cause);
    }

    public WFException(String message, String code) {
        this(message, code, null);
    }

    public WFException(String message, Throwable cause) {
        this(message, StringUtils.EMPTY, cause);
    }

    public WFException(String message, String code, Throwable error) {
        this.code = code;
        this.message = message;
        this.error = error;
    }
}
