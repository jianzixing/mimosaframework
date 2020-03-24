package org.mimosaframework.orm.platform;

public class SQLReplaceHolder {
    private Object holder;

    public SQLReplaceHolder(Object holder) {
        this.holder = holder;
    }

    public Object getHolder() {
        return holder;
    }

    public void setHolder(Object holder) {
        this.holder = holder;
    }
}
