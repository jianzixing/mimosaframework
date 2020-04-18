package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.SQLException;
import java.util.List;

public interface Dialect {
    void setDataSourceWrapper(DataSourceWrapper dswrapper);

    void setMappingGlobalWrapper(MappingGlobalWrapper mappingGlobalWrapper);

    List<TableStructure> getTableStructures(List<String> classTableNames) throws SQLException;

    ColumnType getColumnType(KeyColumnType type);

    List<ColumnEditType> compareColumnChange(TableStructure structure,
                                             MappingField currField,
                                             TableColumnStructure columnStructure);

    SQLBuilderCombine alter(StampAlter alter);

    SQLBuilderCombine create(StampCreate create);

    SQLBuilderCombine drop(StampDrop drop);

    SQLBuilderCombine insert(StampInsert insert) throws SQLException;

    SQLBuilderCombine delete(StampDelete delete);

    SQLBuilderCombine select(StampSelect select);

    SQLBuilderCombine update(StampUpdate update);

    DialectNextStep define(DataDefinition definition) throws SQLException;

    void rebuildTable(MappingTable mappingTable, TableStructure tableStructure) throws SQLException;

    boolean isSupportGeneratedKeys();

    boolean isSelectLimitMustOrderBy();
}
