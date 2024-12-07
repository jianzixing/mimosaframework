package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.SessionTemplate;

import java.util.List;

public interface CurdImplement {
    void setSessionTemplate(SessionTemplate sessionTemplate);

    void setTableClass(Class tableClass);

    void setPrimarykey(String pk);

    @Body
    String add(ModelObject object);

    @Body
    String del(String id);

    @Body
    String dels(List<String> ids);

    @Body
    String delSearch(SearchForm form);

    @Body
    String update(ModelObject object);

    @Body
    String updateSearch(SearchForm form);

    @Body
    String get(String id);

    @Body
    String list(SearchForm form, Long start, Long limit);

    @Body
    String page(SearchForm form, Long start, Long limit);
}
