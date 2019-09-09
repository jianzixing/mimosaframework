package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.DynamicTable;
import org.mimosaframework.orm.DynamicTableItem;

import java.sql.SQLException;
import java.util.List;

public class SimpleTemplateImpl implements SimpleTemplate {
    private PlatformWrapper platformWrapper;
    private DatabasePorter databasePorter;
    private CarryHandler carryHandler;

    public SimpleTemplateImpl(DatabasePorter databasePorter, CarryHandler carryHandler) {
        this.databasePorter = databasePorter;
        this.carryHandler = carryHandler;
        this.platformWrapper = new PlatformWrapperImpl(databasePorter, carryHandler);
    }

    @Override
    public void createTable(DynamicTable table) throws SQLException {
        this.platformWrapper.createTable(table.toMappingTable());
    }

    @Override
    public void dropTable(String table) throws SQLException {
        this.platformWrapper.dropTable(table);
    }

    @Override
    public void addFields(DynamicTable table) throws SQLException {
        String tableName = table.getTableName();
        List<DynamicTableItem> items = table.getTableItems();
        if (items != null) {
            for (DynamicTableItem item : items) {
                this.platformWrapper.addField(tableName, item.toMappingField());
            }
        }
    }

    @Override
    public void dropField(DynamicTable table) throws SQLException {
        String tableName = table.getTableName();
        List<DynamicTableItem> items = table.getTableItems();
        if (items != null) {
            for (DynamicTableItem item : items) {
                this.platformWrapper.dropField(tableName, item.toMappingField());
            }
        }
    }

    @Override
    public long save(String table, ModelObject object) throws SQLException {
        if (object != null) {
            return this.platformWrapper.simpleInsert(table, object);
        }
        return 0;
    }

    @Override
    public int delete(String table, ModelObject where) throws SQLException {
        if (where != null) {
            return this.platformWrapper.simpleDelete(table, where);
        }
        return 0;
    }

    @Override
    public int update(String table, ModelObject object, ModelObject where) throws SQLException {
        if (where != null && object != null) {
            return this.platformWrapper.simpleUpdate(table, object, where);
        }
        return 0;
    }

    @Override
    public List<ModelObject> get(String table, ModelObject where) throws SQLException {
        return this.platformWrapper.simpleSelect(table, where);
    }

    @Override
    public long count(String table, ModelObject where) throws SQLException {
        return this.platformWrapper.simpleCount(table, where);
    }

    @Override
    public List<ModelObject> sqlRunnerQuery(String sql) throws SQLException {
        return this.platformWrapper.select(sql);
    }

    @Override
    public int sqlRunnerUpdate(String sql) throws SQLException {
        return this.platformWrapper.update(sql);
    }
}
