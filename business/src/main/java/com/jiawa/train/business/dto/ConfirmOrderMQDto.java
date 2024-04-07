package com.jiawa.train.business.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ConfirmOrderMQDto {
    /**
     * 日志流程号，用于同转异时，用一个流水号
     */
    private String logId;

    private Date date;

    private String trainCode;
}
