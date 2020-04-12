package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.platform.DifferentColumn;
import org.mimosaframework.orm.platform.SupportMediumText;
import org.mimosaframework.orm.platform.SupportText;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OracleDifferentColumn implements DifferentColumn {
    protected static final Map<Class, String> TYPES_MAPPING = new HashMap<Class, String>();

    static {
        TYPES_MAPPING.put(SupportText.class, "CLOB");
        TYPES_MAPPING.put(SupportMediumText.class, "CLOB");
        TYPES_MAPPING.put(Boolean.class, "NUMBER");
        TYPES_MAPPING.put(boolean.class, "NUMBER");
        TYPES_MAPPING.put(Double.class, "BINARY_DOUBLE");
        TYPES_MAPPING.put(double.class, "BINARY_DOUBLE");
        TYPES_MAPPING.put(BigDecimal.class, "NUMBER");
        TYPES_MAPPING.put(String.class, "VARCHAR2");
        TYPES_MAPPING.put(char.class, "CHAR");
        TYPES_MAPPING.put(Integer.class, "NUMBER");
        TYPES_MAPPING.put(int.class, "NUMBER");
        TYPES_MAPPING.put(Date.class, "DATE");
        TYPES_MAPPING.put(java.sql.Date.class, "DATE");
        TYPES_MAPPING.put(java.sql.Time.class, "DATE");
        TYPES_MAPPING.put(java.sql.Timestamp.class, "TIMESTAMP");
        TYPES_MAPPING.put(java.sql.Blob.class, "BLOB");
        TYPES_MAPPING.put(java.sql.Clob.class, "CLOB");
        TYPES_MAPPING.put(Short.class, "NUMBER");
        TYPES_MAPPING.put(short.class, "NUMBER");
        TYPES_MAPPING.put(Byte.class, "NUMBER");
        TYPES_MAPPING.put(byte.class, "NUMBER");
        TYPES_MAPPING.put(Long.class, "NUMBER");
        TYPES_MAPPING.put(long.class, "NUMBER");
        TYPES_MAPPING.put(Float.class, "BINARY_FLOAT");
        TYPES_MAPPING.put(float.class, "BINARY_FLOAT");
    }

    @Override
    public boolean isLikeColumnName(String f1, String f2) {
        if (f1.equalsIgnoreCase(f2)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isLikeTypeName(String typeName, Class type, int dataType) {
        String mappingTypeName = TYPES_MAPPING.get(type);
        if (mappingTypeName == null) {
            throw new IllegalArgumentException(I18n.print("not_support_type",
                    type.getSimpleName()));
        }
        if (mappingTypeName.equalsIgnoreCase(typeName)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isLikeAutoIncrement(String autoIncrement, boolean isAutoIncrement) {
        if (StringTools.isNotEmpty(autoIncrement)
                && autoIncrement.equalsIgnoreCase("YES")
                && isAutoIncrement) {
            return true;
        }
        if (!isAutoIncrement) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isLikeLength(int dbLen, int dbDigits, int fieldLen, int fieldDigits) {
        if (dbLen == fieldLen && dbDigits == fieldDigits) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isLikeNullable(String dbNullable, boolean nullable) {
        if (StringTools.isNotEmpty(dbNullable)
                && dbNullable.equalsIgnoreCase("NO")
                && nullable) {
            return false;
        }
        if (StringTools.isNotEmpty(dbNullable)
                && dbNullable.equalsIgnoreCase("YES")
                && !nullable) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isLikeDefaultValue(String dbv, String fv) {
        if (StringTools.isNotEmpty(dbv) && StringTools.isNotEmpty(fv)
                && dbv.equals(fv)) {
            return true;
        }
        if (StringTools.isEmpty(dbv) && StringTools.isEmpty(fv)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isLikeComment(String dbComment, String fc) {
        if (StringTools.isNotEmpty(dbComment) && StringTools.isNotEmpty(fc)
                && dbComment.equals(fc)) {
            return true;
        }
        if (StringTools.isEmpty(dbComment) && StringTools.isEmpty(fc)) {
            return true;
        }
        return false;
    }

    @Override
    public String getTypeNameByClass(Class typeClass) {
        return TYPES_MAPPING.get(typeClass);
    }

    @Override
    public String getAutoIncrementTypeNameByClass(Class typeClass) {
        if (typeClass.equals(Long.class)
                || typeClass.equals(long.class)
                || typeClass.equals(int.class)
                || typeClass.equals(Integer.class)) {
            return TYPES_MAPPING.get(typeClass);
        } else {
            return TYPES_MAPPING.get(int.class);
        }
    }

    @Override
    public boolean typeHasLength(Class typeClass) {
        if (typeClass.equals(BigDecimal.class)) return true;
        if (typeClass.equals(String.class)) return true;
        if (typeClass.equals(char.class)) return true;
        return false;
    }

    @Override
    public String getTypeLength(MappingField field) {
        if (this.typeHasLength(field.getMappingFieldType())) {
            if (field.getMappingFieldDecimalDigits() == 0) {
                return "" + field.getMappingFieldLength();
            } else {
                return "" + field.getMappingFieldLength() + "," + field.getMappingFieldDecimalDigits();
            }
        }

        if (field.getMappingFieldType() == Boolean.class
                || field.getMappingFieldType() == boolean.class) {
            return "1";
        }
        if (field.getMappingFieldType() == Integer.class
                || field.getMappingFieldType() == int.class) {
            return "10";
        }
        if (field.getMappingFieldType() == Short.class
                || field.getMappingFieldType() == short.class) {
            return "5";
        }
        if (field.getMappingFieldType() == Byte.class
                || field.getMappingFieldType() == byte.class) {
            return "3";
        }
        if (field.getMappingFieldType() == Long.class
                || field.getMappingFieldType() == long.class) {
            return "19";
        }
        return null;
    }
}
