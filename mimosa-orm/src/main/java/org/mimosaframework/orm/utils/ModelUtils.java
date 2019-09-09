package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.AutoResult;
import org.mimosaframework.orm.Paging;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.orm.criteria.Query;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by yangankang on 16/1/18.
 */
public abstract class ModelUtils {

    /**
     * 通过对比对象集合 id 和 pid 重新组合成一个链表的结构(树结构)
     *
     * @param objects
     * @param idKey
     * @param pidKey
     * @param childrenKey
     * @return
     */
    public static List<ModelObject> getListToTree(List<ModelObject> objects, Object idKey, Object pidKey, String childrenKey) {
        Map<String, ModelObject> map = new LinkedHashMap();
        if (objects != null) {
            for (ModelObject m : objects) {
                map.put(m.getString(idKey), m);
            }

            List<ModelObject> result = new ArrayList();
            List<ModelObject> removes = new ArrayList<>();

            Set<Map.Entry<String, ModelObject>> set = map.entrySet();
            for (Map.Entry<String, ModelObject> entry : set) {
                String pid = entry.getValue().getString(pidKey);
                Set<Map.Entry<String, ModelObject>> set2 = map.entrySet();
                for (Map.Entry<String, ModelObject> cen : set2) {
                    if (cen.getValue().getString(idKey).equals(pid)) {
                        ModelObject object = cen.getValue();
                        List children = object.getModelArray(childrenKey);
                        if (children == null) {
                            children = new ArrayList<ModelObject>();
                        }
                        children.add(entry.getValue());
                        cen.getValue().put(childrenKey, children);
                        removes.add(entry.getValue());
                    }
                }

                result.add(entry.getValue());
            }

            result.removeAll(removes);
            return result;
        }
        return null;
    }

    public static void removeValues(List<ModelObject> objects, Object... keys) {
        if (objects != null && keys != null) {
            for (ModelObject m : objects) {
                for (Object k : keys) {
                    m.remove(k);
                }
            }
        }
    }

    public static void removeValues(Paging paging, Object... keys) {
        List<ModelObject> objects = paging.getObjects();
        if (objects != null && keys != null) {
            for (ModelObject m : objects) {
                for (Object k : keys) {
                    m.remove(k);
                }
            }
        }
    }

    public static void removeValue(ModelObject object, Object... keys) {
        if (object != null && keys != null) {
            for (Object k : keys) {
                object.remove(k);
            }
        }
    }

    public static void setLikeUrlEncodeSearch(ModelObject search, String key) {
        if (search != null && search.containsKey(key) && !search.isEmpty(key)) {
            try {
                search.put(key, "%" + URLEncoder.encode(search.getString(key), "utf-8") + "%");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setEqSearch(ModelObject search, String key, Object tableKey, Query query) {
        if (search != null && search.containsKey(key) && !search.isEmpty(key) && query != null) {
            query.eq(tableKey, search.get(key));
        }
    }

    /**
     * SQL语句查询结果辅助查询
     *
     * @param name
     * @param sessionTemplate
     * @param search
     * @param query
     * @param key
     * @return
     */
    public static Paging getSearch(String name,
                                   SessionTemplate sessionTemplate,
                                   ModelObject search,
                                   Query query,
                                   Object key) {
        boolean runDefault = false;
        if (search != null) {
            search.clearEmpty();
            if (search.size() > 0) {
                long count = AutoResult.setQueryCount(name + ".getSearchCount", sessionTemplate, search);
                if (count > 0) {
                    AutoResult.setQueryIn(name + ".getSearchIds", sessionTemplate, search, query, key);
                    Paging paging = sessionTemplate.paging(query);
                    return paging;
                }
            } else {
                runDefault = true;
            }
        } else {
            runDefault = true;
        }

        if (runDefault) {
            Paging paging = sessionTemplate.paging(query);
            return paging;
        }
        return null;
    }

    public static List<ModelObject> getSearchByName(String name,
                                                    SessionTemplate sessionTemplate,
                                                    ModelObject search,
                                                    Query query,
                                                    Object key) {
        boolean runDefault = false;
        if (search != null) {
            search.clearEmpty();
            if (search.size() > 0) {
                AutoResult.setQueryIn(name, sessionTemplate, search, query, key);
                List<ModelObject> objects = sessionTemplate.list(query);
                return objects;
            } else {
                runDefault = true;
            }
        } else {
            runDefault = true;
        }
        if (runDefault) {
            List<ModelObject> objects = sessionTemplate.list(query);
            return objects;
        }
        return null;
    }

    /**
     * 结果完全依赖sql语句查询
     *
     * @param name
     * @param sessionTemplate
     * @param search
     * @param query
     * @param key
     * @return
     */
    public static Paging getRelySearch(String name,
                                       SessionTemplate sessionTemplate,
                                       ModelObject search,
                                       Query query,
                                       Object key) {
        boolean runDefault = false;
        if (search != null) {
            search.clearEmpty();
            if (search.size() > 0) {
                long count = AutoResult.setQueryCount(name + ".getSearchCount", sessionTemplate, search);
                if (count > 0) {
                    AutoResult.setQueryIn(name + ".getSearchIds", sessionTemplate, search, query, key);
                    Paging paging = sessionTemplate.paging(query);
                    paging.setCount(count);
                    return paging;
                }
            } else {
                runDefault = true;
            }
        } else {
            runDefault = true;
        }

        if (runDefault) {
            Paging paging = sessionTemplate.paging(query);
            return paging;
        }
        return null;
    }

    public static List<ModelObject> getRelySearchByName(String name,
                                                        SessionTemplate sessionTemplate,
                                                        ModelObject search,
                                                        Query query,
                                                        Object key) {
        boolean runDefault = false;
        if (search != null) {
            search.clearEmpty();
            if (search.size() > 0) {
                boolean hasSet = AutoResult.setQueryIn(name, sessionTemplate, search, query, key);
                if (hasSet) {
                    List<ModelObject> objects = sessionTemplate.list(query);
                    return objects;
                }
            } else {
                runDefault = true;
            }
        } else {
            runDefault = true;
        }

        if (runDefault) {
            List<ModelObject> objects = sessionTemplate.list(query);
            return objects;
        }
        return null;
    }

    public static void setLikeSearch(ModelObject search, String key) {
        if (search != null && search.containsKey(key) && !search.isEmpty(key)) {
            search.put(key, "%" + search.getString(key) + "%");
        }
    }

    public static void setValue2Integer(ModelObject search, String key) {
        if (search != null && search.containsKey(key) && !search.isEmpty(key)) {
            search.put(key, Integer.parseInt(search.getString(key)));
        }
    }
}
