package org.mimosaframework.core.exception;

/**
 * @author yangankang
 */
public class ModuleException extends RuntimeException {

    private Object code;

    public ModuleException(ModelCheckerException e) {
        super(e.getMessage());
        this.code = -100;
    }

    public ModuleException(String code) {
        super();
        this.code = code;
    }

    public ModuleException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ModuleException(String code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public ModuleException(int code) {
        super();
        this.code = code;
    }

    public ModuleException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ModuleException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public Object getCode() {
        return code;
    }
}
