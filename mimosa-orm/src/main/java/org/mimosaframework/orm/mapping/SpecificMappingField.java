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
     * 数据库中 decimal(32,2) 精度2就是这个值
     */
    private int mappingFieldDecimalDigits;

    /**
     * 从映射类中读取是否为空
     */
    private boolean mappingFieldNullable;

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

    ///////////////////////////////////////////////////////////////////////

    /**
     * 数据库中本身存在的列名
     */
    private String databaseColumnName;

    /**
     * 数据库中的列数据类型
     */
    private String databaseColumnTypeName;

    /**
     * 数据库中的datatype
     * 来自 java.sql.Types 的 SQL 类型
     */
    private int databaseColumnDataType;

    /**
     * 数据库中的列长度，或者精度
     * 比如varchar 长度 100
     */
    private int databaseColumnLength;

    /**
     * 数据库中读取的小数位
     * 比如decimal 精度 32,2 小数位是2
     */
    private int databaseColumnDecimalDigits;

    /**
     * 数据库中列是否可为空
     * YES --- 如果参数可以包括 NULL
     * NO --- 如果参数不可以包括 NULL
     */
    private String databaseColumnNullable;

    /**
     * 数据库中列是否是主键
     * 1不是 2是 0未知
     */
    private byte databaseColumnPrimaryKey = 0;

    /**
     * 数据库中列是否全局唯一
     * 1不是 2是 0未知
     */
    private byte databaseColumnUnique = 0;

    /**
     * 从数据库中读取备注
     */
    private String databaseColumnComment;

    /**
     * 从数据库中读取是否自增
     * Mysql 如果是 YES 表示是自增
     * Oracle 无法知道用UNKNOWN表示
     */
    private String databaseColumnAutoIncrement;

    /**
     * 从数据库中读取的默认值
     */
    private String databaseColumnDefaultValue;

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
        return mappingFieldLength;
    }

    public void setMappingFieldLength(int mappingFieldLength) {
        if (mappingFieldLength == 255 && this.mappingFieldType.equals(BigDecimal.class)) {
            this.mappingFieldLength = 32;
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

    public String getDatabaseColumnName() {
        return databaseColumnName;
    }

    @Override
    public boolean isMappingAutoIncrement() {
        return this.mappingFieldAutoIncrement;
    }

    @Override
    public void applyFromClassField(MappingField field) {
        SpecificMappingField smf = (SpecificMappingField) field;
        if (this.mappingField == null) this.mappingField = smf.mappingField;
        if (this.mappingFieldType == null) this.mappingFieldType = smf.mappingFieldType;
        if (this.mappingFieldName == null) this.mappingFieldName = smf.mappingFieldName;
        if (this.mappingFieldAnnotation == null) this.mappingFieldAnnotation = smf.mappingFieldAnnotation;
        if (this.mappingFieldLength == 0) this.mappingFieldLength = smf.mappingFieldLength;
        if (this.mappingFieldDecimalDigits == 0) this.mappingFieldDecimalDigits = smf.mappingFieldDecimalDigits;
        this.mappingFieldNullable = smf.mappingFieldNullable;
        if (this.mappingColumnName == null) this.mappingColumnName = smf.mappingColumnName;
        this.mappingFieldPrimaryKey = smf.mappingFieldPrimaryKey;
        this.mappingFieldIndex = smf.mappingFieldIndex;
        this.mappingFieldUnique = smf.mappingFieldUnique;
        if (this.mappingFieldComment == null) this.mappingFieldComment = smf.mappingFieldComment;
        this.mappingFieldAutoIncrement = smf.mappingFieldAutoIncrement;
        this.mappingFieldTimeForUpdate = smf.mappingFieldTimeForUpdate;
        if (this.mappingFieldDefaultValue == null) this.mappingFieldDefaultValue = smf.mappingFieldDefaultValue;
    }

    @Override
    public void applyFromColumnField(MappingField field) {
        SpecificMappingField smf = (SpecificMappingField) field;
        if (smf.databaseColumnName != null) databaseColumnName = smf.databaseColumnName;
        if (smf.databaseColumnTypeName != null) databaseColumnTypeName = smf.databaseColumnTypeName;
        if (smf.databaseColumnDataType != 0) databaseColumnDataType = smf.databaseColumnDataType;
        if (smf.databaseColumnLength != 0) databaseColumnLength = smf.databaseColumnLength;
        if (smf.databaseColumnDecimalDigits != 0) databaseColumnDecimalDigits = smf.databaseColumnDecimalDigits;
        if (smf.databaseColumnNullable != null) databaseColumnNullable = smf.databaseColumnNullable;
        if (smf.databaseColumnPrimaryKey != 0) databaseColumnPrimaryKey = smf.databaseColumnPrimaryKey;
        if (smf.databaseColumnUnique != 0) databaseColumnUnique = smf.databaseColumnUnique;
        if (smf.databaseColumnComment != null) databaseColumnComment = smf.databaseColumnComment;
        if (smf.databaseColumnAutoIncrement != null) databaseColumnAutoIncrement = smf.databaseColumnAutoIncrement;
        if (smf.databaseColumnDefaultValue != null) databaseColumnDefaultValue = smf.databaseColumnDefaultValue;
    }

    public void setDatabaseColumnName(String databaseColumnName) {
        this.databaseColumnName = databaseColumnName;
    }

    public String getDatabaseColumnTypeName() {
        return databaseColumnTypeName;
    }

    public void setDatabaseColumnTypeName(String databaseColumnTypeName) {
        this.databaseColumnTypeName = databaseColumnTypeName;
    }

    public int getDatabaseColumnDataType() {
        return databaseColumnDataType;
    }

    public void setDatabaseColumnDataType(int databaseColumnDataType) {
        this.databaseColumnDataType = databaseColumnDataType;
    }

    public int getDatabaseColumnLength() {
        return databaseColumnLength;
    }

    public void setDatabaseColumnLength(int databaseColumnLength) {
        this.databaseColumnLength = databaseColumnLength;
    }

    public int getDatabaseColumnDecimalDigits() {
        return databaseColumnDecimalDigits;
    }

    public void setDatabaseColumnDecimalDigits(int databaseColumnDecimalDigits) {
        this.databaseColumnDecimalDigits = databaseColumnDecimalDigits;
    }

    public String getDatabaseColumnNullable() {
        return databaseColumnNullable;
    }

    public void setDatabaseColumnNullable(String databaseColumnNullable) {
        this.databaseColumnNullable = databaseColumnNullable;
    }

    public byte getDatabaseColumnPrimaryKey() {
        return databaseColumnPrimaryKey;
    }

    public void setDatabaseColumnPrimaryKey(byte databaseColumnPrimaryKey) {
        this.databaseColumnPrimaryKey = databaseColumnPrimaryKey;
    }

    public byte getDatabaseColumnUnique() {
        return databaseColumnUnique;
    }

    public void setDatabaseColumnUnique(byte databaseColumnUnique) {
        this.databaseColumnUnique = databaseColumnUnique;
    }

    public String getDatabaseColumnComment() {
        return databaseColumnComment;
    }

    public void setDatabaseColumnComment(String databaseColumnComment) {
        this.databaseColumnComment = databaseColumnComment;
    }

    public String getDatabaseColumnAutoIncrement() {
        return databaseColumnAutoIncrement;
    }

    public void setDatabaseColumnAutoIncrement(String databaseColumnAutoIncrement) {
        this.databaseColumnAutoIncrement = databaseColumnAutoIncrement;
    }

    public String getDatabaseColumnDefaultValue() {
        return databaseColumnDefaultValue;
    }

    public void setDatabaseColumnDefaultValue(String databaseColumnDefaultValue) {
        this.databaseColumnDefaultValue = databaseColumnDefaultValue;
    }
}
