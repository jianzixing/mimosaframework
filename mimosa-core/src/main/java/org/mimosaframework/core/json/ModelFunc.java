package org.mimosaframework.core.json;

import org.mimosaframework.core.FieldFunction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public interface ModelFunc {
    <T> boolean containsKey(FieldFunction<T> key);

    <T> Object get(FieldFunction<T> key);

    <T> ModelObject getModelObject(FieldFunction<T> key);

    <T> ModelArray getModelArray(FieldFunction<T> key);

    <T> List<T> getArray(FieldFunction<T> key);

    <T> T getObject(FieldFunction<T> key, Class<T> clazz);

    <T> Boolean getBoolean(FieldFunction<T> key);

    <T> byte[] getBytes(FieldFunction<T> key);

    <T> boolean getBooleanValue(FieldFunction<T> key);

    <T> Byte getByte(FieldFunction<T> key);

    <T> byte getByteValue(FieldFunction<T> key);

    <T> Short getShort(FieldFunction<T> key);

    <T> short getShortValue(FieldFunction<T> key);

    <T> Integer getInteger(FieldFunction<T> key);

    <T> int getIntValue(FieldFunction<T> key);

    <T> Long getLong(FieldFunction<T> key);

    <T> long getLongValue(FieldFunction<T> key);

    <T> Float getFloat(FieldFunction<T> key);

    <T> float getFloatValue(FieldFunction<T> key);

    <T> Double getDouble(FieldFunction<T> key);

    <T> double getDoubleValue(FieldFunction<T> key);

    <T> BigDecimal getBigDecimal(FieldFunction<T> key);

    <T> BigInteger getBigInteger(FieldFunction<T> key);

    <T> String getString(FieldFunction<T> key);

    <T> Date getDate(FieldFunction<T> key);

    <T> java.sql.Date getSqlDate(FieldFunction<T> key);

    <T> java.sql.Timestamp getTimestamp(FieldFunction<T> key);

    <T> Object put(FieldFunction<T> key, Object value);

    <T> ModelObject append(FieldFunction<T> key, Object value);

    <T> Object remove(FieldFunction<T> key);

    <T> ModelObject chainRemove(FieldFunction<T> key);

    <T> boolean isEmpty(FieldFunction<T> key);

    <T> boolean isNotEmpty(FieldFunction<T> key);
}
