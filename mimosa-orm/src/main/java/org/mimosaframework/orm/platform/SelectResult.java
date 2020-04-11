package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;

import java.util.List;

public class SelectResult {
    private List<ModelObject> objects;
    private JDBCTraversing structure;

    public SelectResult(List<ModelObject> objects, JDBCTraversing structure) {
        this.objects = objects;
        this.structure = structure;
    }

    public List<ModelObject> getObjects() {
        return objects;
    }

    public void setObjects(List<ModelObject> objects) {
        this.objects = objects;
    }

    public JDBCTraversing getStructure() {
        return structure;
    }

    public void setStructure(JDBCTraversing structure) {
        this.structure = structure;
    }
}
