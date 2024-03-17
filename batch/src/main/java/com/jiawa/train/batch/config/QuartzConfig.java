package com.jiawa.train.batch.config;

import com.jiawa.train.batch.job.TestJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    /**
     * 声明一个任务
     *
     * @return
     */
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(TestJob.class)
//                里面参数为名字，组
                .withIdentity("TestJob", "test")
                .storeDurably()
                .build();
    }

    /**
     * 声明一个触发器，什么时候触发这个任务
     */
    @Bean
    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
//                给哪个任务用
                .forJob(jobDetail())
                //                里面参数为名字，组
                .withIdentity("trigger", "trigger")
//                开始启动
                .startNow()
//                触发的策略
                .withSchedule(CronScheduleBuilder.cronSchedule("*/2 * * * * ?"))
                .build();
//
    }
}
