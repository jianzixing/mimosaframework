package org.mimosaframework.orm.sql.stamp;

public class StampAlter {
    public KeyTarget target;

    public String name;
    public Class table;

    public StampAlterItem[] items;


    public String charset;
    public String collate;

}
