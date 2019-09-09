package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingField;

public interface DifferentColumn {
    /**
     * 是否相同
     * true 相同
     * false 不相同
     *
     * @return
     */
    boolean isLikeColumnName(String f1, String f2);

    boolean isLikeTypeName(String typeName, Class type, int dataType);

    boolean isLikeAutoIncrement(String autoIncrement, boolean isAutoIncrement);

    boolean isLikeLength(int dbLen, int dbDigits, int fieldLen, int fieldDigits);

    boolean isLikeNullable(String dbNullable, boolean nullable);

    boolean isLikeDefaultValue(String dbv, String fv);

    boolean isLikeComment(String dbComment, String fc);

    String getTypeNameByClass(Class typeClass);

    String getAutoIncrementTypeNameByClass(Class typeClass);

    boolean typeHasLength(Class typeClass);

    String getTypeLength(MappingField field);
}
