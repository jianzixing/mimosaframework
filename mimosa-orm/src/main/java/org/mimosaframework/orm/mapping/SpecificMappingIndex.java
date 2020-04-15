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
}
