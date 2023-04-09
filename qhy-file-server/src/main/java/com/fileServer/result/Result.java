package com.fileServer.result;

import lombok.Data;

@Data
public class Result {
    private Integer code;
    private String backMsg;
    private String frontMsg;
    private Object data;

    public static Result success() {
        Result result = new Result();
        result.setCode(200);
        return result;
    }

    public static Result success(Object obj) {
        Result result = new Result();
        result.setCode(200);
        result.setData(obj);
        return result;
    }

    public static Result error(String err) {
        Result result = new Result();
        result.setCode(500);
        result.setBackMsg(err);
        result.setFrontMsg(err);
        return result;
    }
}