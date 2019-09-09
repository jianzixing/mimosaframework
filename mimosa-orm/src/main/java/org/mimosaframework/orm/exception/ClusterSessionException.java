package org.mimosaframework.orm.exception;

public class ClusterSessionException extends RuntimeException {
    public ClusterSessionException() {
    }

    public ClusterSessionException(String message) {
        super(message);
    }

    public ClusterSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClusterSessionException(Throwable cause) {
        super(cause);
    }

    public ClusterSessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
