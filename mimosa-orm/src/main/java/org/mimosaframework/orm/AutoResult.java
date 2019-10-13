package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.Query;

import java.util.*;

public class AutoResult {
    private ModelObjectConvertKey convert;
    private Object value;
    private Set<Class> tableClasses;

    public static AutoResult getAutoResult(String name, SessionTemplate template, ModelObject search, int start, int limit) {
        if (template != null) {
            try {
                if (search != null) {
                    search.clearEmpty();
                    if (search.size() > 0) {
                        if (start >= 0 && limit > 0) {
                            search.put("start", start);
                            search.put("limit", limit);
                        }
                        return template.getAutonomously(TAutonomously.newInstance(name, search));
                    }
                } else {
                    return template.getAutonomously(TAutonomously.newInstance(name));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("调用自定义SQL语句出错", e);
            }
        }
        return null;
    }

    public static AutoResult getAutoResult(String name, SessionTemplate template, ModelObject search) {
        return getAutoResult(name, template, search, 0, 0);
    }

    public static AutoResult getAutoResult(String name, SessionTemplate template) {
        return getAutoResult(name, template, null, 0, 0);
    }

    public static boolean setQueryIn(String name, SessionTemplate template, ModelObject search, int start, int limit, Query query, Object key, Object rkey) {
        AutoResult result = null;
        if (template != null && search != null) {
            try {
                search.clearEmpty();
                if (search.size() > 0) {
                    if (start >= 0 && limit > 0) {
                        search.put("start", start);
                        search.put("limit", limit);
                    }
                    result = template.getAutonomously(TAutonomously.newInstance(name, search));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("调用自定义SQL语句出错", e);
            }
        }
        if (result != null && !result.isEmptyValue()) {
            if (rkey != null) {
                List list = result.gets(rkey);
                if (list != null && list.size() > 0) {
                    query.in(key, list);
                    return true;
                }
            } else {
                List<String> list = result.gets();
                if (list != null && list.size() > 0) {
                    query.in(key, list);
                    return true;
                }
            }
        } else {

        }
        return false;
    }

    public static boolean setQueryIn(String name, SessionTemplate template, ModelObject search, int start, int limit, Query query, Object key) {
        return setQueryIn(name, template, search, start, limit, query, key, null);
    }

    public static boolean setQueryIn(String name, SessionTemplate template, ModelObject search, Query query, Object key) {
        return setQueryIn(name, template, search, 0, 0, query, key, null);
    }

    public static boolean setQueryIn(String name, SessionTemplate template, Query query, Object key) {
        return setQueryIn(name, template, null, 0, 0, query, key, null);
    }

    public static long setQueryCount(String name, SessionTemplate template, ModelObject search) {
        AutoResult result = null;
        if (template != null && search != null) {
            try {
                search.clearEmpty();
                if (search.size() > 0) {
                    result = template.getAutonomously(TAutonomously.newInstance(name, search));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("调用自定义SQL语句出错", e);
            }
        }
        if (result != null) {
            return result.longValue();
        }
        return 0;
    }

    public AutoResult(ModelObjectConvertKey convert, Object value) {
        this.convert = convert;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    /**
     * 由于字段的转换都是单向的，所以这里可以设置返回的结果
     * 中包含的映射表，然后遍历映射表改变返回值的字段为映射表字段
     *
     * @param tables
     */
    public void setTableClass(Class... tables) {
        if (tableClasses == null) {
            tableClasses = new LinkedHashSet<>();
        } else {
            tableClasses.clear();
        }
        for (Class c : tables) {
            tableClasses.add(c);
        }

        if (this.tableClasses != null && convert != null) {
            if (value instanceof ModelObject) {
                for (Class c : this.tableClasses) {
                    convert.reconvert(c, (ModelObject) value);
                }
            }

            if (value instanceof List) {
                for (Class c : this.tableClasses) {
                    convert.reconvert(c, (List<ModelObject>) value);
                }
            }

            if (value instanceof Map) {
                Set<Map.Entry> set = ((Map) value).entrySet();
                for (Map.Entry entry : set) {
                    if (entry.getValue() instanceof List) {
                        List<ModelObject> entryValue = (List<ModelObject>) entry.getValue();
                        for (Class c : this.tableClasses) {
                            convert.reconvert(c, entryValue);
                        }
                    }
                }
            }
        }
    }

    public boolean isEmptyValue() {
        if (value == null) return true;
        if (value instanceof Iterable) {
            return !((Iterable) value).iterator().hasNext();
        }
        return false;
    }

    public long longValue() {
        if (value != null && value instanceof Number) {
            return (long) value;
        } else {
            Object v = this.getSingleByList();
            if (v != null) {
                if (v instanceof Number) {
                    if (v instanceof Double || v instanceof Float) {
                        return Long.parseLong("" + v);
                    }
                    return (long) v;
                }
                if (v instanceof String && StringTools.isNumber((String) v)) {
                    Long.parseLong((String) v);
                }
            }
        }
        return 0;
    }

    public double doubleValue() {
        if (value != null && value instanceof Number) {
            return (double) value;
        } else {
            Object v = this.getSingleByList();
            if (v != null) {
                if (v instanceof Number) {
                    if (v instanceof Double) {
                        return (double) v;
                    } else {
                        return Double.parseDouble("" + v);
                    }
                }
                if (v instanceof String && StringTools.isNumber((String) v)) {
                    Double.parseDouble((String) v);
                }
            }
        }
        return 0;
    }

    public int intValue() {
        if (value != null && value instanceof Number) {
            return (int) value;
        } else {
            Object v = this.getSingleByList();
            if (v != null) {
                if (v instanceof Number) {
                    if (v instanceof Integer) {
                        return (int) v;
                    } else {
                        return Integer.parseInt("" + v);
                    }
                }
                if (v instanceof String && StringTools.isNumber((String) v)) {
                    Integer.parseInt((String) v);
                }
            }
        }
        return 0;
    }

    public String stringValue() {
        if (value != null && value instanceof String) {
            return (String) value;
        } else {
            Object v = this.getSingleByList();
            if (v != null) {
                if (v instanceof String) return (String) v;
                else return "" + v;
            }
        }
        return null;
    }

    public ModelObject getSingle() {
        if (value instanceof List && ((List) value).size() == 1) {
            ModelObject obj = (ModelObject) ((List) value).get(0);
            return obj;
        }
        return null;
    }

    public List<ModelObject> getObjects() {
        if (value instanceof List) {
            return (List<ModelObject>) value;
        }
        if (value instanceof Map) {
            Set<Map.Entry> map = ((Map) value).entrySet();
            List<ModelObject> objects = new ArrayList<>();
            for (Map.Entry entry : map) {
                Object o = entry.getValue();
                if (o instanceof List) {
                    objects.addAll((List) o);
                } else {
                    objects.add((ModelObject) o);
                }
            }
            return objects;
        }
        return null;
    }

    public Map<String, List<ModelObject>> getMaps() {
        if (value instanceof Map) {
            return (Map<String, List<ModelObject>>) value;
        }
        return null;
    }

    public List<String> getStrings() {
        return getStrings(null);
    }

    public List<String> getStrings(Object key) {
        String name = null;
        if (key != null) {
            name = String.valueOf(key);
        }
        if (value instanceof List) {
            List<String> values = new ArrayList<>();
            for (int i = 0; i < ((List) value).size(); i++) {
                ModelObject obj = (ModelObject) ((List) value).get(i);
                Object v = name == null ? obj.get(obj.keySet().iterator().next()) : obj.get(name);

                if (v instanceof String) {
                    values.add((String) v);
                } else {
                    values.add("" + v);
                }
            }
            return values;
        }
        return null;
    }

    public List<String> gets() {
        return gets(null);
    }

    public List gets(Object key) {
        String name = null;
        if (key != null) {
            name = String.valueOf(key);
        }
        if (value instanceof List) {
            List values = new ArrayList();
            for (int i = 0; i < ((List) value).size(); i++) {
                ModelObject obj = (ModelObject) ((List) value).get(i);
                Object v = name == null ? obj.get(obj.keySet().iterator().next()) : obj.get(name);
                values.add(v);
            }
            return values;
        }
        return null;
    }

    public List<Long> getNumbers() {
        return getNumbers(null);
    }

    public List<Long> getNumbers(Object key) {
        String name = null;
        if (key != null) {
            name = String.valueOf(key);
        }
        if (value instanceof List) {
            List<Long> values = new ArrayList<>();
            for (int i = 0; i < ((List) value).size(); i++) {
                ModelObject obj = (ModelObject) ((List) value).get(i);
                Object v = name == null ? obj.get(obj.keySet().iterator().next()) : obj.get(name);

                if (v instanceof Number) {
                    if (v instanceof Long) {
                        values.add((Long) v);
                    } else {
                        values.add(Long.parseLong("" + v));
                    }
                } else if (v instanceof String && StringTools.isNumber((String) v)) {
                    values.add(Long.parseLong((String) v));
                } else {
                    throw new IllegalArgumentException("值" + v + "不是一个数字");
                }
            }
            return values;
        }
        return null;
    }

    private Object getSingleByList() {
        if (value instanceof List && ((List) value).size() == 1) {
            ModelObject obj = (ModelObject) ((List) value).get(0);
            if (obj.size() == 1) {
                return obj.entrySet().iterator().next().getValue();
            }
        }
        return null;
    }
}
