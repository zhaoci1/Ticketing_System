package com.jiawa.train.batch.controller;

import com.jiawa.train.batch.req.CronJobReq;
import com.jiawa.train.batch.resp.CronJobResp;
import com.jiawa.train.common.resp.AxiosResult;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/admin/job")
public class JobController {

    private static Logger LOG = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @RequestMapping(value = "/run")
    public AxiosResult<Object> run(@RequestBody CronJobReq cronJobReq) throws SchedulerException {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("手动执行任务开始：{}, {}", jobClassName, jobGroupName);
        schedulerFactoryBean.getScheduler().triggerJob(JobKey.jobKey(jobClassName, jobGroupName));
        return new AxiosResult<>();
    }

    @RequestMapping(value = "/add")
    public AxiosResult add(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        String cronExpression = cronJobReq.getCronExpression();
        String description = cronJobReq.getDescription();
        LOG.info("创建定时任务开始：{}，{}，{}，{}", jobClassName, jobGroupName, cronExpression, description);
        AxiosResult axiosResult = new AxiosResult();

        try {
            // 通过SchedulerFactory获取一个调度器实例
            Scheduler sched = schedulerFactoryBean.getScheduler();

            // 启动调度器
            sched.start();

            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(jobClassName)).withIdentity(jobClassName, jobGroupName).build();

            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName).withDescription(description).withSchedule(scheduleBuilder).build();

            sched.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            LOG.error("创建定时任务失败:" + e);
            axiosResult.error(false);
            axiosResult.setMessage("创建定时任务失败:调度异常");
        } catch (ClassNotFoundException e) {
            LOG.error("创建定时任务失败:" + e);
            axiosResult.error(false);
            axiosResult.setMessage("创建定时任务失败：任务类不存在");
        }
        LOG.info("创建定时任务结束：{}", axiosResult);
        return AxiosResult.success(true);
    }

    @RequestMapping(value = "/pause")
    public AxiosResult pause(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("暂停定时任务开始：{}，{}", jobClassName, jobGroupName);
        AxiosResult axiosResult = new AxiosResult();
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            LOG.error("暂停定时任务失败:" + e);
            axiosResult.error(false);
            axiosResult.setMessage("暂停定时任务失败:调度异常");
        }
        LOG.info("暂停定时任务结束：{}", axiosResult);
        return AxiosResult.success(true);
    }

    @RequestMapping(value = "/resume")
    public AxiosResult resume(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("重启定时任务开始：{}，{}", jobClassName, jobGroupName);
        AxiosResult axiosResult = new AxiosResult();
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            LOG.error("重启定时任务失败:" + e);
            axiosResult.error(false);
            axiosResult.setMessage("重启定时任务失败:调度异常");
        }
        LOG.info("重启定时任务结束：{}", axiosResult);
        return AxiosResult.success(true);
    }

    @RequestMapping(value = "/reschedule")
    public AxiosResult reschedule(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        String cronExpression = cronJobReq.getCronExpression();
        String description = cronJobReq.getDescription();
        LOG.info("更新定时任务开始：{}，{}，{}，{}", jobClassName, jobGroupName, cronExpression, description);
        AxiosResult axiosResult = new AxiosResult();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTriggerImpl trigger1 = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
            trigger1.setStartTime(new Date()); // 重新设置开始时间
            CronTrigger trigger = trigger1;

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withDescription(description).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (Exception e) {
            LOG.error("更新定时任务失败:" + e);
            axiosResult.error(false);
            axiosResult.setMessage("更新定时任务失败:调度异常");
        }
        LOG.info("更新定时任务结束：{}", axiosResult);
        return AxiosResult.success(true);
    }

    @RequestMapping(value = "/delete")
    public AxiosResult delete(@RequestBody CronJobReq cronJobReq) {
        String jobClassName = cronJobReq.getName();
        String jobGroupName = cronJobReq.getGroup();
        LOG.info("删除定时任务开始：{}，{}", jobClassName, jobGroupName);
        AxiosResult axiosResult = new AxiosResult();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            LOG.error("删除定时任务失败:" + e);
            axiosResult.error(false);
            axiosResult.setMessage("删除定时任务失败:调度异常");
        }
        LOG.info("删除定时任务结束：{}", axiosResult);
        return AxiosResult.success(true);
    }

    @RequestMapping(value = "/query")
    public AxiosResult query() {
        LOG.info("查看所有定时任务开始");
        AxiosResult axiosResult = new AxiosResult();
        List<CronJobResp> cronJobDtoList = new ArrayList();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    CronJobResp cronJobResp = new CronJobResp();
                    cronJobResp.setName(jobKey.getName());
                    cronJobResp.setGroup(jobKey.getGroup());

                    //get job's trigger
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    CronTrigger cronTrigger = (CronTrigger) triggers.get(0);
                    cronJobResp.setNextFireTime(cronTrigger.getNextFireTime());
                    cronJobResp.setPreFireTime(cronTrigger.getPreviousFireTime());
                    cronJobResp.setCronExpression(cronTrigger.getCronExpression());
                    cronJobResp.setDescription(cronTrigger.getDescription());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
                    cronJobResp.setState(triggerState.name());

                    cronJobDtoList.add(cronJobResp);
                }

            }
        } catch (SchedulerException e) {
            LOG.error("查看定时任务失败:" + e);
            axiosResult.error(false);
            axiosResult.setMessage("查看定时任务失败:调度异常");
        }
        axiosResult.setData(cronJobDtoList);
        LOG.info("查看定时任务结束：{}", axiosResult);
        return axiosResult;
    }

}
