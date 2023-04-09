package com.course.exception;

import com.easyCode.feature.exception.WebExceptionHandler;
import com.easyCode.feature.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;


/**
 * @author 大忽悠
 * @create 2023/2/13 18:22
 */
@Component
public class CourseExceptionHandler extends WebExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({CourseException.class})
    public Result courseException(CourseException e) {
        return Result.error(e.getMessage(), e.getMessage());
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({AccessDeniedException.class})
    public Result accessDenyException(AccessDeniedException e) {
        return Result.error(e.getMessage(), e.getMessage());
    }
}
