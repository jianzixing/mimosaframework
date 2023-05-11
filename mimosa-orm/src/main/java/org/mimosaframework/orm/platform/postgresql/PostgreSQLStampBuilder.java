package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;

public class PostgreSQLStampBuilder implements PlatformStampBuilder {
    @Override
    public PlatformStampAlter alter() {
        return new PostgreSQLStampAlter(new PostgreSQLStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampCreate create() {
        return new PostgreSQLStampCreate(new PostgreSQLStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampDrop drop() {
        return new PostgreSQLStampDrop(new PostgreSQLStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampRename rename() {
        return new PostgreSQLStampRename(new PostgreSQLStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampInsert insert() {
        return new PostgreSQLStampInsert(new PostgreSQLStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampDelete delete() {
        return new PostgreSQLStampDelete(new PostgreSQLStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare(this.select()));
    }

    @Override
    public PlatformStampSelect select() {
        return new PostgreSQLStampSelect(new PostgreSQLStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampUpdate update() {
        return new PostgreSQLStampUpdate(new PostgreSQLStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare(this.select()));
    }

    @Override
    public StampCombineBuilder structure() {
        return new PostgreSQLStampStructure();
    }
}
