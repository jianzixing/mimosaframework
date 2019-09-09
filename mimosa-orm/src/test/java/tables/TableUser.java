package tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.util.Date;

@Table
public enum TableUser {
    @Column(pk = true, type = long.class, strategy = AutoIncrementStrategy.class)
    id,
    @Column(length = 64)
    userName,
    @Column(length = 64)
    password,
    @Column(length = 30)
    realName,
    @Column(type = int.class)
    age,
    @Column(type = int.class, defaultValue = "2")
    level,
    @Column(length = 20)
    address,
    @Column(type = Date.class)
    createdTime,
    @Column(timeForUpdate = true)
    modifiedDate
}
