package org.landocore.wishlist.common.config;

import org.landocore.wishlist.userManagement.repository.internal.
        UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.
        WebMvcConfigurerAdapter;

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
public class DataConfig extends WebMvcConfigurerAdapter {

    /**
     * url de la datasource.
     */
    @Value("${db.url}")
    private String url;

    /**
     * driver de la datasource.
     */
    @Value("${db.driver}")
    private String driver;

    /**
     * username de la datasource.
     */
    @Value("${db.username}")
    private String username;

    /**
     * password de la datasource.
     */
    @Value("${db.password}")
    private String password;

    /**
     * instanciate a datasource.
     * @return DataSource
     */
    @Bean
    public DataSource dataSource() {
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            return dataSource;
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * instanciate the session factory.
     * @return LocalSessionFactoryBean
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setAnnotatedClasses(this.getHibernateAnnotatedEntities());

        Properties props = new Properties();
        props.put("hibernate.dialect",
                "org.hibernate.dialect.PostgreSQL82Dialect");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "update");
        sessionFactory.setHibernateProperties(props);

        return sessionFactory;
    }

    /**
     * instanciates the tansaction manager.
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