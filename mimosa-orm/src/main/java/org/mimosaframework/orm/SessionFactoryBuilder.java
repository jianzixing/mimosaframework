package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.ContextException;

public interface SessionFactoryBuilder {
    SessionFactory build() throws ContextException;
}
