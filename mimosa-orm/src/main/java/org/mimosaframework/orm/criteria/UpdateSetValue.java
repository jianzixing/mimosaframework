package org.mimosaframework.orm.criteria;

public class UpdateSetValue {
    private Object object;
    /**
     * 只有在自增或者自减操作是使用
     */
    private Object step = 1;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getStep() {
        return step;
    }

    public void setStep(Object step) {
        this.step = step;
    }
}
