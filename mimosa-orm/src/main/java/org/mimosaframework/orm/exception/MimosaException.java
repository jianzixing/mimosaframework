package org.mimosaframework.orm.exception;

public class MimosaException extends Exception {
    public MimosaException() {
        super();
    }

    public MimosaException(String message) {
        super(message);
    }

    public MimosaException(String message, Throwable cause) {
        super(message, cause);
    }

    public MimosaException(Throwable cause) {
        super(cause);
    }

    protected MimosaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
