package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.MimosaException;

public interface SessionHolder {
    Session getSession(SessionFactory factory) throws MimosaException;

    boolean begin();

    boolean isSessionTransactional(Session session);

    boolean end();

    boolean close();
}
