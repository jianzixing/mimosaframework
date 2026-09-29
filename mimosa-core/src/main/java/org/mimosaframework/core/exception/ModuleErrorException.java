package org.mimosaframework.core.exception;

import java.util.Map;

/**
 * 较高风险的异常，可以用来区别 ModuleException 用来特殊处理
 * 比如 ModuleException日志warn级别，ModuleRiskException日志error级别
 */
public class ModuleErrorException extends ModuleException {
    public ModuleErrorException(ModelCheckerException e) {
        super(e);
    }

    public ModuleErrorException(Exception e) {
        super(e);
    }

    public ModuleErrorException(String code) {
        super(code);
    }

    public ModuleErrorException(String code, String message) {
        super(code, message);
    }

    public ModuleErrorException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

    public ModuleErrorException(String code, Throwable throwable) {
        super(code, throwable);
    }

    public ModuleErrorException(int code) {
        super(code);
    }

    public ModuleErrorException(int code, String message) {
        super(code, message);
    }

    public ModuleErrorException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

    public ModuleErrorException(Object code) {
        super(code);
    }

    public ModuleErrorException(Object code, String message) {
        super(code, message);
    }

    public ModuleErrorException(Object code, Map<?, ?> args) {
        super(code, args);
    }

    public ModuleErrorException(Object code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
}
