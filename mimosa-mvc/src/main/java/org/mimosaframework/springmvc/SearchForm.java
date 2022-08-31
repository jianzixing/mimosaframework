package org.mimosaframework.springmvc;

import org.mimosaframework.core.exception.ModuleException;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.*;

import java.util.*;

/**
 * 自动装配Query的预处理器对象
 *
 * @author yangankang
 */
public class SearchForm<T> {
    private Query query;

    public SearchForm() {
        query = new DefaultQuery();
    }

    public SearchForm(Query query) {
        this.query = query;
    }

    private List<ModelObject> getItems(List<T> objects) {
        if (objects != null && objects.size() > 0) {
            List<ModelObject> newObjs = new ArrayList<>();
            for (T o : objects) {
                if (o instanceof ModelObject) {
                    newObjs.add((ModelObject) o);
                } else if (o instanceof SearchItem) {
                    newObjs.add((ModelObject) ModelObject.toJSON(o));
                }
            }
            return newObjs;
        }
        return null;
    }

    public Query set(List<T> items) {
        if (items == null || items.size() == 0) return null;
        List<ModelObject> objects = getItems(items);
        for (ModelObject o : objects) {
            String name = o.getString("name");
            // 如果存在就报错以防止构造查询导致数据泄露
            if (StringTools.isNotEmpty(name)) {
                DefaultQuery dq = (DefaultQuery) this.query;
                if (dq.hasFilter(name)) {
                    throw new ModuleException("exist_filter", "已经存在的查询条件");
                }
            }
        }
        for (ModelObject o : objects) {
            String name = o.getString("name");
            String value = o.getString("value");
            String symbol = o.getString("symbol");
            String s = o.getString("start");
            String e = o.getString("end");

            if ((StringTools.isNotEmpty(value)
                    || StringTools.isNotEmpty(s)
                    || StringTools.isNotEmpty(e))
                    && StringTools.isNotEmpty(name)) {
                if (StringTools.isEmpty(symbol) || symbol.equalsIgnoreCase("eq")) {
                    query.eq(name, value);
                } else if (symbol.equalsIgnoreCase("between")) {
                    if (StringTools.isNotEmpty(s) && StringTools.isEmpty(e)) {
                        query.gte(name, s);
                    } else if (StringTools.isEmpty(s) && StringTools.isNotEmpty(e)) {
                        query.lte(name, e);
                    } else {
                        query.between(name, s, e);
                    }
                } else if (symbol.equalsIgnoreCase("gt")) {
                    query.gt(name, value);
                } else if (symbol.equalsIgnoreCase("lt")) {
                    query.lt(name, value);
                } else if (symbol.equalsIgnoreCase("gte")) {
                    query.gte(name, value);
                } else if (symbol.equalsIgnoreCase("lte")) {
                    query.lte(name, value);
                } else if (symbol.equalsIgnoreCase("like")) {
                    query.like(name, value);
                }
            }
        }
        return query;
    }

    public Join set(Class joinTable, ModelObject ons, List<T> objects) {
        return set(null, joinTable, ons, objects);
    }

    public Join set(Join joinMain, Class joinTable, ModelObject ons, List<T> items) {
        if (items == null || items.size() == 0) return null;
        List<ModelObject> objects = this.getItems(items);
        Join join = Criteria.inner(joinTable);
        if (ons != null) {
            Iterator<Map.Entry<Object, Object>> iterator = ons.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> next = iterator.next();
                join.on(next.getKey(), next.getValue());
            }
        }
        for (ModelObject o : objects) {
            String name = o.getString("name");
            String value = o.getString("value");
            String symbol = o.getString("symbol");
            String s = o.getString("start");
            String e = o.getString("end");

            if ((StringTools.isNotEmpty(value)
                    || StringTools.isNotEmpty(s)
                    || StringTools.isNotEmpty(e))
                    && StringTools.isNotEmpty(name)) {
                if (StringTools.isEmpty(symbol)) {
                    join.eq(name, value);
                } else if (symbol.equalsIgnoreCase("between")) {
                    join.between(name, s, e);
                } else if (symbol.equalsIgnoreCase("gt")) {
                    join.gt(name, value);
                } else if (symbol.equalsIgnoreCase("lt")) {
                    join.lt(name, value);
                } else if (symbol.equalsIgnoreCase("gte")) {
                    join.gte(name, value);
                } else if (symbol.equalsIgnoreCase("lte")) {
                    join.lte(name, value);
                } else if (symbol.equalsIgnoreCase("like")) {
                    join.like(name, value);
                }
            }
        }
        if (joinMain != null) {
            joinMain.subjoin(join);
        } else {
            query.subjoin(join);
        }
        return join;
    }

    public Query getQuery(Class table) {
        if (query == null && table != null) query = new DefaultQuery(table);
        return this.getQuery();
    }

    public Query getQuery() {
        return query;
    }

    public Delete getDelete() {
        return this.getDelete(null);
    }

    public Delete getDelete(Class table) {
        Query query = this.getQuery(table);
        if (query != null) {
            Wraps wraps = ((DefaultQuery) query).getLogicWraps();
            DefaultDelete delete = new DefaultDelete(table);
            delete.setLogicWraps(wraps);
            return delete;
        }
        return null;
    }

    public Update getUpdate() {
        return this.getUpdate(null);
    }

    public Update getUpdate(Class table) {
        Query query = this.getQuery(table);
        if (query != null) {
            Wraps wraps = ((DefaultQuery) query).getLogicWraps();
            DefaultUpdate update = new DefaultUpdate(table);
            update.setLogicWraps(wraps);
            return update;
        }
        return null;
    }
}
