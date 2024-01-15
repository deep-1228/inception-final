package com.stanzaliving.inception.error;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Data
@ToString(callSuper = true)
public class WFValidationException extends RuntimeException {
    private static final long serialVersionUID = -4075250719863628707L;

    private final String message;
    private final String code;
    private final Throwable error;

    public WFValidationException() {
        this(StringUtils.EMPTY, StringUtils.EMPTY, null);
    }

    public WFValidationException(String message) {
        this(message, StringUtils.EMPTY, null);
    }

    public WFValidationException(Throwable cause) {
        this(StringUtils.EMPTY, StringUtils.EMPTY, cause);
    }

    public WFValidationException(String message, String code) {
        this(message, code, null);
    }

    public WFValidationException(String message, Throwable cause) {
        this(message, StringUtils.EMPTY, cause);
    }

    public WFValidationException(String message, String code, Throwable error) {
        this.code = code;
        this.message = message;
        this.error = error;
    }
}
