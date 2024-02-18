package org.mimosaframework.orm.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.platform.*;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WarnCompareMapping extends AbstractCompareMapping {
    private static final Log logger = LogFactory.getLog(WarnCompareMapping.class);

    public WarnCompareMapping(MappingGlobalWrapper mappingGlobalWrapper, SessionContext dataSourceWrapper) {
        super(mappingGlobalWrapper, dataSourceWrapper);
        this.mappingLevel = MappingLevel.WARN;
    }

    @Override
    protected boolean createTable(CompareUpdateTableMate tableMate) throws SQLException {
        MappingTable mappingTable = tableMate.getMappingTable();
        logger.error(I18n.print("compare_mapping_warn_create_table",
                mappingTable.getMappingTableName()));
        return false;
    }

    @Override
    protected boolean updateFields(CompareUpdateTableMate tableMate,
                                   Map<MappingField, CompareUpdateMate> updateFields) throws SQLException {
        MappingTable mappingTable = tableMate.getMappingTable();
        Iterator<Map.Entry<MappingField, CompareUpdateMate>> iterator = updateFields.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<MappingField, CompareUpdateMate> entry = iterator.next();
            MappingField mappingField = entry.getKey();
            CompareUpdateMate compareUpdateMate = entry.getValue();
            StringBuilder sb = new StringBuilder();
            Iterator<ColumnEditType> typeIterator = compareUpdateMate.getEditTypes().iterator();
            while (typeIterator.hasNext()) {
                sb.append(typeIterator.next() + "");
                if (typeIterator.hasNext()) sb.append(" ");
            }
            logger.error(I18n.print("compare_mapping_warn_field_update",
                    mappingTable.getMappingTableName(),
                    mappingField.getMappingColumnName(),
                    sb.toString()
            ));
        }
        return false;
    }

    @Override
    protected boolean createFields(CompareUpdateTableMate tableMate) throws SQLException {
        MappingTable mappingTable = tableMate.getMappingTable();
        List<MappingField> createFields = tableMate.getCreateFields();

        for (MappingField mappingField : createFields) {
            logger.error(I18n.print("compare_mapping_warn_field_add",
                    mappingTable.getMappingTableName(),
                    mappingField.getMappingColumnName()));
        }
        return false;
    }

    @Override
    protected boolean deleteFields(CompareUpdateTableMate tableMate) throws SQLException {
        List<TableColumnStructure> delColumns = tableMate.getDelColumns();
        MappingTable mappingTable = tableMate.getMappingTable();

        if (mappingLevel == MappingLevel.WARN) {
            for (TableColumnStructure columnStructure : delColumns) {
                logger.error(I18n.print("compare_mapping_warn_field_del",
                        mappingTable.getMappingTableName(),
                        columnStructure.getColumnName()));
            }
        }
        return false;
    }

    @Override
    protected boolean updateIndices(CompareUpdateTableMate tableMate) throws SQLException {
        List<MappingIndex> indices = tableMate.getUpdateIndexes();
        MappingTable mappingTable = tableMate.getMappingTable();

        for (MappingIndex mappingIndex : indices) {
            logger.error(I18n.print("compare_mapping_warn_index_update",
                    mappingTable.getMappingTableName(),
                    mappingIndex.getIndexName()));
        }
        return false;
    }

    @Override
    protected boolean createIndices(CompareUpdateTableMate tableMate) throws SQLException {
        List<MappingIndex> newIndexes = tableMate.getNewIndexes();
        MappingTable mappingTable = tableMate.getMappingTable();
        for (MappingIndex mappingIndex : newIndexes) {
            logger.error(I18n.print("compare_mapping_warn_index_add",
                    mappingTable.getMappingTableName(),
                    mappingIndex.getIndexName()));
        }
        return false;
    }

    @Override
    protected boolean dropIndices(CompareUpdateTableMate tableMate) throws SQLException {
        List<String> dropIndexes = tableMate.getDropIndexes();
        MappingTable mappingTable = tableMate.getMappingTable();

        for (String indexName : dropIndexes) {
            logger.error(I18n.print("compare_mapping_warn_index_add",
                    mappingTable.getMappingTableName(),
                    indexName));
        }
        return false;
    }
}
