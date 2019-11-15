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
import java.sql.Timestamp;
import java.util.*;

import static org.mimosaframework.core.json.util.TypeUtils.*;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
@SuppressWarnings("serial")
public class ModelObject extends Model implements Map<Object, Object>, Cloneable, Serializable, InvocationHandler, ModelByEnum, ModelByClass {

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

    public static String getKeyName(Object key) {
        if (key != null) {
            if (key instanceof Class) key = ((Class) key).getSimpleName();
            return String.valueOf(key);
        }
        return null;
    }

    public void putAny(Object key, Object value) {
        map.put(key, value);
    }

    public Object getAny(Object key) {
        return map.get(key);
    }

    public void removeAny(Object key) {
        map.remove(key);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(getKeyName(key));
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(getKeyName(key));
    }

    @Override
    public Object put(Object key, Object value) {
        return map.put(getKeyName(key), value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(getKeyName(key));
    }

    public Object get(String key) {
        return map.get(key);
    }

    public ModelObject getModelObject(String key) {
        Object value = map.get(key);

        if (value instanceof ModelObject) {
            return (ModelObject) value;
        }

        return (ModelObject) toJSON(value);
    }

    public ModelArray getModelArray(String key) {
        Object value = map.get(key);

        if (value instanceof ModelArray) {
            return (ModelArray) value;
        }

        return (ModelArray) toJSON(value);
    }

    public <T> List<T> getArray(String key) {
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

    public <T> T getObject(String key, Class<T> clazz) {
        Object obj = map.get(key);
        return TypeUtils.castToJavaBean(obj, clazz);
    }

    public Boolean getBoolean(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return castToBoolean(value);
    }

    public byte[] getBytes(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return castToBytes(value);
    }

    public boolean getBooleanValue(String key) {
        Object value = get(key);

        if (value == null) {
            return false;
        }

        return castToBoolean(value).booleanValue();
    }

    public Byte getByte(String key) {
        Object value = get(key);

        return castToByte(value);
    }

    public byte getByteValue(String key) {
        Object value = get(key);

        if (value == null) {
            return 0;
        }

        return castToByte(value).byteValue();
    }

    public Short getShort(String key) {
        Object value = get(key);

        return castToShort(value);
    }

    public short getShortValue(String key) {
        Object value = get(key);

        if (value == null) {
            return 0;
        }

        return castToShort(value).shortValue();
    }

    public Integer getInteger(String key) {
        Object value = get(key);

        return castToInt(value);
    }

    public int getIntValue(String key) {
        Object value = get(key);

        if (value == null) {
            return 0;
        }

        return castToInt(value).intValue();
    }

    public Long getLong(String key) {
        Object value = get(key);

        return castToLong(value);
    }

    public long getLongValue(String key) {
        Object value = get(key);

        if (value == null) {
            return 0L;
        }

        return castToLong(value).longValue();
    }

    public Float getFloat(String key) {
        Object value = get(key);

        return castToFloat(value);
    }

    public float getFloatValue(String key) {
        Object value = get(key);

        if (value == null) {
            return 0F;
        }

        return castToFloat(value).floatValue();
    }

    public Double getDouble(String key) {
        Object value = get(key);

        return castToDouble(value);
    }

    public double getDoubleValue(String key) {
        Object value = get(key);

        if (value == null) {
            return 0D;
        }

        return castToDouble(value);
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = get(key);

        return castToBigDecimal(value);
    }

    public BigInteger getBigInteger(String key) {
        Object value = get(key);

        return castToBigInteger(value);
    }

    public String getString(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public Date getDate(String key) {
        Object value = get(key);

        return castToDate(value);
    }

    public java.sql.Date getSqlDate(String key) {
        Object value = get(key);

        return castToSqlDate(value);
    }

    public java.sql.Timestamp getTimestamp(String key) {
        Object value = get(key);

        return castToTimestamp(value);
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    public ModelObject chainPut(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public void putAll(Map<? extends Object, ? extends Object> m) {
        map.putAll(m);
    }

    public ModelObject chainPutAll(Map<? extends String, ? extends Object> m) {
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

    public Object remove(String key) {
        return map.remove(key);
    }

    public ModelObject chainRemove(String key) {
        map.remove(key);
        return this;
    }

    public Set<Object> keySet() {
        return map.keySet();
    }

    public Collection<Object> values() {
        return map.values();
    }

    public Set<Map.Entry<Object, Object>> entrySet() {
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
            this.map.remove(key);
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
            this.map.remove(key);
        }
    }

    public boolean isEmpty(String key) {
        if (this.get(key) == null || String.valueOf(this.get(key)).equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNotEmpty(String key) {
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
        String[] strings = null;
        if (removed != null) {
            strings = new String[removed.length];
            for (int i = 0; i < removed.length; i++) {
                strings[i] = getKeyName(removed[i]);
            }
        }

        for (ModelObjectChecker checker : checkers) {
            checker.checkerUpdate(this, strings);
        }
        return true;
    }

    public boolean checkUpdate(Object... removed) {
        try {
            String[] strings = null;
            if (removed != null) {
                strings = new String[removed.length];
                for (int i = 0; i < removed.length; i++) {
                    strings[i] = getKeyName(removed[i]);
                }
            }

            for (ModelObjectChecker checker : checkers) {
                checker.checkerUpdate(this, strings);
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
                    if (getKeyName(o).equals(getKeyName(k))) {
                        in = true;
                    }
                }
                if (!in) {
                    removed.add(o);
                }
            }

            for (Object o : removed) {
                this.map.remove(o);
            }
        }
    }

    public ModelObject copy(Object... keys) {
        if (keys != null) {
            ModelObject object = new ModelObject();
            object.objectClass = this.objectClass;
            for (Object key : keys) {
                if (this.map.get(key) != null) {
                    object.map.put(key, this.get(getKeyName(key)));
                }
            }
            return object;
        }
        return null;
    }

    public ModelObject copy(List keys) {
        return this.copy(keys.toArray());
    }

    @Override
    public boolean containsKey(Enum key) {
        return this.containsKey(key.name());
    }

    @Override
    public Object get(Enum key) {
        return get(key.name());
    }

    @Override
    public ModelObject getModelObject(Enum key) {
        return getModelObject(key.name());
    }

    @Override
    public ModelArray getModelArray(Enum key) {
        return getModelArray(key.name());
    }

    @Override
    public <T> List<T> getArray(Enum key) {
        return getArray(key.name());
    }

    @Override
    public <T> T getObject(Enum key, Class<T> clazz) {
        return getObject(key.name(), clazz);
    }

    @Override
    public Boolean getBoolean(Enum key) {
        return getBoolean(key.name());
    }

    @Override
    public byte[] getBytes(Enum key) {
        return getBytes(key.name());
    }

    @Override
    public boolean getBooleanValue(Enum key) {
        return getBooleanValue(key.name());
    }

    @Override
    public Byte getByte(Enum key) {
        return getByte(key.name());
    }

    @Override
    public byte getByteValue(Enum key) {
        return getByteValue(key.name());
    }

    @Override
    public Short getShort(Enum key) {
        return getShort(key.name());
    }

    @Override
    public short getShortValue(Enum key) {
        return getShortValue(key.name());
    }

    @Override
    public Integer getInteger(Enum key) {
        return getInteger(key.name());
    }

    @Override
    public int getIntValue(Enum key) {
        return getIntValue(key.name());
    }

    @Override
    public Long getLong(Enum key) {
        return getLong(key.name());
    }

    @Override
    public long getLongValue(Enum key) {
        return getLongValue(key.name());
    }

    @Override
    public Float getFloat(Enum key) {
        return getFloat(key.name());
    }

    @Override
    public float getFloatValue(Enum key) {
        return getFloatValue(key.name());
    }

    @Override
    public Double getDouble(Enum key) {
        return getDouble(key.name());
    }

    @Override
    public double getDoubleValue(Enum key) {
        return getDoubleValue(key.name());
    }

    @Override
    public BigDecimal getBigDecimal(Enum key) {
        return getBigDecimal(key.name());
    }

    @Override
    public BigInteger getBigInteger(Enum key) {
        return getBigInteger(key.name());
    }

    @Override
    public String getString(Enum key) {
        return getString(key.name());
    }

    @Override
    public Date getDate(Enum key) {
        return getDate(key.name());
    }

    @Override
    public java.sql.Date getSqlDate(Enum key) {
        return getSqlDate(key.name());
    }

    @Override
    public Timestamp getTimestamp(Enum key) {
        return getTimestamp(key.name());
    }

    @Override
    public Object put(Enum key, Object value) {
        return put(key.name(), value);
    }

    @Override
    public ModelObject chainPut(Enum key, Object value) {
        return chainPut(key.name(), value);
    }

    @Override
    public Object remove(Enum key) {
        return remove(key.name());
    }

    @Override
    public ModelObject chainRemove(Enum key) {
        return chainRemove(key.name());
    }

    @Override
    public boolean isEmpty(Enum key) {
        return isEmpty(key.name());
    }

    @Override
    public boolean isNotEmpty(Enum key) {
        return isNotEmpty(key.name());
    }

    @Override
    public boolean containsKey(Class key) {
        return this.containsKey(key.getSimpleName());
    }

    @Override
    public Object get(Class key) {
        return get(key.getSimpleName());
    }

    @Override
    public ModelObject getModelObject(Class key) {
        return getModelObject(key.getSimpleName());
    }

    @Override
    public ModelArray getModelArray(Class key) {
        return getModelArray(key.getSimpleName());
    }

    @Override
    public <T> List<T> getArray(Class key) {
        return getArray(key.getSimpleName());
    }

    @Override
    public <T> T getObject(Class key, Class<T> clazz) {
        return getObject(key.getSimpleName(), clazz);
    }

    @Override
    public Boolean getBoolean(Class key) {
        return getBoolean(key.getSimpleName());
    }

    @Override
    public byte[] getBytes(Class key) {
        return getBytes(key.getSimpleName());
    }

    @Override
    public boolean getBooleanValue(Class key) {
        return getBooleanValue(key.getSimpleName());
    }

    @Override
    public Byte getByte(Class key) {
        return getByte(key.getSimpleName());
    }

    @Override
    public byte getByteValue(Class key) {
        return getByteValue(key.getSimpleName());
    }

    @Override
    public Short getShort(Class key) {
        return getShort(key.getSimpleName());
    }

    @Override
    public short getShortValue(Class key) {
        return getShortValue(key.getSimpleName());
    }

    @Override
    public Integer getInteger(Class key) {
        return getInteger(key.getSimpleName());
    }

    @Override
    public int getIntValue(Class key) {
        return getIntValue(key.getSimpleName());
    }

    @Override
    public Long getLong(Class key) {
        return getLong(key.getSimpleName());
    }

    @Override
    public long getLongValue(Class key) {
        return getLongValue(key.getSimpleName());
    }

    @Override
    public Float getFloat(Class key) {
        return getFloat(key.getSimpleName());
    }

    @Override
    public float getFloatValue(Class key) {
        return getFloatValue(key.getSimpleName());
    }

    @Override
    public Double getDouble(Class key) {
        return getDouble(key.getSimpleName());
    }

    @Override
    public double getDoubleValue(Class key) {
        return getDoubleValue(key.getSimpleName());
    }

    @Override
    public BigDecimal getBigDecimal(Class key) {
        return getBigDecimal(key.getSimpleName());
    }

    @Override
    public BigInteger getBigInteger(Class key) {
        return getBigInteger(key.getSimpleName());
    }

    @Override
    public String getString(Class key) {
        return getString(key.getSimpleName());
    }

    @Override
    public Date getDate(Class key) {
        return getDate(key.getSimpleName());
    }

    @Override
    public java.sql.Date getSqlDate(Class key) {
        return getSqlDate(key.getSimpleName());
    }

    @Override
    public Timestamp getTimestamp(Class key) {
        return getTimestamp(key.getSimpleName());
    }

    @Override
    public Object put(Class key, Object value) {
        return put(key.getSimpleName(), value);
    }

    @Override
    public ModelObject chainPut(Class key, Object value) {
        return chainPut(key.getSimpleName(), value);
    }

    @Override
    public Object remove(Class key) {
        return remove(key.getSimpleName());
    }

    @Override
    public ModelObject chainRemove(Class key) {
        return chainRemove(key.getSimpleName());
    }

    @Override
    public boolean isEmpty(Class key) {
        return isEmpty(key.getSimpleName());
    }

    @Override
    public boolean isNotEmpty(Class key) {
        return isNotEmpty(key.getSimpleName());
    }
}
