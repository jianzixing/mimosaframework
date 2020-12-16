package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 作为一个中间过渡的Session实现，这里会转发Session请求或者
 * 处理一些需要中间处理的事情
 */
public class SessionAgency implements Session {
    private ContextContainer context;
    private Session session;

    public SessionAgency(ContextContainer context) {
        this.context = context;
        this.session = this.buildRealSession(this.context);
    }

    private Session buildRealSession(ContextContainer context) {
        return context.buildSession();
    }

    @Override
    public ModelObject save(ModelObject obj) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.save(obj);
        }
        return session.save(obj);
    }

    @Override
    public ModelObject saveAndUpdate(ModelObject obj) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.saveAndUpdate(obj);
        }
        return session.saveAndUpdate(obj);
    }

    @Override
    public void save(List<ModelObject> objects) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            session.save(objects);
        } else {
            session.save(objects);
        }
    }

    @Override
    public void update(ModelObject obj) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            session.update(obj);
        } else {
            session.update(obj);
        }
    }

    @Override
    public void update(List<ModelObject> objects) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            session.update(objects);
        } else {
            session.update(objects);
        }
    }

    @Override
    public long update(Update update) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.update(update);
        }
        return session.update(update);
    }

    @Override
    public void delete(ModelObject obj) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            session.delete(obj);
        } else {
            session.delete(obj);
        }
    }

    @Override
    public void delete(List<ModelObject> objects) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            session.delete(objects);
        } else {
            session.delete(objects);
        }
    }

    @Override
    public long delete(Delete delete) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.delete(delete);
        }
        return session.delete(delete);
    }

    @Override
    public void delete(Class c, Serializable id) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            session.delete(c, id);
        } else {
            session.delete(c, id);
        }
    }

    @Override
    public ModelObject get(Class c, Serializable id) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.get(c, id);
        }
        return session.get(c, id);
    }

    @Override
    public ModelObject get(Query query) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.get(query);
        }
        return session.get(query);
    }

    @Override
    public List<ModelObject> list(Query query) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.list(query);
        }
        return session.list(query);
    }

    @Override
    public long count(Query query) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.count(query);
        }
        return session.count(query);
    }

    @Override
    public Paging<ModelObject> paging(Query query) {
        if (this.context.getInterceptSession() != null) {
            AbstractInterceptSession session = this.context.getInterceptSession();
            session.setSession(this.session);
            return session.paging(query);
        }
        return session.paging(query);
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
