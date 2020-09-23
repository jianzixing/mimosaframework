# 自定义SQL语句

mimosa-orm支持自定义SQL执行，执行后悔返回AutoResult对象。执行自定义SQL返回的字段都是数据库
原始字段，默认不会转换字段结构(比如下划线转驼峰)。如果需要转换则设置AutoResult#setTableClass
需要转换的映射类即可，最好设置查询SQL相关的映射类，多余的映射类会遍历导致效率不高。

如果结果中包含映射类中不存在的字段则无法通过AutoResult#setTableClass转换。

执行自定义SQL语句需要调用以下方法：

```java
AutoResult getAutonomously(SQLAutonomously var1)
```

这里使用的SQL语句查询的表一定是数据库中对应的表名称和字段，通过执行SQL返回的AutoResult
中的对象字段是通过convert参数设置的转换后的字段，比如user_name在AutoResult中是userName字段。

AutoResult可以根据查询SQL返回的信息获取需要的值

* getObjects会返回整个查询结果，如果SQL语句返回的是一组数据。
* getNumbers()会返回一个数字列表，如果查询语句返回的是单列且列值为数字。
* getNumbers("id")会返回一个数字列表，如果查询一组数据中有列id且列id值类型是数字。
* gets()返回一个值列表，取一组数据的第一列值。
* gets("name")返回一个值列表，如果查询数据中包含列name则取列name的值。
* getValue()返回查询结果对象，改对象一般为List<ModelObject>。
* getSingle()返回查询数据的第一行数据。
* getStrings()与gets()类似，但是返回的是字符串。
* getStrings("name")与gets("name")类似，但是返回的是字符串。
* doubleValue()返回一个数字，如果查询语句返回的是一个列表则取列表的第一条第一列的值。xxxValue()都是类似方法。


```java
SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance();
sqlAutonomously.add("select * from t_user where id = 20");
AutoResult result = template.getAutonomously(sqlAutonomously);
List<ModelObject> objects = result.getObjects();
```

如果想指定数据源则可以填写数据源名称，第三次可选参数表示是否使用主数据库查询，false是使用从数据库查询。
默认数据源使用default字符串。

```java
sqlAutonomously.add("default", "select * from t_user where id = 20", true);
```


## 
Copyright © 2018-2019 杨安康(yak1992@foxmail.com)
