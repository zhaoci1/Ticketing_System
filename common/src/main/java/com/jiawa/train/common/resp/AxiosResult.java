package com.jiawa.train.common.resp;

import lombok.Data;

public class AxiosResult<T> {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private int code;

    private String message;

    private T data;

    public static <T> AxiosResult<T> success(T data) {
        return getAxiosResult(EnumStatus.OK, data);
    }
    public static <T> AxiosResult<T> error(T data) {
        return getAxiosResult(EnumStatus.ERROR, data);
    }

    private static <T> AxiosResult<T> getAxiosResult(EnumStatus enumStatus, T data) {
        return new AxiosResult<T>(enumStatus, data);
    }

    public AxiosResult() {
    }

    private AxiosResult(EnumStatus enumStatus, T data) {
        this.code = enumStatus.getCode();
        this.message = enumStatus.getMessage();
        this.data = data;
    }

    @Override
    public String toString() {
        return "AxiosResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
