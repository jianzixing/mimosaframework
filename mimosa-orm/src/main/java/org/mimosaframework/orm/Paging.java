package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangankang
 */
public class Paging<T> {
    private long count;
    private List<T> objects;

    public Paging(long count, List<T> objects) {
        this.count = count;
        this.objects = objects;
    }

    public Paging() {
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getObjects() {
        return objects;
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    public List getFields(Object field) {
        if (field != null) {
            String key = String.valueOf(field);
            List list = new ArrayList();
            for (T t : this.objects) {
                if (t instanceof ModelObject) {
                    list.add(((ModelObject) t).get(key));
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        ModelObject object = new ModelObject();
        object.put("count", count);
        object.put("objects", objects);
        return object.toJSONString();
    }
}
