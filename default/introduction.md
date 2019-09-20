## MimosaFramework简介

MimosaFramework是一组框架组合，主要功能是提供数据库读写的工具，和mvc扩展工具。

框架分为三部分，第一部分mimosa-core包含一些基本的工具类以及ModelObject的JSON操作库(使用FastJson为基础)，以及cglib和ognl表达式等开源库(方便独立使用)。
第二部分mimosa-orm是数据库读写操作的核心库(依赖mimosa-core)，用过API操作数据库，通过注解类自动创建数据库表，并且可以提供类似Mybatis的xml配置SQL的方式执行SQL语句。
第三部分mimosa-mvc是在spring-mvc基础上扩展的方便Ajax交互的工具(依赖mimosa-core)，可以简化后台Ajax交互复杂度。

总的来说MimosaFramework可以给我们提供一个方便快捷开发的半自动框架。MimosaFramework并不能帮我们做全部的事情，很多时候依旧需要开发者本身去处理一些事情。

**注意：该框架在我们的使用范围内是稳定的，一般开发者面临的情况非常多，因此如果有任何问题请在
[issues](https://github.com/jianzixing/mimosaframework/issues)
里提出，我们会在未来版本中更正或者修改增加功能。**

_感谢您使用我们的框架，如果有任何商业需求可以[联系我们(北京简子行科技有限公司)](https://www.jianzixing.com.cn)我们将竭诚为您服务。_


---

### mimosa-core包说明

MimosaFramework并不是一个单独的jar，在开发过程中由于需要引用其他开源框架，如果都放在maven中做依赖
一来由于自定义部分无法满足，二来maven之外使用时并不方便。所以我们将一些工具类以及引用的其他开源依赖
放在这个包中。

其中包含AMS和CGLIB包在某些需要解析二进制时使用。

包含一个json解析包，该包是使用fastjson作为基础。在整个ORM框架中处处都会用到的ModelObject就是这个包中
实现的，该类和fastjson的JSONObject类似，但是由于需要增加了一些特有功能。

使用时如下：

```java
ModelObject object = new ModelObject(TableUser.class);
object.put(TableUser.id, 20);
object.put(TableUser.userName, "yangankang_test_save_n_2");
object.put(TableUser.password, "123456");
```

如果使用空构造函数则可以使用setObjectClass方法设置映射类。

```java
ModelObject object = new ModelObject();
object.setObjectClass(TableUser.class);
object.put(TableUser.id, 20);
object.put(TableUser.userName, "yangankang_test_save_n_2");
object.put(TableUser.password, "123456");
```

### mimosa-orm包说明

ORM包是对数据库CURD操作的库，采用Java API方式操作，使用非常方便。具体可以查看API说明。

该包核心类 SessionTemplate 包含对数据库的CURD的全部操作。支持使用Spring创建SessionTemplate，
支持多种数据库，以及Mybatis的动态生成SQL的方式。

### mimosa-mvc包说明

MVC包是在Spring MVC的基础上扩展的，这个工具包只为了方便Ajax的使用以及自动创建访问的URL，可以更方便快速开发
交互应用。


## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
