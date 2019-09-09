package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Autonomously {
    private Map<MimosaDataSource, List<ModelObject>> result;

    public Autonomously(Map<MimosaDataSource, List<ModelObject>> result) {
        this.result = result;
    }

    public List<ModelObject> getCombineObjects() {
        if (result != null) {
            List<ModelObject> objects = null;
            Iterator<Map.Entry<MimosaDataSource, List<ModelObject>>> iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                List<ModelObject> os = iterator.next().getValue();
                if (os != null && os.size() > 0) {
                    if (objects == null) objects = new ArrayList<>();
                    objects.addAll(os);
                }
            }
            return objects;
        }
        return null;
    }

    public Map<MimosaDataSource, List<ModelObject>> getResult() {
        return result;
    }
}
