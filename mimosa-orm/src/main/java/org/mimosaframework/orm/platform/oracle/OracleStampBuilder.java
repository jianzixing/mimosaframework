package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;

public class OracleStampBuilder implements PlatformStampBuilder {
    @Override
    public PlatformStampAlter alter() {
        return new OracleStampAlter(new PlatformStampSection(),
                new OracleStampReference(),
                new OraclePlatformDialect(),
                new OracleStampShare());
    }

    @Override
    public PlatformStampCreate create() {
        return new OracleStampCreate(new PlatformStampSection(),
                new OracleStampReference(),
                new OraclePlatformDialect(),
                new OracleStampShare());
    }

    @Override
    public PlatformStampDrop drop() {
        return new OracleStampDrop(new PlatformStampSection(),
                new OracleStampReference(),
                new OraclePlatformDialect(),
                new OracleStampShare());
    }

    @Override
    public PlatformStampRename rename() {
        return new OracleStampRename(new PlatformStampSection(),
                new OracleStampReference(),
                new OraclePlatformDialect(),
                new OracleStampShare());
    }

    @Override
    public PlatformStampInsert insert() {
        return new OracleStampInsert(new PlatformStampSection(),
                new OracleStampReference(),
                new OraclePlatformDialect(),
                new OracleStampShare());
    }

    @Override
    public PlatformStampDelete delete() {
        return new OracleStampDelete(new PlatformStampSection(),
                new OracleStampReference(),
                new OraclePlatformDialect(),
                new OracleStampShare(this.select()));
    }

    @Override
    public PlatformStampSelect select() {
        return new OracleStampSelect(new PlatformStampSection(),
                new OracleStampReference(),
                new OraclePlatformDialect(),
                new OracleStampShare());
    }

    @Override
    public PlatformStampUpdate update() {
        return new OracleStampUpdate(new PlatformStampSection(),
                new OracleStampReference(),
                new OraclePlatformDialect(),
                new OracleStampShare(this.select()));
    }

    @Override
    public StampCombineBuilder structure() {
        return new OracleStampStructure();
    }
}
