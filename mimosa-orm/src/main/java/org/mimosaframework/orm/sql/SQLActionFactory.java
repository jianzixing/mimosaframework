package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.platform.PlatformFactory;
import org.mimosaframework.orm.sql.alter.*;
import org.mimosaframework.orm.sql.create.*;
import org.mimosaframework.orm.sql.delete.*;
import org.mimosaframework.orm.sql.drop.DropAnyBuilder;
import org.mimosaframework.orm.sql.drop.DropDatabaseBuilder;
import org.mimosaframework.orm.sql.drop.DropTableBuilder;
import org.mimosaframework.orm.sql.drop.RedefineDropBuilder;
import org.mimosaframework.orm.sql.insert.*;
import org.mimosaframework.orm.sql.select.*;
import org.mimosaframework.orm.sql.update.*;
import org.mimosaframework.orm.utils.DatabaseTypes;

public class SQLActionFactory {
    public static AlterAnyBuilder alter() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        AlterBuilder<AlterAnyBuilder> alterBuilder = PlatformFactory.getSQLBuilder(
                databaseTypes, AlterBuilder.class
        );

        SQLProxyInvoker invoker = new SQLProxyInvoker(
                new Class[]{
                        RedefineAlterBuilder.class,
                        AlterAddAnyBuilder.class,
                        AlterAddBuilder.class,
                        AlterAnyBuilder.class,
                        AlterChangeBuilder.class,
                        AlterChangeNextBuilder.class,
                        AlterColumnAssistBuilder.class,
                        AlterColumnsBuilder.class,
                        AlterDatabaseBuilder.class,
                        AlterDropAnyBuilder.class,
                        AlterDropBuilder.class,
                        AlterModifyBuilder.class,
                        AlterModifyNextBuilder.class,
                        AlterNewColumnBuilder.class,
                        AlterOldColumnBuilder.class,
                        AlterRenameAnyBuilder.class,
                        AlterRenameBuilder.class,
                        AlterTableAnyBuilder.class,
                        AlterTableOptionBuilder.class
                },
                alterBuilder
        );

        return (AlterAnyBuilder) invoker.getInterface(AlterBuilder.class).alter();
    }

    public static CreateAnyBuilder create() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        CreateBuilder<CreateAnyBuilder> createBuilder = PlatformFactory.getSQLBuilder(
                databaseTypes, CreateBuilder.class
        );

        SQLProxyInvoker invoker = new SQLProxyInvoker(
                new Class[]{
                        RedefineCreateBuilder.class,
                        ColumnAssistBuilder.class,
                        ColumnTypeBuilder.class,
                        CreateAnyBuilder.class,
                        CreateColumnAssistBuilder.class,
                        CreateDatabaseStartBuilder.class,
                        CreateIndexBuilder.class,
                        CreateIndexColumnsBuilder.class,
                        CreateTableColumnBuilder.class,
                        CreateTableStartBuilder.class,
                        CreateTableTailBuilder.class
                },
                createBuilder
        );

        return (CreateAnyBuilder) invoker.getInterface(CreateBuilder.class).create();
    }

    public static AbsColumnBuilder<ColumnTypeBuilder<CreateColumnAssistBuilder>> column() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        AbsColumnBuilder<ColumnTypeBuilder<CreateColumnAssistBuilder>> columnBuilder =
                PlatformFactory.getSQLBuilder(
                        databaseTypes, AbsColumnBuilder.class
                );
        return columnBuilder;
    }

    public static DeleteStartBuilder delete() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        DeleteBuilder<DeleteStartBuilder> deleteBuilder = PlatformFactory.getSQLBuilder(
                databaseTypes, DeleteBuilder.class
        );

        SQLProxyInvoker invoker = new SQLProxyInvoker(
                new Class[]{
                        RedefineDeleteBuilder.class,
                        ReplaceDeleteFromBuilder.class,
                        BeforeDeleteFromBuilder.class,
                        DeleteStartBuilder.class,
                        DeleteWhereBuilder.class,
                        DeleteWhereOrderByBuilder.class,
                        ReplaceDeleteLogicBuilder.class,
                        ReplaceDeleteWhereBuilder.class,
                        DeleteAsTableBuilder.class,
                        DeleteTableAsBuilder.class
                },
                deleteBuilder
        );

        return (DeleteStartBuilder) invoker.getInterface(DeleteBuilder.class).delete();
    }

    public static DropAnyBuilder drop() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        DropBuilder<DropAnyBuilder> dropBuilder = PlatformFactory.getSQLBuilder(
                databaseTypes, DropBuilder.class
        );

        SQLProxyInvoker invoker = new SQLProxyInvoker(
                new Class[]{
                        RedefineDropBuilder.class,
                        DropAnyBuilder.class,
                        DropDatabaseBuilder.class,
                        DropTableBuilder.class
                },
                dropBuilder
        );

        return (DropAnyBuilder) invoker.getInterface(DropBuilder.class).drop();
    }

    public static InsertStartBuilder insert() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        InsertBuilder<InsertStartBuilder> insertBuilder = PlatformFactory.getSQLBuilder(
                databaseTypes, InsertBuilder.class
        );

        SQLProxyInvoker invoker = new SQLProxyInvoker(
                new Class[]{
                        RedefineInsertBuilder.class,
                        InsertFieldBuilder.class,
                        InsertStartBuilder.class,
                        InsertValuesBuilder.class,
                        ReplaceInsertValuesBuilder.class
                },
                insertBuilder
        );

        return (InsertStartBuilder) invoker.getInterface(InsertBuilder.class).insert();
    }

    public static SelectStartBuilder select() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        SelectBuilder<SelectStartBuilder> selectBuilder = PlatformFactory.getSQLBuilder(
                databaseTypes, SelectBuilder.class
        );

        SQLProxyInvoker invoker = new SQLProxyInvoker(
                new Class[]{
                        RedefineSelectBuilder.class,
                        SelectGHOLBuilder.class,
                        SelectHOLBuilder.class,
                        SelectOLBuilder.class,
                        SelectOnBuilder.class,
                        SelectStartBuilder.class,
                        SelectWhereBuilder.class,
                        ReplaceSelectFieldBuilder.class,
                        SelectAfterOnBuilder.class,
                        ReplaceSelectWhereBuilder.class,
                        SelectTableAliasBuilder.class,
                        ReplaceSelectLogicBuilder.class,
                        HavingOperatorFunctionBuilder.class,
                        SelectGroupByBuilder.class,
                        SelectGroupByNextBuilder.class,
                        OrderByNextBuilder.class,
                        OrderBySealBuilder.class
                },
                selectBuilder
        );

        return (SelectStartBuilder) invoker.getInterface(SelectBuilder.class).select();
    }

    public static UpdateStartBuilder update() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        UpdateBuilder<UpdateStartBuilder> updateBuilder = PlatformFactory.getSQLBuilder(
                databaseTypes, UpdateBuilder.class
        );

        SQLProxyInvoker invoker = new SQLProxyInvoker(
                new Class[]{
                        RedefineUpdateBuilder.class,
                        ReplaceUpdateSetBuilder.class,
                        ReplaceUpdateValueBuilder.class,
                        ReplaceUpdateWhereLogicBuilder.class,
                        UpdateOLBuilder.class,
                        UpdateStartBuilder.class,
                        UpdateWhereBuilder.class,
                        UpdateTableAliasBuilder.class
                },
                updateBuilder
        );

        return (UpdateStartBuilder) invoker.getInterface(UpdateBuilder.class).update();
    }
}
