package org.mimosaframework.orm.auxiliary;


import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.Paging;

import java.util.List;

public interface SearchEngineClient extends AuxiliaryClient {
    void createRegion(String region, ModelObject object);

    String index(String region, ModelObject object);

    int del(String region, Object id);

    String update(String region, ModelObject object);

    Paging<ModelObject> search(String region, ModelObject object);

    Paging<ModelObject> search(String region, ModelObject object, int start, int limit);

    Paging<ModelObject> search(String region, ModelObject object, List<SearchSort> sorts, int start, int limit);
}
