# 使用主从数据库

很多时候为了减轻数据库查询压力会做主从，数据库主从配置根据每个数据库不同的特点配置即可。

如果做数据库主从则在配置数据源时必须配置多个数据源，且不再使用dataSource参数配置，使用
defaultDataSource参数配置一个数据源束，一个数据源束包含一个主库和多个从库。

每个库的数据库连接池DataSource配置好之后使用。

Spring中配置defaultDataSource参数的配置方法，这里包含一个master和三个slave，每个数据源束的
从数据库都需要定义个名称，这个名称可以在查询时指定某个名称的数据源。

```xml
<property name="defaultDataSource">
    <bean class="org.mimosaframework.orm.MimosaDataSource">
        <property name="name" value="default"/>
        <property name="master" ref="ds"/>
        <property name="slaves">
            <map>
                <entry key="slave_1" value-ref="ds_1"/>
                <entry key="slave_2" value-ref="ds_2"/>
                <entry key="slave_3" value-ref="ds_3"/>
            </map>
        </property>
    </bean>
</property>
```


## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
