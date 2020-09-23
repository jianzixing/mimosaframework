# 数据查询(list)

查询方法有三种，get主键、get条件查询、list条件查询。

第一种get主键，需要传入一个主键值。

```java
ModelObject object = template.get(TableUser.class, 20);
```

第二种get条件查询，可以根据需要制定查询条件。get方法在永远返回数量只会小于等于1，如果
数据库有多条数据符合条件，也只会返回第一条。

```java
Query query = Criteria.query(TableUser.class)
                .eq(TableUser.id, 20); // 查询条件
ModelObject object = template.get(query);
```

使用链式编程

```java
ModelObject object = template.query(TableUser.class)
                .eq(TableUser.id, 20) // 查询条件
                .query();
```

第三种list条件查询，可以根据需要制定查询条件。可以指定查询条件的分页信息，排序信息等等。

```java
Query query = Criteria.query(TableUser.class)
                .eq(TableUser.level, 20)
                .limit(0, 10) // 分页数量
                .order(TableUser.id, false); // 使用id降序
List<ModelObject> objects = template.list(query);
```

使用链式编程

```java
List<ModelObject> objects = template.query(TableUser.class)
                .eq(TableUser.level, 20)
                .limit(0, 10)
                .order(TableUser.id, false)
                .list();
```

---


### 主表附表关联查询

Query是查询条件的包装类，只能针对一个表查询，这个表称为主表，如果我们需要查询这个主表的关联表
可以用subjoin查询出主表关联的附表信息。**可以理解为leftjoin查询**

```java
List<ModelObject> objects = template.query(TableUser.class)
                // 通过表TableOrder的userId和TableUser的id关联查询
                .subjoin(TableOrder.class).eq(TableOrder.userId, TableUser.id).query()
                .eq(TableUser.level, 20)
                .limit(0, 10)
                .order(TableUser.id, false)
                .list();
```

**注意：链式编程subjoin和query()方法必须成对出现**

上面的查询语句通过userId字段和id两个关联字段，查询TableUser关联的附表TableOrder的列表。
查询的数据会关联到每一条TableUser数据中，默认的附表在ModelObject的字段为附表名称，比如
以下是获取附表的数据列表：

```java
ModelObject object = objects.get(0);
List<ModelObject> orders = object.getArray(TableOrder.class);
```

或者 

```java
ModelObject object = objects.get(0);
List<ModelObject> orders = object.getArray(TableOrder.class.getName());
```

或者

```java
ModelObject object = objects.get(0);
List<ModelObject> orders = object.getArray("TableOrder");
```

### 自定义附表数据字段以及设置返回列表还是对象

有时我们需要自定义的附表字段名称只需要设置aliasName即可。

```java
List<ModelObject> objects = template.query(TableUser.class)
                // 通过表TableOrder的userId和TableUser的id关联查询
                .subjoin(TableOrder.class).eq(TableOrder.userId, TableUser.id)
                .aliasName("orders")  // 设置返回的别名
                .query()

                .eq(TableUser.level, 20)
                .limit(0, 10)
                .order(TableUser.id, false)
                .list();


ModelObject object = objects.get(0);
List<ModelObject> orders = object.getArray("orders");
```

如果需要返回一个对象而不是数组则可以设置single或者setMulti方法。

```java
List<ModelObject> objects = template.query(TableUser.class)
                .subjoin(TableOrder.class).eq(TableOrder.userId, TableUser.id)
                .aliasName("order")
                .single()  // 这只查询一个对象
                .query()

                .eq(TableUser.level, 20)
                .limit(0, 10)
                .order(TableUser.id, false)
                .list();


ModelObject object = objects.get(0);
ModelObject order = object.getModelObject("order");
```



## 
Copyright © 2018-2019 杨安康(yak1992@foxmail.com)
