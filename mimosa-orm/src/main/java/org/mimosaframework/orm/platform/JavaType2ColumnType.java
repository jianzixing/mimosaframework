package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;
import org.mimosaframework.orm.sql.stamp.KeyColumnType;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;

public class JavaType2ColumnType {
    public static KeyColumnType getColumnTypeByJava(Class c) {
        return getColumnTypeByJava(c, null, 0, 0);
    }

    public static KeyColumnType getColumnTypeByJava(Class c,
                                                    ColumnTypeBuilder builder,
                                                    int length,
                                                    int scale) {
        if (c.equals(Integer.class) || c.equals(int.class)) {
            if (builder != null) builder.intType();
            return KeyColumnType.INT;
        }
        if (c.equals(String.class)) {
            if (builder != null) builder.varchar(length);
            return KeyColumnType.VARCHAR;
        }
        if (c.equals(Character.class) || c.equals(char.class)) {
            if (builder != null) builder.charType(length);
            return KeyColumnType.CHAR;
        }
        if (c.equals(Blob.class) || c.equals(SupportBlob.class)) {
            if (builder != null) builder.blob();
            return KeyColumnType.BLOB;
        }
        if (c.equals(SupportMediumBlob.class)) {
            if (builder != null) builder.mediumBlob();
            return KeyColumnType.MEDIUMBLOB;
        }
        if (c.equals(SupportLongBlob.class)) {
            if (builder != null) builder.longBlob();
            return KeyColumnType.LONGBLOB;
        }
        if (c.equals(Clob.class) || c.equals(SupportText.class)) {
            if (builder != null) builder.text();
            return KeyColumnType.TEXT;
        }
        if (c.equals(SupportMediumText.class)) {
            if (builder != null) builder.mediumText();
            return KeyColumnType.MEDIUMTEXT;
        }
        if (c.equals(SupportLongText.class)) {
            if (builder != null) builder.longText();
            return KeyColumnType.LONGTEXT;
        }
        if (c.equals(Byte.class) || c.equals(byte.class)) {
            if (builder != null) builder.tinyint();
            return KeyColumnType.TINYINT;
        }
        if (c.equals(Short.class) || c.equals(short.class)) {
            if (builder != null) builder.smallint();
            return KeyColumnType.SMALLINT;
        }
        if (c.equals(Long.class) || c.equals(long.class)) {
            if (builder != null) builder.bigint();
            return KeyColumnType.BIGINT;
        }
        if (c.equals(Float.class) || c.equals(float.class)) {
            if (builder != null) builder.floatType();
            return KeyColumnType.FLOAT;
        }
        if (c.equals(Double.class) || c.equals(double.class)) {
            if (builder != null) builder.doubleType();
            return KeyColumnType.DOUBLE;
        }
        if (c.equals(BigDecimal.class)) {
            if (builder != null) builder.decimal(length, scale);
            return KeyColumnType.DECIMAL;
        }
        if (c.equals(Boolean.class) || c.equals(boolean.class)) {
            if (builder != null) builder.booleanType();
            return KeyColumnType.BOOLEAN;
        }
        if (c.equals(java.sql.Date.class)) {
            if (builder != null) builder.date();
            return KeyColumnType.DATE;
        }
        if (c.equals(java.sql.Time.class)) {
            if (builder != null) builder.time();
            return KeyColumnType.TIME;
        }
        if (c.equals(java.util.Date.class)) {
            if (builder != null) builder.datetime();
            return KeyColumnType.DATETIME;
        }
        if (c.equals(java.sql.Timestamp.class)) {
            if (builder != null) builder.timestamp();
            return KeyColumnType.TIMESTAMP;
        }
        return null;
    }
}
