package org.mimosaframework.orm.builder;

public interface Builder<T extends Factory> {
    T buildFactory();

    T buildFactory(String name);
}
