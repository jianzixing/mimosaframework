package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;

import java.util.List;
import java.util.Map;

public class ModelDiffObject {
    private List<ModelObject> removed;
    private List<ModelObject> exists;
    private List<ModelObject> updates;
    private List<ModelObject> inserts;
    private Map<ModelObject, ModelObject> map;

    public ModelDiffObject(List<ModelObject> removed,
                           List<ModelObject> exists,
                           List<ModelObject> updates,
                           List<ModelObject> inserts,
                           Map<ModelObject, ModelObject> map) {
        this.removed = removed;
        this.exists = exists;
        this.updates = updates;
        this.inserts = inserts;
        this.map = map;
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

    public List<ModelObject> getUpdates() {
        return updates;
    }

    public Map<ModelObject, ModelObject> getMap() {
        return map;
    }
}
