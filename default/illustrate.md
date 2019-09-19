# API及配送参数说明

创建SessionTemplate后就可以开始你的coding之旅。

SessionTemplate包含了主要的三部分内容，第一部分是事务处理方法，第二部分是扩展服务处理方法，第三部分就是
最主要的部分数据库CURD处理方法。

**注意：这里的事务处理是独立的，如果您委托给Spring管理了事务，请勿再使用SessionTemplate上的事务。**

## 配置参数说明，Spring为例

以Spring配置为例org.mimosaframework.orm.spring.SpringMimosaSessionFactory所需要参数：

dataSource：默认的链接数据库的数据源，使用Spring初始化的DataSource引用即可。必须参数！

scanPackage：指定扫描的包名称，会自动匹配@Table注解的枚举类，扫描解析后判断数据库是否存在该映射类的
表，然后将表信息一一匹配。重要参数，和mappingClasses必须至少选择一个使用。

mappingClasses：直接指定要解析的映射类名称，和scanPackage最后会汇总到一起。

applicationName：当前应用的名称。非必须

applicationDetail：当前应用的描述。非必须

mappingLevel：数据映射级别，分为NOTHING,CREATE,UPDATE,DROP几个级别。
* NOTHING级别，什么都不会做，只会将映射类和数据库表不一致的地方打印到控制台。
* CREATE级别(默认级别)，会将映射类和数据库表不存在的地方增加上，比如创建表，创建字段，如果存在就不会
做任何事，如果不存在则添加表或者字段。
* UPDATE级别(暂未实现)，仅开发时使用不能用于生产环境。如果表和字段不存在则添加，如果表和字段跟映射类配置
的不一致则更新数据库表或者字段。_注意：在使用时无法更新的字段会删除后再重新创建！_
* DROP级别(暂未实现)，强制和映射类一一对应，比如映射类由5个变为3个则会主动删除丢失的表，也包含UPDATE和CREATE
的功能。_谨慎使用！由于很多数据库本身存在的表也会删除！所以不建议使用!_


convert：自定义的数据库映射类字段和表字段实现。框架本身只实现了一种方法H2U，这个转换方法可以将驼峰命名
转换成下户线名称方式，**注意的是如果自定义实现则必须保证字段本身可以正向转换也可以逆向转换。比如userName
转换成user_name，也可以从user_name转换成userName。这个命名也提现在映射类枚举的字段上，请勿命名连续
大写的方式，否则转换结果比较难看。比如，CURDOpt 转换后 c_u_r_d_opt，这种方式一般无法接受。**

convertType：使用内置的字段命名转换实现，暂时只包含一种实现方式。
* H2U：将驼峰命名转换成下划线命名方式。**注意由于可以正逆向使用，请勿命名连续大写方式。**

mapper：使用Mybatis的Mapper语法的配置文件路径，指定后配置文件和Mybatis的SQL配置文件使用方法相似。部分
配置不支持，详细参考[使用Mybatis的Mapper](./index.html#mapper.md)。

mappers：手动指定具体的文件路径。和mapper使用一致。

strategies：ID生成策略，如果使用数据库自增ID使用 strategy = AutoIncrementStrategy.class 即可。如果
需要自定义ID生成策略则需要在这里配置后，strategy设置为自定义的类名

showSQL：是否在控制台打印SQL日志。使用的是commons-logging，请按需配置。

defaultDataSource：配置一个数据源束，数据源束是包一个主数据源和多个从数据源的组合。


## 配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mimosa name="mimosa_test" description="测试用的配置">
    <convert name="H2U"/>
    <!--<convert class="org.mimosaframework.core.asm.AnnotationVisitor"/>-->
    <mapping scan="org.mimosaframework.orm.table2" level="">
        <value>org.mimosaframework.orm.tables.CharPool</value>
        <value>org.mimosaframework.orm.tables.CharPool</value>
        <value>org.mimosaframework.orm.tables.CharPool</value>
    </mapping>
    <format showSql="true"/>
    <datasource master="master" slaves="slave1:slave1,slave2:slave2"/>

    <mapper value="/"/>

    <wrappers>
        <wrapper name="default" master="master" slaves="slave1:slave1,slave2:slave2"/>
        <wrapper name="c1" master="master" slaves="slave1:slave1,slave2:slave2"/>
        <wrapper name="c2" master="master" slaves="slave1:slave1,slave2:slave2"/>
    </wrappers>

    <strategies>
        <strategy class="org.mimosaframework.orm.tables.TableStudent" wrapper="default"/>
    </strategies>

    <dslist>
        <ds name="master">
            <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
            <property name="driverClassName">com.mysql.jdbc.Driver</property>
            <property name="url">jdbc:mysql://localhost:3306/dbt4?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true</property>
            <property name="username">root</property>
            <property name="password">12345</property>
            <!--初始化的连接数-->
            <property name="initialSize" value="5"/>
            <property name="maxActive" value="100"/>
            <property name="maxWait" value="1000"/>
            <property name="validationQuery" value="select 1"/>
            <property name="testWhileIdle" value="true"/>
            <property name="timeBetweenEvictionRunsMillis" value="3600000"/>
            <property name="minEvictableIdleTimeMillis" value="18000000"/>
            <property name="testOnBorrow" value="true"/>
        </ds>
        <ds name="slave1">
            <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
            <property name="driverClassName">com.mysql.jdbc.Driver</property>
            <property name="url">jdbc:mysql://localhost:3306/dbt4?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true</property>
            <property name="username">root</property>
            <property name="password">12345</property>
            <!--初始化的连接数-->
            <property name="initialSize" value="5"/>
            <property name="maxActive" value="100"/>
            <property name="maxWait" value="1000"/>
            <property name="validationQuery" value="select 1"/>
            <property name="testWhileIdle" value="true"/>
            <property name="timeBetweenEvictionRunsMillis" value="3600000"/>
            <property name="minEvictableIdleTimeMillis" value="18000000"/>
            <property name="testOnBorrow" value="true"/>
        </ds>
        <ds name="slave2">
            <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
            <property name="driverClassName">com.mysql.jdbc.Driver</property>
            <property name="url">jdbc:mysql://localhost:3306/dbt4?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true</property>
            <property name="username">root</property>
            <property name="password">12345</property>
            <!--初始化的连接数-->
            <property name="initialSize" value="5"/>
            <property name="maxActive" value="100"/>
            <property name="maxWait" value="1000"/>
            <property name="validationQuery" value="select 1"/>
            <property name="testWhileIdle" value="true"/>
            <property name="timeBetweenEvictionRunsMillis" value="3600000"/>
            <property name="minEvictableIdleTimeMillis" value="18000000"/>
            <property name="testOnBorrow" value="true"/>
        </ds>
    </dslist>
</mimosa>
```

## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
