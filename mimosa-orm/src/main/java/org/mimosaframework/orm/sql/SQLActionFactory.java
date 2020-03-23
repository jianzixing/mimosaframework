package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.platform.PlatformFactory;
import org.mimosaframework.orm.sql.alter.AlterAnyBuilder;
import org.mimosaframework.orm.sql.create.CreateAnyBuilder;
import org.mimosaframework.orm.sql.delete.DeleteStartBuilder;
import org.mimosaframework.orm.sql.drop.DropAnyBuilder;
import org.mimosaframework.orm.sql.drop.DropDatabaseBuilder;
import org.mimosaframework.orm.sql.drop.DropTableBuilder;
import org.mimosaframework.orm.sql.drop.RedefineDropBuilder;
import org.mimosaframework.orm.sql.insert.InsertStartBuilder;
import org.mimosaframework.orm.sql.select.SelectStartBuilder;
import org.mimosaframework.orm.sql.update.UpdateStartBuilder;
import org.mimosaframework.orm.utils.DatabaseTypes;

public class SQLActionFactory {
    public static AlterAnyBuilder alter() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        AlterBuilder<AlterAnyBuilder> alterBuilder = PlatformFactory.getSQLBuilder(
                databaseTypes, AlterBuilder.class
        );
        return alterBuilder.alter();
    }

    public static CreateAnyBuilder create() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        CreateBuilder<CreateAnyBuilder> createBuilder = PlatformFactory.getSQLBuilder(
                databaseTypes, CreateBuilder.class
        );
        return createBuilder.create();
    }

    public static DeleteStartBuilder delete() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        DeleteBuilder<DeleteStartBuilder> deleteBuilder = PlatformFactory.getSQLBuilder(
                databaseTypes, DeleteBuilder.class
        );
        return deleteBuilder.delete();
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
        return insertBuilder.insert();
    }

    public static SelectStartBuilder select() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        SelectBuilder<SelectStartBuilder> selectBuilder = PlatformFactory.getSQLBuilder(
                databaseTypes, SelectBuilder.class
        );
        return selectBuilder.select();
    }

    public static UpdateStartBuilder update() {
        DatabaseTypes databaseTypes = MimosaDataSource.getDatabaseType();
        UpdateBuilder<UpdateStartBuilder> updateBuilder = PlatformFactory.getSQLBuilder(
                databaseTypes, UpdateBuilder.class
        );
        return updateBuilder.update();
    }
}
