package org.mimosaframework.core.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface ModelBase {
    boolean containsKey(Object key);

    Object get(Object key);

    ModelObject getModelObject(Object key);

    ModelArray getModelArray(Object key);

    <T> List<T> getArray(Object key);

    <T> T getObject(Object key, Class<T> clazz);

    Boolean getBoolean(Object key);

    byte[] getBytes(Object key);

    boolean getBooleanValue(Object key);

    Byte getByte(Object key);

    byte getByteValue(Object key);

    Short getShort(Object key);

    short getShortValue(Object key);

    Integer getInteger(Object key);

    int getIntValue(Object key);

    Long getLong(Object key);

    long getLongValue(Object key);

    Float getFloat(Object key);

    float getFloatValue(Object key);

    Double getDouble(Object key);

    double getDoubleValue(Object key);

    BigDecimal getBigDecimal(Object key);

    BigInteger getBigInteger(Object key);

    String getString(Object key);

    Date getDate(Object key);

    java.sql.Date getSqlDate(Object key);

    java.sql.Timestamp getTimestamp(Object key);

    Object put(Object key, Object value);

    ModelObject chainPut(Object key, Object value);

    Object remove(Object key);

    ModelObject chainRemove(Object key);

    boolean isEmpty(Object key);

    boolean isNotEmpty(Object key);
}
