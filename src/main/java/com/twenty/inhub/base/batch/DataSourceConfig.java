package com.twenty.inhub.base.batch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/*
잠시 중지
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSourceInitializer dataSourceInit(@Qualifier("dataSource") final DataSource dataSource){
        ResourceDatabasePopulator resourceDatabasePopulator =new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("/schema-mariadb.sql"));
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);

        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }
}

 */
