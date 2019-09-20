package org.mimosaframework.orm.mapping;


import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.IDStrategy;
import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.convert.MappingNamedConvert;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Set;

public class DefaultDisassembleMappingClass implements DisassembleMappingClass {
    private Class mappingClass;
    private MappingNamedConvert convert;

    public DefaultDisassembleMappingClass(Class mappingClass, MappingNamedConvert convert) {
        this.mappingClass = mappingClass;
        this.convert = convert;
    }

    @Override
    public MappingTable getMappingTable() {
        Annotation annotation = mappingClass.getAnnotation(Table.class);
        Table table = null;
        if (annotation != null) table = (Table) annotation;
        if (table != null) {
            MappingTable mappingTable = new SpecificMappingTable();

            String tableName = table.value();
            if (tableName.equals("")) {
                if (convert != null) {
                    tableName = convert.convert(mappingClass.getSimpleName());
                } else {
                    tableName = mappingClass.getSimpleName();
                    if (tableName.length() > 1) {
                        tableName = tableName.substring(0, 1).toLowerCase() + tableName.substring(1);
                    } else {
                        tableName = tableName.toLowerCase();
                    }
                }
            }
            ((SpecificMappingTable) mappingTable).setMappingClass(mappingClass);
            ((SpecificMappingTable) mappingTable).setMappingClassName(mappingClass.getSimpleName());
            ((SpecificMappingTable) mappingTable).setMappingTableName(tableName);
            if (StringTools.isNumber(table.engineName())) {
                ((SpecificMappingTable) mappingTable).setEngineName(table.engineName());
            }
            if (StringTools.isNumber(table.charset())) {
                ((SpecificMappingTable) mappingTable).setEncoding(table.charset());
            }


            this.disassembleFields(mappingTable);
            Set<MappingField> mappingFields = mappingTable.getMappingFields();

            if (mappingFields != null) {
                // 检查自增字段的个数,只允许有一个自增字段
                short c = 0;
                for (MappingField field : mappingFields) {
                    if (field.isMappingAutoIncrement()) {
                        c++;
                    }
                }
                if (c > 1) {
                    throw new IllegalArgumentException("表 " + tableName + " 自增长字段只允许有一个");
                }
            }
            return mappingTable;
        }

        return null;
    }

    private void disassembleFields(MappingTable mappingTable) {
        String fieldName = null;
        Column column = null;
        Object fieldObject = null;
        MappingField lastField = null;
        Class<? extends IDStrategy> strategy = null;
        Class<Timestamp> timestamp = null;

        if (mappingClass.isEnum()) {
            for (Object o : mappingClass.getEnumConstants()) {
                fieldName = ((Enum) o).name();
                try {
                    column = o.getClass().getField(fieldName).getAnnotation(Column.class);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

                if (column.type().equals(Timestamp.class)) {
                    if (timestamp != null && timestamp.equals(Timestamp.class)) {
                        throw new IllegalArgumentException("时间戳类型列只允许有一个");
                    }
                    timestamp = Timestamp.class;
                }

                if (column.strategy().equals(AutoIncrementStrategy.class)) {
                    if (strategy != null && strategy.equals(AutoIncrementStrategy.class)) {
                        throw new IllegalArgumentException("自增列只允许有一个");
                    }
                    strategy = AutoIncrementStrategy.class;

                    if (!column.pk()) {
                        throw new IllegalArgumentException("自增策略只能使用在主键列上 " + mappingClass.getSimpleName() + "." + fieldName);
                    }
                }


                fieldObject = o;
                MappingField newField = this.disassembleFieldItem(mappingTable, fieldName, column, fieldObject);
                if (lastField != null) {
                    newField.setPrevious(lastField);
                }
                lastField = newField;
            }
        } else {
            Field[] fields = mappingClass.getDeclaredFields();
            for (Field field : fields) {
                fieldName = field.getName();
                column = field.getAnnotation(Column.class);

                if (column.strategy().equals(AutoIncrementStrategy.class)) {
                    if (strategy != null && strategy.equals(AutoIncrementStrategy.class)) {
                        throw new IllegalArgumentException("自增列只允许有一个");
                    }
                    strategy = AutoIncrementStrategy.class;
                }

                fieldObject = field;
                MappingField newField = this.disassembleFieldItem(mappingTable, fieldName, column, fieldObject);
                if (lastField != null) {
                    newField.setPrevious(lastField);
                }
                lastField = newField;
            }
        }
    }

    private MappingField disassembleFieldItem(MappingTable mappingTable,
                                              String fieldName,
                                              Column column,
                                              Object fieldObject) {
        MappingField mappingField = new SpecificMappingField(mappingTable);
        ((SpecificMappingField) mappingField).setMappingField(fieldObject);
        ((SpecificMappingField) mappingField).setMappingFieldAnnotation(column);
        ((SpecificMappingField) mappingField).setMappingFieldName(fieldName);
        String columnName = column.name();
        if (StringTools.isEmpty(columnName) && convert != null) {
            columnName = convert.convert(fieldName);
        }
        ((SpecificMappingField) mappingField).setMappingColumnName(columnName);
        ((SpecificMappingField) mappingField).setMappingFieldType(column.type());
        ((SpecificMappingField) mappingField).setMappingFieldLength(column.length());
        ((SpecificMappingField) mappingField).setMappingFieldDecimalDigits(column.decimalDigits());
        ((SpecificMappingField) mappingField).setMappingFieldNullable(column.nullable());
        ((SpecificMappingField) mappingField).setMappingFieldPrimaryKey(column.pk());
        ((SpecificMappingField) mappingField).setMappingFieldIndex(column.index());
        ((SpecificMappingField) mappingField).setMappingFieldUnique(column.unique());
        if (StringTools.isNotEmpty(column.comment()))
            ((SpecificMappingField) mappingField).setMappingFieldComment(column.comment());
        ((SpecificMappingField) mappingField).setMappingFieldTimeForUpdate(column.timeForUpdate());
        if (StringTools.isNotEmpty(column.defaultValue()))
            ((SpecificMappingField) mappingField).setMappingFieldDefaultValue(column.defaultValue());

        if (column.strategy().equals(AutoIncrementStrategy.class)) {
            ((SpecificMappingField) mappingField).setMappingFieldAutoIncrement(true);
        }
        mappingTable.addMappingField(mappingField);
        return mappingField;
    }
}
