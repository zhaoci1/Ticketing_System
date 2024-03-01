package com.jiawa.train.common.resp;

public enum EnumStatus {
    OK(20000, "操作成功"),
    ERROR(40000, "操作失败"),
    NO_LOGIN(33333, "未登录"),
    ACCOUNT_ERROR(22222, "用户名或者邮箱不正确"),
    CODE_ERROR(22223, "验证码不正确"),
    CODE_SHIXIAO(22224, "验证码已失效"),
    NO_ACTIVE(22225, "用户未激活"),
    FORM_VALICATOR_ERROR(40000,"表单校验异常");
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