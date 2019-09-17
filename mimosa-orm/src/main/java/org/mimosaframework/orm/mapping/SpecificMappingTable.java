package org.mimosaframework.orm.mapping;

import java.util.*;

public class SpecificMappingTable implements MappingTable {
    private MappingDatabase mappingDatabase;
    private Set<MappingIndex> mappingIndexs;
    // 映射类字段名称对应的字段
    private Map<String, MappingField> mappingFields;
    // 数据库字段名称对应的字段
    private Map<String, MappingField> mappingColumns;

    private Class mappingClass;
    private String mappingClassName;
    /**
     * 如果注解@Table有自定义名称,则就是这个值
     * 这个值是已经变换后的名称，如果正确则和数据库名称一致
     * 注意：别名不转换
     * <p>
     * 如果在匹配数据库是否存在时，有配置集群则是配置的集群
     * 分表名称
     */
    private String mappingTableName;

    /**
     * 数据库引擎名称，一般从映射类中读取如果没有就按照默认来
     */
    private String engineName;

    /**
     * 数据库建表编码，一般从映射类中读取如果没有就按照默认来
     */
    private String encoding;

    /**
     * 数据库中本身存在的名称
     */
    private String databaseTableName;

    public SpecificMappingTable() {
    }

    public SpecificMappingTable(MappingDatabase mappingDatabase) {
        this.mappingDatabase = mappingDatabase;
    }

    public void addDatabaseColumnField(MappingField field) {
        if (this.mappingColumns == null) {
            this.mappingColumns = new LinkedHashMap<>();
        }
        this.mappingColumns.put(field.getDatabaseColumnName().toLowerCase(), field);
    }

    public void addMappingField(MappingField field) {
        if (this.mappingFields == null) {
            this.mappingFields = new LinkedHashMap<>();
        }
        this.mappingFields.put(field.getMappingFieldName(), field);
    }

    public void setMappingDatabase(MappingDatabase mappingDatabase) {
        this.mappingDatabase = mappingDatabase;
    }

    @Override
    public Set<MappingField> getMappingFields() {
        if (this.mappingFields != null) {
            Set<MappingField> fields = new LinkedHashSet<>(this.mappingFields.size());
            Iterator<Map.Entry<String, MappingField>> iterator = this.mappingFields.entrySet().iterator();
            while (iterator.hasNext()) {
                fields.add(iterator.next().getValue());
            }
            return fields;
        }
        return null;
    }

    @Override
    public Set<MappingField> getMappingColumns() {
        if (this.mappingColumns != null) {
            Set<MappingField> fields = new LinkedHashSet<>(this.mappingColumns.size());
            Iterator<Map.Entry<String, MappingField>> iterator = this.mappingColumns.entrySet().iterator();
            while (iterator.hasNext()) {
                fields.add(iterator.next().getValue());
            }
            return fields;
        }
        return null;
    }

    public MappingDatabase getMappingDatabase() {
        return mappingDatabase;
    }

    @Override
    public void applyFromClassMappingTable(MappingTable table) {
        SpecificMappingTable smt = (SpecificMappingTable) table;
        if (this.mappingClass == null) this.mappingClass = smt.mappingClass;
        if (this.mappingClassName == null) this.mappingClassName = smt.mappingClassName;
        if (this.mappingTableName == null) this.mappingTableName = smt.mappingTableName;
        if (this.engineName == null) this.engineName = smt.engineName;
        if (this.encoding == null) this.encoding = smt.encoding;

        Map<String, MappingField> mfs = smt.mappingFields;
        if (mfs != null) {
            Iterator<Map.Entry<String, MappingField>> iterator = mfs.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, MappingField> next = iterator.next();
                MappingField f = next.getValue();
                this.applyFromClassField(f);
            }
        }
    }

    private void applyFromClassField(MappingField f) {
        String columnName = f.getMappingColumnName();
        String fieldName = f.getMappingFieldName();
        MappingField selfColumn = null;
        if (mappingColumns != null) {
            selfColumn = mappingColumns.get(columnName);
            if (selfColumn != null) {
                selfColumn.applyFromClassField(f);
            }
        }
        if (mappingFields == null) {
            mappingFields = new LinkedHashMap<>();
        }
        if (mappingFields != null) {
            if (selfColumn != null) {
                mappingFields.put(fieldName, selfColumn);
            } else {
                f.setMappingTable(this);
                mappingFields.put(fieldName, f);
            }
        }
    }

    public Class getMappingClass() {
        return mappingClass;
    }

    public void setMappingClass(Class mappingClass) {
        this.mappingClass = mappingClass;
    }

    public String getMappingClassName() {
        return mappingClassName;
    }

    public void setMappingClassName(String mappingClassName) {
        this.mappingClassName = mappingClassName;
    }

    public String getMappingTableName() {
        return mappingTableName;
    }

    @Override
    public MappingTable clone() {
        SpecificMappingTable table = new SpecificMappingTable();
        table.mappingDatabase = this.mappingDatabase;
        table.mappingIndexs = this.mappingIndexs;
        table.mappingFields = this.mappingFields;
        table.mappingColumns = this.mappingColumns;
        table.mappingClass = this.mappingClass;
        table.mappingClassName = this.mappingClassName;
        table.mappingTableName = this.mappingTableName;
        table.engineName = this.engineName;
        table.encoding = this.encoding;
        table.databaseTableName = this.databaseTableName;
        return table;
    }

    @Override
    public List<MappingField> getMappingPrimaryKeyFields() {
        if (this.mappingFields != null) {
            List<MappingField> primaryKeys = new ArrayList<>();
            Iterator<Map.Entry<String, MappingField>> iterator = this.mappingFields.entrySet().iterator();
            while (iterator.hasNext()) {
                MappingField field = iterator.next().getValue();
                if (field.isMappingFieldPrimaryKey()) {
                    primaryKeys.add(field);
                }
            }
            return primaryKeys;
        }
        return null;
    }

    public void setMappingTableName(String mappingTableName) {
        this.mappingTableName = mappingTableName;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public String getEncoding() {
        return encoding;
    }

    @Override
    public MappingField getMappingFieldByName(String fieldName) {
        MappingField field = null;
        if (this.mappingFields != null) {
            field = this.mappingFields.get(fieldName);
        }
        if (field == null && this.mappingColumns != null) {
            field = this.mappingColumns.get(fieldName);
        }
        return field;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getDatabaseTableName() {
        return databaseTableName;
    }

    public void setDatabaseTableName(String databaseTableName) {
        this.databaseTableName = databaseTableName;
    }
}