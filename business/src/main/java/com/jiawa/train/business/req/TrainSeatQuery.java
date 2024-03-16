package com.jiawa.train.business.req;

import com.jiawa.train.common.req.PageReq;

public class TrainSeatQuery extends PageReq {
    private String trainCode;

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        return "TrainStationQuery{" +
                "trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }
}