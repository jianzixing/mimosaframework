package org.mimosaframework.orm.sql.stamp;

import java.util.ArrayList;
import java.util.List;

public class StampRename implements StampAction {
    public KeyRenameType renameType;
    public String oldName;
    public String newName;

    public Class tableClass;
    public String tableName;

    @Override
    public List<STItem> getTables() {
        if (tableClass != null) {
            List<STItem> items = new ArrayList<>();
            items.add(new STItem(tableClass));
            return items;
        }
        return null;
    }
}
