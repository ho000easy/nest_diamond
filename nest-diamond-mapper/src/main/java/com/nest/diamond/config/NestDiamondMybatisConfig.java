package com.nest.diamond.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.nest.diamond.mapper"}, sqlSessionFactoryRef = "nestDiamondSqlSessionFactory")
public class NestDiamondMybatisConfig {

    @Bean(name = "nestDiamondDataSource")
    @Primary
    public HikariDataSource nestRobotDataSource(NestDiamondDataSourceConfig nestDiamondDataSourceConfig) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(nestDiamondDataSourceConfig.getNest_diamond_url());
        dataSource.setUsername(nestDiamondDataSourceConfig.getNest_diamond_userName());
        dataSource.setPassword(nestDiamondDataSourceConfig.getNest_diamond_password());

        dataSource.setMaximumPoolSize(nestDiamondDataSourceConfig.getNest_diamond_maxActive());
        dataSource.setMinimumIdle(10);
        dataSource.setConnectionTimeout(60000);
        dataSource.setIdleTimeout(300000);
        dataSource.setMaxLifetime(2000000);
        dataSource.setAutoCommit(true);
//        dataSource.setDriverClassName("com.p6spy.engine.spy.P6SpyDriver");
        dataSource.setPoolName("mysql-pool");
        return dataSource;
    }

    @Bean(name = "nestDiamondSqlSessionFactory")
    @Primary
    public SqlSessionFactory nestRobotSqlSessionFactory(@Qualifier("nestDiamondDataSource") DataSource dataSource, GlobalConfig globalConfig) throws Exception {
        return MyBatisUtil.sqlSessionFactory(dataSource, globalConfig, "classpath:mapper/*.xml");
    }

    @Bean(name = "nestDiamondTransactionManager")
    @Primary
    public DataSourceTransactionManager nestGoldTransactionManager(@Qualifier("nestDiamondDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
