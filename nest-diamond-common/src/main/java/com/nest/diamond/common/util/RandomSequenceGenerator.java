package com.nest.diamond.common.util;

import com.google.common.collect.Lists;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomSequenceGenerator {
    private List<Integer> sequenceList;

    public RandomSequenceGenerator(Integer globalStartSequence, Integer globalEndSequence) {
        this.sequenceList = IntStream.rangeClosed(globalStartSequence, globalEndSequence).boxed().collect(Collectors.toList());
    }

    public List<Integer> randomSequences(Integer size) {
        List<Integer> indexList = getRandomSequence(0, sequenceList.size() - 1, size);
        List<Integer> randomSequences = indexList.stream().map(index -> sequenceList.get(index)).collect(Collectors.toList());
        randomSequences.forEach(randomSequence -> sequenceList.remove(randomSequence));
        return randomSequences;
    }

    public static List<Integer> getRandomSequence(Integer startSequence, Integer endSequence, Integer randomSize) {
        Assert.isTrue(((endSequence - startSequence) + 1) >= randomSize, "序列区间的数字要大于random个数");
        List<Integer> randomSequenceList = Lists.newArrayList();
        for (int i = 0; i < randomSize; i++) {
            Integer sequence = RandomUtil.randomIntegerClosed(startSequence, endSequence);
            while (isExist(randomSequenceList, sequence)) {
                sequence = RandomUtil.randomIntegerClosed(startSequence, endSequence);
            }
            randomSequenceList.add(sequence);
        }
        return randomSequenceList;
    }

    private static boolean isExist(List<Integer> randomSequenceList, Integer finalSequence) {
        return randomSequenceList.stream().filter(randomSequence -> randomSequence.equals(finalSequence)).findAny().isPresent();
    }
}