package org.mimosaframework.springmvc;

import org.mimosaframework.orm.criteria.*;

/**
 * 自动装配Query的预处理器对象
 *
 * @author yangankang
 */
public class SearchForm {

    private Query query;

    public SearchForm(Query query) {
        this.query = query;
    }

    public Query getQuery() {
        return this.query;
    }

    public Delete getDelete() {
        if (this.query != null) {
            Wraps wraps = ((DefaultQuery) this.query).getLogicWraps();
            DefaultDelete delete = new DefaultDelete(null);
            delete.setLogicWraps(wraps);
            return delete;
        }
        return null;
    }

    public Update getUpdate() {
        if (this.query != null) {
            Wraps wraps = ((DefaultQuery) this.query).getLogicWraps();
            DefaultUpdate update = new DefaultUpdate(null);
            update.setLogicWraps(wraps);
            return update;
        }
        return null;
    }
}
