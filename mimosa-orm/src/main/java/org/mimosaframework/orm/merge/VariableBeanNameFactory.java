package org.mimosaframework.orm.merge;

public class VariableBeanNameFactory {
    private static final VariableBeanName variableBeanName = new DefaultVariableBeanName();

    public static VariableBeanName getVariableBeanName() {
        return variableBeanName;
    }
}
