package com.nest.diamond.common.util.concurrent;

import com.google.common.util.concurrent.AbstractListeningExecutorService;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class PriorityListeningExecutorService extends AbstractListeningExecutorService {

    private final PriorityAwareThreadPoolExecutor delegate;

    public PriorityListeningExecutorService(PriorityAwareThreadPoolExecutor delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    /* -------- 核心：把 LFT 适配成 PrioritizedRunnable 提交，返回 LFT 给上层 -------- */

    @Override
    public <T> ListenableFuture<T> submit(Callable<T> task) {
        if (!(task instanceof PriorityAwareThreadPoolExecutor.PrioritizedCallable)) {
            throw new IllegalArgumentException("Callable MUST implement PrioritizedCallable");
        }
        int p = ((PriorityAwareThreadPoolExecutor.PrioritizedCallable<?>) task).priority();
        ListenableFutureTask<T> lft = ListenableFutureTask.create(task);

        // 用底层线程池的“显式优先级 submit”，把 LFT 当成 runnable 执行
        final Future<?> scheduled = delegate.submit(p, (Runnable) lft);

        // 若上层取消 LFT，则尝试把底层队列中的包装任务移除
        lft.addListener(() -> {
            if (lft.isCancelled() && scheduled instanceof Runnable) {
                delegate.remove((Runnable) scheduled);
            }
        }, MoreExecutors.directExecutor());

        return lft;
    }

    @Override
    public <T> ListenableFuture<T> submit(Runnable task, T result) {
        if (!(task instanceof PriorityAwareThreadPoolExecutor.PrioritizedRunnable)) {
            throw new IllegalArgumentException("Runnable MUST implement PrioritizedRunnable");
        }
        int p = ((PriorityAwareThreadPoolExecutor.PrioritizedRunnable) task).priority();
        ListenableFutureTask<T> lft = ListenableFutureTask.create(task, result);

        final Future<?> scheduled = delegate.submit(p, (Runnable) lft);
        lft.addListener(() -> {
            if (lft.isCancelled() && scheduled instanceof Runnable) {
                delegate.remove((Runnable) scheduled);
            }
        }, MoreExecutors.directExecutor());

        return lft;
    }

    @Override
    public ListenableFuture<?> submit(Runnable task) {
        return submit(task, null);
    }

    /* -------- 让 execute(...) 也保留强校验（可选） -------- */

    @Override
    public void execute(Runnable command) {
        // 仍然强制必须是 PrioritizedRunnable
        delegate.execute(command);
    }

    /* -------- 生命周期委托 -------- */

    @Override public void shutdown() { delegate.shutdown(); }
    @Override public List<Runnable> shutdownNow() { return delegate.shutdownNow(); }
    @Override public boolean isShutdown() { return delegate.isShutdown(); }
    @Override public boolean isTerminated() { return delegate.isTerminated(); }
    @Override public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.awaitTermination(timeout, unit);
    }
}
