package com.studyflow.dto;

import lombok.Data;

@Data
public class Result <T>{
    private T data;
    private String message;
    private boolean success;

    private Result(){}

    public static <T> Result<T> ok(){
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setMessage("操作成功");
        return r;
    }
    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setMessage("操作成功");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> ok(String message, T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static <T> Result<T> fail(String message) {
        Result<T> r = new Result<>();
        r.setSuccess(false);
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> fail(String message, T data) {
        Result<T> r = new Result<>();
        r.setSuccess(false);
        r.setMessage(message);
        r.setData(data);
        return r;
    }
}
