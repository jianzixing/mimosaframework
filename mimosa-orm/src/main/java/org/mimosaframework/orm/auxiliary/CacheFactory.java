package org.mimosaframework.orm.auxiliary;

public interface CacheFactory {
    CacheClient build(String group);
}
