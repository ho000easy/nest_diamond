package com.nest.diamond.common.util;

import lombok.SneakyThrows;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class SleepUtil {
    @SneakyThrows
    public static void randomSleep(int minSeconds, int maxSeconds) {
        int randomNum = ThreadLocalRandom.current().nextInt(minSeconds * 1000, maxSeconds * 1000);
        Thread.sleep(randomNum);
    }

    @SneakyThrows
    public static void randomSleepMills(int minMills, int maxMills) {
        int randomNum = ThreadLocalRandom.current().nextInt(minMills, maxMills);
        Thread.sleep(randomNum);
    }

    @SneakyThrows
    public static void sleep(int seconds) {
        Thread.sleep(seconds * 1000);
    }

    @SneakyThrows
    public static void sleepMills(int mills) {
        Thread.sleep(mills);
    }

    public static <T> T randomSleepFunction(int minSeconds, int maxSeconds, boolean shouldSleep, Supplier<T> supplier) {
        if (shouldSleep) {
            randomSleep(minSeconds, maxSeconds);
        }
        return supplier.get();
    }
}