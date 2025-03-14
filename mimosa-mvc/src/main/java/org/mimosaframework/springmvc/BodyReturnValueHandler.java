package org.mimosaframework.springmvc;

import jakarta.servlet.http.HttpServletResponse;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

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
        Body printer = method.getAnnotation(Body.class);
        return printer != null && printer.plaintext();
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        if (o == null) {
            modelAndViewContainer.setRequestHandled(true);
            return;
        }
        Method method = methodParameter.getMethod();
        Body printer = method.getAnnotation(Body.class);
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
            if (o instanceof String) {
                text = (String) o;
            } else if (o instanceof Number) {
                text = String.valueOf(o);
            } else {
                text = ModelObject.toFrontString(o);
            }
            response.getWriter().write(text);
        }
    }
}
