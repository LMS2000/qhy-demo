package com.course.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 大忽悠
 * @create 2023/2/14 10:04
 */
public interface ServiceExecutor {
    ExecutorService COURSE_THREAD_POOL= Executors.newFixedThreadPool(3);
}
