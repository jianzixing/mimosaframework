package org.mimosaframework.core.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface ModelByClass {
    boolean containsKey(Class key);

    Object get(Class key);

    ModelObject getModelObject(Class key);

    ModelArray getModelArray(Class key);

    <T> List<T> getArray(Class key);

    <T> T getObject(Class key, Class<T> clazz);

    Boolean getBoolean(Class key);

    byte[] getBytes(Class key);

    boolean getBooleanValue(Class key);

    Byte getByte(Class key);

    byte getByteValue(Class key);

    Short getShort(Class key);

    short getShortValue(Class key);

    Integer getInteger(Class key);

    int getIntValue(Class key);

    Long getLong(Class key);

    long getLongValue(Class key);

    Float getFloat(Class key);

    float getFloatValue(Class key);

    Double getDouble(Class key);

    double getDoubleValue(Class key);

    BigDecimal getBigDecimal(Class key);

    BigInteger getBigInteger(Class key);

    String getString(Class key);

    Date getDate(Class key);

    java.sql.Date getSqlDate(Class key);

    java.sql.Timestamp getTimestamp(Class key);

    Object put(Class key, Object value);

    ModelObject chainPut(Class key, Object value);

    Object remove(Class key);

    ModelObject chainRemove(Class key);

    boolean isEmpty(Class key);

    boolean isNotEmpty(Class key);
}
