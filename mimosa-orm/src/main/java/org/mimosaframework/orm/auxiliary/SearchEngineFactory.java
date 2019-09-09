package org.mimosaframework.orm.auxiliary;

public interface SearchEngineFactory {
    SearchEngineClient build(String group);
}
