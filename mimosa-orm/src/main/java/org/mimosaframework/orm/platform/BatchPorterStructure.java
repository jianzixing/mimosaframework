package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;

import java.util.List;
import java.util.Map;

public class BatchPorterStructure extends PorterStructure {
    private List<ModelObject> objects;
    private List<String> fields;

    public BatchPorterStructure(ChangerClassify changerClassify,
                                SQLBuilder sqlBuilder,
                                List<ModelObject> objects,
                                List<String> fields) {
        super(changerClassify, sqlBuilder);
        this.objects = objects;
        this.fields = fields;
    }

    public BatchPorterStructure(ChangerClassify changerClassify,
                                SQLBuilder sqlBuilder,
                                Map<Object, List<SelectFieldAliasReference>> references,
                                List<ModelObject> objects,
                                List<String> fields) {
        super(changerClassify, sqlBuilder, references);
        this.objects = objects;
        this.fields = fields;
    }

    public List<ModelObject> getObjects() {
        return objects;
    }

    public void setObjects(List<ModelObject> objects) {
        this.objects = objects;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
