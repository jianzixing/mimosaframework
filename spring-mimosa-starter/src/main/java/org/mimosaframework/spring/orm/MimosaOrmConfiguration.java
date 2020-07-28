package org.mimosaframework.spring.orm;

import org.mimosaframework.orm.spring.SpringMimosaSessionFactory;
import org.mimosaframework.orm.spring.SpringMimosaSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass({SpringMimosaSessionFactory.class, SpringMimosaSessionTemplate.class})
@ConditionalOnBean({DataSource.class})
@EnableConfigurationProperties({MimosaOrmProperties.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
public class MimosaOrmConfiguration {

    @Autowired
    private MimosaOrmProperties mimosaOrmProperties;

    @Bean
    @ConditionalOnMissingBean
    public SpringMimosaSessionFactory mimosaSessionFactory(DataSource dataSource) throws Exception {
        SpringMimosaSessionFactory springMimosaSessionFactory = new SpringMimosaSessionFactory();
        // 将autoConfigurationProperties中的值添加到这里
        springMimosaSessionFactory.setDataSource(dataSource);
        springMimosaSessionFactory.setApplicationName(mimosaOrmProperties.getApplicationName());
        springMimosaSessionFactory.setScanPackage(mimosaOrmProperties.getScanPackage());
        springMimosaSessionFactory.setMapper(mimosaOrmProperties.getMapper());
        springMimosaSessionFactory.setConvertType(mimosaOrmProperties.getConvertType());
        springMimosaSessionFactory.setMappingLevel(mimosaOrmProperties.getMappingLevel());
        springMimosaSessionFactory.setShowSQL(mimosaOrmProperties.isShowSQL());

        return springMimosaSessionFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringMimosaSessionTemplate mimosaSessionTemplate(SpringMimosaSessionFactory mimosaSessionFactory) {
        SpringMimosaSessionTemplate springMimosaSessionTemplate = new SpringMimosaSessionTemplate();
        springMimosaSessionTemplate.setFactory(mimosaSessionFactory);
        return springMimosaSessionTemplate;
    }
}
