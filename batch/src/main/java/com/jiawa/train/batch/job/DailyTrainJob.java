package com.jiawa.train.batch.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.jiawa.train.batch.feign.BusinessFeign;
import com.jiawa.train.common.resp.AxiosResult;
import jakarta.annotation.Resource;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@DisallowConcurrentExecution
public class DailyTrainJob implements Job {
    private static final Logger Log = LoggerFactory.getLogger(DailyTrainJob.class);

    @Resource
    BusinessFeign businessFeign;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Log.info("正在生成15天之后的车次数据");
        Date date = new Date();
        DateTime dateTime = DateUtil.offsetDay(date, 15);
        Date jdkDate = dateTime.toJdkDate();
        AxiosResult axiosResult = businessFeign.genDaily(jdkDate);
        Log.info("15天之后的车次数据生成完毕，结果：{}",axiosResult);
    }
}
