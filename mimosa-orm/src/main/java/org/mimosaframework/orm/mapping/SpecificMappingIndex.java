package org.mimosaframework.orm.mapping;

import java.util.List;

public class SpecificMappingIndex implements MappingIndex {
    private String indexName;
    private List<MappingField> fields;
    private IndexType indexType;

    public SpecificMappingIndex(String indexName, List<MappingField> fields, IndexType indexType) {
        this.indexName = indexName;
        this.fields = fields;
        this.indexType = indexType;
    }

    @Override
    public String getIndexName() {
        return this.indexName;
    }

    @Override
    public List<MappingField> getIndexColumns() {
        return this.fields;
    }

    @Override
    public IndexType getIndexType() {
        return this.indexType;
    }

    @Override
    public boolean isSameColumn(MappingIndex index) {
        List<MappingField> columns = index.getIndexColumns();
        if (fields != null && columns != null && fields.size() == columns.size()) {
            boolean aeq = true;
            for (MappingField field : fields) {
                boolean eq = false;
                for (MappingField column : columns) {
                    if (field.getMappingFieldName().equalsIgnoreCase(column.getMappingFieldName())) {
                        eq = true;
                        break;
                    }
                }
                if (!eq) {
                    aeq = false;
                    break;
                }
            }
            return aeq;
        }
        return false;
    }
}
