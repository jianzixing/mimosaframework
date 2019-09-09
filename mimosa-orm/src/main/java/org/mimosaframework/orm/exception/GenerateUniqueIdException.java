package org.mimosaframework.orm.exception;

public class GenerateUniqueIdException extends Exception {
    public GenerateUniqueIdException() {
    }

    public GenerateUniqueIdException(String message) {
        super(message);
    }

    public GenerateUniqueIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenerateUniqueIdException(Throwable cause) {
        super(cause);
    }

    public GenerateUniqueIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
