package org.landocore.wishlist.common.config;

import org.landocore.wishlist.userManagement.repository.internal.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: seun
 * Date: 30/07/13
 * Time: 22:56
 * Configuration of the datasource
 */
@Configuration
@EnableTransactionManagement
public class TestDataConfig {


    /**
     * instantiates a datasource.
     * @return DataSource
     */
    @Bean
    public DataSource dataSource() {
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
            dataSource.setUrl("jdbc:hsqldb:mem:test-db");
            dataSource.setUsername("sa");
            dataSource.setPassword("");
            return dataSource;
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * instantiates the session factory.
     * @return LocalSessionFactoryBean
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setAnnotatedClasses(this.getHibernateAnnotatedEntities());

        Properties props = new Properties();
        props.put("hibernate.dialect",
                "org.hibernate.dialect.HSQLDialect");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "create");
        sessionFactory.setHibernateProperties(props);

        return sessionFactory;
    }

    /**
     * instantiates the transaction manager.
     * @return HibernateTransactionManager
     */
    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager =
                new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }


    /**
     * gets hibernate annotated classes
     */
    private Class<?>[] getHibernateAnnotatedEntities() {
        return new Class<?>[]{
            org.landocore.wishlist.userManagement.domain.Authority.class,
            org.landocore.wishlist.userManagement.domain.User.class
        };
    }

    /**
     * creates the user repo.
     * @return UserRepositoryImpl
     */
    @Bean
    public UserRepositoryImpl userRepository() {
        return new UserRepositoryImpl(sessionFactory().getObject());
    }




}
