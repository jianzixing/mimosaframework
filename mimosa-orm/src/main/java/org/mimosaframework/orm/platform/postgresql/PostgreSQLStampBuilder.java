package org.mimosaframework.orm.platform.postgresql;

import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;

public class PostgreSQLStampBuilder implements PlatformStampBuilder {
    @Override
    public PlatformStampAlter alter() {
        return new PostgreSQLStampAlter(new PlatformStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampCreate create() {
        return new PostgreSQLStampCreate(new PlatformStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampDrop drop() {
        return new PostgreSQLStampDrop(new PlatformStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampRename rename() {
        return new PostgreSQLStampRename(new PlatformStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampInsert insert() {
        return new PostgreSQLStampInsert(new PlatformStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampDelete delete() {
        return new PostgreSQLStampDelete(new PlatformStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampSelect select() {
        return new PostgreSQLStampSelect(new PlatformStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public PlatformStampUpdate update() {
        return new PostgreSQLStampUpdate(new PlatformStampSection(),
                new PostgreSQLStampReference(),
                new PostgreSQLPlatformDialect(),
                new PostgreSQLStampShare());
    }

    @Override
    public StampCombineBuilder structure() {
        return new PostgreSQLStampStructure();
    }
}
