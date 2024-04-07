package com.jiawa.train.business.enums;

//主题常量

public enum RocketMQTopicEnum {
    CONFIRM_ORDER("CONFIRM_ORDER","确认订单排队");
    private String code;

    private String desc;

    RocketMQTopicEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "RocketMQTopicEnum{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                "} " + super.toString();
    }
}
