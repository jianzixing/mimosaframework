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
            Query query = new DefaultQuery(null);
            SearchForm searchForm = new SearchForm(query);

            for (ModelObject o : objects) {
                String name = o.getString("name");
                String value = o.getString("value");
                String symbol = o.getString("symbol");
                String s = o.getString("start");
                String e = o.getString("end");

                if ((StringTools.isNotEmpty(value)
                        || StringTools.isNotEmpty(s)
                        || StringTools.isNotEmpty(e))
                        && StringTools.isNotEmpty(name)) {
                    if (StringTools.isEmpty(symbol)) {
                        query.and(Criteria.filter().eq(name, value));
                    } else if (symbol.equalsIgnoreCase("between")) {
                        query.and(Criteria.filter().between(name, s, e));
                    } else if (symbol.equalsIgnoreCase("gt")) {
                        query.and(Criteria.filter().gt(name, value));
                    } else if (symbol.equalsIgnoreCase("lt")) {
                        query.and(Criteria.filter().lt(name, value));
                    } else if (symbol.equalsIgnoreCase("gte")) {
                        query.and(Criteria.filter().gte(name, value));
                    } else if (symbol.equalsIgnoreCase("lte")) {
                        query.and(Criteria.filter().lte(name, value));
                    } else if (symbol.equalsIgnoreCase("like")) {
                        query.and(Criteria.filter().like(name, value));
                    }
                }
            }

            return searchForm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
