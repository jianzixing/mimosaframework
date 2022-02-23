package org.mimosaframework.springmvc;

import org.mimosaframework.springmvc.i18n.I18n;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.util.ArrayList;
import java.util.List;

public class MimosaRequestHandlerAdapter extends RequestMappingHandlerAdapter {
    private List<HandlerMethodArgumentResolver> beforeArgumentResolvers;
    private List<HandlerMethodReturnValueHandler> beforeReturnValueHandlers;
    private String defaultContentType = null;
    private ModelObjectArgumentResolver resolver = new ModelObjectArgumentResolver();

    static {
        I18n.register();
    }

    public void setDefaultContentType(String defaultContentType) {
        this.defaultContentType = defaultContentType;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        List<HandlerMethodArgumentResolver> fixResolvers = this.getArgumentResolvers();
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>(fixResolvers);
        resolvers.add(0, resolver);
        if (beforeArgumentResolvers != null) {
            for (HandlerMethodArgumentResolver resolver : beforeArgumentResolvers) {
                resolvers.add(1, resolver);
            }
        }
        resolvers.add(1, new SearchFormArgumentResolver());
        resolvers.add(1, new ListArgumentResolver());

        this.setArgumentResolvers(resolvers);

        List<HandlerMethodReturnValueHandler> fixReturnValueHandlers = this.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>(fixReturnValueHandlers);
        returnValueHandlers.add(0, new PrinterReturnValueHandler(defaultContentType));
        if (beforeReturnValueHandlers != null) {
            for (HandlerMethodReturnValueHandler handler : beforeReturnValueHandlers) {
                returnValueHandlers.add(0, handler);
            }
        }
        this.setReturnValueHandlers(returnValueHandlers);
    }

    public void setBeforeArgumentResolvers(List<HandlerMethodArgumentResolver> beforeArgumentResolvers) {
        this.beforeArgumentResolvers = beforeArgumentResolvers;
    }

    public void setBeforeReturnValueHandlers(List<HandlerMethodReturnValueHandler> beforeReturnValueHandlers) {
        this.beforeReturnValueHandlers = beforeReturnValueHandlers;
    }
}
