/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mimosaframework.core.json;

import org.mimosaframework.core.exception.ModelCheckerException;
import org.mimosaframework.core.json.annotation.JSONField;
import org.mimosaframework.core.json.parser.ParserConfig;
import org.mimosaframework.core.json.util.TypeUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.mimosaframework.core.json.util.TypeUtils.*;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
@SuppressWarnings("serial")
public class ModelObject extends Model implements Map<Object, Object>, Cloneable, Serializable, InvocationHandler {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private static final List<ModelObjectChecker> checkers = new ArrayList();

    private final Map<Object, Object> map;

    public static void addChecker(ModelObjectChecker checker) {
        if (!ModelObject.checkers.contains(checker)) {
            ModelObject.checkers.add(checker);
        }
    }

    public static void removeChecker(ModelObjectChecker checker) {
        checkers.remove(checker);
    }

    public ModelObject() {
        this(DEFAULT_INITIAL_CAPACITY, true);
    }

    public ModelObject(Map<Object, Object> map) {
        this.map = map;
    }

    public ModelObject(boolean ordered) {
        this(DEFAULT_INITIAL_CAPACITY, ordered);
    }

    public ModelObject(int initialCapacity) {
        this(initialCapacity, true);
    }

    public ModelObject(int initialCapacity, boolean ordered) {
        if (ordered) {
            map = new LinkedHashMap<Object, Object>(initialCapacity);
        } else {
            map = new HashMap<Object, Object>(initialCapacity);
        }
    }

    public static ModelObject builder(Class tableContactsClass) {
        return new ModelObject(tableContactsClass);
    }

    private String getKeyValue(Object key) {
        if (key != null) {
            if (key instanceof Class) key = ((Class) key).getSimpleName();
            return String.valueOf(key);
        }
        return null;
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(getKeyValue(key));
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Object get(Object key) {
        return map.get(getKeyValue(key));
    }

    public Object getObjectKey(Object key) {
        return map.get(key);
    }

    public ModelObject getModelObject(Object key) {
        Object value = map.get(getKeyValue(key));

        if (value instanceof ModelObject) {
            return (ModelObject) value;
        }

        return (ModelObject) toJSON(value);
    }

    public ModelArray getModelArray(Object key) {
        Object value = map.get(getKeyValue(key));

        if (value instanceof ModelArray) {
            return (ModelArray) value;
        }

        return (ModelArray) toJSON(value);
    }

    public <T> List<T> getArray(Object key) {
        ModelArray array = this.getModelArray(key);
        if (array != null) {
            List<T> list = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                Object o = array.get(i);
                list.add((T) o);
            }
            return list;
        }
        return null;
    }

    public <T> T getObject(Object key, Class<T> clazz) {
        Object obj = map.get(getKeyValue(key));
        return TypeUtils.castToJavaBean(obj, clazz);
    }

    public Boolean getBoolean(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return castToBoolean(value);
    }

    public byte[] getBytes(Object key) {
        Object value = get(getKeyValue(key));

        if (value == null) {
            return null;
        }

        return castToBytes(value);
    }

    public boolean getBooleanValue(Object key) {
        Object value = get(getKeyValue(key));

        if (value == null) {
            return false;
        }

        return castToBoolean(value).booleanValue();
    }

    public Byte getByte(Object key) {
        Object value = get(getKeyValue(key));

        return castToByte(value);
    }

    public byte getByteValue(Object key) {
        Object value = get(getKeyValue(key));

        if (value == null) {
            return 0;
        }

        return castToByte(value).byteValue();
    }

    public Short getShort(Object key) {
        Object value = get(getKeyValue(key));

        return castToShort(value);
    }

    public short getShortValue(Object key) {
        Object value = get(getKeyValue(key));

        if (value == null) {
            return 0;
        }

        return castToShort(value).shortValue();
    }

    public Integer getInteger(Object key) {
        Object value = get(getKeyValue(key));

        return castToInt(value);
    }

    public int getIntValue(Object key) {
        Object value = get(getKeyValue(key));

        if (value == null) {
            return 0;
        }

        return castToInt(value).intValue();
    }

