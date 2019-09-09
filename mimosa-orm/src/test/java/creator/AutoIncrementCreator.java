package creator;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

@Table
public enum AutoIncrementCreator {
    @Column(type = long.class, pk = true, strategy = AutoIncrementStrategy.class)
    id,
    @Column(type = byte.class)
    value,
    @Column(type = String.class)
    value2
}
