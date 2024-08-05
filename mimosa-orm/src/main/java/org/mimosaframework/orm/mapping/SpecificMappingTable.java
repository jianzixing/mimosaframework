package org.mimosaframework.orm.mapping;

import java.util.*;

public class SpecificMappingTable implements MappingTable {
    private Set<MappingIndex> mappingIndexes;
    private Map<String, MappingField> mappingFields;

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

    private String version;

    private String sourceMappingTableName;

    public SpecificMappingTable() {
    }

    public void addMappingField(MappingField field) {
        if (this.mappingFields == null) {
            this.mappingFields = new LinkedHashMap<>();
        }
        this.mappingFields.put(field.getMappingFieldName(), field);
    }

    public void addMappingIndex(MappingIndex mappingIndex) {
        if (this.mappingIndexes == null) {
            this.mappingIndexes = new LinkedHashSet<>();
        }
        this.mappingIndexes.add(mappingIndex);
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

    public Class getMappingClass() {
        return mappingClass;
    }

    public void setMappingClass(Class mappingClass) {
        this.mappingClass = mappingClass;
    }

    public String getMappingClassName() {
        return mappingClassName;
    }

    @Override
    public String getSourceMappingTableName() {
        return this.sourceMappingTableName;
    }

    @Override
    public MappingField getAutoIncrementField() {
        if (mappingFields != null) {
            for (MappingField mappingField : mappingFields.values()) {
                if (mappingField.isMappingFieldAutoIncrement()) {
                    return mappingField;
                }
            }
        }
        return null;
    }

    @Override
    public MappingField getMappingFieldByColumnName(String str) {
        if (this.mappingFields != null) {
            Collection<MappingField> fields = this.mappingFields.values();
            for (MappingField f : fields) {
                if (f.getMappingColumnName().equals(str)) {
                    return f;
                }
            }
        }
        return null;
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
        table.mappingIndexes = this.mappingIndexes;
        table.mappingFields = this.mappingFields;
        table.mappingClass = this.mappingClass;
        table.mappingClassName = this.mappingClassName;
        table.mappingTableName = this.mappingTableName;
        table.engineName = this.engineName;
        table.encoding = this.encoding;
        table.version = this.version;
        table.sourceMappingTableName = this.sourceMappingTableName;
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
    public String getVersion() {
        return version;
    }

    @Override
    public MappingField getMappingFieldByName(String fieldName) {
        if (this.mappingFields != null) {
            return this.mappingFields.get(fieldName);
        }
        return null;
    }

    @Override
    public MappingField getMappingFieldByJavaName(String fieldName) {
        if (this.mappingFields != null) {
            return this.mappingFields.get(fieldName);
        }
        return null;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setSourceMappingTableName(String sourceMappingTableName) {
        this.sourceMappingTableName = sourceMappingTableName;
    }

    public Set<MappingIndex> getMappingIndexes() {
        return mappingIndexes;
    }

    public void setMappingIndexes(Set<MappingIndex> mappingIndexes) {
        this.mappingIndexes = mappingIndexes;
    }

    public void setMappingFields(Map<String, MappingField> mappingFields) {
        this.mappingFields = mappingFields;
    }

    public Map<String, MappingField> getMappingFieldsMap() {
        return this.mappingFields;
    }

    public Map<String, MappingField> getMappingColumnsMap() {
        return this.mappingFields;
    }
}
