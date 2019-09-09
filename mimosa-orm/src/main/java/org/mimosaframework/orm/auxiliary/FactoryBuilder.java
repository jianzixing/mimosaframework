package org.mimosaframework.orm.auxiliary;

public interface FactoryBuilder<T> {
    String getName();

    void loadConfig(FactoryBuilderConfig config);

    T getFactory();
}
