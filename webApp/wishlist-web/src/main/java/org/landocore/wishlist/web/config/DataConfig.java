package org.landocore.wishlist.web.config;

import org.hibernate.SessionFactory;
import org.landocore.wishlist.repositories.login.UserRepository;
import org.landocore.wishlist.repositories.login.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 30/07/13
 * Time: 22:56
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableTransactionManagement
public class DataConfig extends WebMvcConfigurerAdapter {


    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource(){
        try{
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
            dataSource.setUrl(env.getRequiredProperty("db.url"));
            dataSource.setUsername(env.getRequiredProperty("db.username"));
            dataSource.setPassword(env.getRequiredProperty("db.password"));
            return dataSource;
        } catch (IllegalStateException e){
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SessionFactory sessionFactory(){
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setAnnotatedPackages(new String[]{"org.landocore.wishlist.beans"});

        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "update");
        sessionFactory.setHibernateProperties(props);

        return sessionFactory.getObject();
    }

    @Bean
    public HibernateTransactionManager transactionManager(){
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory());
        return transactionManager;
    }

    @Bean
    public UserRepository userRepository(){
        UserRepository userRepository = new UserRepositoryImpl(sessionFactory());
        return userRepository;
    }
}
