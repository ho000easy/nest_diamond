package com.nest.diamond.common.util.concurrent;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class ParallelUtil {

    @SneakyThrows
    public static <T> List<T> parallelExecute(int threadNum, Collection<? extends Callable<T>> tasks) {
        return parallelExecute("default-pool-%d", threadNum, tasks);
    }

    @SneakyThrows
    public static <T> List<T> parallelExecute(String threadPoolNamePrefix, int coreThreadNum, int maxThreadNum, Collection<? extends Callable<T>> tasks) {
        ListeningExecutorService executorService = listeningExecutorService(threadPoolNamePrefix, coreThreadNum, maxThreadNum);
        List<ListenableFuture<T>> futures = Lists.newArrayList();
        for (Callable task : tasks) {
            futures.add(executorService.submit(task));
        }
        List ret = Futures.successfulAsList(futures).get();
        executorService.shutdownNow();
        return ret;
    }

    @SneakyThrows
    public static <T> List<T> parallelExecute(String threadPoolNamePrefix, int threadNum, Collection<? extends Callable<T>> tasks) {
        ListeningExecutorService executorService = listeningExecutorService(threadPoolNamePrefix, threadNum);
        List<ListenableFuture<T>> futures = Lists.newArrayList();
        for (Callable task : tasks) {
            futures.add(executorService.submit(task));
        }
        List ret = Futures.successfulAsList(futures).get();
        executorService.shutdownNow();
        return ret;
    }

    @SneakyThrows
    public static <T> List<ListenableFuture<T>> parallelExecuteAsync(String threadPoolNamePrefix, int threadNum, Collection<? extends Callable<T>> tasks) {
        ListeningExecutorService executorService = listeningExecutorService(threadPoolNamePrefix, threadNum);
        List<ListenableFuture<T>> futures = Lists.newArrayList();
        for (Callable<T> task : tasks) {
            futures.add(executorService.submit(task));
        }
        return futures;
    }

    @SneakyThrows
    public static <T> ExecutorServiceAndFutures<T> parallelExecuteAsyncV2(String threadPoolNamePrefix, int threadNum, Collection<? extends Callable<T>> tasks) {
        ListeningExecutorService executorService = listeningExecutorService(threadPoolNamePrefix, threadNum);
        List<ListenableFuture<T>> futures = Lists.newArrayList();
        for (Callable<T> task : tasks) {
            futures.add(executorService.submit(task));
        }
        return new ExecutorServiceAndFutures<T>(executorService, futures);
    }


    public static ListeningExecutorService listeningExecutorService(String threadPoolPrefix, int threadNum) {
        ThreadFactory guavaThreadFactory = new ThreadFactoryBuilder().setNameFormat(threadPoolPrefix).build();

        ThreadPoolExecutor threadPoolExecutor =  new ThreadPoolExecutor(threadNum, threadNum,
                5L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(), guavaThreadFactory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);

        return MoreExecutors.listeningDecorator(threadPoolExecutor);
    }

    public static ListeningExecutorService listeningPriorityExecutorService(String threadPoolPrefix, int threadNum) {
        ThreadFactory guavaThreadFactory = new ThreadFactoryBuilder().setNameFormat(threadPoolPrefix).build();

        PriorityAwareThreadPoolExecutor threadPoolExecutor =  new PriorityAwareThreadPoolExecutor(threadNum, threadNum,
                5L, TimeUnit.MINUTES, guavaThreadFactory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);

        return new PriorityListeningExecutorService(threadPoolExecutor);
    }

    public static ListeningExecutorService listeningExecutorService(String threadPoolPrefix, int coreThreadNum, int maxThreadNum) {
        ThreadFactory guavaThreadFactory = new ThreadFactoryBuilder().setNameFormat(threadPoolPrefix).build();

        ThreadPoolExecutor threadPoolExecutor =  new ThreadPoolExecutor(coreThreadNum, maxThreadNum,
                5L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(), guavaThreadFactory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);

        return MoreExecutors.listeningDecorator(threadPoolExecutor);

    }

    @Data
    @AllArgsConstructor
    public static class ExecutorServiceAndFutures<T>{
        private ExecutorService executorService;
        private List<ListenableFuture<T>> futures;
    }

}
