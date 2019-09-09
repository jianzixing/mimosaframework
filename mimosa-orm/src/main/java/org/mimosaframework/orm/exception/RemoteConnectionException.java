package org.mimosaframework.orm.exception;

public class RemoteConnectionException extends Exception {
    public RemoteConnectionException() {
    }

    public RemoteConnectionException(String message) {
        super(message);
    }

    public RemoteConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteConnectionException(Throwable cause) {
        super(cause);
    }

    public RemoteConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
