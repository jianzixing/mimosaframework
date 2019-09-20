# 数量查询(count)

查询数量只能查询一个表的数量，可以根据条件查询，**这里的Query和数据查询的Query是可以共用的，
多次使用不会互相影响**

这里如果使用subjoin无效，limit和order都是无效参数。

```java
Query query = Criteria.query(TableUser.class).eq(TableUser.level, 20);
long count = template.count(query);
```

## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
