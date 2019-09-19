# 数据更新(update)

更新数据有三种方法，update单个、update多个、update条件更新。

注意：更新时如果想设置某个字段为空则可以使用Keyword.NULL枚举。

第一种方法update单个，使用时必须在ModelObject设置要更新对象的主键。该方法会根据
主键更新ModelObject中已有的值，没有的值则不会更新，如果需要更某个字段为空则，在
ModelObject对象中设置该字段为null或者使用Keyword.NULL即可。

```java
ModelObject object = new ModelObject(TableUser.class);
...
object.put(TableUser.realName, null);
template.update(object);
```

第二种方法update多个，使用时必须保证更新的多个ModelObject是同一个表中的且每个
对象的主键值都存在。

```java
List<ModelObject> objects = new ArrayList<ModelObject>();
...
template.update(objects);
```

第三中方法update条件更新，可以根据需求自由更新表字段。
```java
Update update = Criteria.update(TableUser.class)
                .eq(TableUser.id, 20) // 更新条件
                .value(TableUser.realName, "安康"); // 更新的字段
template.update(update);
```

或者使用链式编程

```java
template.update(TableUser.class)
                .eq(TableUser.id, 20) // 更新条件
                .value(TableUser.realName, "安康") // 更新的字段
                .update(); // 需要最后调用更新方法
```


## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
