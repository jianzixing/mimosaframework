# 分页查询(paging)

在很多时候我们查询是需要查询分页数据，如果需要计算出总页数就需要当前查询条件的符合数，
如果使用list和count组合就可以得到想要的结果，我们提供了一个专门的方法用来组合查询。

```java
Query query = Criteria.query(TableUser.class)
                .eq(TableUser.level, 20)
                .limit(0, 20);
Paging paging = template.paging(query);
List<ModelObject> objects = paging.getObjects();
long count = paging.getCount();
```

其中getObjects可以获取当前查询的数据，getCount获取当前查询条件的总条数。

## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
