package org.landocore.wishlist.repositories;

import org.landocore.wishlist.repositories.login.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.*;
import org.springframework.orm.hibernate4.*;
import org.springframework.transaction.annotation.*;

import javax.sql.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 30/07/13
 * Time: 22:56
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class DataTestConfig {

    @Bean
    public DataSource dataSource(){
        try{
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl("jdbc:postgresql://localhost:5432/wishlist");
            dataSource.setUsername("wishlist");
            dataSource.setPassword("wishlist");
            return dataSource;
        } catch (IllegalStateException e){
            throw new RuntimeException(e);
        }
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(){
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setAnnotatedPackages(new String[]{"org.landocore.wishlist.beans"});

        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "update");
        sessionFactory.setHibernateProperties(props);

        return sessionFactory;
    }


    @Bean
    public UserRepositoryImpl userRepository(){
        UserRepositoryImpl userRepository = new UserRepositoryImpl(sessionFactory().getObject());
        return userRepository;
    }
}
