package org.mimosaframework.core.exception;

/**
 * @author yangankang
 */
public class ModuleForceException extends Exception {

    private Object code;

    public ModuleForceException(ModelCheckerException e) {
        super(e.getMessage());
        this.code = -100;
        e.printStackTrace();
    }

    public ModuleForceException(Exception e) {
        super(e.getMessage());
        this.code = -100;
        if (e instanceof ModuleForceException) {
            this.code = ((ModuleForceException) e).code;
        }
        if (!(e instanceof ModuleForceException) && !(e instanceof ModelCheckerException)) {
            e.printStackTrace();
        }
    }

    public ModuleForceException(String code) {
        super();
        this.code = code;
    }

    public ModuleForceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ModuleForceException(String code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
        throwable.printStackTrace();
    }

    public ModuleForceException(String code, Throwable throwable) {
        super(throwable.getMessage(), throwable);
        this.code = code;
        if (!(throwable instanceof ModuleForceException) && !(throwable instanceof ModelCheckerException)) {
            throwable.printStackTrace();
        }
    }

    public ModuleForceException(int code) {
        super();
        this.code = code;
    }

    public ModuleForceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ModuleForceException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
        throwable.printStackTrace();
    }

    public Object getCode() {
        return code;
    }
}
