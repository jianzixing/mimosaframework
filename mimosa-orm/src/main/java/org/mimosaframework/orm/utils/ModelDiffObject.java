package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;

import java.util.List;

public class ModelDiffObject {
    private List<ModelObject> removed;
    private List<ModelObject> exists;
    private List<ModelObject> inserts;

    public ModelDiffObject(List<ModelObject> removed, List<ModelObject> exists, List<ModelObject> inserts) {
        this.removed = removed;
        this.exists = exists;
        this.inserts = inserts;
    }

    public List<ModelObject> getRemoved() {
        return removed;
    }

    public List<ModelObject> getExists() {
        return exists;
    }

    public List<ModelObject> getInserts() {
        return inserts;
    }
}
