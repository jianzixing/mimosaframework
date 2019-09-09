package org.mimosaframework.springmvc.exception;

import org.mimosaframework.core.exception.ModelCheckerException;

/**
 * @author yangankang
 */
public class ModuleException extends Exception {

    private StockCode code;

    public ModuleException(ModelCheckerException e) {
        super(e.getMessage());
        this.code = StockCode.ARG_VALID;
    }

    public ModuleException(StockCode code) {
        super();
        this.code = code;
    }

    public ModuleException(StockCode code, String message) {
        super(message);
        this.code = code;
    }

    public ModuleException(StockCode code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public ModuleException(String code) {
        super();
        this.code = new StockCode(code);
    }

    public ModuleException(String code, String message) {
        super(message);
        this.code = new StockCode(code);
    }

    public ModuleException(String code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = new StockCode(code);
    }

    public StockCode getCode() {
        return code;
    }
}
