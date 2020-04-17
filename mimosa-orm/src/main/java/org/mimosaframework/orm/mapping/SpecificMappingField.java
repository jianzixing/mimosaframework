package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.annotation.Column;

import java.math.BigDecimal;

public class SpecificMappingField implements MappingField {
    private MappingTable mappingTable;
    private MappingField previous;

    /**
     * 映射类中的映射字段，如果是枚举则是枚举值
     * 如果是bean则是bean的字段类型Field
     */
    private Object mappingField;

    /**
     * 映射类的数据类型
     */
    private Class mappingFieldType;

    /**
     * 从映射类中读取的字段名称，仅仅是字段名称
     */
    private String mappingFieldName;

    /**
     * 从映射类中读取的注解
     */
    private Column mappingFieldAnnotation;

    /**
     * 从映射类中读取的字段长度
     */
    private int mappingFieldLength;

    /**
     * 从映射类中读取的精度
     * 数据库中 decimal(30,2) 精度2就是这个值
     */
    private int mappingFieldDecimalDigits;

    /**
     * 从映射类中读取是否为空
     */
    private boolean mappingFieldNullable = true;

    /**
     * 如果@Column有别名，则使用别名，如果不是别名则是转换后的字段名称
     * 应该和数据库列名一致
     * 注意：别名不转换
     */
    private String mappingColumnName;

    /**
     * 从映射类中读取是否为主键
     */
    private boolean mappingFieldPrimaryKey;

    /**
     * 从映射类中读取是否创建索引
     */
    private boolean mappingFieldIndex;

    /**
     * 从映射类中读取是否全局唯一
     */
    private boolean mappingFieldUnique;

    /**
     * 从映射类中读取备注
     */
    private String mappingFieldComment;

    /**
     * 从映射类中读取是否自增
     */
    private boolean mappingFieldAutoIncrement;

    /**
     * 从映射类中读取是否自动更新列
     * 只有添加字段的时候有效
     */
    private boolean mappingFieldTimeForUpdate;

    /**
     * 从映射类中读取的默认值
     */
    private String mappingFieldDefaultValue;

    public SpecificMappingField() {
    }

    public SpecificMappingField(MappingTable mappingTable) {
        this.mappingTable = mappingTable;
    }

    public Object getMappingField() {
        return mappingField;
    }

    public MappingField getPrevious() {
        return previous;
    }

    public void setPrevious(MappingField previous) {
        this.previous = previous;
    }

    public void setMappingField(Object mappingField) {
        this.mappingField = mappingField;
    }

    public Class getMappingFieldType() {
        return mappingFieldType;
    }

    public void setMappingFieldType(Class mappingFieldType) {
        this.mappingFieldType = mappingFieldType;
    }

    @Override
    public void setMappingTable(MappingTable mappingTable) {
        this.mappingTable = mappingTable;
    }

    public MappingTable getMappingTable() {
        return mappingTable;
    }

    public String getMappingFieldName() {
        return mappingFieldName;
    }

    public void setMappingFieldName(String mappingFieldName) {
        this.mappingFieldName = mappingFieldName;
    }

    public Column getMappingFieldAnnotation() {
        return mappingFieldAnnotation;
    }

    public void setMappingFieldAnnotation(Column mappingFieldAnnotation) {
        this.mappingFieldAnnotation = mappingFieldAnnotation;
    }

    public int getMappingFieldLength() {
        if (this.mappingFieldLength == 0
                && (this.mappingFieldType.equals(String.class)
                || this.mappingFieldType.equals(Character.class)
                || this.mappingFieldType.equals(char.class))) {
            return 255;
        } else if ((this.mappingFieldLength == 0 || this.mappingFieldLength == 255)
                && this.mappingFieldType.equals(BigDecimal.class)) {
            return 30;
        }
        return mappingFieldLength;
    }

    public void setMappingFieldLength(int mappingFieldLength) {
        if (mappingFieldLength == 255 && this.mappingFieldType.equals(BigDecimal.class)) {
            this.mappingFieldLength = 30;
        } else if (this.mappingFieldType.equals(String.class)
                || this.mappingFieldType.equals(Character.class)
                || this.mappingFieldType.equals(char.class)) {
            this.mappingFieldLength = mappingFieldLength;
        } else {
            this.mappingFieldLength = 0;
        }
    }

    public int getMappingFieldDecimalDigits() {
        return mappingFieldDecimalDigits;
    }

    public void setMappingFieldDecimalDigits(int mappingFieldDecimalDigits) {
        this.mappingFieldDecimalDigits = mappingFieldDecimalDigits;
    }

    public boolean isMappingFieldNullable() {
        if (this.mappingFieldAutoIncrement || this.mappingFieldPrimaryKey) {
            return false;
        }
        return mappingFieldNullable;
    }

    public void setMappingFieldNullable(boolean mappingFieldNullable) {
        this.mappingFieldNullable = mappingFieldNullable;
    }

    public String getMappingColumnName() {
        return mappingColumnName;
    }

    public void setMappingColumnName(String mappingColumnName) {
        this.mappingColumnName = mappingColumnName;
    }

    public boolean isMappingFieldPrimaryKey() {
        return mappingFieldPrimaryKey;
    }

    public void setMappingFieldPrimaryKey(boolean mappingFieldPrimaryKey) {
        this.mappingFieldPrimaryKey = mappingFieldPrimaryKey;
    }

    public boolean isMappingFieldIndex() {
        return mappingFieldIndex;
    }

    public void setMappingFieldIndex(boolean mappingFieldIndex) {
        this.mappingFieldIndex = mappingFieldIndex;
    }

    public boolean isMappingFieldUnique() {
        return mappingFieldUnique;
    }

    public void setMappingFieldUnique(boolean mappingFieldUnique) {
        this.mappingFieldUnique = mappingFieldUnique;
    }

    public String getMappingFieldComment() {
        return mappingFieldComment;
    }

    public void setMappingFieldComment(String mappingFieldComment) {
        this.mappingFieldComment = mappingFieldComment;
    }

    public boolean isMappingFieldAutoIncrement() {
        return mappingFieldAutoIncrement;
    }

    public void setMappingFieldAutoIncrement(boolean mappingFieldAutoIncrement) {
        this.mappingFieldAutoIncrement = mappingFieldAutoIncrement;
    }

    public boolean isMappingFieldTimeForUpdate() {
        return mappingFieldTimeForUpdate;
    }

    public void setMappingFieldTimeForUpdate(boolean mappingFieldTimeForUpdate) {
        this.mappingFieldTimeForUpdate = mappingFieldTimeForUpdate;
    }

    public String getMappingFieldDefaultValue() {
        return mappingFieldDefaultValue;
    }

    public void setMappingFieldDefaultValue(String mappingFieldDefaultValue) {
        this.mappingFieldDefaultValue = mappingFieldDefaultValue;
    }

    @Override
    public boolean isMappingAutoIncrement() {
        return this.mappingFieldAutoIncrement;
    }
}
