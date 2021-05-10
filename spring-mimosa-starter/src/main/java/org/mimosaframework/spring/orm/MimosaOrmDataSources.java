package org.mimosaframework.spring.orm;

import org.mimosaframework.orm.MimosaDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MimosaOrmDataSources {
    private List<MimosaDataSource> dataSources;

    public MimosaOrmDataSources() {
    }

    public MimosaOrmDataSources(DataSource dataSource) throws SQLException {
        MimosaDataSource mds = new MimosaDataSource(dataSource, MimosaDataSource.DEFAULT_DS_NAME);
        this.dataSources = new ArrayList<>();
        this.dataSources.add(mds);
    }

    public MimosaOrmDataSources(List<MimosaDataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public List<MimosaDataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<MimosaDataSource> dataSources) {
        this.dataSources = dataSources;
    }
}
