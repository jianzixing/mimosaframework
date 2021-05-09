package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.platform.CompareUpdateTableMate;
import org.mimosaframework.orm.platform.DialectNextStep;
import org.mimosaframework.orm.platform.SessionContext;
import org.mimosaframework.orm.platform.TableStructure;

import java.sql.SQLException;
import java.util.List;

public class CreateCompareMapping extends AbstractCompareMapping {
    public CreateCompareMapping(MappingGlobalWrapper mappingGlobalWrapper, SessionContext dataSourceWrapper) {
        super(mappingGlobalWrapper, dataSourceWrapper);
        this.mappingLevel = MappingLevel.CREATE;
    }

    @Override
    protected boolean createTable(CompareUpdateTableMate tableMate) throws SQLException {
        MappingTable mappingTable = tableMate.getMappingTable();
        DialectNextStep step = executor.createTable(mappingTable);
        if (step == DialectNextStep.REBUILD) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean createFields(CompareUpdateTableMate tableMate) throws SQLException {
        List<MappingField> createFields = tableMate.getCreateFields();
        MappingTable mappingTable = tableMate.getMappingTable();
        TableStructure tableStructure = tableMate.getStructure();

        boolean isRebuildTable = false;
        for (MappingField mappingField : createFields) {
            DialectNextStep step = executor.createField(mappingTable, tableStructure, mappingField);
            if (step == DialectNextStep.REBUILD) isRebuildTable = true;
        }
        return isRebuildTable;
    }

    @Override
    protected boolean updateIndices(CompareUpdateTableMate tableMate) throws SQLException {
        boolean isRebuildTable = false;
        List<MappingIndex> indices = tableMate.getUpdateIndexes();
        MappingTable mappingTable = tableMate.getMappingTable();
        for (MappingIndex index : indices) {
            DialectNextStep step = executor.dropIndex(mappingTable, index.getIndexName());
            if (step == DialectNextStep.REBUILD) isRebuildTable = true;
        }
        for (MappingIndex mappingIndex : indices) {
            DialectNextStep step = executor.createIndex(mappingTable, mappingIndex);
            if (step == DialectNextStep.REBUILD) isRebuildTable = true;
        }
        return isRebuildTable;
    }

    @Override
    protected boolean createIndices(CompareUpdateTableMate tableMate) throws SQLException {
        boolean isRebuildTable = false;
        List<MappingIndex> newIndexes = tableMate.getNewIndexes();
        MappingTable mappingTable = tableMate.getMappingTable();
        for (MappingIndex mappingIndex : newIndexes) {
            DialectNextStep step = executor.createIndex(mappingTable, mappingIndex);
            if (step == DialectNextStep.REBUILD) isRebuildTable = true;
        }
        return isRebuildTable;
    }
}
