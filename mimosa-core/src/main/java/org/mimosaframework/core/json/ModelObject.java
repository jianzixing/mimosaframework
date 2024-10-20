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

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.core.exception.ModelCheckerException;
import org.mimosaframework.core.json.annotation.JSONField;
import org.mimosaframework.core.json.parser.ParserConfig;
import org.mimosaframework.core.json.util.TypeUtils;
import org.mimosaframework.core.utils.ClassUtils;

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
 * 使用的fastjson的开源代码
 */
@SuppressWarnings("serial")
public class ModelObject extends Model implements Map<Object, Object>, Cloneable, Serializable, InvocationHandler, ModelBase, ModelFunc {

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

    public static ModelObjectBuilder builder(Class<?> tableContactsClass) {
        return new ModelObjectBuilder(tableContactsClass);
    }

    public static ModelObjectBuilder builder() {
        return new ModelObjectBuilder();
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
        return map.containsKey(ClassUtils.value(key));
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(ClassUtils.value(key));
    }

    @Override
    public Object remove(Object key) {
        return map.remove(ClassUtils.value(key));
    }

    public Object get(String key) {
        return map.get(key);
    }

    public ModelObject getModelObject(Object key) {
        key = ClassUtils.value(key);
        Object value = map.get(key);

        if (value instanceof ModelObject) {
            return (ModelObject) value;
        }

        return (ModelObject) toJSON(value);
    }

    public ModelArray getModelArray(Object key) {
        key = ClassUtils.value(key);
        Object value = map.get(key);

        if (value instanceof ModelArray) {
            return (ModelArray) value;
        }

        return (ModelArray) toJSON(value);
    }

    public <T> List<T> getArray(Object key) {
        key = ClassUtils.value(key);
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
        key = ClassUtils.value(key);
        Object obj = map.get(key);
        return TypeUtils.castToJavaBean(obj, clazz);
    }

    public Boolean getBoolean(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return castToBoolean(value);
    }

    public byte[] getBytes(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return castToBytes(value);
    }

    public boolean getBooleanValue(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        if (value == null) {
            return false;
        }

        return castToBoolean(value).booleanValue();
    }

    public Byte getByte(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        return castToByte(value);
    }

    public byte getByteValue(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        if (value == null) {
            return 0;
        }

        return castToByte(value).byteValue();
    }

    public Short getShort(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        return castToShort(value);
    }

    public short getShortValue(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        if (value == null) {
            return 0;
        }

        return castToShort(value).shortValue();
    }

    public Integer getInteger(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        return castToInt(value);
    }

    public int getIntValue(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        if (value == null) {
            return 0;
        }

        return castToInt(value).intValue();
    }

    public Long getLong(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        return castToLong(value);
    }

    public long getLongValue(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        if (value == null) {
            return 0L;
        }

        return castToLong(value).longValue();
    }

    public Float getFloat(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        return castToFloat(value);
    }

    public float getFloatValue(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        if (value == null) {
            return 0F;
        }

        return castToFloat(value).floatValue();
    }

    public Double getDouble(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        return castToDouble(value);
    }

    public double getDoubleValue(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        if (value == null) {
            return 0D;
        }

        return castToDouble(value);
    }

    public BigDecimal getBigDecimal(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        return castToBigDecimal(value);
    }

    public BigInteger getBigInteger(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        return castToBigInteger(value);
    }

