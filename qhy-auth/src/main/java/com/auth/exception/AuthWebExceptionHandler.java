package com.auth.exception;

import com.easyCode.feature.exception.WebExceptionHandler;
import com.easyCode.feature.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author 大忽悠
 * @create 2023/2/9 12:06
 */
@Component
public class AuthWebExceptionHandler extends WebExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AuthException.class})
    public Result authException(AuthException e) {
        return Result.error(e.getMessage(), e.getMessage());
    }
}
