package org.mimosaframework.orm.spring;

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
import java.sql.SQLException;

@Configuration
@ConditionalOnClass({SpringMimosaSessionFactory.class, SpringMimosaSessionTemplate.class})
@ConditionalOnBean({DataSource.class})
@EnableConfigurationProperties({AutoConfigurationProperties.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
public class AutoConfigurationLoader {

    @Autowired
    private AutoConfigurationProperties autoConfigurationProperties;

    @Bean
    @ConditionalOnMissingBean
    public SpringMimosaSessionFactory mimosaSessionFactory(DataSource dataSource) throws Exception {
        SpringMimosaSessionFactory springMimosaSessionFactory = new SpringMimosaSessionFactory();
        // 将autoConfigurationProperties中的值添加到这里
        springMimosaSessionFactory.setDataSource(dataSource);
        springMimosaSessionFactory.setApplicationName(autoConfigurationProperties.getApplicationName());
        springMimosaSessionFactory.setScanPackage(autoConfigurationProperties.getScanPackage());
        springMimosaSessionFactory.setMapper(autoConfigurationProperties.getMapper());
        springMimosaSessionFactory.setConvertType(autoConfigurationProperties.getConvertType());
        springMimosaSessionFactory.setMappingLevel(autoConfigurationProperties.getMappingLevel());
        springMimosaSessionFactory.setShowSQL(autoConfigurationProperties.isShowSQL());

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
