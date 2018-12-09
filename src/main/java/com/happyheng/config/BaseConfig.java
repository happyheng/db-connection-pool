package com.happyheng.config;

import com.happyheng.db.factory.ConnectionFactory;
import com.happyheng.db.pool.DbConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by happyheng on 2018/12/5.
 */
@Configuration
@ComponentScan("com.happyheng")
@PropertySource("classpath:config.properties")
public class BaseConfig {

    @Autowired
    Environment env;

    @Bean
    public ConnectionFactory getFactory() {

        return new ConnectionFactory(env.getProperty("db.driver"),
                env.getProperty("db.dbUrl"),
                env.getProperty("db.userName"),
                env.getProperty("db.password"));
    }

    @Bean
    public DbConnectionPool getConnectionPool(ConnectionFactory factory) {

        return new DbConnectionPool(2, factory);
    }

}
