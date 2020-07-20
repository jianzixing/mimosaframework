package org.mimosaframework.orm.criteria;

public class UpdateSetValue {
    private UpdateSpecialType type;
    /**
     * 只有在自增或者自减操作是使用
     */
    private Number step = 1;

    public UpdateSpecialType getType() {
        return type;
    }

    public void setType(UpdateSpecialType type) {
        this.type = type;
    }

    public Number getStep() {
        return step;
    }

    public void setStep(Number step) {
        this.step = step;
    }
}
