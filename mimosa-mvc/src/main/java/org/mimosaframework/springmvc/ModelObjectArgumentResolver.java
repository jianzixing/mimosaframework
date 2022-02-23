package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.ModelObject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;

public class ModelObjectArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class type = methodParameter.getParameterType();
        Method method = methodParameter.getMethod();

        Printer printer = method.getAnnotation(Printer.class);
        RequestBody requestBody = methodParameter.getMethodAnnotation(RequestBody.class);
        if (requestBody == null && printer != null) {
            if (type.isAssignableFrom(ModelObject.class)) {
                return true;
            }
            if (!type.getName().startsWith("java.util")
                    && !type.getName().startsWith("java.lang")
                    && !type.isEnum()
                    && !type.isPrimitive()) {
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
