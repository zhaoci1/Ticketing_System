package com.jiawa.train.common.resp;

public enum EnumStatus {
    OK(200, "操作成功"),
    ERROR(400, "操作失败"),
    NO_LOGIN(333, "未登录"),
    ACCOUNT_ERROR(222, "用户名或者邮箱不正确"),
    CODE_ERROR(223, "验证码不正确"),
    CODE_SHIXIAO(224, "验证码已失效"),
    NO_ACTIVE(225, "用户未激活"),
    FORM_VALICATOR_ERROR(400,"表单校验异常");
    ;

    private int code;

    private String message;

    EnumStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}