package com.nest.diamond.common.util.concurrent;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/** 任务必须自报优先级（越小越高），否则直接抛异常 */
public class PriorityAwareThreadPoolExecutor extends ThreadPoolExecutor {


    /** 让任务“自报优先级”的接口（二选一实现其一即可） */
    public interface PrioritizedCallable<V> extends Callable<V> { int priority(); }
    public interface PrioritizedRunnable extends Runnable { int priority(); }

    private static final AtomicLong SEQ = new AtomicLong(0);



    /* ================= 构造器 ================= */

    /**
     * 建议：prestartAllCores=true，保证任务先入队再按优先级取出；
     * 同时不建议 allowCoreThreadTimeOut(true)，否则“活跃线程 < core”时任务可能绕过队列直接执行。
     */
    public PriorityAwareThreadPoolExecutor(int corePoolSize,
                                           int maximumPoolSize,
                                           long keepAliveTime,
                                           TimeUnit unit,
                                           ThreadFactory threadFactory,
                                           boolean prestartAllCores) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit,
              new PriorityBlockingQueue<Runnable>(), // 关键：优先级队列
              Objects.requireNonNull(threadFactory, "threadFactory"),
              new AbortPolicy());
        // 为保证严格排序，核心线程建议常驻
        super.allowCoreThreadTimeOut(false);
        if (prestartAllCores) super.prestartAllCoreThreads();
    }

    public PriorityAwareThreadPoolExecutor(int corePoolSize,
                                           int maximumPoolSize,
                                           long keepAliveTime,
                                           TimeUnit unit,
                                           ThreadFactory threadFactory) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, true);
    }

    /* ================= 强校验：不是 Prioritized* 就抛异常 ================= */

    private static <T> int resolvePriorityOrThrow(Callable<T> c) {
        if (!(c instanceof PrioritizedCallable))
            throw new IllegalArgumentException("Callable MUST implement PrioritizedCallable");
        return ((PrioritizedCallable<?>) c).priority();
    }

    private static int resolvePriorityOrThrow(Runnable r) {
        if (!(r instanceof PrioritizedRunnable))
            throw new IllegalArgumentException("Runnable MUST implement PrioritizedRunnable");
        return ((PrioritizedRunnable) r).priority();
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        // submit(Callable) 走这里：强制校验优先级
        return new PrioritizedFutureTask<>(callable, resolvePriorityOrThrow(callable));
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        // submit(Runnable, result) 走这里：同样强制校验
        return new PrioritizedFutureTask<>(runnable, value, resolvePriorityOrThrow(runnable));
    }

    @Override
    public void execute(Runnable command) {
        // 直接 execute(Runnable) 也强制要求是 PrioritizedRunnable
        if (command instanceof PrioritizedFutureTask) {
            super.execute(command);
            return;
        }
        if (!(command instanceof PrioritizedRunnable)) {
            throw new IllegalArgumentException("Runnable MUST implement PrioritizedRunnable");
        }
        super.execute(new PrioritizedFutureTask<>(command, null, ((PrioritizedRunnable) command).priority()));
    }

    /* =================（可选）显示优先级的便捷提交接口 =================
       如果你坚持所有任务都“自报优先级”，可以把这两个方法删掉以避免双通道。 */
    public <T> Future<T> submit(int priority, Callable<T> task) {
        Objects.requireNonNull(task, "task");
        // 允许通过显式优先级提交（跳过自报接口）；如不需要可删除
        PrioritizedFutureTask<T> f = new PrioritizedFutureTask<>(task, priority);
        super.execute(f);
        return f;
    }

    public Future<?> submit(int priority, Runnable task) {
        Objects.requireNonNull(task, "task");
        PrioritizedFutureTask<?> f = new PrioritizedFutureTask<>(task, null, priority);
        super.execute(f);
        return f;
    }

    /** 入队元素：可比较（priority asc, seq asc），确保同优先级 FIFO */
    private static final class PrioritizedFutureTask<V>
            extends FutureTask<V> implements Comparable<PrioritizedFutureTask<?>> {
        final int priority; final long seq;
        PrioritizedFutureTask(Callable<V> c, int p){ super(c); this.priority=p; this.seq=SEQ.getAndIncrement(); }
        PrioritizedFutureTask(Runnable r, V v, int p){ super(r, v); this.priority=p; this.seq=SEQ.getAndIncrement(); }
        @Override
        public int compareTo(PrioritizedFutureTask<?> o) {
            // priority DESC（大在前），同优先级按 seq ASC（先提交先执行）
            int c = Integer.compare(o.priority, this.priority);
            return (c != 0) ? c : Long.compare(this.seq, o.seq);
        }
    }
}
