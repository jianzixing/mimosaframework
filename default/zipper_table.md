# 拉链表(getZipperTable)

如果我们需要快速获取全部数据库数据，这时可以使用拉链表。

拉链表使用jdbc的特殊设置，请根据实际情况使用：

```java
statement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
statement.setFetchSize(fetchSize);
statement.setFetchDirection(ResultSet.FETCH_REVERSE);
```

该方式有使用局限，某些情况下无法使用。

* 假如你使用MySQL数据库及驱动,请注意数据库和驱动版本大于5,并且如果你没有在URL上设置参数useCursorFetch=true则设置fetchSize数属于无效行为
* 其他数据库需要支持才可以使用

## 
Copyright © 2018-2019 杨安康(yak1992@foxmail.com)
