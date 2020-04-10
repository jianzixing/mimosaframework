package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;

import java.util.List;

/**
 * @author yangankang
 */
public interface ModelObjectConvertKey {

    ModelObject convert(Class tableClass, ModelObject object);

    ModelObject reconvert(Class tableClass, ModelObject object);

    List<ModelObject> convert(Class tableClass, List<ModelObject> objects);

    List<ModelObject> reconvert(Class tableClass, List<ModelObject> objects);

    void setMappingGlobalWrapper(MappingGlobalWrapper mappingGlobalWrapper);
}
