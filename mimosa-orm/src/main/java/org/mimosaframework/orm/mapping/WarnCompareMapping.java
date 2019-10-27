package org.mimosaframework.orm.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.platform.ActionDataSourceWrapper;

import java.sql.SQLException;
import java.util.List;

public class WarnCompareMapping extends NothingCompareMapping {
    private static final Log logger = LogFactory.getLog(WarnCompareMapping.class);

    public WarnCompareMapping(ActionDataSourceWrapper dataSourceWrapper, NotMatchObject notMatchObject) {
        super(dataSourceWrapper, notMatchObject);
    }

    @Override
    public void doMapping() throws SQLException {
        super.doMapping();

        List<MappingTable> missingTables = notMatchObject.getMissingTables();
        List<MappingField> missingFields = notMatchObject.getMissingFields();
        List<MappingField> changeFields = notMatchObject.getChangeFields();

        if (missingTables != null) {
            for (MappingTable table : missingTables) {
                logger.warn(Messages.get(LanguageMessageFactory.PROJECT,
                        WarnCompareMapping.class, "defect_class",
                        table.getMappingTableName(), table.getMappingClass().getSimpleName()));
            }
        }

        if (missingFields != null) {
            for (MappingField field : missingFields) {
                MappingTable table = field.getMappingTable();
                if (table == null) {
                    logger.warn(Messages.get(LanguageMessageFactory.PROJECT,
                            WarnCompareMapping.class, "defect_field", field.getMappingColumnName()));
                } else {
                    logger.warn(Messages.get(LanguageMessageFactory.PROJECT,
                            WarnCompareMapping.class, "defect_field_table",
                            field.getMappingColumnName(), table.getMappingTableName()));
                }
            }
        }

        if (changeFields != null) {
            for (MappingField field : changeFields) {
                MappingTable table = field.getMappingTable();
                if (table == null) {
                    logger.warn(Messages.get(LanguageMessageFactory.PROJECT,
                            WarnCompareMapping.class, "update_field", field.getMappingColumnName()));
                } else {
                    logger.warn(Messages.get(LanguageMessageFactory.PROJECT,
                            WarnCompareMapping.class, "update_field_table",
                            field.getMappingColumnName(), table.getMappingTableName()));
                }
            }
        }
    }
}
