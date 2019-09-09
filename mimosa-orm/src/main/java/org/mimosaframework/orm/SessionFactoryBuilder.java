package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.ContextException;

import java.io.InputStream;

public interface SessionFactoryBuilder {
    SessionFactory build() throws ContextException;
}
