package com.nest.diamond.common.util;

import lombok.SneakyThrows;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class SignUtil {

    public static String signPrefixedMessage(Credentials credentials, String message){
        Sign.SignatureData signature = Sign.signPrefixedMessage(message.getBytes(StandardCharsets.UTF_8), credentials.getEcKeyPair());
        return signData2Hash(signature);
    }

    public static String signUnHashedMessage(Credentials credentials, String message){
        Sign.SignatureData signature = Sign.signMessage(message.getBytes(StandardCharsets.UTF_8), credentials.getEcKeyPair());
        return signData2Hash(signature);
    }

    public static String signUnHashedMessage(Credentials credentials, byte[] bytes){
        Sign.SignatureData signature = Sign.signMessage(bytes, credentials.getEcKeyPair());
        return signData2Hash(signature);
    }
    public static String signHashedMessage(Credentials credentials, byte[] bytes){
        Sign.SignatureData signature = Sign.signMessage(bytes, credentials.getEcKeyPair(), false);
        return signData2Hash(signature);
    }

    public static byte[] signHashedMessage2Bytes(Credentials credentials, byte[] bytes){
        Sign.SignatureData signature = Sign.signMessage(bytes, credentials.getEcKeyPair(), false);
        return signData2Bytes(signature);
    }

    @SneakyThrows
    public static String recover2Address(String messageHashHex, String signature){
        byte[] bytes = Numeric.hexStringToByteArray(signature);
        byte[] v = new byte[1];
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        System.arraycopy(bytes, 0, r, 0, 32);
        System.arraycopy(bytes, 32, s, 0, 32);
        System.arraycopy(bytes, 64, v, 0, 1);
        Sign.SignatureData signatureData = new Sign.SignatureData(v, r, s);
        BigInteger _publicKey = Sign.signedMessageToKey(Numeric.hexStringToByteArray(messageHashHex), signatureData);
        String _address = "0x" + Keys.getAddress(_publicKey);
        return _address;
    }

    public static Sign.SignatureData convert(String signature){
        byte[] bytes = Numeric.hexStringToByteArray(signature);
        return convert(bytes);
    }

    public static Sign.SignatureData convert(byte[] signature){
        byte[] v = new byte[1];
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        System.arraycopy(signature, 0, r, 0, 32);
        System.arraycopy(signature, 32, s, 0, 32);
        System.arraycopy(signature, 64, v, 0, 1);
        Sign.SignatureData signatureData = new Sign.SignatureData(v, r, s);
        return signatureData;
    }

    @SneakyThrows
    public static String recover2Address(String messageHashHex, Sign.SignatureData signatureData){
        BigInteger _publicKey = Sign.signedMessageToKey(Numeric.hexStringToByteArray(messageHashHex), signatureData);
        String _address = "0x" + Keys.getAddress(_publicKey);
        return _address;
    }



    private static String signData2Hash(Sign.SignatureData signature) {
        return Numeric.toHexString(signData2Bytes(signature));
    }

    private static byte[] signData2Bytes(Sign.SignatureData signature) {
        byte[] retval = new byte[65];
        System.arraycopy(signature.getR(), 0, retval, 0, 32);
        System.arraycopy(signature.getS(), 0, retval, 32, 32);
        System.arraycopy(signature.getV(), 0, retval, 64, 1);
        return retval;
    }
}
