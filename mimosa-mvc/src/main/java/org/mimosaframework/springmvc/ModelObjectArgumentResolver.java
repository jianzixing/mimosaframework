package org.mimosaframework.springmvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.json.parser.Feature;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;
import java.util.List;

public class ModelObjectArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> type = methodParameter.getParameterType();
        Method method = methodParameter.getMethod();

        Body body = method.getAnnotation(Body.class);
        RequestBody requestBody = methodParameter.getMethodAnnotation(RequestBody.class);
        if (requestBody == null && body != null) {
            return !ClassUtils.isPrimitiveOrWrapper(type)
                   && !String.class.equals(type)
                   && !type.isEnum()
                   && !type.isAssignableFrom(List.class)
                   && !type.isArray()
                   && !type.isAssignableFrom(HttpServletResponse.class)
                   && !type.isAssignableFrom(HttpServletRequest.class)
                    ;
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
        String txt = value.trim();
        if (txt.startsWith("{") && txt.endsWith("}") || txt.startsWith("[") && txt.endsWith("]")) {
            try {
                ModelObject object = ModelObject.parseObject(value, Feature.OrderedField);
                if (type.isAssignableFrom(ModelObject.class)) {
                    return object;
                } else {
                    return ModelObject.toJavaObject(object, type);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("unable to convert to " + type.getSimpleName() + " with argument " + value, e);
            }
        } else {
            throw new IllegalArgumentException("unable to convert to " + type.getSimpleName() + " with argument " + value);
        }
    }
}
