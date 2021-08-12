package org.mimosaframework.spring.orm;

import org.mimosaframework.orm.spring.SpringBeanSessionTemplate;
import org.mimosaframework.orm.spring.SpringMimosaSessionFactory;
import org.mimosaframework.orm.spring.SpringMimosaSessionTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 由于使用了@ConditionalOnSingleCandidate(DataSource.class)这个注解
 * 需要引用spring-boot-starter-jdbc启动器才可以成功
 */
@Configuration
@ConditionalOnClass({SpringMimosaSessionFactory.class, SpringMimosaSessionTemplate.class})
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties({MimosaOrmProperties.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
public class MimosaOrmConfiguration implements InitializingBean {

    @Autowired
    private MimosaOrmProperties mimosaOrmProperties;

    @Bean
    @ConditionalOnMissingBean
    public MimosaOrmDataSources mimosaOrmDataSources(DataSource dataSource) throws SQLException {
        return new MimosaOrmDataSources(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringMimosaSessionFactory mimosaSessionFactory(MimosaOrmDataSources mimosaOrmDataSources) throws Exception {
        SpringMimosaSessionFactory springMimosaSessionFactory = new SpringMimosaSessionFactory();
        // 将autoConfigurationProperties中的值添加到这里
        springMimosaSessionFactory.setDataSources(mimosaOrmDataSources.getDataSources());
        springMimosaSessionFactory.setApplicationName(mimosaOrmProperties.getApplicationName());
        springMimosaSessionFactory.setScanPackage(mimosaOrmProperties.getScanPackage());
        springMimosaSessionFactory.setMapper(mimosaOrmProperties.getMapper());
        springMimosaSessionFactory.setConvertType(mimosaOrmProperties.getConvertType());
        springMimosaSessionFactory.setMappingLevel(mimosaOrmProperties.getMappingLevel());
        springMimosaSessionFactory.setShowSQL(mimosaOrmProperties.isShowSQL());
        springMimosaSessionFactory.setTablePrefix(mimosaOrmProperties.getPrefix());
        springMimosaSessionFactory.setTableCompare(mimosaOrmProperties.getTableCompare());

        return springMimosaSessionFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringMimosaSessionTemplate mimosaSessionTemplate(SpringMimosaSessionFactory mimosaSessionFactory) {
        SpringMimosaSessionTemplate springMimosaSessionTemplate = new SpringMimosaSessionTemplate();
        springMimosaSessionTemplate.setFactory(mimosaSessionFactory);
        return springMimosaSessionTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringBeanSessionTemplate mimosaBeanSessionTemplate(SpringMimosaSessionFactory mimosaSessionFactory) {
        SpringBeanSessionTemplate springMimosaSessionTemplate = new SpringBeanSessionTemplate();
        springMimosaSessionTemplate.setFactory(mimosaSessionFactory);
        return springMimosaSessionTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
