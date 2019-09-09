package tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

@Table("t_query_db")
public enum TableQuery {
    @Column(pk = true, type = long.class, strategy = AutoIncrementStrategy.class)
    id,
    @Column(type = long.class, index = true)
    ca,
    @Column(type = long.class, index = true)
    cb,
    @Column(timeForUpdate = true)
    ta,
    @Column(type = long.class)
    cc
}
