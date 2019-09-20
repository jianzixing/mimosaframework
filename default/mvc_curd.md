# 统一CURD操作

假设我们不想为每一个表写Service和Controller，想为某一个表使用一个全局的天删改查操作，这个时候就可以
使用统一CURD操作。

首先你需要定义一个CURD实现，这里给一个常用实现：

```java

import org.mimosaframework.springmvc.utils.ResponseMessage;
import org.mimosaframework.core.exception.ModelCheckerException;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.Paging;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.springmvc.CurdImplement;
import org.mimosaframework.springmvc.Printer;
import org.mimosaframework.springmvc.SearchForm;

import java.util.List;

public class CURDService implements CurdImplement {
    private SessionTemplate sessionTemplate;
    private Class clazz;
    private String primaryKey;

    @Override
    public void setSessionTemplate(SessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    @Override
    public void setTableClass(Class aClass) {
        this.clazz = aClass;
    }

    @Override
    public void setPrimarykey(String s) {
        this.primaryKey = s;
    }

    @Printer
    @Override
    public String add(ModelObject object) {
        object.setObjectClass(this.clazz);
        try {
            object.checkAndThrowable();
        } catch (ModelCheckerException e) {
            return new ResponseMessage().toString();
        }
        sessionTemplate.save(object);
        return new ResponseMessage().toString();
    }

    @Printer
    @Override
    public String del(String id) {
        sessionTemplate.delete(this.clazz, id);
        return new ResponseMessage().toString();
    }

    @Printer
    @Override
    public String dels(List<String> ids) {
        sessionTemplate.delete(Criteria.delete(this.clazz).in(this.primaryKey, ids));
        return new ResponseMessage().toString();
    }

    @Printer
    @Override
    public String delSearch(SearchForm search) {
        return null;
    }

    @Printer
    @Override
    public String update(ModelObject object) {
        object.setObjectClass(this.clazz);
        try {
            object.checkUpdateThrowable();
        } catch (ModelCheckerException e) {
            return new ResponseMessage().toString();
        }
        sessionTemplate.update(object);
        return new ResponseMessage().toString();
    }

    @Printer
    @Override
    public String updateSearch(SearchForm search) {
        return null;
    }

    @Printer
    @Override
    public String get(String id) {
        ModelObject object = sessionTemplate.get(this.clazz, id);
        return new ResponseMessage(object).toString();
    }

    @Printer
    @Override
    public String list(SearchForm search, Long start, Long limit) {
        List<ModelObject> objects = null;
        if (start != null && limit != null) {
            objects = sessionTemplate.list(Criteria.query(this.clazz).limit(start, limit));
        } else {
            objects = sessionTemplate.list(Criteria.query(this.clazz));
        }
        return new ResponseMessage(objects).toString();
    }

    @Printer
    @Override
    public String page(SearchForm search, Long start, Long limit) {
        if (start == null) start = new Long(0);
        if (limit == null) limit = new Long(10);
        Paging objects = sessionTemplate.paging(Criteria.query(this.clazz).limit(start, limit));
        return new ResponseMessage(objects).toString();
    }
}
```


这个实现需要配置到spring mvc的配置文件中，在org.mimosaframework.springmvc.MimosaRequestHandlerMapping
中配置属性 <property name="curdImplementClass" value="com.jianzixing.webapp.modules.CURDService"/>

这时还没法直接使用，还需要定义一个Controller的标识：

```java
@APIController
public class PageSeoController {

    @CURDPrinter(pk = "id")
    public TablePageSeo getPageSeo() {
        return TablePageSeo.id;
    }
}
```


定义一个APIController类然后定义一个方法，注解@CURDPrinter(pk = "id")表示这个表的主键字段是id。

返回的类型是映射类，返回值没有任何意义。
