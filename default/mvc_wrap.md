# 自定义包装参数

使用自定义包装参数需要在MimosaRequestHandlerAdapter中配置好解析类，然后自定义包装类的解析过程。
最后在APIController的Printer方法中使用即可，在每次用户访问时会自定包装好参数值通过参数传入。

在spring mvc中配置

```xml
<bean class="org.mimosaframework.springmvc.MimosaRequestHandlerAdapter">
    <property name="beforeArgumentResolvers">
        <list>
            <bean class="com.jianzixing.webapp.handler.RequestAdminInstance"/>
        </list>
    </property>
    <property name="beforeReturnValueHandlers">
        <list>
            <bean class="com.jianzixing.webapp.handler.ResponseFileObjectInstance"/>
            <bean class="com.jianzixing.webapp.handler.ResponseFileInstance"/>
            <bean class="com.jianzixing.webapp.handler.ResponseFileLogoInstance"/>
        </list>
    </property>
    <property name="messageConverters">
        <list>
            <ref bean="stringHttpMessageConverter"/>
        </list>
    </property>
</bean>
```

beforeArgumentResolvers配置自定义包装类，例子如下：

首先定义一个自定义的包装类

```java
public class RequestAdminWrapper {
    private int id;
    private String userName;
    private String password;
    private int roleId;
    private String realName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
```

然后supportsParameter判断这个包装类是否支持，如果支持后再resolveArgument中组装好支持的包装类。

```java
import com.jianzixing.webapp.modules.system.tables.TableAdmin;
import org.mimosaframework.core.json.ModelObject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

import static com.jianzixing.webapp.modules.system.admin.AdminController.LOGIN_SESS_STR;

public class RequestAdminInstance implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class type = parameter.getParameterType();
        if (type.isAssignableFrom(RequestAdminWrapper.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        ModelObject object = (ModelObject) request.getSession().getAttribute(LOGIN_SESS_STR);
        if (object != null) {
            RequestAdminWrapper wrapper = new RequestAdminWrapper();
            wrapper.setId(object.getIntValue(TableAdmin.id));
            wrapper.setUserName(object.getString(TableAdmin.userName));
            wrapper.setPassword(object.getString(TableAdmin.password));
            wrapper.setRealName(object.getString(TableAdmin.realName));
            wrapper.setRoleId(object.getIntValue(TableAdmin.roleId));
            return wrapper;
        }
        return null;
    }
}
```


最后在使用的APIController中的方法参数上使用这个包装类，在有用户请求时会将组装好的包装类传入

```java
@Printer
public ResponseMessage getLoginAdmin(RequestAdminWrapper wrapper) {
    ...
    return new ResponseMessage();
}
```


## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
