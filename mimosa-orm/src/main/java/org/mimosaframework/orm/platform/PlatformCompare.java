package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingIndex;
import org.mimosaframework.orm.mapping.MappingTable;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PlatformCompare {
    void tableCreate(MappingGlobalWrapper wrapper,
                     MappingTable mappingTable) throws SQLException;

    void fieldUpdate(MappingGlobalWrapper wrapper,
                     MappingTable mappingTable,
                     TableStructure tableStructure,
                     Map<MappingField, List<ColumnEditType>> updateFields,
                     Map<MappingField, TableColumnStructure> columnStructures) throws SQLException;

    void fieldAdd(MappingGlobalWrapper wrapper,
                  MappingTable mappingTable,
                  TableStructure tableStructure,
                  List<MappingField> mappingFields) throws SQLException;

    void fieldDel(MappingGlobalWrapper wrapper,
                  MappingTable mappingTable,
                  TableStructure tableStructure,
                  List<TableColumnStructure> mappingFields) throws SQLException;

    void indexUpdate(MappingGlobalWrapper wrapper,
                     MappingTable mappingTable,
                     List<MappingIndex> mappingIndexes,
                     List<String> delIndexNames) throws SQLException;

    void indexAdd(MappingGlobalWrapper wrapper,
                  MappingTable mappingTable,
                  List<MappingIndex> mappingIndexes) throws SQLException;
}
