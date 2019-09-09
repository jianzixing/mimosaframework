package org.mimosaframework.core.exception;

/**
 * @author yangankang
 */
public class ModelCheckerException extends Exception {
    private Enum code;
    private String field;

    public ModelCheckerException(String field,Enum code) {
        super();
        this.field = field;
        this.code = code;
    }

    public ModelCheckerException(String field,Enum code, String message) {
        super(message);
        this.field = field;
        this.code = code;
    }

    public ModelCheckerException(String field,Enum code, String message, Throwable throwable) {
        super(message, throwable);
        this.field = field;
        this.code = code;
    }

    public Enum getCode() {
        return code;
    }

    public String getField() {
        return field;
    }
}
