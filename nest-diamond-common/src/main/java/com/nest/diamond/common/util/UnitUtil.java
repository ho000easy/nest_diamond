package com.nest.diamond.common.util;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class UnitUtil {
    public static String fromEther2Str(BigInteger value){
        return Convert.fromWei(value.toString(), Convert.Unit.ETHER).toPlainString();
    }

    public static String fromGWei2Str(BigInteger value){
        return Convert.fromWei(value.toString(), Convert.Unit.GWEI).toString();
    }

    public static String fromMWei2Str(BigInteger value){
        return Convert.fromWei(value.toString(), Convert.Unit.MWEI).toString();
    }

    public static BigDecimal fromEther2Decimal(BigInteger value){
        return Convert.fromWei(value.toString(), Convert.Unit.ETHER);
    }

    public static BigDecimal fromGWei2Decimal(BigInteger value){
        return Convert.fromWei(value.toString(), Convert.Unit.GWEI);
    }

    public static BigInteger ether2Wei(String etherStr){
        return Convert.toWei(etherStr, Convert.Unit.ETHER).toBigInteger();
    }

    public static BigInteger ether2Wei(BigDecimal etherDecimal){
        return Convert.toWei(etherDecimal, Convert.Unit.ETHER).toBigInteger();
    }

    public static BigInteger ether2MWEI(BigDecimal etherDecimal){
        return Convert.toWei(etherDecimal, Convert.Unit.MWEI).toBigInteger();
    }


    public static BigDecimal toEtherWeiDecimal(String etherStr){
        return Convert.toWei(etherStr, Convert.Unit.ETHER);
    }


    public static BigInteger precisionEther(BigInteger amount, int precision){
        return Convert.toWei(fromEther2Decimal(amount).setScale(precision, RoundingMode.DOWN), Convert.Unit.ETHER).toBigInteger();
    }

    // 币的真实余额
    public static BigDecimal fromWei2Decimal(BigInteger balance, int factor){
        return new BigDecimal(balance).divide(BigDecimal.TEN.pow(factor));
    }

    public static BigInteger fromDecimal2Wei(BigDecimal amount, int factor){
        return amount.multiply(BigDecimal.TEN.pow(factor)).toBigInteger();
    }

    public static Convert.Unit fromFactor(int factor){
        Convert.Unit[] units = Convert.Unit.values();
        for(Convert.Unit unit : units){
            if(BigDecimal.TEN.pow(factor).equals(unit.getWeiFactor())){
                return unit;
            }
        }
        throw new IllegalArgumentException("找不到对应的unit factor " + factor);
    }

}
