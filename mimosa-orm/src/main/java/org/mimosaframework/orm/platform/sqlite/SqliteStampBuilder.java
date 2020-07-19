package org.mimosaframework.orm.platform.sqlite;

import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;

public class SqliteStampBuilder implements PlatformStampBuilder {
    @Override
    public PlatformStampAlter alter() {
        return new SqliteStampAlter(new PlatformStampSection(),
                new SqliteStampReference(),
                new SqlitePlatformDialect(),
                new SqliteStampShare());
    }

    @Override
    public PlatformStampCreate create() {
        return new SqliteStampCreate(new PlatformStampSection(),
                new SqliteStampReference(),
                new SqlitePlatformDialect(),
                new SqliteStampShare());
    }

    @Override
    public PlatformStampDrop drop() {
        return new SqliteStampDrop(new PlatformStampSection(),
                new SqliteStampReference(),
                new SqlitePlatformDialect(),
                new SqliteStampShare());
    }

    @Override
    public PlatformStampRename rename() {
        return new SqliteStampRename(new PlatformStampSection(),
                new SqliteStampReference(),
                new SqlitePlatformDialect(),
                new SqliteStampShare());
    }

    @Override
    public PlatformStampInsert insert() {
        return new SqliteStampInsert(new PlatformStampSection(),
                new SqliteStampReference(),
                new SqlitePlatformDialect(),
                new SqliteStampShare());
    }

    @Override
    public PlatformStampDelete delete() {
        return new SqliteStampDelete(new PlatformStampSection(),
                new SqliteStampReference(),
                new SqlitePlatformDialect(),
                new SqliteStampShare());
    }

    @Override
    public PlatformStampSelect select() {
        return new SqliteStampSelect(new PlatformStampSection(),
                new SqliteStampReference(),
                new SqlitePlatformDialect(),
                new SqliteStampShare());
    }

    @Override
    public PlatformStampUpdate update() {
        return new SqliteStampUpdate(new PlatformStampSection(),
                new SqliteStampReference(),
                new SqlitePlatformDialect(),
                new SqliteStampShare());
    }

    @Override
    public StampCombineBuilder structure() {
        return new SqliteStampStructure();
    }
}
