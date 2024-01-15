package com.stanzaliving.inception.error;

public class PreconditionFailedException extends RuntimeException {

    private static final long serialVersionUID = -3368655266237942363L;
    private String code;

    public PreconditionFailedException(String message) {
        super(message);
    }

    public PreconditionFailedException(Throwable cause) {
        super(cause);
    }

    public PreconditionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreconditionFailedException(String message, String code) {
        super(message);
        this.code = code;
    }

}

