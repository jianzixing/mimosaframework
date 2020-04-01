package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.sql.alter.*;
import org.mimosaframework.orm.sql.create.*;
import org.mimosaframework.orm.sql.delete.*;
import org.mimosaframework.orm.sql.drop.*;
import org.mimosaframework.orm.sql.insert.*;
import org.mimosaframework.orm.sql.select.*;
import org.mimosaframework.orm.sql.update.*;

public class SQLActionFactory {
    public static AlterAnyBuilder alter() {
        AlterBuilder<AlterAnyBuilder> alterBuilder = new DefaultSQLAlterBuilder();

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
                        AlterTableOptionBuilder.class,
                        AlterPKColumnBuilder.class
                },
                alterBuilder
        );

        return (AlterAnyBuilder) invoker.getInterface(AlterBuilder.class).alter();
    }

    public static CreateAnyBuilder create() {
        CreateBuilder<CreateAnyBuilder> createBuilder = new DefaultSQLCreateBuilder();

        SQLProxyInvoker invoker = new SQLProxyInvoker(
                new Class[]{
                        RedefineCreateBuilder.class,
                        ColumnAssistBuilder.class,
                        ColumnTypeBuilder.class,
                        CreateAnyBuilder.class,
                        CreateColumnAssistBuilder.class,
                        CreateDatabaseStartBuilder.class,
                        CreateIndexBuilder.class,
                        CreateCollateExtraBuilder.class,
                        CreateIndexColumnsBuilder.class,
                        CreateTableStartBuilder.class,
                        CreateTableTailBuilder.class
                },
                createBuilder
        );

        return (CreateAnyBuilder) invoker.getInterface(CreateBuilder.class).create();
    }

    public static DeleteStartBuilder delete() {
        DeleteBuilder<DeleteStartBuilder> deleteBuilder = new DefaultSQLDeleteBuilder();

        SQLProxyInvoker invoker = new SQLProxyInvoker(
                new Class[]{
                        RedefineDeleteBuilder.class,
                        DeleteUsingBuilder.class,
                        BeforeDeleteFromBuilder.class,
                        DeleteStartBuilder.class,
                        DeleteAsTableBuilder.class,
                        DeleteTableAsBuilder.class,
                        DeleteWhereNextBuilder.class,
                        CommonWhereBuilder.class,
                        CommonWhereCompareBuilder.class,
                        CommonWhereNextImplBuilder.class
                },
                deleteBuilder
        );

        return (DeleteStartBuilder) invoker.getInterface(DeleteBuilder.class).delete();
    }

    public static DropAnyBuilder drop() {
        DropBuilder<DropAnyBuilder> dropBuilder = new DefaultSQLDropBuilder();

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
        InsertBuilder<InsertStartBuilder> insertBuilder = new DefaultSQLInsertBuilder();

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
        SelectBuilder<SelectStartBuilder> selectBuilder = new DefaultSQLSelectBuilder();

        SQLProxyInvoker invoker = new SQLProxyInvoker(
                new Class[]{
                        RedefineSelectBuilder.class,
                        SelectGHOLBuilder.class,
                        SelectHOLBuilder.class,
                        SelectOLBuilder.class,
                        SelectStartBuilder.class,
                        ReplaceSelectFieldBuilder.class,
                        SelectJoinsBuilder.class,
                        SelectTableAliasBuilder.class,
                        HavingOperatorFunctionBuilder.class,
                        SelectGroupByBuilder.class,
                        SelectGroupByNextBuilder.class,
                        OrderByNextBuilder.class,
                        OrderBySealBuilder.class,
                        SelectFieldFunBuilder.class
                },
                selectBuilder
        );

        return (SelectStartBuilder) invoker.getInterface(SelectBuilder.class).select();
    }

    public static UpdateStartBuilder update() {
        UpdateBuilder<UpdateStartBuilder> updateBuilder = new DefaultSQLUpdateBuilder();

        SQLProxyInvoker invoker = new SQLProxyInvoker(
                new Class[]{
                        RedefineUpdateBuilder.class,
                        ReplaceUpdateSetBuilder.class,
                        ReplaceUpdateValueBuilder.class,
                        UpdateWhereNextBuilder.class,
                        UpdateOLBuilder.class,
                        UpdateStartBuilder.class,
                        UpdateTableAliasBuilder.class
                },
                updateBuilder
        );

        return (UpdateStartBuilder) invoker.getInterface(UpdateBuilder.class).update();
    }

    public static CommonWhereBuilder wrapperBuilder() {
        WrapperBuilder wrapperBuilder = new SimpleCommonWhereBuilder();
        SQLProxyInvoker invoker = new SQLProxyInvoker(
                new Class[]{
                        AboutChildBuilder.class,
                        WrapperBuilder.class,
                        LogicBuilder.class,
                        OperatorLinkBuilder.class,
                        BetweenValueBuilder.class,
                        OperatorBuilder.class,
                        OperatorFunctionBuilder.class,
                        AbsValueBuilder.class,
                        AbsWhereValueBuilder.class,
                        AbsWhereColumnBuilder.class,
                        CommonWhereCompareBuilder.class,
                        CommonWhereBuilder.class,
                        CommonWhereNextBuilder.class,
                        CommonWhereNextImplBuilder.class
                },
                wrapperBuilder
        );

        return invoker.getInterface(CommonWhereBuilder.class);
    }
}
