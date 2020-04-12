package org.mimosaframework.springmvc;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.springmvc.i18n.I18n;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringValueResolver;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MimosaRequestHandlerMapping extends RequestMappingInfoHandlerMapping
        implements EmbeddedValueResolverAware {

    private boolean useSuffixPatternMatch = true;
    private boolean useRegisteredSuffixPatternMatch = false;
    private boolean useTrailingSlashMatch = true;
    private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
    private StringValueResolver embeddedValueResolver;
    private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();

    private String prefix = null;
    private Map<String, String> prefixs = null;
    private Map<String, String> replaces = null;
    private Class<? extends CurdImplement> curdImplementClass;
    private SessionTemplate sessionTemplate;

    static {
        I18n.register();
    }

    public void setCurdImplementClass(Class<? extends CurdImplement> curdImplementClass) {
        this.curdImplementClass = curdImplementClass;
    }

    public void setSessionTemplate(SessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setPrefixs(Map<String, String> prefixs) {
        this.prefixs = prefixs;
    }

    public void setReplaces(Map<String, String> replaces) {
        this.replaces = replaces;
    }

    public void setUseSuffixPatternMatch(boolean useSuffixPatternMatch) {
        this.useSuffixPatternMatch = useSuffixPatternMatch;
    }

    public void setUseRegisteredSuffixPatternMatch(boolean useRegisteredSuffixPatternMatch) {
        this.useRegisteredSuffixPatternMatch = useRegisteredSuffixPatternMatch;
        this.useSuffixPatternMatch = (useRegisteredSuffixPatternMatch || this.useSuffixPatternMatch);
    }

    public void setUseTrailingSlashMatch(boolean useTrailingSlashMatch) {
        this.useTrailingSlashMatch = useTrailingSlashMatch;
    }

    public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
        Assert.notNull(contentNegotiationManager, "ContentNegotiationManager must not be null");
        this.contentNegotiationManager = contentNegotiationManager;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

    @Override
    public void afterPropertiesSet() {
        this.config = new RequestMappingInfo.BuilderConfiguration();
        this.config.setPathHelper(getUrlPathHelper());
        this.config.setPathMatcher(getPathMatcher());
        this.config.setSuffixPatternMatch(this.useSuffixPatternMatch);
        this.config.setTrailingSlashMatch(this.useTrailingSlashMatch);
        this.config.setRegisteredSuffixPatternMatch(this.useRegisteredSuffixPatternMatch);
        this.config.setContentNegotiationManager(getContentNegotiationManager());

        super.afterPropertiesSet();
    }


    public boolean useSuffixPatternMatch() {
        return this.useSuffixPatternMatch;
    }

    public boolean useRegisteredSuffixPatternMatch() {
        return this.useRegisteredSuffixPatternMatch;
    }

    public boolean useTrailingSlashMatch() {
        return this.useTrailingSlashMatch;
    }

    public ContentNegotiationManager getContentNegotiationManager() {
        return this.contentNegotiationManager;
    }

    public List<String> getFileExtensions() {
        return this.config.getFileExtensions();
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return ((AnnotationUtils.findAnnotation(beanType, Controller.class) != null) ||
                (AnnotationUtils.findAnnotation(beanType, RequestMapping.class) != null));
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = createRequestMappingInfo(method);
        if (info != null) {
            RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
            if (typeInfo != null) {
                info = typeInfo.combine(info);
            }
        }
        return info;
    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        Printer printer = AnnotatedElementUtils.findMergedAnnotation(element, Printer.class);
        RequestCondition<?> condition = (element instanceof Class<?> ?
                getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
        if (requestMapping != null) {
            return createRequestMappingInfo(requestMapping, condition);
        }

        // only support method
        if (element instanceof Method) {
            APIController apiController = ((Method) element).getDeclaringClass().getAnnotation(APIController.class);
            if (apiController != null) {
                Class type = ((Method) element).getDeclaringClass();
                String className = ((Method) element).getDeclaringClass().getSimpleName();
                String methodName = ((Method) element).getName();

                if (className.indexOf("Controller") >= 0) {
                    className = className.replace("Controller", "");
                }
                if (apiController != null && StringTools.isNotEmpty(apiController.value())) {
                    className = apiController.value();
                }

                this.isCurdController(type, element, className);

                if (printer != null) {
                    if (StringTools.isNotEmpty(printer.value())) {
                        methodName = printer.value();
                    }

                    String url = this.getCommonPrefix(type)
                            + "/" + this.replaceCommonPrefix(type, className)
                            + "/" + methodName;
                    String lineUrl = this.getCommonPrefix(type)
                            + "/" + StringTools.humpToLine(this.replaceCommonPrefix(type, className))
                            + "/" + StringTools.humpToLine(methodName);

                    if (StringTools.isNotEmpty(printer.path())) {
                        url += printer.path();
                        lineUrl += printer.path();
                    }
                    String[] path = new String[]{url, url.toLowerCase(), lineUrl};


                    return createRequestMappingInfoByPrinter(printer, condition, path);
                }
            }
        }

        return null;
    }

    private void isCurdController(Class type, AnnotatedElement element, String className) {
        if (curdImplementClass != null && sessionTemplate != null) {
            CURDPrinter curdPrinter = AnnotationUtils.findAnnotation(element, CURDPrinter.class);

            try {
                this.setCurdController(type, curdPrinter, element, className);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void setCurdController(Class type, CURDPrinter curdPrinter, AnnotatedElement element, String className) throws IllegalAccessException, InstantiationException {
        if (curdPrinter != null) {
            Method m = (Method) element;
            String cname = m.getName();
            if (StringTools.isNotEmpty(curdPrinter.name())) {
                cname = curdPrinter.name();
            }

            CurdImplement curdImplement = curdImplementClass.newInstance();
            curdImplement.setSessionTemplate(sessionTemplate);
            curdImplement.setTableClass(m.getReturnType());
            curdImplement.setPrimarykey(curdPrinter.pk());


            String[] methodNames = new String[]{
                    "add",
                    "del",
                    "dels",
                    "delSearch",
                    "update",
                    "updateSearch",
                    "get",
                    "list",
                    "page"
            };
            Method[] methods = new Method[0];
            try {
                methods = new Method[]{
                        curdImplementClass.getMethod("add", ModelObject.class),
                        curdImplementClass.getMethod("del", String.class),
                        curdImplementClass.getMethod("dels", List.class),
                        curdImplementClass.getMethod("delSearch", SearchForm.class),
                        curdImplementClass.getMethod("update", ModelObject.class),
                        curdImplementClass.getMethod("updateSearch", SearchForm.class),
                        curdImplementClass.getMethod("get", String.class),
                        curdImplementClass.getMethod("list", SearchForm.class, Long.class, Long.class),
                        curdImplementClass.getMethod("page", SearchForm.class, Long.class, Long.class),
                };
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            if (methodNames.length != methods.length) {
                throw new IllegalArgumentException("init curd controller but method names length not equals methods length");
            }

            for (int i = 0; i < methodNames.length; i++) {
                String methodName = methodNames[i];
                Method method = methods[i];
                String url = this.getCommonPrefix(type)
                        + "/" + this.replaceCommonPrefix(type, className)
                        + "/" + methodName;
                String lineUrl = this.getCommonPrefix(type)
                        + "/" + StringTools.humpToLine(this.replaceCommonPrefix(type, className))
                        + "/" + StringTools.humpToLine(methodName);

                String[] path = new String[]{url, lineUrl};
                RequestMappingInfo info = RequestMappingInfo
                        .paths(resolveEmbeddedValuesInPatterns(path))
                        .methods(new RequestMethod[]{})
                        .params(new String[]{})
                        .headers(new String[]{})
                        .consumes(new String[]{})
                        .produces(new String[]{})
                        .mappingName("")
                        .customCondition(null)
                        .options(this.config)
                        .build();
                registerHandlerMethod(curdImplement, method, info);
            }
        }
    }

    private synchronized String getCommonPrefix(Class c) {
        String name = c.getName();
        if (prefixs != null) {
            Iterator<Map.Entry<String, String>> iterator = prefixs.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String key = entry.getKey();
                if (name.indexOf(key) >= 0 && entry.getValue() != null) {
                    return entry.getValue();
                }
            }
        }
        if (prefix != null) {
            return prefix;
        }
        return "";
    }

    private synchronized String replaceCommonPrefix(Class c, String name) {
        String clsName = c.getName();
        String simpleName = c.getSimpleName();
        clsName = clsName.substring(0, clsName.length() - simpleName.length() - 1);
        if (replaces != null) {
            String replace = replaces.get(clsName);
            if (StringTools.isNotEmpty(replace)) {
                String[] rs = replace.split(";");
                if (rs.length >= 1) {
                    String r1 = rs[0].trim();
                    String r2 = rs.length == 1 ? "" : rs[1];
                    if (name.startsWith(rs[0].trim())) {
                        return name.replaceFirst(r1, r2.trim());
                    }
                }
            }
        }
        return name;
    }

    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        return null;
    }

    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        return null;
    }

    protected RequestMappingInfo createRequestMappingInfoByPrinter(Printer requestMapping, RequestCondition<?> customCondition, String[] path) {

        return RequestMappingInfo
                .paths(resolveEmbeddedValuesInPatterns(path))
                .methods(requestMapping.method())
                .params(requestMapping.params())
                .headers(requestMapping.headers())
                .consumes(requestMapping.consumes())
                .produces(requestMapping.produces())
                .mappingName(requestMapping.name())
                .customCondition(customCondition)
                .options(this.config)
                .build();
    }

    protected RequestMappingInfo createRequestMappingInfo(RequestMapping requestMapping, RequestCondition<?> customCondition) {

        return RequestMappingInfo
                .paths(resolveEmbeddedValuesInPatterns(requestMapping.path()))
                .methods(requestMapping.method())
                .params(requestMapping.params())
                .headers(requestMapping.headers())
                .consumes(requestMapping.consumes())
                .produces(requestMapping.produces())
                .mappingName(requestMapping.name())
                .customCondition(customCondition)
                .options(this.config)
                .build();
    }

    protected String[] resolveEmbeddedValuesInPatterns(String[] patterns) {
        if (this.embeddedValueResolver == null) {
            return patterns;
        } else {
            String[] resolvedPatterns = new String[patterns.length];
            for (int i = 0; i < patterns.length; i++) {
                resolvedPatterns[i] = this.embeddedValueResolver.resolveStringValue(patterns[i]);
            }
            return resolvedPatterns;
        }
    }

    @Override
    protected CorsConfiguration initCorsConfiguration(Object handler, Method method, RequestMappingInfo mappingInfo) {
        HandlerMethod handlerMethod = createHandlerMethod(handler, method);
        CrossOrigin typeAnnotation = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), CrossOrigin.class);
        CrossOrigin methodAnnotation = AnnotationUtils.findAnnotation(method, CrossOrigin.class);

        if (typeAnnotation == null && methodAnnotation == null) {
            return null;
        }

        CorsConfiguration config = new CorsConfiguration();
        updateCorsConfig(config, typeAnnotation);
        updateCorsConfig(config, methodAnnotation);

        if (CollectionUtils.isEmpty(config.getAllowedOrigins())) {
            config.setAllowedOrigins(Arrays.asList(CrossOrigin.DEFAULT_ORIGINS));
        }
        if (CollectionUtils.isEmpty(config.getAllowedMethods())) {
            for (RequestMethod allowedMethod : mappingInfo.getMethodsCondition().getMethods()) {
                config.addAllowedMethod(allowedMethod.name());
            }
        }
        if (CollectionUtils.isEmpty(config.getAllowedHeaders())) {
            config.setAllowedHeaders(Arrays.asList(CrossOrigin.DEFAULT_ALLOWED_HEADERS));
        }
        if (config.getAllowCredentials() == null) {
            config.setAllowCredentials(CrossOrigin.DEFAULT_ALLOW_CREDENTIALS);
        }
        if (config.getMaxAge() == null) {
            config.setMaxAge(CrossOrigin.DEFAULT_MAX_AGE);
        }
        return config;
    }

    private void updateCorsConfig(CorsConfiguration config, CrossOrigin annotation) {
        if (annotation == null) {
            return;
        }
        for (String origin : annotation.origins()) {
            config.addAllowedOrigin(origin);
        }
        for (RequestMethod method : annotation.methods()) {
            config.addAllowedMethod(method.name());
        }
        for (String header : annotation.allowedHeaders()) {
            config.addAllowedHeader(header);
        }
        for (String header : annotation.exposedHeaders()) {
            config.addExposedHeader(header);
        }

        String allowCredentials = annotation.allowCredentials();
        if ("true".equalsIgnoreCase(allowCredentials)) {
            config.setAllowCredentials(true);
        } else if ("false".equalsIgnoreCase(allowCredentials)) {
            config.setAllowCredentials(false);
        } else if (!allowCredentials.isEmpty()) {
            throw new IllegalStateException("@CrossOrigin's allowCredentials value must be \"true\", \"false\", "
                    + "or an empty string (\"\"); current value is [" + allowCredentials + "].");
        }

        if (annotation.maxAge() >= 0 && config.getMaxAge() == null) {
            config.setMaxAge(annotation.maxAge());
        }
    }

    protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
        return super.getHandlerInternal(request);
    }

}
