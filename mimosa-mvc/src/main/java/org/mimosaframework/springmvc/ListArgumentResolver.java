package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.Model;
import org.mimosaframework.core.json.ModelArray;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Method method = parameter.getMethod();
        Printer printer = method.getAnnotation(Printer.class);
        Class type = parameter.getParameterType();
        if (List.class.isAssignableFrom(type) && printer != null) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        Class<?> elementClass = getElementTypeFromAnnotation(parameter);
        String value = webRequest.getParameter(parameter.getParameterName());

        if (StringTools.isNotEmpty(value)) {
            List actualParameter = new ArrayList();
            Object attribute = null;
            if (elementClass != null) {
                if (BeanUtils.isSimpleValueType(elementClass)) {
                    List<?> array = ModelArray.parseArray(value, elementClass);
                    for (int i = 0; i < array.size(); i++) {
                        attribute = array.get(i);
                        actualParameter.add(attribute);
                    }
                } else if (elementClass.isAssignableFrom(ModelObject.class)) {
                    List<?> array = ModelArray.parseArray(value, elementClass);
                    for (int i = 0; i < array.size(); i++) {
                        attribute = new ModelObject((Map<Object, Object>) array.get(i));
                        actualParameter.add(attribute);
                    }
                } else {
                    ModelArray array = ModelArray.parseArray(value);
                    String name = ClassUtils.getShortNameAsProperty(elementClass);
                    if (array != null && array.size() != 0) {
                        for (int i = 0; i < array.size(); i++) {
                            Object item = array.get(i);
                            if (item instanceof Model) {
                                Object obj = ModelObject.toJavaObject((Model) item, elementClass);
                                actualParameter.add(obj);
                            } else {
                                // 如果不是我想要的结果那就交给Spring初始化吧
                                attribute = BeanUtils.instantiateClass(elementClass);
                                WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);
                                actualParameter.add(binder.convertIfNecessary(array.get(i), elementClass, parameter));
                            }
                        }
                    }
                }
            } else {
                List<?> array = ModelArray.parseArray(value);
                if (array != null && array.size() != 0) {
                    for (int i = 0; i < array.size(); i++) {
                        actualParameter.add(array.get(i));
                    }
                }
            }
            return actualParameter;
        }
        return null;
    }

    private Class<?> getElementTypeFromAnnotation(MethodParameter parameter) {
        Type type = parameter.getGenericParameterType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type clazz = pType.getActualTypeArguments()[0];
            return (Class<?>) clazz;
        }
        return null;
    }
}
