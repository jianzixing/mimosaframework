package org.mimosaframework.orm;

import org.mimosaframework.core.asm.Type;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.SpecificMappingField;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

public class DynamicTableItem {
    private String fieldName;
    private Class<? extends IDStrategy> strategy;
    private Class type = String.class;
    private int length = 255;
    private int decimalDigits;
    private boolean nullable = true;
    private boolean pk = false;
    private boolean index = false;
    private boolean unique = false;
    private String comment;
    private boolean timeForUpdate = false;
    private String defaultValue;

    public MappingField toMappingField() {
        SpecificMappingField field = new SpecificMappingField();
        field.setMappingColumnName(fieldName);
        if (strategy != null && strategy.isAssignableFrom(AutoIncrementStrategy.class)) {
            field.setMappingFieldAutoIncrement(true);
        }
        field.setMappingFieldType(type);
        field.setMappingFieldLength(length);
        field.setMappingFieldDecimalDigits(decimalDigits);
        field.setMappingFieldNullable(nullable);
        field.setMappingFieldPrimaryKey(pk);
        field.setMappingFieldIndex(index);
        field.setMappingFieldUnique(unique);
        field.setMappingFieldComment(comment);
        field.setMappingFieldTimeForUpdate(timeForUpdate);
        field.setMappingFieldDefaultValue(defaultValue);

        field.setMappingFieldName(fieldName);

        return field;
    }

    public static DynamicTableItem build() {
        return new DynamicTableItem();
    }

    public static DynamicTableItem build(String fieldName, Class type, int length) {
        return new DynamicTableItem(fieldName, type, length);
    }

    public static DynamicTableItem build(String fieldName, Class type, int length, boolean nullable, String comment, String defaultValue) {
        return new DynamicTableItem(fieldName, type, length, nullable, comment, defaultValue);
    }

    public DynamicTableItem() {
    }

    public DynamicTableItem(String fieldName, Class type, int length) {
        this.fieldName = fieldName;
        this.type = type;
        this.length = length;
    }

    public DynamicTableItem(String fieldName, Class type, int length, boolean nullable, String comment, String defaultValue) {
        this.fieldName = fieldName;
        this.type = type;
        this.length = length;
        this.nullable = nullable;
        this.comment = comment;
        this.defaultValue = defaultValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class<? extends IDStrategy> getStrategy() {
        return strategy;
    }

    public void setStrategy(Class<? extends IDStrategy> strategy) {
        this.strategy = strategy;
    }

    /**
     * case 'V':
     * return VOID_TYPE;
     * case 'Z':
     * return BOOLEAN_TYPE;
     * case 'C':
     * return CHAR_TYPE;
     * case 'B':
     * return BYTE_TYPE;
     * case 'S':
     * return SHORT_TYPE;
     * case 'I':
     * return INT_TYPE;
     * case 'F':
     * return FLOAT_TYPE;
     * case 'J':
     * return LONG_TYPE;
     * case 'D':
     * return DOUBLE_TYPE;
     *
     * @return
     */
    public Type getASMType() {
        if (type.isAssignableFrom(Boolean.class)
                || type.isAssignableFrom(boolean.class)) {
            return Type.getType("Z");
        }
        if (type.isAssignableFrom(Character.class)
                || type.isAssignableFrom(char.class)) {
            return Type.getType("C");
        }
        if (type.isAssignableFrom(Byte.class)
                || type.isAssignableFrom(byte.class)) {
            return Type.getType("B");
        }
        if (type.isAssignableFrom(Short.class)
                || type.isAssignableFrom(short.class)) {
            return Type.getType("S");
        }
        if (type.isAssignableFrom(Integer.class)
                || type.isAssignableFrom(int.class)) {
            return Type.getType("I");
        }
        if (type.isAssignableFrom(Float.class)
                || type.isAssignableFrom(float.class)) {
            return Type.getType("F");
        }
        if (type.isAssignableFrom(Long.class)
                || type.isAssignableFrom(long.class)) {
            return Type.getType("J");
        }
        if (type.isAssignableFrom(Double.class)
                || type.isAssignableFrom(double.class)) {
            return Type.getType("D");
        }

        return Type.getType("L" + type.getName().replaceAll("\\.", "/") + ";");
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    public boolean isIndex() {
        return index;
    }

    public void setIndex(boolean index) {
        this.index = index;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isTimeForUpdate() {
        return timeForUpdate;
    }

    public void setTimeForUpdate(boolean timeForUpdate) {
        this.timeForUpdate = timeForUpdate;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
