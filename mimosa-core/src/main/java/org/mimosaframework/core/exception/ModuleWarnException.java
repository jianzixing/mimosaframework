package org.mimosaframework.core.exception;

import java.util.Map;

/**
 * 较高风险的异常，可以用来区别 ModuleException 用来特殊处理
 * 比如 ModuleException日志warn级别，ModuleRiskException日志error级别
 */
public class ModuleWarnException extends ModuleException {
    public ModuleWarnException(ModelCheckerException e) {
        super(e);
    }

    public ModuleWarnException(Exception e) {
        super(e);
    }

    public ModuleWarnException(String code) {
        super(code);
    }

    public ModuleWarnException(String code, String message) {
        super(code, message);
    }

    public ModuleWarnException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

    public ModuleWarnException(String code, Throwable throwable) {
        super(code, throwable);
    }

    public ModuleWarnException(int code) {
        super(code);
    }

    public ModuleWarnException(int code, String message) {
        super(code, message);
    }

    public ModuleWarnException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }

    public ModuleWarnException(Object code) {
        super(code);
    }

    public ModuleWarnException(Object code, String message) {
        super(code, message);
    }

    public ModuleWarnException(Object code, Map<?, ?> args) {
        super(code, args);
    }

    public ModuleWarnException(Object code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
}
