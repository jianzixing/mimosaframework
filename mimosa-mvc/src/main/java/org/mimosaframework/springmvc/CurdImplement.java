package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.SessionTemplate;

import java.util.List;

public interface CurdImplement {
    void setSessionTemplate(SessionTemplate sessionTemplate);

    void setTableClass(Class tableClass);

    void setPrimarykey(String pk);

    @Response
    String add(ModelObject object);

    @Response
    String del(String id);

    @Response
    String dels(List<String> ids);

    @Response
    String delSearch(SearchForm form);

    @Response
    String update(ModelObject object);

    @Response
    String updateSearch(SearchForm form);

    @Response
    String get(String id);

    @Response
    String list(SearchForm form, Long start, Long limit);

    @Response
    String page(SearchForm form, Long start, Long limit);
}
