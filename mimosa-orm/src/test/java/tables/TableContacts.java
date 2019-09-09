package tables;

import org.mimosaframework.orm.annotation.Cache;
import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

@Table
@Cache
public enum TableContacts {
    @Column(pk = true, type = long.class, strategy = AutoIncrementStrategy.class)
    id,
    @Column(length = 100)
    name,
    @Column(length = 15)
    phone,
    @Column(length = 10)
    rel,
    @Column(timeForUpdate = true)
    modifiedDate
}
