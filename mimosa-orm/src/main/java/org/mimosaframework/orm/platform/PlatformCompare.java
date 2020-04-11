package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingIndex;
import org.mimosaframework.orm.mapping.MappingTable;

import java.util.List;
import java.util.Map;

public interface PlatformCompare {
    void tableCreate(MappingGlobalWrapper wrapper,
                     PlatformDialect dialect,
                     MappingTable mappingTable);

    void fieldUpdate(MappingGlobalWrapper wrapper,
                     PlatformDialect dialect,
                     MappingTable mappingTable,
                     Map<MappingField, List<ColumnEditType>> updateFields);

    void fieldAdd(MappingGlobalWrapper wrapper,
                  PlatformDialect dialect,
                  MappingTable mappingTable,
                  List<MappingField> mappingFields);

    void fieldDel(MappingGlobalWrapper wrapper,
                  PlatformDialect dialect,
                  MappingTable mappingTable,
                  List<TableColumnStructure> mappingFields);

    void indexUpdate(MappingGlobalWrapper wrapper,
                     PlatformDialect dialect,
                     MappingTable mappingTable,
                     List<MappingIndex> mappingIndexes);

    void indexAdd(MappingGlobalWrapper wrapper,
                  PlatformDialect dialect,
                  MappingTable mappingTable,
                  List<MappingIndex> mappingIndexes);
}
