package org.mimosaframework.core.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface ModelByEnum {
    boolean containsKey(Enum key);

    Object get(Enum key);

    ModelObject getModelObject(Enum key);

    ModelArray getModelArray(Enum key);

    <T> List<T> getArray(Enum key);

    <T> T getObject(Enum key, Class<T> clazz);

    Boolean getBoolean(Enum key);

    byte[] getBytes(Enum key);

    boolean getBooleanValue(Enum key);

    Byte getByte(Enum key);

    byte getByteValue(Enum key);

    Short getShort(Enum key);

    short getShortValue(Enum key);

    Integer getInteger(Enum key);

    int getIntValue(Enum key);

    Long getLong(Enum key);

    long getLongValue(Enum key);

    Float getFloat(Enum key);

    float getFloatValue(Enum key);

    Double getDouble(Enum key);

    double getDoubleValue(Enum key);

    BigDecimal getBigDecimal(Enum key);

    BigInteger getBigInteger(Enum key);

    String getString(Enum key);

    Date getDate(Enum key);

    java.sql.Date getSqlDate(Enum key);

    java.sql.Timestamp getTimestamp(Enum key);

    Object put(Enum key, Object value);

    ModelObject chainPut(Enum key, Object value);

    Object remove(Enum key);

    ModelObject chainRemove(Enum key);

    boolean isEmpty(Enum key);

    boolean isNotEmpty(Enum key);
}
