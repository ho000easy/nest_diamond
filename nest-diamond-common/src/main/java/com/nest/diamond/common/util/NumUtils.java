package com.nest.diamond.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumUtils {
    public static final BigInteger BIG_INTEGER_POWER256 = new BigInteger("2").pow(256).subtract(new BigInteger("1"));

    // ------------------BigInteger
    public static boolean xGreaterThanY(BigInteger x, BigInteger y){
        return x.compareTo(y) > 0;
    }

    public static boolean xLessThanY(BigInteger x, BigInteger y){
        return x.compareTo(y) < 0;
    }

    public static boolean xLessThanOrEquals(BigInteger x, BigInteger y){
        return x.compareTo(y) <= 0;
    }

    public static boolean xEqualsY(BigInteger x, BigInteger y){
        return x.compareTo(y) == 0;
    }

    public static boolean xEqualsY(Long x, Long y){
        return x.compareTo(y) == 0;
    }

    public static boolean xGreaterOrEqualsThanY(BigInteger x, BigInteger y){
        return x.compareTo(y) >= 0;
    }
    public static boolean xGreaterOrEqualsThanY(BigDecimal x, BigDecimal y){
        return x.compareTo(y) >= 0;
    }

    public static boolean xLessThanOrEqualsY(BigDecimal x, BigDecimal y){
        return x.compareTo(y) <= 0;
    }
    public static boolean xGreaterThanZero(BigInteger x){
        return x.compareTo(new BigInteger("0")) > 0;
    }

    public static boolean xGreaterThanZero(BigDecimal x){
        return x.compareTo(BigDecimal.ZERO) > 0;
    }


    public static boolean xLessThanZero(BigInteger x){
        return x.compareTo(new BigInteger("0")) < 0;
    }

    // ------------------Integer
    public static boolean xGreaterThanY(Integer x, Integer y){
        return x.compareTo(y) > 0;
    }
    public static boolean xGreaterThanOrEqualsY(Integer x, Integer y){
        return x.compareTo(y) >= 0;
    }

    public static boolean xLessThanY(Integer x, Integer y){
        return x.compareTo(y) < 0;
    }

    public static boolean xLessThanOrEquals(Integer x, Integer y){
        return x.compareTo(y) <= 0;
    }

    public static boolean xEqualsY(Integer x, Integer y){
        return x.compareTo(y) == 0;
    }

    public static boolean xGreaterThanY(BigDecimal x, BigDecimal y){
        return x.compareTo(y) > 0;
    }

    public static boolean xGreaterThanOrEqualsY(BigDecimal x, BigDecimal y){
        return x.compareTo(y) >= 0;
    }

    public static boolean xLessThanY(BigDecimal x, BigDecimal y){
        return x.compareTo(y) < 0;
    }

    public static boolean xLessThanOrEquals(BigDecimal x, BigDecimal y){
        return x.compareTo(y) <= 0;
    }

    public static boolean xEqualsY(BigDecimal x, BigDecimal y){
        return x.compareTo(y) == 0;
    }


    public static boolean xLessThanOrEqualsZero(BigInteger x){
        return x.compareTo(new BigInteger("0")) <= 0;
    }
    public static boolean xLessThanY(Long x, Long y){
        return x.compareTo(y) < 0;
    }
    public static boolean xGreaterOrEqualsThanY(Long x, Long y){
        return x.compareTo(y) >= 0;
    }

    public static boolean xGreaterThanY(Long x, Long y){
        return x.compareTo(y) > 0;
    }

    public static boolean xGreaterOrEqualsThanY(Integer x, Integer y){
        return x.compareTo(y) >= 0;
    }
    public static boolean xGreaterThanZero(Long x){
        return x.compareTo(0L) > 0;
    }

    public static boolean xLessThanZero(Long x){
        return x.compareTo(0L) < 0;
    }

    public static boolean xLessThanOrEqualsZero(Long x){
        return x.compareTo(0L) <= 0;
    }



    public static int getNonZeroPrecision(BigDecimal number) {// 获取第一个小数位置
        String numberStr = number.toPlainString();
        String[] splits = numberStr.split("\\.");
        if(splits.length <= 1){
            return 0;
        }
        numberStr = splits[1];
        for (int i = 0; i < numberStr.length(); i++) {
            if (numberStr.charAt(numberStr.length()-1-i) != '0') {
                return numberStr.length() - i;
            }
        }
        return 0;
    }

    public static int findFirstNonZeroPosition(BigDecimal number) {
        String numberStr = number.toPlainString();
        String[] splits = numberStr.split("\\.");
        if(splits.length <= 1){
            return 0;
        }
        numberStr = splits[1];
        for (int i = 0; i < numberStr.length(); i++) {
            if (numberStr.charAt(i) != '0') {
                return i + 1;
            }
        }
        return 0;
    }

}
