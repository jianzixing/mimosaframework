package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;
import org.mimosaframework.orm.sql.stamp.KeyColumnType;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;

public class JavaType2ColumnType {

    public static boolean isNumber(KeyColumnType type, KeyColumnType... ext) {
        if (type == KeyColumnType.INT) return true;
        if (type == KeyColumnType.TINYINT) return true;
        if (type == KeyColumnType.SMALLINT) return true;
        if (type == KeyColumnType.BIGINT) return true;
        if (type == KeyColumnType.FLOAT) return true;
        if (type == KeyColumnType.DOUBLE) return true;
        if (type == KeyColumnType.DECIMAL) return true;
        if (ext != null) {
            for (KeyColumnType i : ext) {
                if (i == type) return true;
            }
        }
        return false;
    }

    public static boolean isTime(KeyColumnType type) {
        if (type == KeyColumnType.DATE) return true;
        if (type == KeyColumnType.TIME) return true;
        if (type == KeyColumnType.DATETIME) return true;
        if (type == KeyColumnType.TIMESTAMP) return true;
        return false;
    }

    public static boolean isBoolean(KeyColumnType type) {
        if (type == KeyColumnType.BOOLEAN) return true;
        return false;
    }

    public static void setBuilderType(KeyColumnType type, ColumnTypeBuilder builder, int length, int scale) {
        if (type == KeyColumnType.INT) {
            if (builder != null) builder.intType();
        }
        if (type == KeyColumnType.VARCHAR) {
            if (builder != null) builder.varchar(length);
        }
        if (type == KeyColumnType.CHAR) {
            if (builder != null) builder.charType(length);
        }
        if (type == KeyColumnType.BLOB) {
            if (builder != null) builder.blob();
        }
        if (type == KeyColumnType.MEDIUMBLOB) {
            if (builder != null) builder.mediumBlob();
        }
        if (type == KeyColumnType.LONGBLOB) {
            if (builder != null) builder.longBlob();
        }
        if (type == KeyColumnType.TEXT) {
            if (builder != null) builder.text();
        }
        if (type == KeyColumnType.JSON) {
            if (builder != null) builder.json();
        }
        if (type == KeyColumnType.MEDIUMTEXT) {
            if (builder != null) builder.mediumText();
        }
        if (type == KeyColumnType.LONGTEXT) {
            if (builder != null) builder.longText();
        }
        if (type == KeyColumnType.TINYINT) {
            if (builder != null) builder.tinyint();
        }
        if (type == KeyColumnType.SMALLINT) {
            if (builder != null) builder.smallint();
        }
        if (type == KeyColumnType.BIGINT) {
            if (builder != null) builder.bigint();
        }
        if (type == KeyColumnType.FLOAT) {
            if (builder != null) builder.floatType();
        }
        if (type == KeyColumnType.DOUBLE) {
            if (builder != null) builder.doubleType();
        }
        if (type == KeyColumnType.DECIMAL) {
            if (builder != null) builder.decimal(length, scale);
        }
        if (type == KeyColumnType.BOOLEAN) {
            if (builder != null) builder.booleanType();
        }
        if (type == KeyColumnType.DATE) {
            if (builder != null) builder.date();
        }
        if (type == KeyColumnType.TIME) {
            if (builder != null) builder.time();
        }
        if (type == KeyColumnType.DATETIME) {
            if (builder != null) builder.datetime();
        }
        if (type == KeyColumnType.TIMESTAMP) {
            if (builder != null) builder.timestamp();
        }
    }

    public static KeyColumnType getColumnTypeByJava(Class c,
                                                    ColumnTypeBuilder builder,
                                                    int length,
                                                    int scale) {
        KeyColumnType type = getColumnTypeByJava(c);
        setBuilderType(type, builder, length, scale);
        return type;
    }

    public static KeyColumnType getColumnTypeByJava(Class c) {
        if (c.equals(Integer.class) || c.equals(int.class)) {
            return KeyColumnType.INT;
        }
        if (c.equals(String.class)) {
            return KeyColumnType.VARCHAR;
        }
        if (c.equals(Character.class) || c.equals(char.class)) {
            return KeyColumnType.CHAR;
        }
        if (c.equals(Blob.class) || c.equals(SupportBlob.class)) {
            return KeyColumnType.BLOB;
        }
        if (c.equals(SupportMediumBlob.class)) {
            return KeyColumnType.MEDIUMBLOB;
        }
        if (c.equals(SupportLongBlob.class)) {
            return KeyColumnType.LONGBLOB;
        }
        if (c.equals(Clob.class) || c.equals(SupportText.class)) {
            return KeyColumnType.TEXT;
        }
        if (c.equals(SupportMediumText.class)) {
            return KeyColumnType.MEDIUMTEXT;
        }
        if (c.equals(SupportLongText.class)) {
            return KeyColumnType.LONGTEXT;
        }
        if (c.equals(SupportJSON.class)) {
            return KeyColumnType.JSON;
        }
        if (c.equals(Byte.class) || c.equals(byte.class)) {
            return KeyColumnType.TINYINT;
        }
        if (c.equals(Short.class) || c.equals(short.class)) {
            return KeyColumnType.SMALLINT;
        }
        if (c.equals(Long.class) || c.equals(long.class)) {
            return KeyColumnType.BIGINT;
        }
        if (c.equals(Float.class) || c.equals(float.class)) {
            return KeyColumnType.FLOAT;
        }
        if (c.equals(Double.class) || c.equals(double.class)) {
            return KeyColumnType.DOUBLE;
        }
        if (c.equals(BigDecimal.class)) {
            return KeyColumnType.DECIMAL;
        }
        if (c.equals(Boolean.class) || c.equals(boolean.class)) {
            return KeyColumnType.BOOLEAN;
        }
        if (c.equals(java.sql.Date.class)) {
            return KeyColumnType.DATE;
        }
        if (c.equals(java.sql.Time.class)) {
            return KeyColumnType.TIME;
        }
        if (c.equals(java.util.Date.class)) {
            return KeyColumnType.DATETIME;
        }
        if (c.equals(java.sql.Timestamp.class)) {
            return KeyColumnType.TIMESTAMP;
        }
        return null;
    }
}
