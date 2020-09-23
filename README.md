<img src="https://repository-images.githubusercontent.com/207211209/39fc7180-d94f-11e9-8866-d05f91f10f31" width="600" align="center"/>

## 一、MimosaFramework简介
MimosaFramework是一组框架组合，主要功能是提供数据库读写的工具，和mvc扩展工具。

框架分为三部分，第一部分mimosa-core包含一些基本的工具类以及ModelObject的JSON操作库(使用FastJson为基础)，以及cglib和ognl表达式等开源库(方便独立使用)。
第二部分mimosa-orm是数据库读写操作的核心库(依赖mimosa-core)，用过API操作数据库，通过注解类自动创建数据库表，并且可以提供类似Mybatis的xml配置SQL的方式执行SQL语句。
第三部分mimosa-mvc是在spring-mvc基础上扩展的方便Ajax交互的工具(依赖mimosa-core)，可以简化后台Ajax交互复杂度。

总的来说MimosaFramework可以给我们提供一个方便快捷开发的半自动框架。MimosaFramework并不能帮我们做全部的事情，很多时候依旧需要开发者本身去处理一些事情。

框架的参考文档点击这里 [参考文档](https://mimosaframework.org) 访问查看MimosaFramework详细教程。

**注意：该框架在我们的使用范围内是稳定的，一般开发者面临的情况非常多，因此如果有任何问题请在
[issues](https://github.com/jianzixing/mimosaframework/issues)
里提出，我们会在未来版本中更正或者修改增加功能。**

## 二、开始使用 mimosa-orm 框架

##### 第一步、创建一个maven项目，并且引用jar包

```xml
<dependencies>
    <!--必须引入的核心包-->
    <dependency>
        <groupId>org.mimosaframework.core</groupId>
        <artifactId>mimosa-core</artifactId>
        <version>3.3.7</version>
    </dependency>

    <!--必须引入的核心包-->
    <dependency>
        <groupId>org.mimosaframework.orm</groupId>
        <artifactId>mimosa-orm</artifactId>
        <version>3.3.7</version>
    </dependency>

    <!--额外依赖的日志包-->
    <dependency>
        <groupId>commons-logging</groupId>
        <artifactId>commons-logging</artifactId>
        <version>1.2</version>
    </dependency>

    <!--数据库连接池包-->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.10</version>
    </dependency>

    <!--Mysql驱动包-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.11</version>
    </dependency>
</dependencies>
```

以上jar包除了mimosa-core、mimosa-orm必须引入外，其他的依照实际情况引入。

##### 第二步、创建映射表包名称 com.study.test.tables 并创建映射类

```java
package com.study.test.tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.util.Date;

@Table
public enum TableUser {
    @Column(pk = true, type = long.class, strategy = AutoIncrementStrategy.class)
    id,
    @Column(length = 64)
    userName,
    @Column(length = 64)
    password,
    @Column(length = 30)
    realName,
    @Column(type = int.class)
    age,
    @Column(type = int.class, defaultValue = "2")
    level,
    @Column(length = 20)
    address,
    @Column(type = Date.class)
    createdTime,
    @Column(timeForUpdate = true)
    modifiedDate
}
```

PS:映射类使用枚举类，类上必须使用注解 @Table 作为标识，每个枚举对象使用 @Column 修饰配置表字段信息。

##### 第三步、创建框架配置文件 mimosa.xml 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mimosa name="mimosa_test" description="测试用的配置">
    <convert name="H2U"/>
    <mapping scan="com.study.test.tables"/>
    <format showSql="true"/>
    <datasource wrapper="default"/>

    <wrappers>
        <wrapper name="default" master="master"/>
    </wrappers>

    <dslist>
        <ds name="master">
            <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
            <property name="driverClassName">com.mysql.jdbc.Driver</property>
            <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true
            </property>
            <property name="username">root</property>
            <property name="password">12345</property>
            <!--初始化的连接数-->
            <property name="initialSize" value="5"/>
            <property name="maxActive" value="10000"/>
            <property name="maxWait" value="60000"/>
            <property name="validationQuery" value="select 1"/>
            <property name="testWhileIdle" value="true"/>
            <property name="timeBetweenEvictionRunsMillis" value="3600000"/>
            <property name="minEvictableIdleTimeMillis" value="18000000"/>
            <property name="testOnBorrow" value="true"/>
        </ds>
    </dslist>
</mimosa>
```
convert: 表字段映射驼峰转下户线配置

mapping: 扫描的包名称，将注解@Table的类作为映射类并创建表

format: 配置一些特殊信息，比如是否打印SQL到控制台

wrappers: 数据源束，一个数据源束分为一个主库和若干个从库组成，主库不能为空，从库可以没有

datasource: 当前使用的数据源束

dslist: 数据源列表，可以配置多个数据源供数据源束使用

##### 第四步、初始化并使用Mimosa框架

```java
package com.study.test;

import com.study.test.tables.TableUser;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;

import java.util.Date;

public class Start {
    public static void main(String[] args) throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/mimosa.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);

        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.id, 20);
        object.put(TableUser.userName, "yangankang_test_save_n_2");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "北京xxx科技有限公司");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        template.saveAndUpdate(object);
    }
}
```

结束: 接下来你可以看到数据库中会插入一条记录。以上是mimosa-orm的基本用法，详细用法请到[官方文档](https://mimosaframework.org)查看


## 三、在Spring中使用 mimosa-orm 框架

#### 在Spring的配置文件中配置如下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd"
       default-autowire="byName">

    <bean id="ds" class="com.alibaba.druid.pool.DruidDataSource" destroy-method="close">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
        <property name="url"
                  value="jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true"/>
        <property name="username" value="root"/>
        <property name="password" value="12345"/>
        <!--初始化的连接数-->
        <property name="initialSize" value="5"/>
        <property name="maxActive" value="10000"/>
        <property name="maxWait" value="1000"/>
        <property name="validationQuery" value="select 1"/>
        <property name="testWhileIdle" value="true"/>
        <property name="timeBetweenEvictionRunsMillis" value="3600000"/>
        <property name="minEvictableIdleTimeMillis" value="18000000"/>
        <property name="testOnBorrow" value="true"/>
    </bean>

    <bean name="factory" class="org.mimosaframework.orm.spring.SpringMimosaSessionFactory">
        <property name="applicationName" value="mimosa-spring"/>
        <property name="scanPackage" value="tables"/>
        <property name="dataSource" ref="ds"/>
        <property name="convertType" value="H2U"/>
        <property name="showSQL" value="true"/>
    </bean>
    <bean name="template" class="org.mimosaframework.orm.spring.SpringMimosaSessionTemplate">
        <property name="factory" ref="factory"/>
    </bean>
</beans>
```