    public Long getLong(Object key) {
        Object value = get(getKeyValue(key));

        return castToLong(value);
    }

    public long getLongValue(Object key) {
        Object value = get(getKeyValue(key));

        if (value == null) {
            return 0L;
        }

        return castToLong(value).longValue();
    }

    public Float getFloat(Object key) {
        Object value = get(getKeyValue(key));

        return castToFloat(value);
    }

    public float getFloatValue(Object key) {
        Object value = get(getKeyValue(key));

        if (value == null) {
            return 0F;
        }

        return castToFloat(value).floatValue();
    }

    public Double getDouble(Object key) {
        Object value = get(getKeyValue(key));

        return castToDouble(value);
    }

    public double getDoubleValue(Object key) {
        Object value = get(getKeyValue(key));

        if (value == null) {
            return 0D;
        }

        return castToDouble(value);
    }

    public BigDecimal getBigDecimal(Object key) {
        Object value = get(getKeyValue(key));

        return castToBigDecimal(value);
    }

    public BigInteger getBigInteger(Object key) {
        Object value = get(getKeyValue(key));

        return castToBigInteger(value);
    }

    public String getString(Object key) {
        Object value = get(getKeyValue(key));

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public Date getDate(Object key) {
        Object value = get(getKeyValue(key));

        return castToDate(value);
    }

    public java.sql.Date getSqlDate(Object key) {
        Object value = get(getKeyValue(key));

        return castToSqlDate(value);
    }

    public java.sql.Timestamp getTimestamp(Object key) {
        Object value = get(getKeyValue(key));

        return castToTimestamp(value);
    }

    public Object put(Object key, Object value) {
        return map.put(getKeyValue(key), value);
    }

    public Object putObjectKey(Object key, Object value) {
        return map.put(key, value);
    }

    public ModelObject chainPut(Object key, Object value) {
        map.put(getKeyValue(key), value);
        return this;
    }

    public void putAll(Map<? extends Object, ? extends Object> m) {
        map.putAll(m);
    }

    public ModelObject chainPutAll(Map<? extends Object, ? extends Object> m) {
        map.putAll(m);
        return this;
    }

    public void clear() {
        map.clear();
    }

    public ModelObject chainClear() {
        map.clear();
        return this;
    }

    public Object remove(Object key) {
        return map.remove(getKeyValue(key));
    }

    public Object removeObjectKey(Object key) {
        return map.remove(key);
    }

    public ModelObject chainRemove(Object key) {
        map.remove(getKeyValue(key));
        return this;
    }

    public Set<Object> keySet() {
        return map.keySet();
    }

    public Collection<Object> values() {
        return map.values();
    }

    public Set<Entry<Object, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Object clone() {
        ModelObject object = new ModelObject(map instanceof LinkedHashMap //
                ? new LinkedHashMap<Object, Object>(map) //
                : new HashMap<Object, Object>(map));
        object.setObjectClass(objectClass);
        return object;
    }

    public boolean equals(Object obj) {
        return this.map.equals(obj);
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1) {
            if (method.getName().equals("equals")) {
                return this.equals(args[0]);
            }

            Class<?> returnType = method.getReturnType();
            if (returnType != void.class) {
                throw new ModelException("illegal setter");
            }

            String name = null;
            JSONField annotation = method.getAnnotation(JSONField.class);
            if (annotation != null) {
                if (annotation.name().length() != 0) {
                    name = annotation.name();
                }
            }

            if (name == null) {
                name = method.getName();

                if (!name.startsWith("set")) {
                    throw new ModelException("illegal setter");
                }

                name = name.substring(3);
                if (name.length() == 0) {
                    throw new ModelException("illegal setter");
                }
                name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
            }

            map.put(name, args[0]);
            return null;
        }

        if (parameterTypes.length == 0) {
            Class<?> returnType = method.getReturnType();
            if (returnType == void.class) {
                throw new ModelException("illegal getter");
            }

            String name = null;
            JSONField annotation = method.getAnnotation(JSONField.class);
            if (annotation != null) {
                if (annotation.name().length() != 0) {
                    name = annotation.name();
                }
            }

            if (name == null) {
                name = method.getName();
                if (name.startsWith("get")) {
                    name = name.substring(3);
                    if (name.length() == 0) {
                        throw new ModelException("illegal getter");
                    }
                    name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                } else if (name.startsWith("is")) {
                    name = name.substring(2);
                    if (name.length() == 0) {
                        throw new ModelException("illegal getter");
                    }
                    name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                } else if (name.startsWith("hashCode")) {
                    return this.hashCode();
                } else if (name.startsWith("toString")) {
                    return this.toString();
                } else {
                    throw new ModelException("illegal getter");
                }
            }

            Object value = map.get(name);
            return TypeUtils.cast(value, method.getGenericReturnType(), ParserConfig.getGlobalInstance());
        }

        throw new UnsupportedOperationException(method.toGenericString());
    }


