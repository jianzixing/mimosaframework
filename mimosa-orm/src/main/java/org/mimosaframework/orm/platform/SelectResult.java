package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;

import java.util.List;

public class SelectResult {
    private List<ModelObject> objects;
    private PorterStructure structure;

    public SelectResult(List<ModelObject> objects, PorterStructure structure) {
        this.objects = objects;
        this.structure = structure;
    }

    public List<ModelObject> getObjects() {
        return objects;
    }

    public void setObjects(List<ModelObject> objects) {
        this.objects = objects;
    }

    public PorterStructure getStructure() {
        return structure;
    }

    public void setStructure(PorterStructure structure) {
        this.structure = structure;
    }
}
