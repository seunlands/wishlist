package org.landocore.wishlist.common.service;

import org.landocore.wishlist.common.config.SecurityConfig;
import org.landocore.wishlist.common.repository.DataConfigTests;
import org.landocore.wishlist.usermanagement.repository.internal.UserRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
@ComponentScan(basePackages = { "org.landocore.wishlist.*.service.internal" })
@Import({DataConfigTests.class, SecurityConfig.class})
public class ServiceConfigTests {



}