    /**
     * 当前的json对象的绑定对象
     */
    private Class objectClass;

    public ModelObject(Class objectClass) {
        this();
        this.objectClass = objectClass;
    }

    public Class getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(Class objectClass) {
        this.objectClass = objectClass;
    }

    /**
     * 以下方法为扩展方法，方便对JSON的值和对象的操作
     * <p>
     * 清除包含null的字段
     */
    public void clearNull() {
        List keys = new ArrayList();
        for (Map.Entry entry : this.entrySet()) {
            Object object = entry.getValue();
            if (object == null) {
                keys.add(entry.getKey());
            }
        }
        for (Object key : keys) {
            this.remove(key);
        }
    }

    /**
     * 清除所有无效字段，为空的包括null 和 ""
     */
    public void clearEmpty() {
        List keys = new ArrayList();
        for (Map.Entry entry : this.entrySet()) {
            Object object = entry.getValue();
            if (object == null) {
                keys.add(entry.getKey());
            } else if (object instanceof String && String.valueOf(object).equals("")) {
                keys.add(entry.getKey());
            }
        }
        for (Object key : keys) {
            this.remove(key);
        }
    }

    public boolean isEmpty(Object key) {
        if (this.get(key) == null || String.valueOf(this.get(key)).equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNotEmpty(Object key) {
        if (!isEmpty(key)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean check() {
        try {
            for (ModelObjectChecker checker : checkers) {
                checker.checker(this, null);
            }
            return true;
        } catch (ModelCheckerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAndThrowable() throws ModelCheckerException {
        for (ModelObjectChecker checker : checkers) {
            checker.checker(this, null);
        }
        return true;
    }

    public boolean checkUpdateThrowable() throws ModelCheckerException {
        return this.checkUpdateThrowable(null);
    }

    public boolean checkUpdate() {
        return this.checkUpdate(null);
    }

    public boolean checkUpdateThrowable(Object... removed) throws ModelCheckerException {
        for (ModelObjectChecker checker : checkers) {
            checker.checkerUpdate(this, removed);
        }
        return true;
    }

    public boolean checkUpdate(Object... removed) {
        try {
            for (ModelObjectChecker checker : checkers) {
                checker.checkerUpdate(this, removed);
            }
            return true;
        } catch (ModelCheckerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 只保留参数中的值，其他的都去掉
     *
     * @param ks
     */
    public void retain(Object... ks) {
        if (ks != null && ks.length > 0) {
            List<Object> removed = new ArrayList<>();
            Set<Object> objects = this.keySet();
            for (Object o : objects) {
                boolean in = false;
                for (Object k : ks) {
                    if (getKeyValue(o).equals(getKeyValue(k))) {
                        in = true;
                    }
                }
                if (!in) {
                    removed.add(o);
                }
            }

            for (Object o : removed) {
                this.remove(o);
            }
        }
    }

    public ModelObject copy(Object... keys) {
        if (keys != null) {
            ModelObject object = new ModelObject();
            object.objectClass = this.objectClass;
            for (Object key : keys) {
                if (this.get(key) != null) {
                    object.put(key, this.get(key));
                }
            }
            return object;
        }
        return null;
    }

    public ModelObject copy(List keys) {
        return this.copy(keys.toArray());
    }
}
