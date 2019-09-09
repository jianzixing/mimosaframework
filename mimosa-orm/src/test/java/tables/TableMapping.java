package tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

@Table
public enum TableMapping {
    @Column(pk = true, strategy = AutoIncrementStrategy.class)
    id,
    @Column(type = byte.class)
    f1,
    @Column(type = String.class, length = 60)
    f2,
    @Column
    f3
}
