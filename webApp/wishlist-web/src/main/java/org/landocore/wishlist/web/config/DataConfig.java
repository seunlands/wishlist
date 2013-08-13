package org.landocore.wishlist.web.config;

import org.landocore.wishlist.repositories.login.UserRepositoryImpl;
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
    public final DataSource dataSource() {
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
    public final LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        Class<?>[] hibernateAnnotatedClasses = new Class<?>[] {
                org.landocore.wishlist.beans.login.User.class,
                org.landocore.wishlist.beans.login.Authority.class
        };
        sessionFactory.setAnnotatedClasses(hibernateAnnotatedClasses);

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
    public final HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager =
                new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    /**
     * creates the user repo.
     * @return UserRepositoryImpl
     */
    @Bean
    public final UserRepositoryImpl userRepository() {
        UserRepositoryImpl userRepository =
                new UserRepositoryImpl(sessionFactory().getObject());
        return userRepository;
    }
}
