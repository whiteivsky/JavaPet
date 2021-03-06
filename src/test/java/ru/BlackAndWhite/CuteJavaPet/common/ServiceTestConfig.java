package ru.BlackAndWhite.CuteJavaPet.common;

import lombok.extern.log4j.Log4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.BlackAndWhite.CuteJavaPet.controllers.ErrorController;
//import ru.BlackAndWhite.CuteJavaPet.controllers.CustomErrorController;

@Log4j
@Configuration
@ComponentScan(value = {"ru.BlackAndWhite.CuteJavaPet.*"}, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ErrorController.class))
public class ServiceTestConfig {

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();
        //dataSource.setUrl("jdbc:mysql://localhost:3306/javapettest?verifyServerCertificate=false&useSSL=false&requireSSL=false");
        dataSource.setUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    @Bean
    public TransactionFactory transactionFactory() {
        return new JdbcTransactionFactory();
    }

    @Bean
    public SqlSession getSqlSession() {
        return getSqlSessionFactory().openSession();
    }

    private SqlSessionFactory getSqlSessionFactory() {

        Environment environment = new Environment("development", transactionFactory(), dataSource());
        org.apache.ibatis.session.Configuration myBatisConfig = new org.apache.ibatis.session.Configuration(environment);

        myBatisConfig.addMappers("ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers");
        myBatisConfig.setMultipleResultSetsEnabled(true);

        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        return builder.build(myBatisConfig);
    }
}
