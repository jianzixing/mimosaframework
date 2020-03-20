package org.mimosaframework.orm.sql.test;

import java.io.Serializable;

public interface OperatorFunctionBuilder<T>
        extends
        OperatorBuilder<AbsWhereValueBuilder<T>> {
    T isNull(Serializable field);

    T isNull(Class table, Serializable field);

    T isNull(String aliasName, Serializable field);
    
    T isNotNull(Serializable field);

    T isNotNull(Class table, Serializable field);

    T isNotNull(String aliasName, Serializable field);
}