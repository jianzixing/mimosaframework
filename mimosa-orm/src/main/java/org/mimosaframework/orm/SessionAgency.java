package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class SessionAgency implements Session {
    private ContextContainer context;
    private Session session;

    public SessionAgency(ContextContainer context) {
        this.context = context;
        this.session = this.buildRealSession(this.context);
    }

    private Session buildRealSession(ContextContainer context) {
        return new DefaultSession(context);
    }

    @Override
    public ModelObject save(ModelObject obj) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                return session.save(obj);
            }
            return session.save(obj);
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public ModelObject saveAndUpdate(ModelObject obj) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                return session.saveAndUpdate(obj);
            }
            return session.saveAndUpdate(obj);
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public void save(List<ModelObject> objects) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                session.save(objects);
            } else {
                session.save(objects);
            }
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public void update(ModelObject obj) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                session.update(obj);
            } else {
                session.update(obj);
            }
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public void update(List<ModelObject> objects) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                session.update(objects);
            } else {
                session.update(objects);
            }
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public long update(Update update) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                return session.update(update);
            }
            return session.update(update);
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public void delete(ModelObject obj) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                session.delete(obj);
            } else {
                session.delete(obj);
            }
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public void delete(List<ModelObject> objects) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                session.delete(objects);
            } else {
                session.delete(objects);
            }
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public long delete(Delete delete) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                return session.delete(delete);
            }
            return session.delete(delete);
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public void delete(Class c, Serializable id) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                session.delete(c, id);
            } else {
                session.delete(c, id);
            }
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public ModelObject get(Class c, Serializable id) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                return session.get(c, id);
            }
            return session.get(c, id);
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public ModelObject get(Query query) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                return session.get(query);
            }
            return session.get(query);
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public List<ModelObject> list(Query query) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                return session.list(query);
            }
            return session.list(query);
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public long count(Query query) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                return session.count(query);
            }
            return session.count(query);
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public Paging<ModelObject> paging(Query query) {
        SessionBrevityBuilder brevityBuilder = SessionBrevityBuilder.getBuildBrevity();
        try {
            if (this.context.getInterceptSession() != null) {
                AbstractInterceptSession session = this.context.getInterceptSession();
                session.setSession(this.session);
                return session.paging(query);
            }
            return session.paging(query);
        } finally {
            SessionBrevityBuilder.release();
        }
    }

    @Override
    public ZipperTable<ModelObject> getZipperTable(Class c) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.getZipperTable(c);
        }
        return session.getZipperTable(c);
    }

    @Override
    public AutoResult calculate(Function function) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.calculate(function);
        }
        return session.calculate(function);
    }

    @Override
    public AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.getAutonomously(autonomously);
        }
        return session.getAutonomously(autonomously);
    }

    @Override
    public AutoResult getAutonomously(TAutonomously autonomously) throws Exception {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.getAutonomously(autonomously);
        }
        return session.getAutonomously(autonomously);
    }

    @Override
    public List<DataSourceTableName> getDataSourceNames(Class c) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.getDataSourceNames(c);
        }
        return session.getDataSourceNames(c);
    }


    @Override
    public void close() throws IOException {
        session.close();
    }
}
