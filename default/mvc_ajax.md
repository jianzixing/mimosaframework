# 快速Ajax交互

使用自动生成访问地址需要在Controller上添加注解@APIController，这个注解是继承@Controller的，
这表示使用@APIController注解也可以包含Spring Controller的所有功能。

如果想快速使用Ajax交互还需要在方法上使用注解@Printer表示这个方法会自动生成访问地址。

方法参数和spring mvc的自动包装一致，返回的对象ResponsePageMessage或者ResponseMessage是
自带的一种返回类型，返回到前端是一个json字符串。这两种返回类型可以设置返回的消息code和消息内容。

ResponsePageMessage继承自ResponseMessage在做分页管理时直接将Paging对象传入即可。

```java
@APIController
public class AdminController {
    @Printer
    public ResponsePageMessage getAdmins(int start, int limit, ModelObject search) {
        Paging objects = adminService.getAdmins(start, limit, search);
        return new ResponsePageMessage(objects.getCount(), objects.getObjects());
    }
    
    @RequestMapping("/admin/login")
    public String toAdminLoginPage() {
        return "admin/login";
    }
}
```

如果使用@RequestMapping注解则需要自己编写url这是spring mvc的功能，具体参考spring mvc。

Ajax的自动生成的url请参考[MVC扩展工具说明](./index.html#mvc_intro.md)


## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
