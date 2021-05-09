package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;
import java.util.*;

public class UpdateCompareMapping extends CreateCompareMapping {

    public UpdateCompareMapping(MappingGlobalWrapper mappingGlobalWrapper, SessionContext dataSourceWrapper) {
        super(mappingGlobalWrapper, dataSourceWrapper);
        this.mappingLevel = MappingLevel.UPDATE;
    }

    @Override
    protected boolean createTable(CompareUpdateTableMate tableMate) throws SQLException {
        return super.createTable(tableMate);
    }

    @Override
    protected void rebuildTable(CompareUpdateTableMate tableMate) throws SQLException {
        MappingTable mappingTable = tableMate.getMappingTable();
        // 重建表
        executor.doDialectRebuild(tableMate.getTableStructures(), mappingTable, tableMate.getStructure());
        Set<MappingIndex> mappingIndices = mappingTable.getMappingIndexes();
        if (mappingIndices != null) {
            PlatformDialect dialect = tableMate.getDialect();
            if (!dialect.isSupportSameColumnIndex()) {
                // 如果不支持多列索引则这里删除相同列索引
                Set<MappingIndex> onlyDiffIndex = new LinkedHashSet<>();
                for (MappingIndex index : mappingIndices) {
                    boolean is = false;
                    for (MappingIndex odi : onlyDiffIndex) {
                        if (index.isSameColumn(odi)) {
                            is = true;
                            break;
                        }
                    }
                    if (!is) {
                        onlyDiffIndex.add(index);
                    }
                }
                mappingIndices = onlyDiffIndex;
            }
            for (MappingIndex mappingIndex : mappingIndices) {
                executor.createIndex(mappingTable, mappingIndex);
            }
        }
    }

    @Override
    protected boolean updateFields(CompareUpdateTableMate tableMate,
                                   Map<MappingField, CompareUpdateMate> updateFields) throws SQLException {
        boolean isRebuildTable = false;
        MappingTable mappingTable = tableMate.getMappingTable();
        TableStructure tableStructure = tableMate.getStructure();

        Iterator<Map.Entry<MappingField, CompareUpdateMate>> iterator = updateFields.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<MappingField, CompareUpdateMate> entry = iterator.next();
            MappingField mappingField = entry.getKey();
            CompareUpdateMate compareUpdateMate = entry.getValue();

            DialectNextStep step = executor.modifyField(mappingTable, tableStructure,
                    mappingField, compareUpdateMate.getStructure());
            if (step == DialectNextStep.REBUILD) isRebuildTable = true;
        }
        return isRebuildTable;
    }

    @Override
    protected boolean createFields(CompareUpdateTableMate tableMate) throws SQLException {
        return super.createFields(tableMate);
    }

    @Override
    protected boolean deleteFields(CompareUpdateTableMate tableMate) throws SQLException {
        boolean isRebuildTable = false;
        List<TableColumnStructure> delColumns = tableMate.getDelColumns();
        MappingTable mappingTable = tableMate.getMappingTable();
        if (mappingLevel == MappingLevel.UPDATE) {
            TableStructure tableStructure = tableMate.getStructure();
            for (TableColumnStructure columnStructure : delColumns) {
                DialectNextStep step = executor.dropField(mappingTable, tableStructure, columnStructure);
                if (step == DialectNextStep.REBUILD) isRebuildTable = true;
            }
        }
        return isRebuildTable;
    }

    @Override
    protected boolean updateIndices(CompareUpdateTableMate tableMate) throws SQLException {
        return super.updateIndices(tableMate);
    }

    @Override
    protected boolean createIndices(CompareUpdateTableMate tableMate) throws SQLException {
        return super.createIndices(tableMate);
    }

    @Override
    protected boolean dropIndices(CompareUpdateTableMate tableMate) throws SQLException {
        boolean isRebuildTable = false;
        List<String> dropIndexes = tableMate.getDropIndexes();
        MappingTable mappingTable = tableMate.getMappingTable();
        for (String indexName : dropIndexes) {
            DialectNextStep step = executor.dropIndex(mappingTable, indexName);
            if (step == DialectNextStep.REBUILD) isRebuildTable = true;
        }
        return isRebuildTable;
    }
}
