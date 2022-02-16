package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.ModelArray;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.criteria.DefaultQuery;
import org.mimosaframework.orm.criteria.Query;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

public class SearchFormArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class type = parameter.getParameterType();
        if (type.isAssignableFrom(SearchForm.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String parameterName = parameter.getParameterName();
        String param = webRequest.getParameter(parameterName);
        if (param == null) {
            return null;
        }

        try {
            List<ModelObject> objects = (List<ModelObject>) ModelArray.parse(param);
            SearchForm searchForm = new SearchForm();
            searchForm.set(objects);
            return searchForm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
