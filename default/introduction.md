## 一、MimosaFramework简介
MimosaFramework是一组框架组合，主要功能是提供数据库读写的工具，和mvc扩展工具。

框架分为三部分，第一部分mimosa-core包含一些基本的工具类以及ModelObject的JSON操作库(使用FastJson为基础)，以及cglib和ognl表达式等开源库(方便独立使用)。
第二部分mimosa-orm是数据库读写操作的核心库(依赖mimosa-core)，用过API操作数据库，通过注解类自动创建数据库表，并且可以提供类似Mybatis的xml配置SQL的方式执行SQL语句。
第三部分mimosa-mvc是在spring-mvc基础上扩展的方便Ajax交互的工具(依赖mimosa-core)，可以简化后台Ajax交互复杂度。

总的来说MimosaFramework可以给我们提供一个方便快捷开发的半自动框架。MimosaFramework并不能帮我们做全部的事情，很多时候依旧需要开发者本身去处理一些事情。

**注意：该框架在我们的使用范围内是稳定的，一般开发者面临的情况非常多，因此如果有任何问题请在
[issues](https://github.com/jianzixing/mimosaframework/issues)
里提出，我们会在未来版本中更正或者修改增加功能。**

_感谢您使用我们的框架，如果有任何商业需求可以[联系我们(北京简子行科技有限公司)](https://www.jianzixing.com.cn)我们将竭诚为您服务。_



## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
