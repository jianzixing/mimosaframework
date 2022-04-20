package org.mimosaframework.springmvc.utils;

import org.mimosaframework.core.exception.ModelCheckerException;
import org.mimosaframework.core.exception.ModuleException;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.springmvc.exception.StockCode;
import org.mimosaframework.springmvc.i18n.I18n;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author yangankang
 */
public class ResponseMessage<T> implements Serializable {
    protected Object code;
    protected String msg;
    protected T data;

    public static ResponseMessage getInstance() {
        return new ResponseMessage();
    }

    public static ResponseMessage getInstance(int code) {
        return new ResponseMessage(code);
    }

    public static ResponseMessage getInstance(int code, String msg) {
        return new ResponseMessage(code, msg);
    }

    public static ResponseMessage getInstance(int code, Object data) {
        return new ResponseMessage(code, data);
    }

    public static ResponseMessage getInstance(int code, String msg, Object data) {
        return new ResponseMessage(code, msg, data);
    }

    public static ResponseMessage getInstance(String code) {
        return new ResponseMessage(code);
    }

    public static ResponseMessage getInstance(String code, String msg) {
        return new ResponseMessage(code, msg);
    }

    public static ResponseMessage getInstance(String code, Object data) {
        return new ResponseMessage(code, data);
    }

    public static ResponseMessage getInstance(String code, String msg, Object data) {
        return new ResponseMessage(code, msg, data);
    }

    public static ResponseMessage fromString(String str) {
        return ModelObject.parseObject(str, ResponseMessage.class);
    }

    public ResponseMessage() {
        this.code = 100;
        this.msg = I18n.print("success");
    }

    public ResponseMessage(int code) {
        this.code = code;
    }

    public ResponseMessage(Object data) {
        if (data instanceof ModuleException) {
            this.code = ((ModuleException) data).getCode();
            this.msg = ((ModuleException) data).getMessage();
        } else if (data instanceof ModelCheckerException) {
            Object code = ((ModelCheckerException) data).getCode();
            if (code == null) code = -100;
            this.code = code;
            this.msg = ((ModelCheckerException) data).getMessage();
        } else if (data instanceof UndeclaredThrowableException || data instanceof UndeclaredThrowableException) {
            this.code = -9997;
            Throwable causedBy = ((Exception) data);
            int c = 0;
            while (true) {
                c++;
                if (c > 10) break;
                causedBy = causedBy.getCause();
                if (causedBy == null) break;
                if (causedBy instanceof IllegalStateException) {
                    continue;
                }
                String msg = causedBy.getMessage();
                if (StringTools.isNotEmpty(msg)) break;
            }
            if (!this.setMatchMessage(causedBy)) {
                this.msg = I18n.print("access_fail") + "," + causedBy.getClass().getSimpleName() +
                        ":" + causedBy.getMessage();
            }
            ((Exception) data).printStackTrace();
        } else if (data instanceof TransactionException) {
            this.code = -9998;

            int c = 0;
            Throwable throwable = ((TransactionException) data).getCause();
            while (true) {
                if (c > 2) {
                    break;
                }
                Throwable nextThrowable = throwable.getCause();
                if (nextThrowable == null) {
                    break;
                }
                throwable = nextThrowable;
                if (nextThrowable.getCause() == null) {
                    break;
                }
                c++;
            }
            String msg = throwable.getMessage();
            if (!this.setMatchMessage(throwable)) {
                this.msg = I18n.print("trans_fail") + "," + throwable.getClass().getSimpleName() + ":" + msg;
            }
            ((Exception) data).printStackTrace();
        } else if (data instanceof InvocationTargetException) {
            Throwable throwable = ((InvocationTargetException) data);
            if (!this.setMatchMessage(throwable)) {
                if (throwable.getMessage() == null && throwable.getCause() != null) {
                    this.msg = throwable.getCause().getMessage();
                }
                if (throwable.getMessage() != null) {
                    this.msg = throwable.getMessage();
                }
            }
            ((Exception) data).printStackTrace();
        } else if (data instanceof Exception) {
            this.code = -9999;
            if (!this.setMatchMessage(((Exception) data))) {
                this.msg = ((Exception) data).getClass().getSimpleName() + ": " + ((Exception) data).getMessage();
            }
            ((Exception) data).printStackTrace();
        } else {
            this.code = 100;
            this.data = (T) data;
        }
    }

    public boolean success() {
        if (this.code != null && this.code.equals(SUCCESS)) {
            return true;
        }
        return false;
    }

    private boolean setMatchMessage(Throwable throwable) {
        if (throwable != null) {
            if (throwable.getMessage() != null && throwable.getMessage().indexOf("Duplicate entry") >= 0) {
                this.msg = I18n.print("duplicate_unique_field") + ": " + throwable.getMessage();
                return true;
            }
        }
        return false;
    }


    public ResponseMessage(StockCode code, String msg) {
        this.code = code.getCode();
        this.msg = msg;
    }

    public ResponseMessage(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseMessage(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseMessage(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseMessage(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseMessage(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseMessage(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return ModelObject.toJSONString(this);
    }

    public static final int SUCCESS = 100;
}
