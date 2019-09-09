package org.mimosaframework.orm.builder;

public interface Factory<T> {
    T build() throws Exception;
}
