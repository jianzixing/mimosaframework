package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.platform.BatchPorterStructure;
import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SelectFieldAliasReference;
import org.mimosaframework.orm.platform.TypeForRunner;

import java.util.List;
import java.util.Map;

public class AIBatchPorterStructure extends BatchPorterStructure {
    private MappingField field;

    public AIBatchPorterStructure(TypeForRunner changerClassify, SQLBuilder sqlBuilder, List<ModelObject> objects, List<String> fields) {
        super(changerClassify, sqlBuilder, objects, fields);
    }

    public AIBatchPorterStructure(TypeForRunner changerClassify, SQLBuilder sqlBuilder, Map<Object, List<SelectFieldAliasReference>> references, List<ModelObject> objects, List<String> fields) {
        super(changerClassify, sqlBuilder, references, objects, fields);
    }

    public MappingField getField() {
        return field;
    }

    public void setField(MappingField field) {
        this.field = field;
    }
}
