package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampInsert implements StampAction {
    public String tableName;
    public Class tableClass;

    public StampColumn[] columns;
    public Object[][] values;
    public StampSelect select;

    public StampInsertSequence autoField;

    @Override
    public List<STItem> getTables() {
        List<STItem> items = null;
        if (tableClass != null) {
            if (items == null) items = new ArrayList<>();
            items.add(new STItem(tableClass));
        }
        return items;
    }

    public static class StampInsertSequence {
        public int type = 0; // 执行方式 0 默认程序获取序列ID可以返回ID 1插入自动添加ID无法返回ID
        public String columnName;
    }
}
