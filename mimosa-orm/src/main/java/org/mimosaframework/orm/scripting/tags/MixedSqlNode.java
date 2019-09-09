package org.mimosaframework.orm.scripting.tags;

import org.mimosaframework.orm.scripting.DynamicContext;
import org.mimosaframework.orm.scripting.SqlNode;

import java.util.List;

public class MixedSqlNode implements SqlNode {
    private List<SqlNode> contents;
    private String action;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    public MixedSqlNode(List<SqlNode> contents, String action) {
        this.contents = contents;
        this.action = action;
    }

    public boolean apply(DynamicContext context) {
        if (this.contents != null) {
            for (SqlNode sqlNode : contents) {
                sqlNode.apply(context);
            }
        }
        return true;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}