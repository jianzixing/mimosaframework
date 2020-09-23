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
* NOTHING级别，什么都不会做，如果什么都不做会导致数据库表不存在，需要手动创建表否则无法使用API。

* WARN级别，会将映射类和数据库表不一致的地方打印到控制台。

* CREATE级别(默认级别)，会将映射类和数据库表不存在的地方增加上，比如创建表，创建字段，如果存在就不会
做任何事，如果不存在则添加表或者字段。

* UPDATE级别(暂未实现)，仅开发时使用不能用于生产环境。如果表和字段不存在则添加，如果表和字段跟映射类配置
的不一致则更新数据库表或者字段。_注意：在使用时无法更新的字段会删除后再重新创建！_

* DROP级别(暂未实现)，强制和映射类一一对应，比如映射类的字段由5个变为3个则会主动删除丢失的字段，也包含UPDATE和CREATE
的功能。_谨慎使用！_


convert：自定义的数据库映射类字段和表字段实现。框架本身只实现了一种方法H2U，这个转换方法可以将驼峰命名
转换成下户线名称方式。

convertType：使用内置的字段命名转换实现，暂时只包含一种实现方式。
* H2U：将驼峰命名转换成下划线命名方式。

mapper：使用Mybatis的Mapper语法的配置文件路径，指定后配置文件和Mybatis的SQL配置文件使用方法相似。部分
配置不支持，详细参考[使用Mybatis的Mapper](./index.html#mapper.md)。

mappers：手动指定具体的文件路径。和mapper使用一致。

strategies：ID生成策略，如果使用数据库自增ID使用 strategy = AutoIncrementStrategy.class 即可。如果
需要自定义ID生成策略则需要在这里配置后，strategy设置为自定义的类名

showSQL：是否在控制台打印SQL日志。使用的是commons-logging，请按需配置。

defaultDataSource：配置一个默认数据源束，数据源束是包一个主数据源和多个从数据源的组合。

dataSourceWrapper：配置多个数据源束，必须指定数据源束名称。


## xml配置文件示例

#### 1、最简单的使用方式，只配置扫描的包和一个默认数据源。

* xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mimosa name="mimosa_test" description="测试用的配置">
    <mapping scan="tables"/>
    <datasource>
        <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
        <property name="driverClassName">com.mysql.jdbc.Driver</property>
        <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true
        </property>
        <property name="username">root</property>
        <property name="password">12345</property>
        <!--初始化的连接数-->
        <property name="initialSize" value="5"/>
        <property name="maxActive" value="10000"/>
        <property name="maxWait" value="1000"/>
        <property name="validationQuery" value="select 1"/>
        <property name="testWhileIdle" value="true"/>
        <property name="timeBetweenEvictionRunsMillis" value="3600000"/>
        <property name="minEvictableIdleTimeMillis" value="18000000"/>
        <property name="testOnBorrow" value="true"/>
    </datasource>
</mimosa>

```

* Spring配置

```xml
<bean name="factory" class="org.mimosaframework.orm.spring.SpringMimosaSessionFactory">
    <property name="scanPackage" value="tables"/>
    <property name="dataSource" ref="ds"/>
</bean>
<bean name="template" class="org.mimosaframework.orm.spring.SpringMimosaSessionTemplate">
    <property name="factory" ref="factory"/>
</bean>
```


#### 2、配置扫描的映射类的包，并且配置映射级别，增加扫描包之外的映射类。这里会自动将扫描的包和手动配置的映射类整合到一起。

* xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mimosa name="mimosa_test" description="测试用的配置">
    <mapping scan="tables" level="CREATE">
        <value>tables.TableUser</value>
        <value>xmlcontext.TableAddition</value>
    </mapping>

    <datasource>
        <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
        <property name="driverClassName">com.mysql.jdbc.Driver</property>
        <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true
        </property>
        <property name="username">root</property>
        <property name="password">12345</property>
        <!--初始化的连接数-->
        <property name="initialSize" value="5"/>
        <property name="maxActive" value="10000"/>
        <property name="maxWait" value="1000"/>
        <property name="validationQuery" value="select 1"/>
        <property name="testWhileIdle" value="true"/>
        <property name="timeBetweenEvictionRunsMillis" value="3600000"/>
        <property name="minEvictableIdleTimeMillis" value="18000000"/>
        <property name="testOnBorrow" value="true"/>
    </datasource>
</mimosa>
```

* Spring配置

```xml
<bean name="factory" class="org.mimosaframework.orm.spring.SpringMimosaSessionFactory">
    <property name="scanPackage" value="tables"/>
    <property name="dataSource" ref="ds"/>
    <property name="mappingLevel" value="CREATE"/>
    <property name="mappingClasses">
        <set>
            <value>tables.TableUser</value>
            <value>xmlcontext.TableAddition</value>
        </set>
    </property>
    <property name="showSQL" value="true"/>
</bean>
<bean name="template" class="org.mimosaframework.orm.spring.SpringMimosaSessionTemplate">
    <property name="factory" ref="factory"/>
</bean>
```


#### 3、配置自定义表名或者字段转换器，可以配置参数或者不配置参数。只需要实现MappingNamedConvert接口即可。

* xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mimosa name="mimosa_test" description="测试用的配置">
    <mapping level="CREATE">
        <value>tables.TableUser</value>
    </mapping>

    <!--不带参数实例化-->
    <!--<convert class="xmlcontext.TestConvert"/>-->

    <!--带参数实例化-->
    <convert class="xmlcontext.TestConvert">
        <property name="pm">abc</property>
    </convert>

    <datasource>
        <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
        <property name="driverClassName">com.mysql.jdbc.Driver</property>
        <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true
        </property>
        <property name="username">root</property>
        <property name="password">12345</property>
        <!--初始化的连接数-->
        <property name="initialSize" value="5"/>
        <property name="maxActive" value="10000"/>
        <property name="maxWait" value="1000"/>
        <property name="validationQuery" value="select 1"/>
        <property name="testWhileIdle" value="true"/>
        <property name="timeBetweenEvictionRunsMillis" value="3600000"/>
        <property name="minEvictableIdleTimeMillis" value="18000000"/>
        <property name="testOnBorrow" value="true"/>
    </datasource>
</mimosa>

```

* Spring配置

```xml
<bean name="factory" class="org.mimosaframework.orm.spring.SpringMimosaSessionFactory">
    <property name="dataSource" ref="ds"/>
    <property name="mappingLevel" value="CREATE"/>
    <property name="mappingClasses">
        <set>
            <value>tables.TableUser</value>
        </set>
    </property>

    <property name="convert">
        <bean class="xmlcontext.TestConvert">
            <property name="pm" value="xxx"/>
        </bean>
    </property>
</bean>
<bean name="template" class="org.mimosaframework.orm.spring.SpringMimosaSessionTemplate">
    <property name="factory" ref="factory"/>
</bean>
```


#### 4、使用自带的字段名称转换器，包含两种，第一种是默认的DEFAULT，第二种是H2U驼峰下户线转换。

* xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mimosa name="mimosa_test" description="测试用的配置">
    <mapping level="CREATE">
        <value>tables.TableUser</value>
    </mapping>

    <!--使用内置转换器-->
    <!--<convert name="H2U"/>-->
    <convert name="DEFAULT"/>

    <datasource>
        <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
        <property name="driverClassName">com.mysql.jdbc.Driver</property>
        <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true
        </property>
        <property name="username">root</property>
        <property name="password">12345</property>
        <!--初始化的连接数-->
        <property name="initialSize" value="5"/>
        <property name="maxActive" value="10000"/>
        <property name="maxWait" value="1000"/>
        <property name="validationQuery" value="select 1"/>
        <property name="testWhileIdle" value="true"/>
        <property name="timeBetweenEvictionRunsMillis" value="3600000"/>
        <property name="minEvictableIdleTimeMillis" value="18000000"/>
        <property name="testOnBorrow" value="true"/>
    </datasource>
</mimosa>

```

* Spring配置

```xml
<bean name="factory" class="org.mimosaframework.orm.spring.SpringMimosaSessionFactory">
    <property name="dataSource" ref="ds"/>
    <property name="mappingLevel" value="CREATE"/>
    <property name="mappingClasses">
        <set>
            <value>tables.TableUser</value>
        </set>
    </property>

    <property name="convertType" value="H2U"/>
    <!-- <property name="convertType" value="DEFAULT"/>-->
</bean>
<bean name="template" class="org.mimosaframework.orm.spring.SpringMimosaSessionTemplate">
    <property name="factory" ref="factory"/>
</bean>
```


#### 5、配置自定义的mapper路径，mapper是置顶的**包名称**，比如 com.mimosaframework.xxx
mappers指定的mapper文件路径(必须.xml结尾)

* xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mimosa name="mimosa_test" description="测试用的配置">
    <mapping level="CREATE">
        <value>tables.TableUser</value>
    </mapping>

    <!--使用内置转换器-->
    <convert name="H2U"/>

    <!--这里是要扫描的包，只支持单层扫描-->
    <mapper value="mapper1"/>
    <!--这里需要是资源的全路径-->
    <mappers>
        <value>/mapper2/user2_mapper.xml</value>
    </mappers>

    <datasource>
        <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
        <property name="driverClassName">com.mysql.jdbc.Driver</property>
        <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true
        </property>
        <property name="username">root</property>
        <property name="password">12345</property>
        <!--初始化的连接数-->
        <property name="initialSize" value="5"/>
        <property name="maxActive" value="10000"/>
        <property name="maxWait" value="1000"/>
        <property name="validationQuery" value="select 1"/>
        <property name="testWhileIdle" value="true"/>
        <property name="timeBetweenEvictionRunsMillis" value="3600000"/>
        <property name="minEvictableIdleTimeMillis" value="18000000"/>
        <property name="testOnBorrow" value="true"/>
    </datasource>
</mimosa>
```

* Spring配置

```xml
<bean name="factory" class="org.mimosaframework.orm.spring.SpringMimosaSessionFactory">
    <property name="dataSource" ref="ds"/>
    <property name="mappingLevel" value="CREATE"/>
    <property name="mappingClasses">
        <set>
            <value>tables.TableUser</value>
        </set>
    </property>

    <property name="convertType" value="H2U"/>

    <property name="mapper" value="mapper1"/>
    <property name="mappers">
        <value>/mapper2/user2_mapper.xml</value>
    </property>
</bean>
<bean name="template" class="org.mimosaframework.orm.spring.SpringMimosaSessionTemplate">
    <property name="factory" ref="factory"/>
</bean>
```


#### 6、自定义ID生成策略，strategy可以可以带参数也可以不带参数。

**这里配置是先初始化，一般情况下需要额外的配置或者使用spring初始化才会这么做。
如果不在这里配置已经定义好strategy的实现，那么在表上指定后一样可以使用。**

* xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mimosa name="mimosa_test" description="测试用的配置">
    <mapping level="CREATE">
        <value>xmlcontext.TableAddition</value>
    </mapping>

    <!--使用内置转换器-->
    <convert name="H2U"/>

    <strategies>
        <strategy class="xmlcontext.TestStrategy">
            <property name="pm" value="177.0.0.1"/>
        </strategy>
    </strategies>

    <datasource>
        <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
        <property name="driverClassName">com.mysql.jdbc.Driver</property>
        <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true
        </property>
        <property name="username">root</property>
        <property name="password">12345</property>
        <!--初始化的连接数-->
        <property name="initialSize" value="5"/>
        <property name="maxActive" value="10000"/>
        <property name="maxWait" value="1000"/>
        <property name="validationQuery" value="select 1"/>
        <property name="testWhileIdle" value="true"/>
        <property name="timeBetweenEvictionRunsMillis" value="3600000"/>
        <property name="minEvictableIdleTimeMillis" value="18000000"/>
        <property name="testOnBorrow" value="true"/>
    </datasource>
</mimosa>

```

* Spring配置

```xml
<bean name="factory" class="org.mimosaframework.orm.spring.SpringMimosaSessionFactory">
    <property name="dataSource" ref="ds"/>
    <property name="mappingLevel" value="CREATE"/>
    <property name="mappingClasses">
        <set>
            <value>tables.TableUser</value>
            <value>xmlcontext.TableAddition</value>
        </set>
    </property>

    <property name="convertType" value="H2U"/>

    <property name="strategies">
        <bean class="xmlcontext.TestStrategy">
            <property name="pm" value="xxx"/>
        </bean>
    </property>
</bean>
<bean name="template" class="org.mimosaframework.orm.spring.SpringMimosaSessionTemplate">
    <property name="factory" ref="factory"/>
</bean>
```


#### 7、是否打印SQL语句，使用的是commons-logging

* xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mimosa name="mimosa_test" description="测试用的配置">
    <mapping level="CREATE">
        <value>tables.TableUser</value>
    </mapping>

    <!--使用内置转换器-->
    <convert name="H2U"/>

    <format showSql="true"/>

    <datasource>
        <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
        <property name="driverClassName">com.mysql.jdbc.Driver</property>
        <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true
        </property>
        <property name="username">root</property>
        <property name="password">12345</property>
        <!--初始化的连接数-->
        <property name="initialSize" value="5"/>
        <property name="maxActive" value="10000"/>
        <property name="maxWait" value="1000"/>
        <property name="validationQuery" value="select 1"/>
        <property name="testWhileIdle" value="true"/>
        <property name="timeBetweenEvictionRunsMillis" value="3600000"/>
        <property name="minEvictableIdleTimeMillis" value="18000000"/>
        <property name="testOnBorrow" value="true"/>
    </datasource>
</mimosa>

```

* Spring配置

```xml
<bean name="factory" class="org.mimosaframework.orm.spring.SpringMimosaSessionFactory">
    <property name="dataSource" ref="ds"/>
    <property name="mappingLevel" value="CREATE"/>
    <property name="mappingClasses">
        <set>
            <value>tables.TableUser</value>
            <value>xmlcontext.TableAddition</value>
        </set>
    </property>

    <property name="showSQL" value="true"/>
</bean>
<bean name="template" class="org.mimosaframework.orm.spring.SpringMimosaSessionTemplate">
    <property name="factory" ref="factory"/>
</bean>
```


#### 8、配置默认的数据源束

* xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mimosa name="mimosa_test" description="测试用的配置">
    <mapping level="CREATE">
        <value>tables.TableUser</value>
    </mapping>

    <!--使用内置转换器-->
    <convert name="H2U"/>
    <format showSql="true"/>

    <datasource master="master" slaves="slaveName1:slave1,slaveName2:slave2"/>

    <dslist>
        <ds name="master">
            <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
            <property name="driverClassName">com.mysql.jdbc.Driver</property>
            <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true</property>
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
            <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true</property>
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
            <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true</property>
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

* Spring配置

```xml
<bean name="factory" class="org.mimosaframework.orm.spring.SpringMimosaSessionFactory">
    <property name="mappingLevel" value="CREATE"/>
    <property name="mappingClasses">
        <set>
            <value>tables.TableUser</value>
            <value>xmlcontext.TableAddition</value>
        </set>
    </property>

    <property name="defaultDataSource">
        <bean class="org.mimosaframework.orm.MimosaDataSource">
            <property name="master" ref="ds"/>
        </bean>
    </property>
</bean>
<bean name="template" class="org.mimosaframework.orm.spring.SpringMimosaSessionTemplate">
    <property name="factory" ref="factory"/>
</bean>
```


#### 9、配置多个数据源束，必须包含一个default的数据源束。

* xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<mimosa name="mimosa_test" description="测试用的配置">
    <mapping level="CREATE">
        <value>tables.TableUser</value>
    </mapping>

    <!--使用内置转换器-->
    <convert name="H2U"/>
    <format showSql="true"/>

    <wrappers>
        <!--default表示是默认数据源束-->
        <wrapper name="default" master="master" slaves="slave1:slave1,slave2:slave2"/>
        <wrapper name="c1" master="master" slaves="slave1:slave1,slave2:slave2"/>
        <wrapper name="c2" master="master" slaves="slave1:slave1,slave2:slave2"/>
    </wrappers>

    <dslist>
        <ds name="master">
            <property name="dataSourceClass">com.alibaba.druid.pool.DruidDataSource</property>
            <property name="driverClassName">com.mysql.jdbc.Driver</property>
            <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true</property>
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
            <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true</property>
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
            <property name="url">jdbc:mysql://localhost:3306/mimosa?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true</property>
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

* Spring配置

```xml
<bean name="factory" class="org.mimosaframework.orm.spring.SpringMimosaSessionFactory">
    <property name="mappingLevel" value="CREATE"/>
    <property name="mappingClasses">
        <set>
            <value>tables.TableUser</value>
            <value>xmlcontext.TableAddition</value>
        </set>
    </property>

    <property name="dataSources">
        <bean class="org.mimosaframework.orm.MimosaDataSource">
            <property name="name" value="default"/>
            <property name="master" ref="ds"/>
        </bean>
        <bean class="org.mimosaframework.orm.MimosaDataSource">
            <property name="name" value="ds1"/>
            <property name="master" ref="ds"/>
        </bean>
        <bean class="org.mimosaframework.orm.MimosaDataSource">
            <property name="name" value="ds2"/>
            <property name="master" ref="ds"/>
        </bean>
    </property>
</bean>
<bean name="template" class="org.mimosaframework.orm.spring.SpringMimosaSessionTemplate">
    <property name="factory" ref="factory"/>
</bean>
```



## 
Copyright © 2018-2019 杨安康(yak1992@foxmail.com)
