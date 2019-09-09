package org.mimosaframework.orm.auxiliary;

public interface RPCFactory {
    <T> T getObject(Class<T> c);

    void register(Object o);
}
