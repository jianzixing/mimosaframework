# 数据保存(save)

保存方法有三种，save一个、saveAndUpdate、save多个。

第一种save也是最常用的方法。一般情况下如果设置主键的生成策略为AutoIncrementStrategy
表示使用数据库自增策略，如果save时数据中包含主键添加时会带上主键信息，这时需要保证主键
不会和数据库已存在数据重复。

```java
ModelObject object = new ModelObject(TableUser.class);
object.put(TableUser.userName, "yangankang_test_save_n_2");
object.put(TableUser.password, "123456");
object.put(TableUser.age, 25);
object.put(TableUser.level, 10);
object.put(TableUser.createdTime, new Date());
template.save(object);
```

第二种saveAndUpdate方法，使用时需会判断ModelObject中是否有主键，如果有主键数据则
使用update方法，如果没有主键则使用save方法

```java
ModelObject object = new ModelObject(TableUser.class);
object.put(TableUser.userName, "yangankang_test_save_n_2");
object.put(TableUser.password, "123456");
object.put(TableUser.age, 25);
object.put(TableUser.level, 10);
object.put(TableUser.createdTime, new Date());
template.saveAndUpdate(object);
```

第三种save多个，假设有批量添加需求则可以将需要添加的对象组成List然后调用save方法。
save一个List的时必须这个List的ModelObject的映射类都是一个，简单说就是批量添加
只能添加一个表。

```java
List<ModelObject> objects = new ArrayList<ModelObject>();
...
template.save(objects);
```


## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)