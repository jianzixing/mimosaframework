package org.mimosaframework.springmvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.springmvc.utils.ResponseMessage;
import org.mimosaframework.springmvc.utils.ResponsePageMessage;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.io.IOException;

/**
 * 统一异常处理
 * 配置SimpleMappingExceptionResolver异常处理
 * <bean name="exceptionResolver"  class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">   
 * <property name="defaultErrorView" value="error"></property>   
 * 定义异常处理页面用来获取异常信息的变量名，默认名为exception
 * <property name="exceptionAttribute" value="ex"></property>   
 * 定义需要特殊处理的异常，用类名或完全路径名作为key，异常也页名作为值 
 * <property name="exceptionMappings">        
 * <props>            
 * <prop key="com.myproject.frame.exception.NotLoginException">login</prop>
 * <prop key="java.lang.Exception">errorPage</prop>
 * </props>
 * </property>
 * </bean>
 */
public class MimosaHandlerMethodExceptionResolver extends ExceptionHandlerExceptionResolver {
    private static final String DEFAULT_CONTENT_TYPE = "application/json;charset=UTF-8";
    private String contentType = null;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception e) {
        if (handler instanceof HandlerMethod && e != null) {
            HandlerMethod methodHandler = (HandlerMethod) handler;
            Body printer = methodHandler.getMethodAnnotation(Body.class);
            if (printer != null) {
                if (StringTools.isNotEmpty(printer.contentType())) {
                    response.setContentType(printer.contentType());
                } else if (StringTools.isNotEmpty(this.contentType)) {
                    response.setContentType(this.contentType);
                } else {
                    response.setContentType(DEFAULT_CONTENT_TYPE);
                }
                try {
                    MethodParameter parameter = methodHandler.getReturnType();
                    if (parameter.getParameterType().isAssignableFrom(ResponsePageMessage.class)) {
                        String json = (new ResponsePageMessage(e)).toString();
                        response.getWriter().write(json);
                    } else {
                        String json = (new ResponseMessage(e)).toString();
                        response.getWriter().write(json);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return new ModelAndView();
            }
        }
        return super.doResolveHandlerMethodException(request, response, handler, e);
    }
}
