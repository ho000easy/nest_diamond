package com.nest.diamond.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class RandomUtil {


    private static final String[] NUMS = {
            "123", "321", "122",
            "112", "1234", "1122", "1222", "1112", "12345", "111222", "12222", "11112", "1230", "12333", "123", "321",
            "122", "112", "1234", "1122", "1222", "1112", "12345", "111222", "12222", "11112", "1231", "12333", "123",
            "321", "122", "112", "1234", "1122", "1222", "1112", "12345", "111222", "12222", "11112", "1232", "12333",
            "123", "321", "122", "112", "1234", "1122", "1222", "1112", "12345", "111222", "12222", "11112", "1233",
            "12333", "123", "321", "122", "112", "1234", "1122", "1222", "1112", "12345", "111222", "12222", "11112",
            "1234", "12333", "123", "321", "122", "112", "1234", "1122", "1222", "1112", "12345", "111222", "12222",
            "11112", "1235", "12333", "123", "321", "122", "112", "1234", "1122",
            "1222", "1112", "12345", "111222", "12222", "11112", "1236", "12333", "123", "321", "122", "112", "1234",
            "1122", "1222", "1112", "12345", "111222", "12222", "11112", "1237", "12333", "123", "321", "122", "112",
            "1234", "1122", "1222", "1112", "12345", "111222", "12222", "11112",
            "1238", "12333", "123", "321", "122", "112", "1234", "1122", "1222", "1112", "12345", "111222", "12222",
            "11112", "1239", "12333", "234", "432", "233", "223", "2345", "2233", "2333", "2223", "23456", "222333", "23333",
            "22223", "2340", "23444", "234", "432", "233", "223", "2345", "2233", "2333", "2223", "23456", "222333", "23333", "22223", "2341",
            "23444", "234", "432", "233", "223", "2345", "2233", "2333", "2223", "23456", "222333", "23333", "22223", "2342", "23444", "234", "432",
            "233", "223", "2345", "2233", "2333", "2223", "23456", "222333", "23333", "22223", "2343", "23444", "234", "432", "233", "223", "2345", "2233",
            "2333", "2223", "23456", "222333", "23333", "22223", "2344", "23444", "234", "432", "233", "223", "2345", "2233", "2333", "2223", "23456", "222333",
            "23333", "22223", "2345", "23444", "234", "432", "233", "223", "2345", "2233", "2333", "2223", "23456", "222333", "23333", "22223", "2346", "23444",
            "234", "432", "233", "223", "2345", "2233", "2333", "2223", "23456", "222333", "23333", "22223", "2347", "23444",
            "234", "432", "233", "223", "2345", "2233", "2333", "2223", "23456", "222333", "23333", "22223", "2348",
            "23444", "234", "432", "233", "223", "2345", "2233", "2333", "2223", "23456", "222333", "23333", "22223",
            "2349", "23444", "345", "543", "344", "334", "3456", "3344", "3444", "3334", "34567", "333444", "34444",
            "33334", "3450", "34555", "345", "543", "344", "334", "3456", "3344", "3444", "3334", "34567", "333444", "34444", "33334", "3451",
            "34555", "345", "543", "344", "334", "3456", "3344", "3444", "3334", "34567", "333444", "34444", "33334", "3452", "34555", "345", "543",
            "344", "334", "3456", "3344", "3444", "3334", "34567", "333444", "34444", "33334", "3453", "34555", "345", "543", "344", "334", "3456",
            "3344", "3444", "3334", "34567", "333444", "34444", "33334", "3454", "34555", "345", "543", "344", "334", "3456", "3344", "3444", "3334",
            "34567", "333444", "34444", "33334", "3455", "34555", "345", "543", "344", "334", "3456", "3344", "3444", "3334", "34567", "333444", "34444",
            "33334", "3456", "34555", "345", "543", "344", "334", "3456", "3344", "3444", "3334", "34567", "333444", "34444", "33334", "3457", "34555", "345",
            "543", "344", "334", "3456", "3344", "3444", "3334", "34567", "333444", "34444", "33334", "3458", "34555", "345", "543", "344", "334", "3456", "3344",
            "3444", "3334", "34567", "333444", "34444", "33334", "3459", "34555", "456", "654", "455", "445", "4567", "4455", "4555", "4445", "45678", "444555",
            "45555", "44445", "4560", "45666", "456", "654", "455", "445", "4567", "4455", "4555", "4445", "45678", "444555", "45555", "44445", "4561", "45666",
            "456", "654", "455", "445", "4567", "4455", "4555", "4445", "45678", "444555", "45555", "44445", "4562", "45666", "456", "654", "455", "445", "4567",
            "4455", "4555", "4445", "45678", "444555", "45555", "44445", "4563", "45666", "456", "654", "455", "445", "4567", "4455", "4555", "4445", "45678",
            "444555", "45555", "44445", "4564", "45666", "456", "654", "455", "445", "4567", "4455", "4555", "4445", "45678", "444555", "45555", "44445", "4565",
            "45666", "456", "654", "455", "445", "4567", "4455", "4555", "4445", "45678", "444555", "45555", "44445", "4566", "45666", "456", "654", "455", "445",
            "4567", "4455", "4555", "4445", "45678", "444555", "45555", "44445", "4567", "45666", "456", "654", "455", "445", "4567", "4455", "4555", "4445", "45678",
            "444555", "45555", "44445", "4568", "45666", "456", "654", "455", "445", "4567", "4455", "4555", "4445", "45678", "444555", "45555", "44445", "4569", "45666",
            "567", "765", "566", "556", "5678", "5566", "5666", "5556", "56789", "555666", "56666", "55556", "5670", "56777", "567", "765", "566", "556", "5678", "5566",
            "5666", "5556", "56789", "555666", "56666", "55556", "5671", "56777", "567", "765", "566", "556", "5678", "5566", "5666", "5556", "56789", "555666", "56666",
            "55556", "5672", "56777", "567", "765", "566", "556", "5678", "5566", "5666", "5556", "56789", "555666", "56666", "55556", "5673", "56777", "567", "765", "566",
            "556", "5678", "5566", "5666", "5556", "56789", "555666",
            "56666", "55556", "5674", "56777", "567", "765", "566", "556", "5678", "5566", "5666", "5556", "56789", "555666", "56666", "55556", "5675", "56777", "567", "765", "566", "556", "5678",
            "5566", "5666", "5556", "56789", "555666", "56666", "55556", "5676", "56777", "567", "765", "566", "556", "5678", "5566", "5666", "5556", "56789", "555666", "56666",
            "55556", "5677", "56777", "567", "765", "566", "556", "5678", "5566", "5666", "5556", "56789", "555666", "56666", "55556", "5678", "56777", "567", "765", "566", "556",
            "5678", "5566", "5666", "5556", "56789", "555666", "56666", "55556", "5679", "56777"
    };

    private static final String[] WORDS = {
            "apple", "banana", "cat", "doge", "elephant", "fish", "giraffe", "horse", "iguana", "jaguar",
            "kangaroo", "lion", "monkey", "noodle", "ostrich", "penguin", "quail", "rabbit", "snake",
            "tiger", "unicorn", "vulture", "whale", "xylophone", "yak", "zebra",
            "bird", "car", "dolphin", "eagle", "fox", "goat", "hamster", "island", "jellyfish", "koala",
            "leopard", "mango", "nightingale", "octopus", "panda", "queen", "raccoon", "squirrel", "toucan",
            "umbrella", "violet", "watermelon", "xylophone", "yacht", "zeppelin",
            "amazon", "bmw", "coca-cola", "disney", "ebay", "facebook", "google", "honda", "ibm", "jaguar",
            "kfc", "lego", "microsoft", "nike", "oracle", "pepsi", "qantas", "redbull", "sony", "tesla",
            "uniqlo", "volkswagen", "walmart", "xerox", "yahoo", "zara", "musk", "space",
            "btc", "eth", "xrp", "ltc", "doge", "ada", "dot", "bnb", "link", "xlm",
            "cat", "dog", "bird", "fish", "bear", "lion", "tiger", "horse", "rabbit", "deer",
            "tree", "flower", "grass", "leaf", "rose", "vine", "fern", "bush", "palm", "oak",
            "sunset", "sunrise", "waterfall", "canyon", "glacier", "volcano", "meadow", "plateau", "valley", "cliff",
            "sunny", "cloudy", "rainy", "windy", "stormy", "foggy", "snowy", "hail", "sleet", "thunder", "web3", "nft", "lover",
            "hodler", "bear", "cool", "code", "hot", "login", "ens"
    };

    private static final String[] ADDITIONAL_WORDS = {
            "hello", "world", "goodbye", "friend", "family", "love", "happy", "sad",
            "time", "day", "night", "morning", "afternoon", "evening", "week", "month",
            "year", "today", "tomorrow", "yesterday", "sun", "moon", "star", "sky",
            "water", "fire", "earth", "air", "tree", "flower", "grass", "mountain",
            "river", "ocean", "island", "city", "country", "music", "art", "book",
            "movie", "photo", "food", "drink", "friendship", "laughter", "dream",
            "success", "failure", "hope", "faith", "kindness", "happiness", "peace",
            "brave", "strong", "beautiful", "gentle", "wise", "learn", "grow", "explore",
            "create", "inspire", "discover", "imagine", "play", "relax", "travel",
            "exercise", "smile", "laugh", "sing", "dance", "read", "write", "listen",
            "baby", "child", "parent", "sibling", "grandparent", "uncle", "aunt",
            "cousin", "nephew", "niece", "teacher", "student", "doctor", "nurse",
            "engineer", "scientist", "artist", "musician", "actor", "writer", "poet",
            "chef", "athlete", "pilot", "police", "firefighter", "soldier", "veteran",
            "computer", "internet", "phone", "camera", "television", "radio", "car",
            "bicycle", "airplane", "train", "boat", "bus", "school", "hospital",
            "library", "park", "beach", "mountain", "forest", "desert", "garden",
            "home", "office", "restaurant", "store", "market", "museum", "theater",
            "music", "art", "history", "science", "math", "language", "culture",
            "friendship", "relationship", "marriage", "divorce", "birth", "death",
            "celebration", "holiday", "tradition", "custom", "adventure", "challenge",
            "success", "failure", "dream", "goal", "hope", "faith", "kindness", "love",
            "happiness", "sadness", "anger", "fear", "anxiety", "stress", "peace",
            "bravery", "strength", "beauty", "wisdom", "knowledge", "learning", "growth",
            "exploration", "creativity", "imagination", "play", "relaxation", "travel",
            "exercise", "health", "smile", "laughter", "singing", "dancing", "reading",
            "writing", "listening", "watching", "thinking", "meditation", "reflection",
            "nature", "sunrise", "sunset", "beauty", "rain", "snow", "wind", "cloud",
            "bird", "animal", "insect", "pet", "wildlife", "ocean", "river", "lake",
            "mountain", "forest", "tree", "flower", "grass", "desert", "star", "moon",
            "sky", "planet", "earth", "universe", "technology", "innovation", "communication",
            "social", "media", "network", "virtual", "reality", "artificial", "intelligence",
            "data", "information", "software", "hardware", "algorithm", "programming",
            "cybersecurity", "encryption", "privacy", "science", "discovery", "research",
            "experiment", "theory", "mathematics", "geometry", "statistics", "physics",
            "chemistry", "biology", "astronomy", "history", "civilization", "ancient",
            "modern", "culture", "language", "literature", "poetry", "art", "painting",
            "sculpture", "music", "film", "theater", "architecture", "fashion", "sports",
            "football", "soccer", "basketball", "tennis", "golf", "baseball", "swimming",
            "running", "cycling", "hiking", "yoga", "fitness", "health", "nutrition",
            "cooking", "food", "drink", "restaurant", "coffee", "tea", "chocolate",
            "dessert", "friendship", "love", "relationship", "marriage", "divorce",
            "parenting", "education", "school", "college", "university", "learning",
            "student", "teacher", "knowledge", "career", "job", "success", "failure",
            "entrepreneur", "leadership", "business", "economy", "finance", "money",
            "investment", "marketing", "advertising", "customer", "product", "service",
            "travel", "adventure", "exploration", "vacation", "holiday", "tourism",
            "sightseeing", "nature", "beach", "mountain", "city", "culture", "history",
            "world", "country", "language", "music", "film", "art", "literature",
            "science", "technology", "sports", "hobby", "entertainment", "celebrity",
            "friend", "family", "party", "celebration", "memory", "emotion", "dream",
            "imagination", "inspiration", "creativity", "reflection", "mindfulness",
            "meditation", "peace", "happiness", "gratitude", "kindness", "compassion",
            "forgiveness", "hope", "faith", "courage", "strength", "perseverance",
            "challenge", "opportunity", "change", "growth", "transformation", "legacy",
            "legacy", "age", "youth", "wisdom", "knowledge", "curiosity", "curiosity",
            "equality", "diversity", "inclusion", "justice", "freedom", "democracy",
            "humanity", "environment", "climate", "sustainability", "conservation",
            "peace", "love", "harmony", "global", "local", "community", "together",
            "communication", "collaboration", "connection", "relationship", "network",
            "empathy", "compassion", "listening", "understanding", "support", "help",
            "volunteer", "service", "giving", "sharing", "appreciation", "reflection",
            "well-being", "mind", "body", "spirit", "balance", "resilience", "self-care",
            "self-improvement", "self-discovery", "self-expression", "self-confidence",
            "self-esteem", "achievement", "fulfillment", "joy", "fun", "laughter",
            "adventure", "play", "creativity", "passion", "purpose", "meaning", "transcendence",
            "connection", "gratitude", "kindness", "generosity", "forgiveness", "hope",
            "optimism", "faith", "courage", "resilience", "growth", "change", "challenge",
            "perseverance", "reflection", "mindfulness", "meditation", "peace", "love",
            "harmony", "environment", "nature", "sustainability", "community", "communication",
            "collaboration", "connection", "relationship", "empathy", "compassion",
            "listening", "understanding", "support", "help", "service", "giving",
            "sharing", "appreciation", "well-being", "mind", "body", "spirit", "balance",
            "resilience", "self-care", "self-improvement", "self-discovery", "self-expression",
            "self-confidence", "self-esteem", "achievement", "fulfillment", "joy",
            "fun", "laughter", "adventure", "play", "creativity", "passion", "purpose",
            "meaning", "transcendence"
    };


    private static Random r = new Random();

    private static final List<String> allList = new ArrayList<>();
    private static final List<String> wordList = new ArrayList<>();
    private static final List<String> words = new ArrayList<>();

    private static final List<String> shortWords = new ArrayList<>();

    private static final List<String> digtList = new ArrayList<>();



    static {
        for (String word : WORDS) {
            words.add(word);
            allList.add(word);
            if (word.length() > 3) {
                wordList.add(word);
            }

            if (word.length() < 7) {
                shortWords.add(word);
            }
        }

        for (String word : ADDITIONAL_WORDS) {
            words.add(word);
            allList.add(word);
            if (word.length() > 3) {
                wordList.add(word);
            }
            if (word.length() < 7) {
                shortWords.add(word);
            }
        }

        for (String num : NUMS) {
            allList.add(num);
            digtList.add(num);
        }
    }

    public static boolean isEventOccur(int probability) {
        // 校验概率值是否在1-100之间
        if (probability < 1 || probability > 100) {
            throw new IllegalArgumentException("概率值必须在1-100之间");
        }

        // 生成0-1之间的随机数
        Random random = new Random();
        double randomValue = random.nextDouble();

        // 将概率转换为0-1的小数，并判断随机数是否小于等于概率
        return randomValue <= probability / 100.0;
    }

    public static String generateRandomDigital() {
        Random random = new Random();
        int randomIndex = random.nextInt(digtList.size());
        return digtList.get(randomIndex);
    }

    public static String generateRandomWord() {
        Random random = new Random();
        int randomIndex = random.nextInt(wordList.size());
        return wordList.get(randomIndex);
    }


    public static String generateRandom() {
        Random random = new Random();
        int randomIndex = random.nextInt(allList.size());
        return allList.get(randomIndex);
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }


    public static String shortWords(List<String> shortWordList) {
        Random random = new Random();
        int randomIndex = random.nextInt(shortWordList.size());
        return shortWordList.get(randomIndex);
    }

    public static String generateRandomWordAll(Integer count) {
        Random random = new Random();
        int length = random.nextInt(count) + 1;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(words.size());
            sb.append(words.get(randomIndex)).append(" ");
        }
        return sb.toString();
    }


    public static String generateRandom(Integer min, Integer max, String randomStr) {
        Random random = new Random();
        int length = random.nextInt(min) + max;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(randomStr.length());
            char randomChar = randomStr.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }


    public static String generateRandomNum(Integer min, Integer max) {
        String CHARACTERS = "0123456789";
        Random random = new Random();
        int length = random.nextInt(min) + max;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }


    public static int randomIntegerClosed(int min, int max) {
        if (min == max) {
            return min;
        }

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return r.nextInt((max - min) + 1) + min;
    }


    public static BigDecimal randomPercent(BigDecimal number, BigDecimal lowerPercent, BigDecimal upperPercent, Integer maxScale) {
        if (NumUtils.xGreaterThanY(lowerPercent, upperPercent)) {
            throw new IllegalArgumentException(String.format("lowerPercent %s 大于等于 lowerPercent %s", lowerPercent, upperPercent));
        }
        Assert.isTrue(NumUtils.xLessThanY(lowerPercent, upperPercent), "百分比下限高于上限");
        BigDecimal min = number.multiply(lowerPercent).setScale(maxScale, RoundingMode.DOWN);
        int _minScale = NumUtils.findFirstNonZeroPosition(min);
        min = min.setScale(_minScale, RoundingMode.DOWN);
        BigDecimal max = number.multiply(upperPercent).setScale(maxScale, RoundingMode.DOWN);
        BigDecimal _min_scale_number = UnitUtil.fromWei2Decimal(BigInteger.ONE, maxScale);
        if (NumUtils.xEqualsY(max, _min_scale_number)) {
            return max;
        }
        Assert.isTrue(NumUtils.xGreaterThanY(max, _min_scale_number),
                String.format("number: %s; max: %s 小于等于按照maxScale %s 算出的最小值 %s", number, max, maxScale, _min_scale_number));
        return randomAmountAndPrecision(min, max, maxScale);
    }


    public static BigDecimal randomAmountAndPrecision(BigDecimal min, BigDecimal max, int maxScale) {
        return randomAmountAndPrecision(min, max, null, maxScale);
    }

    public static BigDecimal randomAmountAndPrecision(BigDecimal min, BigDecimal max, BigDecimal gap, int maxScale) {
        BigDecimal _max = gap == null ? max : max.subtract(gap);
        if (NumUtils.xEqualsY(min, _max)) {
            log.info("randomAmountAndPrecision,min==max,min:{},max:{},gap:{}", min, max, gap);
            return min;
        }
        Assert.isTrue(NumUtils.xGreaterThanY(_max, min), gap != null
                ? String.format("min + gap 大于等于 max, %s %s", min, _max)
                : String.format("min 大于等于 max, %s %s", min, _max));

        int _minScale = NumUtils.getNonZeroPrecision(min);
        Assert.isTrue(_minScale < maxScale + 1, String.format("min第一个小数位置大于maxScale, min %s, maxScale %d", min, maxScale));

        int scale = ThreadLocalRandom.current().nextInt(_minScale, maxScale + 1);
        BigDecimal randomDecimal = randomBigDecimal(min, _max);

        BigDecimal value = randomDecimal.setScale(scale, RoundingMode.DOWN);
        Assert.isTrue(!NumUtils.xEqualsY(value, BigDecimal.ZERO),
                String.format("min %s, max %s, 实际scale %d, maxScale %d，计算结果为0", min, max, scale, maxScale));
        return value;
    }

    public static BigDecimal randomBigDecimal(BigDecimal min, BigDecimal max) {
        if (NumUtils.xEqualsY(min, max)) {
            return min;
        }
        BigInteger factor = new BigInteger("10").pow(10);

        long minLong = min.multiply(new BigDecimal(factor)).longValue();
        long maxLong = max.multiply(new BigDecimal(factor)).longValue();

        long _amountLong = ThreadLocalRandom.current().nextLong(minLong, maxLong);
        return new BigDecimal(_amountLong).divide(new BigDecimal(factor));
    }


    public static BigDecimal randomReserved(BigDecimal amount, BigDecimal minReservedAmount, BigDecimal maxReservedAmount, Integer sendMaxScale) {
        Assert.isTrue(NumUtils.xGreaterThanY(amount, maxReservedAmount),
                String.format("原始金额 %s 小于最大保留金额 %s", amount, maxReservedAmount));

        BigDecimal reservedAmount = randomBigDecimal(minReservedAmount, maxReservedAmount);

        int minScale = NumUtils.getNonZeroPrecision(minReservedAmount);
        int scale = ThreadLocalRandom.current().nextInt(minScale, sendMaxScale + 1);

        return amount.subtract(reservedAmount).setScale(scale, RoundingMode.DOWN);
    }

    public static BigDecimal randomReservedFixScale(BigDecimal amount, BigDecimal minReservedAmount, BigDecimal maxReservedAmount, Integer sendMaxScale) {
        Assert.isTrue(NumUtils.xGreaterThanY(amount, maxReservedAmount),
                String.format("原始金额 %s 小于最大保留金额 %s", amount, maxReservedAmount));

        BigDecimal reservedAmount = randomBigDecimal(minReservedAmount, maxReservedAmount);

        return amount.subtract(reservedAmount).setScale(sendMaxScale, RoundingMode.DOWN);
    }

    public static String generateHex(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        while (sb.length() < length) {
            sb.append(Integer.toHexString(random.nextInt()));
        }

        return sb.substring(0, length).toUpperCase();
    }


    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789";
    private static SecureRandom random = new SecureRandom();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String generateRandomStringWithSpecialSymbol(int min, int max) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789!#_@&";

        int length = random.nextInt(max - min + 1) + min; // 确保长度在 min 到 max 之间

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String generateRandomStringLowerCase(Integer length) {
        String charStr = "0123456789abcdefghijklmnopqrstuvwxyz";
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charStr.length());
            char randomChar = charStr.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static String generateRandomString(Integer min, Integer max) {
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        int length = randomIntegerClosed(min, max);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static <T> T getRandomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }


    public static Long generateId() {
        Random random = new Random();
        // 定义范围：5 × 10^15 到 9.99 × 10^15，接近 5969538580013354
        long min = 1000000000000000L; // 5 × 10^15
        long max = 9999999999999999L; // 10^16 - 1
        return min + (long) (random.nextDouble() * (max - min));
    }


}
