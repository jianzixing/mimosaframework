package tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

@Table
public enum TableDisperseByUser {
    @Column(pk = true, strategy = AutoIncrementStrategy.class)
    id,
    @Column(type = long.class)
    userId,
    @Column(length = 20)
    detail
}
