package org.mimosaframework.orm.mapping;

import java.util.ArrayList;
import java.util.List;

public class NotMatchObject {
    private List<MappingTable> missingTables;
    private List<MappingField> missingFields;
    private List<MappingField> changeFields;

    public void addMissingTable(MappingTable table) {
        if (missingTables == null) {
            missingTables = new ArrayList<>();
        }
        missingTables.add(table);
    }

    public void addMissingField(MappingField field) {
        if (missingFields == null) {
            missingFields = new ArrayList<>();
        }
        missingFields.add(field);
    }

    public void addChangeField(MappingField field) {
        if (changeFields == null) {
            changeFields = new ArrayList<>();
        }
        changeFields.add(field);
    }

    public List<MappingTable> getMissingTables() {
        return missingTables;
    }

    public List<MappingField> getMissingFields() {
        return missingFields;
    }

    public List<MappingField> getChangeFields() {
        return changeFields;
    }
}
