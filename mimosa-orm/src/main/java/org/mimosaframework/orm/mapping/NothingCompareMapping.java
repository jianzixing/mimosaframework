package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MappingLevel;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.platform.SessionContext;

import java.sql.SQLException;

public class NothingCompareMapping extends AbstractCompareMapping {
    public NothingCompareMapping(MappingGlobalWrapper mappingGlobalWrapper, SessionContext sessionContext) {
        super(mappingGlobalWrapper, sessionContext);
        this.mappingLevel = MappingLevel.NOTHING;
    }

    @Override
    public void doMapping() throws ContextException {
        // do nothing
    }
}
