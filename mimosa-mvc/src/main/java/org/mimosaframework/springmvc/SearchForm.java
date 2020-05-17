package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.*;

import java.util.*;

/**
 * 自动装配Query的预处理器对象
 *
 * @author yangankang
 */
public class SearchForm {
    private List<ModelObject> objects;

    public SearchForm(List<ModelObject> objects) {
        this.objects = objects;
    }

    public Query getQuery(Class table) {
        return this.getQuery(table, null);
    }

    public Query getQuery(Class table, Map<String, Class> map) {
        Query query = new DefaultQuery(table);
        Map<String, List<ModelObject>> classQuery = new HashMap<>();
        for (ModelObject o : objects) {
            String tableKey = o.getString("table");
            List<ModelObject> nq = classQuery.get(tableKey);
            if (nq == null) nq = new ArrayList<>();
            nq.add(o);
            classQuery.put(tableKey, nq);
        }
        List<ModelObject> m1 = classQuery.get(null);
        for (ModelObject o : m1) {
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
                    query.eq(name, value);
                } else if (symbol.equalsIgnoreCase("between")) {
                    query.between(name, s, e);
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

        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<String, List<ModelObject>>> iterator = classQuery.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<ModelObject>> entry = iterator.next();
                String key = entry.getKey();
                if (key != null) {
                    List<ModelObject> m2 = entry.getValue();
                    for (ModelObject o : m2) {
                        String tableKey = o.getString("table");
                        Class c = map.get(tableKey);
                        if (c != null) {
                            Join join = Criteria.inner(c);
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
                            query.subjoin(join);
                        }
                    }
                }
            }
        }
        return query;
    }

    public Delete getDelete(Class table) {
        Query query = this.getQuery(table, null);
        if (query != null) {
            Wraps wraps = ((DefaultQuery) query).getLogicWraps();
            DefaultDelete delete = new DefaultDelete(null);
            delete.setLogicWraps(wraps);
            return delete;
        }
        return null;
    }

    public Update getUpdate(Class table) {
        Query query = this.getQuery(table, null);
        if (query != null) {
            Wraps wraps = ((DefaultQuery) query).getLogicWraps();
            DefaultUpdate update = new DefaultUpdate(null);
            update.setLogicWraps(wraps);
            return update;
        }
        return null;
    }
}
