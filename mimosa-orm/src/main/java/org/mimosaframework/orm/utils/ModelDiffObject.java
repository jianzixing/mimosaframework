package org.mimosaframework.orm.utils;

import java.util.List;
import java.util.Map;

public class ModelDiffObject<T> {
    private List<T> removed;
    private List<T> exists;
    private List<T> updates;
    private List<T> inserts;
    private Map<T, T> map;

    public ModelDiffObject(List<T> removed,
                           List<T> exists,
                           List<T> updates,
                           List<T> inserts,
                           Map<T, T> map) {
        this.removed = removed;
        this.exists = exists;
        this.updates = updates;
        this.inserts = inserts;
        this.map = map;
    }

    public List<T> getRemoved() {
        return removed;
    }

    public List<T> getExists() {
        return exists;
    }

    public List<T> getInserts() {
        return inserts;
    }

    public List<T> getUpdates() {
        return updates;
    }

    public Map<T, T> getMap() {
        return map;
    }

    public Map<T, T> getUpdateMap() {
        return map;
    }

    public boolean hasRemove() {
        return removed != null && removed.size() > 0;
    }

    public boolean hasExist() {
        return exists != null && exists.size() > 0;
    }

    public boolean hasInsert() {
        return inserts != null && inserts.size() > 0;
    }

    public boolean hasUpdate() {
        return updates != null && updates.size() > 0;
    }
}
