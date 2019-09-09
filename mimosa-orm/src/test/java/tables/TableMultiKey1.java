package tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;

@Table
public enum TableMultiKey1 {
    @Column(pk = true, type = long.class)
    id1,
    @Column(pk = true, type = long.class)
    id2
}
