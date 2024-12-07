package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.json.parser.Feature;
import org.mimosaframework.orm.annotation.Table;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;

public class ModelObjectArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> type = methodParameter.getParameterType();
        Method method = methodParameter.getMethod();

        Body body = method.getAnnotation(Body.class);
        RequestBody requestBody = methodParameter.getMethodAnnotation(RequestBody.class);
        if (requestBody == null && body != null) {
            return !ClassUtils.isPrimitiveOrWrapper(type);
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String parameterName = methodParameter.getParameterName();
        Class<?> type = methodParameter.getParameterType();
        String value = nativeWebRequest.getParameter(parameterName);
        if (value == null) {
            return null;
        }

        ModelObject object = ModelObject.parseObject(value, Feature.OrderedField);
        if (type.isAssignableFrom(ModelObject.class)) {
            return object;
        } else {
            return ModelObject.toJavaObject(object, type);
        }
    }
}
