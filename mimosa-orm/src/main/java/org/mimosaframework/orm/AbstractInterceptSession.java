package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class AbstractInterceptSession implements Session {
    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public ModelObject save(ModelObject obj) {
        return session.save(obj);
    }

    @Override
    public ModelObject saveAndUpdate(ModelObject obj) {
        return session.saveAndUpdate(obj);
    }

    @Override
    public void save(List<ModelObject> objects) {
        session.save(objects);
    }

    @Override
    public void update(ModelObject obj) {
        session.update(obj);
    }

    @Override
    public void update(List<ModelObject> objects) {
        session.update(objects);
    }

    @Override
    public long update(Update update) {
        return session.update(update);
    }

    @Override
    public void delete(ModelObject obj) {
        session.delete(obj);
    }

    @Override
    public void delete(List<ModelObject> objects) {
        session.delete(objects);
    }

    @Override
    public long delete(Delete delete) {
        return session.delete(delete);
    }

    @Override
    public void delete(Class c, Serializable id) {
        session.delete(c, id);
    }

    @Override
    public ModelObject get(Class c, Serializable id) {
        return session.get(c, id);
    }

    @Override
    public ModelObject get(Query query) {
        return session.get(query);
    }

    @Override
    public List<ModelObject> list(Query query) {
        return session.list(query);
    }

    @Override
    public long count(Query query) {
        return session.count(query);
    }

    @Override
    public Paging<ModelObject> paging(Query query) {
        return session.paging(query);
    }

    @Override
    public ZipperTable<ModelObject> getZipperTable(Class c) {
        return session.getZipperTable(c);
    }

    @Override
    public ModelObject calculate(Function function) {
        return session.calculate(function);
    }

    @Override
    public AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception {
        return session.getAutonomously(autonomously);
    }

    @Override
    public AutoResult getAutonomously(TAutonomously autonomously) throws Exception {
        return session.getAutonomously(autonomously);
    }

    @Override
    public List<DataSourceTableName> getDataSourceNames(Class c) {
        return session.getDataSourceNames(c);
    }

    @Override
    public void close() throws IOException {

    }
}
