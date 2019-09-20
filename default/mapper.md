# 使用Mybatis的Mapper

如果在程序中使用AutoResult getAutonomously(SQLAutonomously sqlAuto)方法，那么每次修改
SQL都要重新部署程序，这样显然会变得复杂，所以使用另一种配置文件的方式会简化复杂度。

AutoResult getAutonomously(TAutonomously tAuto)方法使用的是配置文件配置SQL的方式，
在maven项目中可以把配置文件放到resources/mapper下，并在使用时配置参数 mapper 或者 mappers
指定初始化的配置文件的路径。

```java
TAutonomously tAutonomously = TAutonomously.newInstance();
tAutonomously.add("orderMapper.getOrders");
AutoResult result = template.getAutonomously(tAutonomously);
List<ModelObject> objects = result.getObjects();
```

使用时需要自定判断使用的是哪个mapper文件的哪个方法，orderMapper.getOrders表示使用
orderMapper.xml中的id为getOrders的语句。

以下是一个orderMapper.xml的配置文件。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//localhost//DTD Config 3.0//EN" "/mimosa-mapper.dtd">
<mapper>
    <sql id="getSearchOrderPublic">
        from t_order t1,t_user t2 where t1.user_id=t2.id and t2.user_name=#{userName}
    </sql>
    <select id="getSearchOrder">
        select t1.id as id
        <include refid="getSearchOrderPublic"/>
    </select>

    <select id="getSearchOrderCount">
        select count(1) as c
        <include refid="getSearchOrderPublic"/>
    </select>

    <select id="getOrders">
        select t1.id as id from t_order t1,t_order_goods t2
        where t1.id=t2.order_id
        <if test="uid!=null">
            and t1.user_id=#{uid}
        </if>
        <if test="keyword!=null">
            and (t1.number like #{keyword} or t2.goods_name like #{keyword} or t2.serial_number like #{keyword})
        </if>
        <if test="start!=null and limit!=null">
            limit #{start},#{limit}
        </if>
    </select>
</mapper>

```

mapper的配置文件和Mybatis的类似，具体参考Mybatis的写法。

返回值AutoResult参考[自定义SQL语句](./index.html#custom_sql.md)中的说明。



## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
