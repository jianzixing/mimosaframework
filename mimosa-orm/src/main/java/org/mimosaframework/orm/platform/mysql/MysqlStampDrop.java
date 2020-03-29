package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.KeyTarget;
import org.mimosaframework.orm.sql.stamp.StampDrop;
import org.mimosaframework.orm.sql.stamp.StampDropBuilder;

public class MysqlStampDrop extends MysqlAbstractStamp implements StampDropBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampDrop drop) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP");
        if (drop.target == KeyTarget.DATABASE) {
            sb.append(" DATABASE");
            if (drop.checkExist) {
                sb.append(" IF EXIST");
            }
            sb.append(" " + drop.name);
        }
        if (drop.target == KeyTarget.TABLE) {
            sb.append(" TABLE");
            if (drop.checkExist) {
                sb.append(" IF EXIST");
            }
            sb.append(" " + this.getTableName(wrapper, drop.table, drop.name));
        }
        if (drop.target == KeyTarget.INDEX) {
            sb.append(" INDEX");
            sb.append(" " + drop.name);
            sb.append(" ON");
            sb.append(" " + this.getTableName(wrapper, drop.table, drop.tableName));
        }
        return null;
    }
}