Spring代码使用如下:

```java
package session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.orm.criteria.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tables.TableUser;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-config.xml")
public class SpringContextTesting {

    @Autowired
    SessionTemplate template;

    @Test
    public void test() {
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang2");
        object.put(TableUser.password, "1234562");
        object.put(TableUser.realName, "北京简子行科技");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        template.save(object);
    }
}
```
以上是在单元测试中使用，请根据实际情况使用 SessionTemplate 即可。


## 四、在 spring mvc 中使用 mimosa-mvc

使用时与 spring mvc 差别不大，首先将springmvc的配置文件中的RequestHandlerMapping和RequestHandlerAdapter
替换成 mimosa-mvc 提供的RequestHandlerMapping和RequestHandlerAdapter。然后配置好要扫描的java包(prefixs参数)

**注意，不能使用mvc:annotation-driven和自定的一起使用否则 spring mvc 自动覆盖自定义的Mapping和Adapter。**

然后在Java类上添加注解@APIController，之后在方法上添加 @Printer 即可。

mimosa-mvc会自动根据配置生成访问的url = /<prefixs配置的value值>/<@APIController修饰的类名去掉Controller关键字>
/<@Printer修饰的方法名>.<web.xml中配置拦截的后缀>

比如以下控制器：
```java
@APIController
public class HelloController {

    @Printer
    public String getName(String name) {
        return "Hello " + name;
    }
    
}
```

可以访问 /admin/hello/get_name.html 地址即可获取到想要的值。


#### 在 spring mvc 中添加配置信息
```xml
<!--只能使用自己的处理器且不能使用mvc:annotation-driven注解-->
<bean class="org.mimosaframework.springmvc.MimosaRequestHandlerMapping">
    <property name="curdImplementClass" value="com.jianzixing.webapp.modules.CURDService"/>
    <property name="sessionTemplate" ref="mimosaSessionTemplate"/>
    <property name="prefixs">
        <map>
            <entry key="com.jianzixing.webapp.modules" value="/admin"/>
            <entry key="com.jianzixing.webapp.app" value="/app"/>
            <entry key="com.jianzixing.webapp.web" value="/web"/>
        </map>
    </property>
    <property name="replaces">
        <map>
            <entry key="com.jianzixing.webapp.app" value="App;"/>
            <entry key="com.jianzixing.webapp.web" value="Web;"/>
        </map>
    </property>
</bean>
<!-- 在处理ajax跨域文件上传时,会先请求一个OPTIONS的请求,由于自定义的Adapter并不支持所以主动注册一下自带的Http Adapter -->
<bean class="org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter"/>
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

以上配置中最重要的是
org.mimosaframework.springmvc.MimosaRequestHandlerMapping
org.mimosaframework.springmvc.MimosaRequestHandlerAdapter
这两个自定义映射适配器

curdImplementClass：如果有一样的操作可以统一使用一个天删改查处理方法

sessionTemplate：统一的天删改查处理时需要的SessionTemplate

prefixs：必须配置的，指定扫描那些包并且这些包对应的url前缀

replaces：将url前缀的某些单词替换成想要的单词，比如 Web;w 就是将Web替换成w


## 五、mimosa框架的特色功能

* 使用API方式操作数据库，在常用操作上更简单快速方便。
* 自动通过映射类生成表及表字段，简化频繁操作数据库烦恼
* 可以使用Mybatis的Mapper配置支持，通过ognl表达式和Mybatis的xml处理来执行SQL语句

## 六、历史版本

版本v3.4.2 (待更新)
* update未设置tableClass提示

版本 v3.4.1
* 增加日志输出国际化
* 增加Builder类，跨平台查询组装SQL
* 更改ModelObject的API限制键值类型

版本 v3.4.0
* 增加Session拦截器AbstractInterceptSession类，可以自主控制Session执行。
Spring可以使用属性setInterceptSession，xml可以使用标签interceptSession。
* Query接口增加了getSubjoins方法，方便增加查询条件。(返回List增删无效)
* 唯一索引unique设置无效问题
* calculate方法查询，增加distinct函数支持

版本 v3.3.9
* 增加对数据库db2的支持
* 增加对数据sqlite3的支持
* 增加修改数据库字段类型支持，扩展统一部分字段类型
* 3.3.8保存时会移除自增ID主键数据，这个版本后允许保存自定义主键(自增ID主键)数据。
* checkAndThrowable自动清空空值，checkUpdateThrowable不再清空空值。

版本 v3.3.8
* 已更新

~~版本 v3.3.7~~
* 已废弃


##
Copyright © 2018-2020 杨安康 邮箱 yak1992@foxmail.com
