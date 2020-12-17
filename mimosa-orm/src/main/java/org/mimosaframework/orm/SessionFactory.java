package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.MimosaException;

public interface SessionFactory {

    Session openSession() throws MimosaException;

    Session getCurrentSession() throws MimosaException;
}
