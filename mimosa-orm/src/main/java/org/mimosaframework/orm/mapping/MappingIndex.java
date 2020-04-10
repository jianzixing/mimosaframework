package org.mimosaframework.orm.mapping;

import java.util.List;

public interface MappingIndex {
    String getIndexName();

    List<MappingField> getIndexColumns();

    IndexType getIndexType();
}
