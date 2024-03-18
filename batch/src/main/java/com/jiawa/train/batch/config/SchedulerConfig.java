package com.jiawa.train.batch.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

//将quartz集成到项目
@Configuration
public class SchedulerConfig {
    @Resource
    private MyJobFactory myJobFactory;


    //    这里的DataSource参数就是yml那个数据库数据源，会和quartz关联起来
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dataSource") DataSource dataSource) {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setJobFactory(myJobFactory);
//        程序启动好，2秒之后开始执行quartz
        factoryBean.setStartupDelay(2);
        return factoryBean;

    }
}
