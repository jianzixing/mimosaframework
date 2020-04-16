package org.mimosaframework.orm.platform;

import java.util.List;

public class CompareUpdateMate {
    private List<ColumnEditType> editTypes;
    private TableColumnStructure structure;

    public CompareUpdateMate(List<ColumnEditType> editTypes, TableColumnStructure structure) {
        this.editTypes = editTypes;
        this.structure = structure;
    }

    public List<ColumnEditType> getEditTypes() {
        return editTypes;
    }

    public void setEditTypes(List<ColumnEditType> editTypes) {
        this.editTypes = editTypes;
    }

    public TableColumnStructure getStructure() {
        return structure;
    }

    public void setStructure(TableColumnStructure structure) {
        this.structure = structure;
    }
}