    public String getString(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public Date getDate(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        return castToDate(value);
    }

    public java.sql.Date getSqlDate(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        return castToSqlDate(value);
    }

    public java.sql.Timestamp getTimestamp(Object key) {
        key = ClassUtils.value(key);
        Object value = get(key);

        return castToTimestamp(value);
    }

    public Object put(Object key, Object value) {
        key = ClassUtils.value(key);
        return map.put(key, value);
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    public ModelObject append(Object key, Object value) {
        key = ClassUtils.value(key);
        map.put(key, value);
        return this;
    }

    public ModelObject append(String key, Object value) {
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

    public ModelObject chainRemove(Object key) {
        key = ClassUtils.value(key);
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

    public boolean isEmpty(Object key) {
        key = ClassUtils.value(key);
        Object value = this.get(key);
        if (value == null || (value instanceof String && value.equals(""))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNotEmpty(Object key) {
        key = ClassUtils.value(key);
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
                strings[i] = ClassUtils.value(removed[i]);
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
                    strings[i] = ClassUtils.value(removed[i]);
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
                    if (ClassUtils.value(o).equals(ClassUtils.value(k))) {
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
                if (this.map.get(ClassUtils.value(key)) != null) {
                    object.map.put(ClassUtils.value(key), this.get(ClassUtils.value(key)));
                }
            }
            return object;
        }
        return null;
    }

    public ModelObject copy(List keys) {
        return this.copy(keys.toArray());
    }

    public void trim() {
        Iterator<Map.Entry<Object, Object>> iterator = this.map.entrySet().iterator();
        Map<Object, Object> map = new HashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            Object value = entry.getValue();
            if (value instanceof String) {
                map.put(entry.getKey(), ((String) value).trim());
            }
        }
        iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            this.map.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public <T> boolean containsKey(FieldFunction<T> key) {
        return this.containsKey((Object) key);
    }

    @Override
    public <T> Object get(FieldFunction<T> key) {
        return this.get((Object) key);
    }

    @Override
    public <T> ModelObject getModelObject(FieldFunction<T> key) {
        return this.getModelObject((Object) key);
    }

    @Override
    public <T> ModelArray getModelArray(FieldFunction<T> key) {
        return this.getModelArray((Object) key);
    }

    @Override
    public <T> List<T> getArray(FieldFunction<T> key) {
        return this.getArray((Object) key);
    }

    @Override
    public <T> T getObject(FieldFunction<T> key, Class<T> clazz) {
        return this.getObject((Object) key, clazz);
    }

    @Override
    public <T> Boolean getBoolean(FieldFunction<T> key) {
        return this.getBoolean((Object) key);
    }

    @Override
    public <T> byte[] getBytes(FieldFunction<T> key) {
        return this.getBytes((Object) key);
    }

    @Override
    public <T> boolean getBooleanValue(FieldFunction<T> key) {
        return this.getBooleanValue((Object) key);
    }

    @Override
    public <T> Byte getByte(FieldFunction<T> key) {
        return this.getByte((Object) key);
    }

    @Override
    public <T> byte getByteValue(FieldFunction<T> key) {
        return this.getByteValue((Object) key);
    }

    @Override
    public <T> Short getShort(FieldFunction<T> key) {
        return this.getShort((Object) key);
    }

    @Override
    public <T> short getShortValue(FieldFunction<T> key) {
        return this.getShortValue((Object) key);
    }

    @Override
    public <T> Integer getInteger(FieldFunction<T> key) {
        return this.getInteger((Object) key);
    }

    @Override
    public <T> int getIntValue(FieldFunction<T> key) {
        return this.getIntValue((Object) key);
    }

    @Override
    public <T> Long getLong(FieldFunction<T> key) {
        return this.getLong((Object) key);
    }

    @Override
    public <T> long getLongValue(FieldFunction<T> key) {
        return this.getLongValue((Object) key);
    }

    @Override
    public <T> Float getFloat(FieldFunction<T> key) {
        return this.getFloat((Object) key);
    }

    @Override
    public <T> float getFloatValue(FieldFunction<T> key) {
        return this.getFloatValue((Object) key);
    }

    @Override
    public <T> Double getDouble(FieldFunction<T> key) {
        return this.getDouble((Object) key);
    }

    @Override
    public <T> double getDoubleValue(FieldFunction<T> key) {
        return this.getDoubleValue((Object) key);
    }

    @Override
    public <T> BigDecimal getBigDecimal(FieldFunction<T> key) {
        return this.getBigDecimal((Object) key);
    }

    @Override
    public <T> BigInteger getBigInteger(FieldFunction<T> key) {
        return this.getBigInteger((Object) key);
    }

    @Override
    public <T> String getString(FieldFunction<T> key) {
        return this.getString((Object) key);
    }

    @Override
    public <T> Date getDate(FieldFunction<T> key) {
        return this.getDate((Object) key);
    }

    @Override
    public <T> java.sql.Date getSqlDate(FieldFunction<T> key) {
        return this.getSqlDate((Object) key);
    }

    @Override
    public <T> Timestamp getTimestamp(FieldFunction<T> key) {
        return this.getTimestamp((Object) key);
    }

    @Override
    public <T> Object put(FieldFunction<T> key, Object value) {
        return this.put((Object) key, value);
    }

    @Override
    public <T> ModelObject append(FieldFunction<T> key, Object value) {
        return this.append((Object) key, value);
    }

    @Override
    public <T> Object remove(FieldFunction<T> key) {
        return this.remove((Object) key);
    }

    @Override
    public <T> ModelObject chainRemove(FieldFunction<T> key) {
        return this.chainRemove((Object) key);
    }

    @Override
    public <T> boolean isEmpty(FieldFunction<T> key) {
        return this.isEmpty((Object) key);
    }

    @Override
    public <T> boolean isNotEmpty(FieldFunction<T> key) {
        return this.isNotEmpty((Object) key);
    }
}
