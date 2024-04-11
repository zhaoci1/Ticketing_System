package com.jiawa.train.business.resp;

import lombok.Data;

@Data
public class SeatSellResp {
    /**
     * 箱序
     */
    private Integer carriageIndex;

    /**
     * 排号
     */
    private String row;

    /**
     * 列号
     */
    private String col;

    /**
     * 座位类型
     */
    private String seatType;

    /**
     * 售卖情况
     */
    private String sell;
}
