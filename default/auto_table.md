# 自动创建表说明

ORM默认的mappingLevel是CREATE级别，改级别是任何环境都非常合适的级别。在程序启动后
就会判断缺失的表和字段然后自动补全。如果字段被修改则需要手动删除字段重新创建或者手动修改
数据库中的字段信息。

在程序扫描映射类时只会扫描带有注解@Table的类，@Table有三个参数。
* 如果不使用参数直接注解@Table则使用映射类的类名称，并自动将Table前缀修改为t_，比如TableUser表名称就是t_user。
* value参数可以指定表名称。
* engineName参数可以指定建表语句的引擎(需要数据库支持)。
* charset参数可以指定表字符集。

映射类的字段需要@Column 注解才会解析，否则不会作为映射字段使用。@Column注解的字段默认长度255的字符串类型。
* name：字段名称，如果不填写默认使用映射类字段的名称。
* strategy：ID生成策略，默认不使用ID生成策略，如果需要数据库自增字段则使用AutoIncrementStrategy。
* type：字段映射类型默认使用String，字符串类型。
* length：字段长度，String时有效。
* decimalDigits：如果使用小数可以指定小数的位数，整数位数使用length指定，length=10、decimalDigits=2表示
BigDecimal(10,2)
* nullable：是否可为空。
* pk：是否是主键字段，如果一个表中有多个字段设置pk=true则表示这个表的主键是联合主键。
* index：是否建立索引。
* unique：是否是唯一索引。
* comment：字段说明注解。
* timeForUpdate：MySQL中有效，该字段不需要指定，每次更新时会自动更新改字段(数据库行为)。
* defaultValue：添加新数据的默认值。

以上是和数据库建表相关参数，以下是添加保存时可以使用的限制条件：
* extMinLength：字段最小长度
* extDecimalFormat：自定格式数字，使用DecimalFormat格式化，改字段为数字时有效。
* extRegExp：使用正则表达式判断是否符合
* extCanUpdate：更新时需要去掉的字段，比如createTime字段更新时不需要。

## 
Copyright © 2018-2019 [北京简子行科技有限公司](https://www.jianzixing.com.cn)
