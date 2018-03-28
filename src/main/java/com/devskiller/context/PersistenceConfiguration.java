package com.devskiller.context;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(value = "com.devskiller.dao")
public class PersistenceConfiguration {

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
    LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
    factoryBean.setDataSource(dateSource());
    factoryBean.setJpaProperties(hibernateProperties());
    factoryBean.setPackagesToScan("com.devskiller.model");
    factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
    return factoryBean;
  }

  @Bean
  public DataSource dateSource() {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
  }

  @Bean
  public PlatformTransactionManager platformTransactionManager() {
    return new JpaTransactionManager();
  }

  private Properties hibernateProperties() {
      Properties properties = new Properties();
      properties.put("hibernate.hbm2ddl.auto", "create-drop");
      properties.put("hibernate.show_sql", "true");
      properties.put("hibernate.format_sql", "true");
      properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
      return properties;
   }
}
