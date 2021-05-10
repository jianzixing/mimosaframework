package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.builder.BuilderUtils;
import org.mimosaframework.orm.exception.ContextException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.HashSet;
import java.util.Set;

public class ModelObjectArgumentResolver implements HandlerMethodArgumentResolver {
    private String[] packages;
    private Set<Class> supportClass = new HashSet<>();

    public void setPackages(String[] packages) throws ContextException {
        if (packages != null && packages.length > 0) {
            this.packages = packages;
            for (String pkg : packages) {
                Set<Class> supportClass1 = BuilderUtils.getMappingClass(Table.class, pkg, null);
                if (supportClass1 != null && supportClass1.size() > 0) {
                    this.supportClass.addAll(supportClass1);
                }
                Set<Class> supportClass2 = BuilderUtils.getMappingClass(Argument.class, pkg, null);
                if (supportClass2 != null && supportClass2.size() > 0) {
                    this.supportClass.addAll(supportClass2);
                }
            }
        }
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class type = methodParameter.getParameterType();
        RequestBody requestBody = methodParameter.getMethodAnnotation(RequestBody.class);
        if (requestBody == null) {
            if (type.isAssignableFrom(ModelObject.class)) {
                return true;
            }
            if (this.supportClass.contains(type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String parameterName = methodParameter.getParameterName();
        Class type = methodParameter.getParameterType();
        String value = nativeWebRequest.getParameter(parameterName);
        if (value == null) {
            return null;
        }

        ModelObject object = ModelObject.parseObject(value);
        if (type.isAssignableFrom(ModelObject.class)) {
            return object;
        } else {
            return ModelObject.toJavaObject(object, type);
        }
    }
}
