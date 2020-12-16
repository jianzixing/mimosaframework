package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.MimosaException;

public class SimpleSessionHolder implements SessionHolder {
    private boolean isCacheSession = false;
    private Session session = null;

    @Override
    public Session getSession(SessionFactory factory) throws MimosaException {
        if (this.isCacheSession && this.session != null) {
            return this.session;
        }
        Session session = factory.openSession();
        if (this.isCacheSession) {
            this.session = session;
        }
        return session;
    }

    @Override
    public boolean begin() {
        this.isCacheSession = true;
        return true;
    }

    @Override
    public boolean isSessionTransactional(Session session) {
        return this.isCacheSession && this.session == session;
    }

    @Override
    public boolean end() {
        this.isCacheSession = false;
        return true;
    }

    @Override
    public boolean close() {
        return false;
    }
}
