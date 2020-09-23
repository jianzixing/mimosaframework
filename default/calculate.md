# 执行函数(calculate)

指定函数只能在一个表中执行，且函数字段只允许使用一个字段。执行函数有以下几种

* SUM       求和函数
* COUNT     数量函数
* MAX       最大函数
* MIN       最小函数
* AVG       平均值函数

```java
ModelObject cal = template.calculate(Criteria.fun(TableUser.class)
                // 使用的函数和作用字段
                .addFunction(BasicFunction.MAX, TableUser.age)
                // 使用的函数和作用字段，并设置返回值别名
                .addFunction(BasicFunction.AVG, TableUser.age, "avg")
                // 查询条件
                .eq(TableUser.level, 20));

Double age = cal.getDoubleValue(TableUser.age);
Double avg = cal.getDoubleValue("avg");
```

执行后可以通过设置的别名(如果没有别名默认使用字段名称)获取结果。

单表执行使用范围有限，本身使用数据库计算大量数据是一个很耗费资源的行为，我们不建议这么做，
如果确实需要执行特殊函数，可以使用[自定义SQL语句](./index.html#custom_sql.md)
或者使用[使用Mybatis的Mapper](./index.html#mapper.md)的方式。

## 
Copyright © 2018-2019 杨安康(yak1992@foxmail.com)
