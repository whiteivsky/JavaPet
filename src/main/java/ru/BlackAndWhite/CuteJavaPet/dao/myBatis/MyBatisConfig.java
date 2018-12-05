package ru.BlackAndWhite.CuteJavaPet.dao.myBatis;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;

@Configuration
@PropertySource("classpath:/application.properties")
public class MyBatisConfig {
    @Resource
    private org.springframework.core.env.Environment env;


    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setUrl(env.getRequiredProperty("db.url"));
        dataSource.setUsername(env.getRequiredProperty("db.username"));
        dataSource.setPassword(env.getRequiredProperty("db.password"));
        dataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
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
