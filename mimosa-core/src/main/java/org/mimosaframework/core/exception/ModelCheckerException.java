package org.mimosaframework.core.exception;

/**
 * @author yangankang
 */
public class ModelCheckerException extends RuntimeException {
    private String code;
    private String field;

    public ModelCheckerException(String field, String code) {
        super();
        this.field = field;
        this.code = code;
    }

    public ModelCheckerException(String field, String code, String message) {
        super(message);
        this.field = field;
        this.code = code;
    }

    public ModelCheckerException(String field, String code, String message, Throwable throwable) {
        super(message, throwable);
        this.field = field;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getField() {
        return field;
    }
}
