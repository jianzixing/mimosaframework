package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.convert.MappingNamedConvert;

import java.util.List;

/**
 * @author yangankang
 */
public interface ModelObjectConvertKey {

    ModelObject convert(ModelObject object);

    ModelObject reconvert(ModelObject object);

    List<ModelObject> convert(List<ModelObject> objects);

    List<ModelObject> reconvert(List<ModelObject> objects);

    void setMappingNamedConvert(MappingNamedConvert mappingNamedConvert);
}
