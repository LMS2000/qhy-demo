package com.gateway.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 大忽悠
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Result {
    private Integer code;
    private String backMsg;
    private String frontMsg;
    private Object data;

    public static Result error(Integer errorCode, String backErrMsg, String frontErrMsg) {
        Result result = new Result();
        result.setCode(errorCode);
        result.setBackMsg(backErrMsg);
        result.setFrontMsg(frontErrMsg);
        return result;
    }
}
