package org.mimosaframework.springmvc;

import jakarta.servlet.http.HttpServletResponse;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.Serializable;
import java.lang.reflect.Method;

public class BodyReturnValueHandler implements HandlerMethodReturnValueHandler {
    private static final String DEFAULT_CONTENT_TYPE = "application/json;charset=UTF-8";
    private String contentType = null;

    public BodyReturnValueHandler(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        Class<?> c = methodParameter.getParameterType();
        Method method = methodParameter.getMethod();
        ApiRequest printer = method.getAnnotation(ApiRequest.class);
        boolean isSupportType = printer != null && printer.plaintext();

        if (!isPrimitiveOrWrapper(c) && !Serializable.class.isAssignableFrom(c)) {
            isSupportType = false;
        }

        return isSupportType;
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        if (o == null) {
            modelAndViewContainer.setRequestHandled(true);
            return;
        }
        Method method = methodParameter.getMethod();
        ApiRequest printer = method.getAnnotation(ApiRequest.class);
        if (printer != null && printer.plaintext()) {
            modelAndViewContainer.setRequestHandled(true);
            HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
            if (StringTools.isNotEmpty(printer.contentType())) {
                response.setContentType(printer.contentType());
            } else if (StringTools.isNotEmpty(this.contentType)) {
                response.setContentType(this.contentType);
            } else {
                response.setContentType(DEFAULT_CONTENT_TYPE);
            }
            String[] headers = printer.headers();
            if (headers != null) {
                for (String s : headers) {
                    String[] s1 = s.split("=");
                    if (s1.length > 1) {
                        String k = s1[0];
                        String v = "";
                        for (int i = 1; i < s1.length; i++) {
                            String s2 = s1[i];
                            if (i != 1) v += "=" + s2;
                            else v += s2;
                        }
                        response.addHeader(k, v);
                    }
                }
            }
            String text = null;
            if (isPrimitiveOrWrapper(o.getClass())) {
                text = String.valueOf(o);
            } else {
                text = ModelObject.toFrontString(o);
            }
            response.getWriter().write(text);
        }
    }

    private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        // 判断基本类型
        if (clazz.isPrimitive()) {
            return true;
        }

        // 判断包装类型
        return clazz == Number.class ||
               clazz == Integer.class ||
               clazz == Boolean.class ||
               clazz == Character.class ||
               clazz == Byte.class ||
               clazz == Short.class ||
               clazz == Long.class ||
               clazz == Float.class ||
               clazz == Double.class;
    }
}
