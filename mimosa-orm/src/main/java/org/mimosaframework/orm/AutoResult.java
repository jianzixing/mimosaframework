package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.utils.Model2BeanFactory;
import org.mimosaframework.orm.utils.ModelObjectToBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class AutoResult {
    private Model2BeanFactory model2BeanFactory = new ModelObjectToBean();
    private ModelObjectConvertKey convert;
    private Object value;
    private Set<Class> tableClasses;
    private MappingGlobalWrapper mappingGlobalWrapper;

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
                        return template.mapper(Mapper.newInstance(name, search));
                    }
                } else {
                    return template.mapper(Mapper.newInstance(name));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(I18n.print("call_custom_sql_error"), e);
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

    public static boolean setQueryIn(String name, SessionTemplate template, ModelObject search, int start, int limit, Query query, Serializable key, Object rkey) {
        AutoResult result = null;
        if (template != null && search != null) {
            try {
                search.clearEmpty();
                if (search.size() > 0) {
                    if (start >= 0 && limit > 0) {
                        search.put("start", start);
                        search.put("limit", limit);
                    }
                    result = template.mapper(Mapper.newInstance(name, search));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(I18n.print("call_custom_sql_error"), e);
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

    public static boolean setQueryIn(String name, SessionTemplate template, ModelObject search, int start, int limit, Query query, Serializable key) {
        return setQueryIn(name, template, search, start, limit, query, key, null);
    }

    public static boolean setQueryIn(String name, SessionTemplate template, ModelObject search, Query query, Serializable key) {
        return setQueryIn(name, template, search, 0, 0, query, key, null);
    }

    public static boolean setQueryIn(String name, SessionTemplate template, Query query, Serializable key) {
        return setQueryIn(name, template, null, 0, 0, query, key, null);
    }

    public static long setQueryCount(String name, SessionTemplate template, ModelObject search) {
        AutoResult result = null;
        if (template != null && search != null) {
            try {
                search.clearEmpty();
                if (search.size() > 0) {
                    result = template.mapper(Mapper.newInstance(name, search));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(I18n.print("call_custom_sql_error"), e);
            }
        }
        if (result != null) {
            return result.longValue();
        }
        return 0;
    }

    public AutoResult(MappingGlobalWrapper mappingGlobalWrapper, ModelObjectConvertKey convert, Object value) {
        this.mappingGlobalWrapper = mappingGlobalWrapper;
        this.convert = convert;
        this.value = value;
    }

    public AutoResult(Object value) {
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

    private void setMapping(ModelObject mapping) {
        this.setMapping(mapping, this.value);
    }

    private void setMapping(ModelObject mapping, Object value) {
        if (mapping != null) {
            if (value != null && value instanceof List) {
                for (Object v : (List) value) {
                    this.setMapping(mapping, v);
                }
            }

            if (value instanceof Map) {
                Set set = ((Map) value).keySet();
                for (Object key : set) {
                    if (mapping.containsKey(key)) {
                        Object v = ((Map<?, ?>) value).get(key);
                        ((Map<?, ?>) value).remove(key);
                        ((Map) value).put(mapping.get(key), v);
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
        BigDecimal bigDecimal = this.bigDecimalValue(null);
        if (bigDecimal != null) {
            return bigDecimal.intValue();
        }
        return 0;
    }

    public Long longValue(String field) {
        BigDecimal bigDecimal = this.bigDecimalValue(field);
        if (bigDecimal != null) {
            return bigDecimal.longValue();
        }
        return null;
    }

    public double doubleValue() {
        BigDecimal bigDecimal = this.bigDecimalValue(null);
        if (bigDecimal != null) {
            return bigDecimal.intValue();
        }
        return 0;
    }

    public Double doubleValue(String field) {
        BigDecimal bigDecimal = this.bigDecimalValue(field);
        if (bigDecimal != null) {
            return bigDecimal.doubleValue();
        }
        return null;
    }

    public int intValue() {
        BigDecimal bigDecimal = this.bigDecimalValue(null);
        if (bigDecimal != null) {
            return bigDecimal.intValue();
        }
        return 0;
    }

    public Integer intValue(String field) {
        BigDecimal bigDecimal = this.bigDecimalValue(field);
        if (bigDecimal != null) {
            return bigDecimal.intValue();
        }
        return null;
    }

    public BigDecimal bigDecimalValue(String field) {
        if (this.value instanceof Map) {
            BigDecimal bigDecimal = null;
            Iterator<Map.Entry> iterator = ((Map) this.value).entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                Object entryValue = entry.getValue();
                if (entryValue instanceof List) {
                    List v = (List) entryValue;
                    for (Object o : v) {
                        if (o instanceof ModelObject) {
                            if (field == null) {
                                Object next = ((ModelObject) o).entrySet().iterator().next().getValue();
                                if (next instanceof Number || (next instanceof String && StringTools.isNumber((String) next))) {
                                    if (bigDecimal == null) bigDecimal = new BigDecimal("0");
                                    bigDecimal = bigDecimal.add(new BigDecimal(String.valueOf(next)));
                                }
                            } else {
                                if (bigDecimal == null) bigDecimal = new BigDecimal("0");
                                bigDecimal = bigDecimal.add(((ModelObject) o).getBigDecimal(field));
                            }
                        }
                    }
                }
            }
            return bigDecimal;
        }
        if (this.value instanceof List) {
            BigDecimal bigDecimal = null;
            List v = (List) this.value;
            for (Object o : v) {
                if (o instanceof ModelObject) {
                    if (field == null) {
                        Object next = ((ModelObject) o).entrySet().iterator().next().getValue();
                        if (next instanceof Number || (next instanceof String && StringTools.isNumber((String) next))) {
                            if (bigDecimal == null) bigDecimal = new BigDecimal("0");
                            bigDecimal = bigDecimal.add(new BigDecimal(String.valueOf(next)));
                        }
                    } else {
                        if (bigDecimal == null) bigDecimal = new BigDecimal("0");
                        bigDecimal = bigDecimal.add(((ModelObject) o).getBigDecimal(field));
                    }
                }
            }
            return bigDecimal;
        }
        if (this.value instanceof Integer
            || this.value instanceof Long
            || this.value instanceof Byte
            || this.value instanceof Short
            || this.value instanceof Double
            || this.value instanceof Float) {
            return new BigDecimal("" + this.value);
        }
        return null;
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

    public String stringValue(String field) {
        ModelObject object = this.getSingle();
        if (object != null) {
            object.getString(field);
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
        return this.getObjects(null);
    }

    public List<ModelObject> getObjects(ModelObject mapping) {
        if (mapping != null) this.setMapping(mapping);
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
                } else if (o instanceof Map) {
                    objects.add((ModelObject) o);
                }
            }
            return objects;
        }
        return null;
    }

    public <T> List<T> getBeans(Class<T> t) {
        return this.getBeans(t, null);
    }

    public <T> List<T> getBeans(Class<T> t, ModelObject mapping) {
        if (t != null) this.setTableClass(t);
        List<ModelObject> objects = this.getObjects(mapping);
        if (objects != null && objects.size() > 0) {
            List<T> r = new ArrayList<>();
            MappingTable table = this.mappingGlobalWrapper.getMappingTable(t);
            if (table != null) {
                List<MappingField> fields = table.getMappingPrimaryKeyFields();
                List<ModelObject> rm = null;
                for (int i = 0; i < objects.size(); i++) {
                    ModelObject object = objects.get(i);
                    boolean has = false;
                    for (int j = 0; j < objects.size(); j++) {
                        ModelObject object2 = objects.get(j);
                        int k = 0;
                        for (MappingField field : fields) {
                            String fieldName = field.getMappingFieldName();
                            Object v1 = object.get(fieldName);
                            Object v2 = object2.get(fieldName);
                            if ((v1 == null && v2 == null || v1 != null && v1.equals(v2)) && object != object2) {
                                k++;
                            }
                        }
                        if (fields.size() == k) {
                            has = true;
                            break;
                        }
                    }
                    if (has) {
                        if (rm == null) rm = new ArrayList<>();
                        rm.add(object);
                    }
                }
                if (rm != null) objects.removeAll(rm);
            }
            for (ModelObject object : objects) {
                T ins = model2BeanFactory.toJavaObject(object, t);
                r.add(ins);
            }
            return r;
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
        if (value instanceof List || value instanceof Map) {
            Object selfValue = value;
            if (value instanceof Map) {
                List list = null;
                Iterator<Map.Entry<Object, Object>> iterator = ((Map<Object, Object>) value).entrySet().iterator();
                while (iterator.hasNext()) {
                    Object mapValue = iterator.next().getValue();
                    if (mapValue instanceof List) {
                        if (list == null) {
                            list = new ArrayList();
                        }
                        list.addAll((List) mapValue);
                    }
                }
                if (list != null && list.size() > 0) {
                    selfValue = list;
                } else {
                    return null;
                }
            }

            List<Long> values = new ArrayList<>();
            for (int i = 0; i < ((List) selfValue).size(); i++) {
                ModelObject obj = (ModelObject) ((List) selfValue).get(i);
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
                    throw new IllegalArgumentException(I18n.print("value_not_number", "" + v));
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
