package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;

public class MysqlStampBuilder implements PlatformStampBuilder {
    @Override
    public PlatformStampAlter alter() {
        return new MysqlStampAlter(new PlatformStampSection(),
                new MysqlStampReference(),
                new MysqlPlatformDialect(),
                new MysqlStampShare());
    }

    @Override
    public PlatformStampCreate create() {
        return new MysqlStampCreate(new PlatformStampSection(),
                new MysqlStampReference(),
                new MysqlPlatformDialect(),
                new MysqlStampShare());
    }

    @Override
    public PlatformStampDrop drop() {
        return new MysqlStampDrop(new PlatformStampSection(),
                new MysqlStampReference(),
                new MysqlPlatformDialect(),
                new MysqlStampShare());
    }

    @Override
    public PlatformStampRename rename() {
        return new MysqlStampRename(new PlatformStampSection(),
                new MysqlStampReference(),
                new MysqlPlatformDialect(),
                new MysqlStampShare());
    }

    @Override
    public PlatformStampInsert insert() {
        return new MysqlStampInsert(new PlatformStampSection(),
                new MysqlStampReference(),
                new MysqlPlatformDialect(),
                new MysqlStampShare());
    }

    @Override
    public PlatformStampDelete delete() {
        return new MysqlStampDelete(new PlatformStampSection(),
                new MysqlStampReference(),
                new MysqlPlatformDialect(),
                new MysqlStampShare());
    }

    @Override
    public PlatformStampSelect select() {
        return new MysqlStampSelect(new PlatformStampSection(),
                new MysqlStampReference(),
                new MysqlPlatformDialect(),
                new MysqlStampShare());
    }

    @Override
    public PlatformStampUpdate update() {
        return new MysqlStampUpdate(new PlatformStampSection(),
                new MysqlStampReference(),
                new MysqlPlatformDialect(),
                new MysqlStampShare());
    }

    public PlatformStampInsert save() {
        return new MysqlStampSave(new PlatformStampSection(),
                new MysqlStampReference(),
                new MysqlPlatformDialect(),
                new MysqlStampShare());
    }

    @Override
    public StampCombineBuilder structure() {
        return new MysqlStampStructure();
    }
}
