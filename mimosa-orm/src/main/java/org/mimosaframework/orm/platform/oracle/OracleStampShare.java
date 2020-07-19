package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.PlatformStampShare;

public class OracleStampShare extends PlatformStampShare {

    public void addAutoIncrement(MappingGlobalWrapper wrapper, Class table, String tableStr) {
        String tableName = this.commonality.getReference().getTableName(wrapper, table, tableStr);
        String seqName = tableName + "_SEQ";
        this.commonality.getSection().getDeclares().add("SEQUENCE_COUNT NUMBER");
        this.commonality.getSection().getBuilders().add(new ExecuteImmediate().setProcedure("SELECT COUNT(1) INTO SEQUENCE_COUNT FROM USER_SEQUENCES WHERE SEQUENCE_NAME = '" + seqName + "'"));
        this.commonality.getSection().getBuilders().add(new ExecuteImmediate("IF SEQUENCE_COUNT = 0 THEN ",
                "CREATE SEQUENCE " + seqName + " INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 9999999999999999", "END IF"));
    }
}
