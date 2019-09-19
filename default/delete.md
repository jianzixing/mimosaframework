# 数据删除(delete)

数据删除有四种方法，delete单个、delete多个、delete条件删除、delete主键，除了第三种
其它方法底层都是使用条件删除。

第一种delete单个，单个删除需要传入ModelObject对象且ModelObject对象中必须包含主键，

```java
ModelObject object = new ModelObject(TableUser.class);
...
template.delete(object);
```

第二种delete多个，删除多个ModelObject对象时需要每个ModelObject对象都包含主键值。

```java
List<ModelObject> objects = new ArrayList<ModelObject>();
...
template.delete(objects);
```

第三种delete条件删除，根据需要自由删除数据。

```java
Delete delete = Criteria.delete(TableUser.class)
                .eq(TableUser.id, 20); // 删除条件;
        template.delete(delete);
```

或者使用链式编程

```java
template.delete(TableUser.class)
                .eq(TableUser.id, 20) // 删除条件
                .delete(); // 需要最后调用一下删除方法
```


第四中主键删除，主键删除需要传入数据的主键值。

```java
template.delete(TableUser.class, 20);
```

## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
