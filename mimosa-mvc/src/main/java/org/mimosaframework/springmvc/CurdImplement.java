package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.SessionTemplate;

import java.util.List;

public interface CurdImplement {
    void setSessionTemplate(SessionTemplate sessionTemplate);

    void setTableClass(Class tableClass);

    void setPrimarykey(String pk);

    @Printer
    String add(ModelObject object);

    @Printer
    String del(String id);

    @Printer
    String dels(List<String> ids);

    @Printer
    String delSearch(SearchForm form);

    @Printer
    String update(ModelObject object);

    @Printer
    String updateSearch(SearchForm form);

    @Printer
    String get(String id);

    @Printer
    String list(SearchForm form, Long start, Long limit);

    @Printer
    String page(SearchForm form, Long start, Long limit);
}
