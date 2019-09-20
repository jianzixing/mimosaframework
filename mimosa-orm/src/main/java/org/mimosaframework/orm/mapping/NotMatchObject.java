package org.mimosaframework.orm.mapping;

import org.mimosaframework.orm.MimosaDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NotMatchObject {
    private MimosaDataSource mimosaDataSource;
    private MappingDatabase mappingDatabase;
    private Set<MappingTable> matchMappingTables;

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

    public MimosaDataSource getMimosaDataSource() {
        return mimosaDataSource;
    }

    public void setMimosaDataSource(MimosaDataSource mimosaDataSource) {
        this.mimosaDataSource = mimosaDataSource;
    }

    public MappingDatabase getMappingDatabase() {
        return mappingDatabase;
    }

    public void setMappingDatabase(MappingDatabase mappingDatabase) {
        this.mappingDatabase = mappingDatabase;
    }

    public Set<MappingTable> getMatchMappingTables() {
        return matchMappingTables;
    }

    public void setMatchMappingTables(Set<MappingTable> matchMappingTables) {
        this.matchMappingTables = matchMappingTables;
    }
}
