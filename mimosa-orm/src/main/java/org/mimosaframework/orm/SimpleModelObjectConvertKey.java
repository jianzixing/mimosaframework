package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.convert.MappingNamedConvert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yangankang
 */
public class SimpleModelObjectConvertKey implements ModelObjectConvertKey {
    private MappingNamedConvert mappingNamedConvert;

    public SimpleModelObjectConvertKey() {
    }

    public SimpleModelObjectConvertKey(MappingNamedConvert mappingNamedConvert) {
        this.mappingNamedConvert = mappingNamedConvert;
    }

    @Override
    public ModelObject convert(ModelObject object) {
        if (mappingNamedConvert == null) {
            throw new IllegalArgumentException("没有设置数据库字段映射转换器");
        }
        Set<Object> objectSet = object.keySet();
        Set<Object> keyset = new LinkedHashSet<Object>();
        keyset.addAll(objectSet);
        for (Object o : keyset) {
            String key = mappingNamedConvert.convert(String.valueOf(o));
            Object value = object.get(o);
            object.remove(o);
            object.put(key, value);
        }
        return object;
    }

    @Override
    public ModelObject reconvert(ModelObject object) {
        if (mappingNamedConvert == null) {
            throw new IllegalArgumentException("没有设置数据库字段映射转换器");
        }
        Set<Object> objectSet = object.keySet();
        Set<Object> keyset = new LinkedHashSet<Object>();
        keyset.addAll(objectSet);
        for (Object o : keyset) {
            String key = mappingNamedConvert.reverse(String.valueOf(o));
            Object value = object.get(o);
            object.remove(o);
            object.put(key, value);
        }
        return object;
    }

    @Override
    public List<ModelObject> convert(List<ModelObject> objects) {
        if (objects != null && objects.size() > 0) {
            List<ModelObject> list = new ArrayList<>();
            for (ModelObject o : objects) {
                list.add(this.convert(o));
            }
            return list;
        }
        return null;
    }

    @Override
    public List<ModelObject> reconvert(List<ModelObject> objects) {
        if (objects != null && objects.size() > 0) {
            List<ModelObject> list = new ArrayList<>();
            for (ModelObject o : objects) {
                list.add(this.reconvert(o));
            }
            return list;
        }
        return null;
    }

    @Override
    public void setMappingNamedConvert(MappingNamedConvert mappingNamedConvert) {
        this.mappingNamedConvert = mappingNamedConvert;
    }
}
