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

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import org.mimosaframework.core.json.parser.Feature;
import org.mimosaframework.core.json.parser.ParserConfig;
import org.mimosaframework.core.json.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
@SuppressWarnings("serial")
public class ModelArray extends Model implements List<Object>, Cloneable, RandomAccess, Serializable {

    private final List<Object> list;
    protected transient Object relatedArray;
    protected transient Type componentType;

    public ModelArray() {
        this.list = new ArrayList<Object>();
    }

    public ModelArray(List<Object> list) {
        this.list = list;
    }

    public ModelArray(int initialCapacity) {
        this.list = new ArrayList<Object>(initialCapacity);
    }

    /**
     * @return
     * @since 1.1.16
     */
    public Object getRelatedArray() {
        return relatedArray;
    }

    public void setRelatedArray(Object relatedArray) {
        this.relatedArray = relatedArray;
    }

    public Type getComponentType() {
        return componentType;
    }

    public void setComponentType(Type componentType) {
        this.componentType = componentType;
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public Iterator<Object> iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    public boolean add(Object e) {
        return list.add(e);
    }

    public ModelArray fluentAdd(Object e) {
        list.add(e);
        return this;
    }

    public boolean remove(Object o) {
        return list.remove(o);
    }

    public ModelArray fluentRemove(Object o) {
        list.remove(o);
        return this;
    }

    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    public boolean addAll(Collection<? extends Object> c) {
        return list.addAll(c);
    }

    public ModelArray fluentAddAll(Collection<? extends Object> c) {
        list.addAll(c);
        return this;
    }

    public boolean addAll(int index, Collection<? extends Object> c) {
        return list.addAll(index, c);
    }

    public ModelArray fluentAddAll(int index, Collection<? extends Object> c) {
        list.addAll(index, c);
        return this;
    }

    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    public ModelArray fluentRemoveAll(Collection<?> c) {
        list.removeAll(c);
        return this;
    }

    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    public ModelArray fluentRetainAll(Collection<?> c) {
        list.retainAll(c);
        return this;
    }

    public void clear() {
        list.clear();
    }

    public ModelArray fluentClear() {
        list.clear();
        return this;
    }

    public Object set(int index, Object element) {
        if (index == -1) {
            list.add(element);
            return null;
        }

        if (list.size() <= index) {
            for (int i = list.size(); i < index; ++i) {
                list.add(null);
            }
            list.add(element);
            return null;
        }

        return list.set(index, element);
    }

    public ModelArray fluentSet(int index, Object element) {
        set(index, element);
        return this;
    }

    public void add(int index, Object element) {
        list.add(index, element);
    }

    public ModelArray fluentAdd(int index, Object element) {
        list.add(index, element);
        return this;
    }

    public Object remove(int index) {
        return list.remove(index);
    }

    public ModelArray fluentRemove(int index) {
        list.remove(index);
        return this;
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    public ListIterator<Object> listIterator() {
        return list.listIterator();
    }

    public ListIterator<Object> listIterator(int index) {
        return list.listIterator(index);
    }

    public List<Object> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    public Object get(int index) {
        return list.get(index);
    }

    public ModelObject getModelObject(int index) {
        Object value = list.get(index);

        if (value instanceof ModelObject) {
            return (ModelObject) value;
        }

        return (ModelObject) toJSON(value);
    }

    public ModelArray getModelArray(int index) {
        Object value = list.get(index);

        if (value instanceof ModelArray) {
            return (ModelArray) value;
        }

        return (ModelArray) toJSON(value);
    }

    public <T> T getObject(int index, Class<T> clazz) {
        Object obj = list.get(index);
        return TypeUtils.castToJavaBean(obj, clazz);
    }

    public Boolean getBoolean(int index) {
        Object value = get(index);

        if (value == null) {
            return null;
        }

        return TypeUtils.castToBoolean(value);
    }

    public boolean getBooleanValue(int index) {
        Object value = get(index);

        if (value == null) {
            return false;
        }

        return TypeUtils.castToBoolean(value).booleanValue();
    }

    public Byte getByte(int index) {
        Object value = get(index);

        return TypeUtils.castToByte(value);
    }

    public byte getByteValue(int index) {
        Object value = get(index);

        if (value == null) {
            return 0;
        }

        return TypeUtils.castToByte(value).byteValue();
    }

    public Short getShort(int index) {
        Object value = get(index);

        return TypeUtils.castToShort(value);
    }

    public short getShortValue(int index) {
        Object value = get(index);

        if (value == null) {
            return 0;
        }

        return TypeUtils.castToShort(value).shortValue();
    }

    public Integer getInteger(int index) {
        Object value = get(index);

        return TypeUtils.castToInt(value);
    }

    public int getIntValue(int index) {
        Object value = get(index);

        if (value == null) {
            return 0;
        }

        return TypeUtils.castToInt(value).intValue();
    }

    public Long getLong(int index) {
        Object value = get(index);

        return TypeUtils.castToLong(value);
    }

    public long getLongValue(int index) {
        Object value = get(index);

        if (value == null) {
            return 0L;
        }

        return TypeUtils.castToLong(value).longValue();
    }

    public Float getFloat(int index) {
        Object value = get(index);

        return TypeUtils.castToFloat(value);
    }

    public float getFloatValue(int index) {
        Object value = get(index);

        if (value == null) {
            return 0F;
        }

        return TypeUtils.castToFloat(value).floatValue();
    }

    public Double getDouble(int index) {
        Object value = get(index);

        return TypeUtils.castToDouble(value);
    }

    public double getDoubleValue(int index) {
        Object value = get(index);

        if (value == null) {
            return 0D;
        }

        return TypeUtils.castToDouble(value);
    }

    public BigDecimal getBigDecimal(int index) {
        Object value = get(index);

        return TypeUtils.castToBigDecimal(value);
    }

    public BigInteger getBigInteger(int index) {
        Object value = get(index);

        return TypeUtils.castToBigInteger(value);
    }

    public String getString(int index) {
        Object value = get(index);

        return TypeUtils.castToString(value);
    }

    public java.util.Date getDate(int index) {
        Object value = get(index);

        return TypeUtils.castToDate(value);
    }

    public java.sql.Date getSqlDate(int index) {
        Object value = get(index);

        return TypeUtils.castToSqlDate(value);
    }

    public java.sql.Timestamp getTimestamp(int index) {
        Object value = get(index);

        return TypeUtils.castToTimestamp(value);
    }

    @Override
    public Object clone() {
        return new ModelArray(new ArrayList<Object>(list));
    }

    public boolean equals(Object obj) {
        return this.list.equals(obj);
    }

    public int hashCode() {
        return this.list.hashCode();
    }

    public <T> List<T> toJavaObjects(Class<T> clazz) {
        if (this.size() > 0) {
            List<T> list = new ArrayList<T>();
            for (int i = 0; i < this.size(); i++) {
                list.add(TypeUtils.cast(this.get(i), clazz, ParserConfig.getGlobalInstance()));
            }
            return list;
        }
        return null;
    }

    public static ModelArray parseArray(String text, Feature... features) {
        return (ModelArray) parse(text, features);
    }

    public static ModelArray parseArray(String text) {
        Object obj = parse(text);
        if (obj instanceof ModelObject) {
            ModelArray array = new ModelArray();
            array.add(obj);
            return array;
        }

        return (ModelArray) Model.toJSON(obj);
    }

    public static ModelArrayBuilder builder() {
        return new ModelArrayBuilder();
    }
}
