package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.annotation.Column;

public interface MappingField {
    void setMappingTable(MappingTable mappingTable);

    Object getMappingField();

    MappingField getPrevious();

    void setPrevious(MappingField field);

    String getMappingFieldName();

    Column getMappingFieldAnnotation();

    /**
     * 如果@Column有别名，则使用别名，如果不是别名则是转换后的字段名称
     * 应该和数据库列名一致
     * 注意：别名不转换
     */
    String getMappingColumnName();

    boolean isMappingAutoIncrement();

    boolean isMappingFieldIndex();

    boolean isMappingFieldUnique();

    Class getMappingFieldType();

    boolean isMappingFieldAutoIncrement();

    int getMappingFieldLength();

    int getMappingFieldDecimalDigits();

    boolean isMappingFieldNullable();

    String getMappingFieldDefaultValue();

    String getMappingFieldComment();

    boolean isMappingFieldTimeForUpdate();

    boolean isMappingFieldPrimaryKey();

    MappingTable getMappingTable();
}
