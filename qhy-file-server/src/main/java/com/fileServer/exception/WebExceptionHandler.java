package com.fileServer.exception;

import com.fileServer.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author 大忽悠
 * @create 2023/2/13 14:33
 */
@ControllerAdvice
@Slf4j
public class WebExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result exception(Exception exception){
        log.error("出现异常: ",exception);
        return Result.error(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileException.class)
    public Result fileException(FileException exception){
        log.error("出现异常: ",exception);
        return Result.error(exception.getMessage());
    }
}
