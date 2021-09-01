package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.i18n.I18n;

import java.sql.SQLException;

public class MimosaSessionFactory implements SessionFactory {
    private Configuration configuration;
    private Session currentSession;

    public MimosaSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Session openSession() throws MimosaException {
        Session session = null;
        try {
            session = this.configuration.buildSession();
        } catch (SQLException e) {
            throw new MimosaException(I18n.print("create_new_session_error"), e);
        }
        currentSession = session;
        return session;
    }

    @Override
    public Session getCurrentSession() throws MimosaException {
        return currentSession;
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }
}
