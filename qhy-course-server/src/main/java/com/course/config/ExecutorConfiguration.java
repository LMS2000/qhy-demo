package com.course.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 大忽悠
 * @create 2023/3/8 9:12
 */
@Configuration
public class ExecutorConfiguration {

    @Bean("asyncExecutor")
    public Executor asyncExecutor() {
        //创建一个线程池，核心线程数为2，最大线程池大小为5，线程等待时间为60秒，创建线程用自定义的线程工厂
        //使用CallerRunsPolicy策略(线程打满和队列满之后的请求由main自调用方（main）处理)
       return new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS,
               new ArrayBlockingQueue<>(100), new ThreadFactory() {
           private AtomicInteger count=new AtomicInteger(1);
           @Override
           public Thread newThread(@NotNull Runnable r) {
               return new Thread(r,"async-thread-pool-"+count.getAndIncrement());
           }
       },new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
