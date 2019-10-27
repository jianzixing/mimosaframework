package org.mimosaframework.orm.platform.db2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.platform.DifferentColumn;
import org.mimosaframework.orm.platform.MediumText;
import org.mimosaframework.orm.platform.Text;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DB2DifferentColumn implements DifferentColumn {
    private static final Log logger = LogFactory.getLog(DB2DifferentColumn.class);
    protected static final Map<Class, String> TYPES_MAPPING = new HashMap<Class, String>();

    static {
        TYPES_MAPPING.put(Text.class, "CLOB");
        TYPES_MAPPING.put(MediumText.class, "CLOB");
        TYPES_MAPPING.put(Double.class, "DOUBLE");
        TYPES_MAPPING.put(double.class, "DOUBLE");
        TYPES_MAPPING.put(BigDecimal.class, "DECIMAL");
        TYPES_MAPPING.put(String.class, "VARCHAR");
        TYPES_MAPPING.put(char.class, "CHAR");
        TYPES_MAPPING.put(Integer.class, "INTEGER");
        TYPES_MAPPING.put(int.class, "INTEGER");
        TYPES_MAPPING.put(Date.class, "TIMESTAMP");
        TYPES_MAPPING.put(java.sql.Date.class, "DATE");
        TYPES_MAPPING.put(java.sql.Time.class, "TIME");
        TYPES_MAPPING.put(java.sql.Timestamp.class, "TIMESTAMP");
        TYPES_MAPPING.put(java.sql.Blob.class, "BLOB");
        TYPES_MAPPING.put(java.sql.Clob.class, "CLOB");
        TYPES_MAPPING.put(Short.class, "SMALLINT");
        TYPES_MAPPING.put(short.class, "SMALLINT");
        TYPES_MAPPING.put(Byte.class, "SMALLINT");
        TYPES_MAPPING.put(byte.class, "SMALLINT");
        TYPES_MAPPING.put(Long.class, "BIGINT");
        TYPES_MAPPING.put(long.class, "BIGINT");
        TYPES_MAPPING.put(Float.class, "FLOAT");
        TYPES_MAPPING.put(float.class, "FLOAT");

        TYPES_MAPPING.put(Boolean.class, "CHAR(1)");
        TYPES_MAPPING.put(boolean.class, "CHAR(1)");
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
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    DB2DifferentColumn.class, "not_support_type",
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
        if (typeClass.equals(Text.class)) return true;
        if (typeClass.equals(MediumText.class)) return true;
        return false;
    }

    @Override
    public String getTypeLength(MappingField field) {
        if (this.typeHasLength(field.getMappingFieldType())) {
            if (field.getMappingFieldType() == BigDecimal.class) {
                if (field.getMappingFieldDecimalDigits() == 0) {
                    return "" + field.getMappingFieldLength();
                } else {
                    int maxLength = field.getMappingFieldLength();
                    if (maxLength > 31) {
                        maxLength = 31;
                        logger.warn(Messages.get(LanguageMessageFactory.PROJECT,
                                DB2DifferentColumn.class, "db2_max_decimal_len"));
                    }
                    return "" + maxLength + "," + field.getMappingFieldDecimalDigits();
                }
            } else if (field.getMappingFieldType() == Text.class) {
                return "64K";
            } else if (field.getMappingFieldType() == MediumText.class) {
                return "16M";
            } else {
                return "" + field.getMappingFieldLength();
            }
        }
        return null;
    }
}
