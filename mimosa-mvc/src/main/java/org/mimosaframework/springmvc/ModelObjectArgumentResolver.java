package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.ModelObject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ModelObjectArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class type = methodParameter.getParameterType();
        if (type.isAssignableFrom(ModelObject.class)) {
            return true;
        }

        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String parameterName = methodParameter.getParameterName();
        String value = nativeWebRequest.getParameter(parameterName);
        if (value == null) {
            return null;
        }

        ModelObject object = ModelObject.parseObject(value);
        return object;
    }
}
