package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;

public class Clone {
    public static ModelObject cloneModelObject(ModelObject object) {
        return (ModelObject) object.clone();
    }
}
