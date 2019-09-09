package org.mimosaframework.orm.exception;

public class SQLInsertException extends RuntimeException {
    public SQLInsertException() {
        super();
    }

    public SQLInsertException(String message) {
        super(message);
    }

    public SQLInsertException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLInsertException(Throwable cause) {
        super(cause);
    }

    protected SQLInsertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
