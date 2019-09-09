package tables;

import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.annotation.Table;

@Table
public enum TableMultiKey2 {
    @Column(pk = true, type = long.class)
    id3,
    @Column(pk = true, type = long.class)
    id4
}
