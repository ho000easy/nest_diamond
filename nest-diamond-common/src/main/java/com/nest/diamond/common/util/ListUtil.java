package com.nest.diamond.common.util;

import java.util.*;
import java.util.stream.Collectors;

public class ListUtil {

    public static <T> List<T> pickRandom(List<T> list, int n) {
        if (n > list.size()) {
            throw new IllegalArgumentException("n 大于 list 大小");
        }
        List<T> copy = new ArrayList<>(list); // 避免打乱原始 list
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }


    // 将List数组分成指定大小的子列表
    public static <T> List<List<T>> partitionList(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            int endIndex = Math.min(i + size, list.size());
            partitions.add(list.subList(i, endIndex));
        }
        return partitions;
    }


    public static <E, C> List<List<E>> findConsecutiveObjects(List<E> inputList, List<C> comparisonList, ConsecutivePredication<E, C> consecutivePredication) {
        List<List<E>> result = new ArrayList<>();
        List<E> currentCombination = new ArrayList<>();
        int comparisonIndex = 0;

        for (E obj : inputList) {
            C comparison = comparisonList.get(comparisonIndex);
            if (consecutivePredication.test(obj, comparison)) {
                currentCombination.add(obj);
                comparisonIndex++;
                if (comparisonIndex == comparisonList.size()) {
                    result.add(new ArrayList<>(currentCombination));
                    currentCombination.clear();
                    comparisonIndex = 0;
                }
            } else {
                currentCombination.clear();
                comparisonIndex = 0;
            }
        }

        return result;
    }

    /**
     每段的数量（元素个数）必须是 5 的倍数。
     前 N-1 段的数量必须相等。
     最后一段包含剩余元素，数量不要求是 5 的倍数。
     */
    public static List<int[]> divideSequence(int startSequence, int endSequence, int n) {
        if (startSequence > endSequence || n <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        List<int[]> result = new ArrayList<>();
        int totalLength = endSequence - startSequence + 1;

        // 如果n=1，直接返回整个序列
        if (n == 1) {
            result.add(new int[]{startSequence, endSequence});
            return result;
        }

        // 计算5的份数
        int portions = totalLength / 5; // 每份5个元素，忽略余数

        // 将份数均分到N段，前N-1段份数相等
        int portionsPerSegment = portions / n; // 每段的基本份数
        int remainingPortions = portions % n; // 剩余份数

        int currentStart = startSequence;
        for (int i = 0; i < n; i++) {
            // 计算当前段的份数
            int currentPortions = portionsPerSegment + (i < remainingPortions ? 1 : 0);
            if (i == n - 1) {
                // 最后一段包含所有剩余份数
                currentPortions = portions - (portionsPerSegment * (n - 1));
            }
            if (currentPortions == 0 && i < n - 1) continue; // 前N-1段跳过空段

            // 计算当前段的数量（份数 * 5）
            int currentSegmentSize = currentPortions * 5;
            if (currentSegmentSize == 0 && i < n - 1) continue; // 前N-1段不能为0

            // 如果是最后一段，包含所有剩余元素（包括不足5的部分）
            if (i == n - 1 && currentStart <= endSequence) {
                currentSegmentSize = endSequence - currentStart + 1;
            }

            if (currentSegmentSize > 0) {
                int currentEnd = currentStart + currentSegmentSize - 1;
                result.add(new int[]{currentStart, currentEnd});
                currentStart = currentEnd + 1;
            }
        }

        return result;
    }

    /**
     * 将 list 分成 n 个大小尽量均衡的子列表（差值 ≤ 1）。
     * @param list 原列表
     * @param n    目标份数（<=0 时抛异常；>size 时会出现若干空列表）
     * @param copy 是否拷贝（true = 返回独立 ArrayList；false = 返回 subList 视图）
     */
    public static <T> List<List<T>> splitIntoN(List<T> list, int n, boolean copy) {
        Objects.requireNonNull(list, "list");
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");

        int size = list.size();
        // 基础份额 & 余数（前 r 份分到 +1）
        int q = size / n, r = size % n;

        List<List<T>> result = new ArrayList<>(n);
        int from = 0;
        for (int i = 0; i < n; i++) {
            int partSize = q + (i < r ? 1 : 0);
            int to = from + partSize;
            List<T> view = list.subList(Math.min(from, size), Math.min(to, size));
            result.add(copy ? new ArrayList<>(view) : view);
            from = to;
        }
        return result;
    }

    // 便捷重载：默认返回独立副本，避免 subList 带来的结构修改异常
    public static <T> List<List<T>> splitIntoN(List<T> list, int n) {
        return splitIntoN(list, n, true);
    }

    public static List<Integer> toIntList(String s) {
        if (s == null || s.trim().isEmpty()) return Collections.emptyList();
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .map(Integer::valueOf)   // 非法数字会抛 NumberFormatException
                .collect(Collectors.toList());
    }

    public static List<Long> toLongList(String s) {
        if (s == null || s.trim().isEmpty()) return Collections.emptyList();
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public static String intsToCsv(List<Integer> list) {
        return (list == null || list.isEmpty())
                ? ""
                : list.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    public interface ConsecutivePredication<E, C>{
        Boolean test(E element, C comparison);
    }
}
