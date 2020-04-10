package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingIndex;
import org.mimosaframework.orm.mapping.MappingTable;

import java.util.List;
import java.util.Map;

public interface PlatformCompare {
    void tableCreate(MappingGlobalWrapper wrapper, MappingTable mappingTable);

    void fieldUpdate(MappingGlobalWrapper wrapper,
                     MappingTable mappingTable,
                     Map<MappingField, List<ColumnEditType>> updateFields);

    void fieldAdd(MappingGlobalWrapper wrapper,
                  MappingTable mappingTable,
                  List<MappingField> mappingFields);

    void fieldDel(MappingGlobalWrapper wrapper,
                  MappingTable mappingTable,
                  List<TableColumnStructure> mappingFields);

    void indexUpdate(MappingGlobalWrapper wrapper,
                     MappingTable mappingTable,
                     List<MappingIndex> mappingIndexes);

    void indexAdd(MappingGlobalWrapper wrapper,
                  MappingTable mappingTable,
                  List<MappingIndex> mappingIndexes);
}
