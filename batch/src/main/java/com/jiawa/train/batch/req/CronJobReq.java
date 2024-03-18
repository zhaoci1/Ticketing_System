package com.jiawa.train.batch.req;

import lombok.Data;

@Data
public class CronJobReq {
//    组
    private String group;
//    任务的名字，类名
    private String name;

    private String description;
    private String cronExpression;

    @Override
    public String toString() {
        return "CronJobReq{" +
                "group='" + group + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                '}';
    }
}
