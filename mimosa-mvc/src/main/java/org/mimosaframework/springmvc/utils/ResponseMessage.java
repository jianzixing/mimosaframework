package org.mimosaframework.springmvc.utils;

import org.mimosaframework.core.exception.ModelCheckerException;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.springmvc.exception.ModuleException;
import org.mimosaframework.springmvc.exception.StockCode;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author yangankang
 */
public class ResponseMessage {

    private Object code;
    private String msg;
    private Object data;

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

    public ResponseMessage() {
        this.code = 100;
        this.msg = "处理成功";
    }

    public ResponseMessage(int code) {
        this.code = code;
    }

    public ResponseMessage(Object data) {
        if (data instanceof ModuleException) {
            this.code = ((ModuleException) data).getCode().getCode();
            this.msg = ((ModuleException) data).getMessage();
            ((ModuleException) data).printStackTrace();
        } else if (data instanceof ModelCheckerException) {
            int code = ((ModelCheckerException) data).getCode().ordinal();
            if (code == 0) code = -100;
            this.code = code;
            this.msg = ((ModelCheckerException) data).getMessage();
            ((ModelCheckerException) data).printStackTrace();
        } else if (data instanceof UndeclaredThrowableException || data instanceof UndeclaredThrowableException) {
            this.code = -9997;
            Throwable causedBy = ((Exception) data);
            int c = 0;
            while (true) {
                c++;
                if (c > 10) break;
                causedBy = causedBy.getCause();
                if (causedBy instanceof IllegalStateException) {
                    continue;
                }
                String msg = causedBy.getMessage();
                if (StringTools.isNotEmpty(msg)) break;
                if (causedBy == null) break;
            }
            if (!this.setMatchMessage(causedBy)) {
                this.msg = "访问失败," + causedBy.getClass().getSimpleName() +
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
                throwable = throwable.getCause();
                if (throwable.getCause() == null) {
                    break;
                }
                c++;
            }
            String msg = throwable.getMessage();
            if (!this.setMatchMessage(throwable)) {
                this.msg = "执行事务失败," + throwable.getClass().getSimpleName() + ":" + msg;
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
            this.data = data;
        }
    }

    private boolean setMatchMessage(Throwable throwable) {
        if (throwable != null) {
            if (throwable.getMessage().indexOf("Duplicate entry") >= 0) {
                this.msg = "唯一字段重复: " + msg;
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

    public ResponseMessage(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public ResponseMessage(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseMessage(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseMessage(String code, Object data) {
        this.code = code;
        this.data = data;
    }

    public ResponseMessage(String code, String msg, Object data) {
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toString() {
        return ModelObject.toJSONString(this);
    }

    public static final int SUCCESS = 100;
}
