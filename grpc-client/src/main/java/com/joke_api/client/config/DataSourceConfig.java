package com.joke_api.client.config;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.ZoneOffset;

@Configuration
public class DataSourceConfig {

    @Bean
    public Database database(DataSource dataSource) {
        var config = new DatabaseConfig();
        config.setDataSource(dataSource);
        config.setDataTimeZone(ZoneOffset.UTC.getId());
        return DatabaseFactory.create(config);
    }

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

}
