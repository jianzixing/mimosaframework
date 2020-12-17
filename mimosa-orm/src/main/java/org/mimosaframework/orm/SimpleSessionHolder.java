package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.transaction.TransactionManagerUtils;

import java.io.IOException;

public class SimpleSessionHolder implements SessionHolder {
    private Session session = null;

    @Override
    public Session getSession(SessionFactory factory) throws MimosaException {
        if (TransactionManagerUtils.isTransactional()) {
            if (this.session != null) {
                return this.session;
            } else {
                this.session = factory.openSession();
                TransactionManagerUtils.addSessionHolder(factory, this);
                return this.session;
            }
        } else {
            Session session = factory.openSession();
            this.session = session;
            return session;
        }
    }

    @Override
    public boolean begin() {
        return true;
    }

    @Override
    public boolean isSessionTransactional(Session session) {
        return TransactionManagerUtils.isTransactional() && this.session == session;
    }

    @Override
    public boolean end() {
        return true;
    }

    @Override
    public boolean close() {
        if (!TransactionManagerUtils.isTransactional() && this.session != null) {
            try {
                this.session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
