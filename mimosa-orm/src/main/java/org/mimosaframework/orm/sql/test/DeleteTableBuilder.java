package org.mimosaframework.orm.sql.test;

/**
 * DELETE t1 FROM A t1,B t2 WHERE t1.id=t2.bid
 */
public interface DeleteTableBuilder extends DeleteBuilder {
    DeleteFromsBuilder table(Class table);

    DeleteFromsBuilder table(String aliasName);
}
