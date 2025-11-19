package com.nest.diamond.common.util;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;

import java.security.SecureRandom;
import java.util.List;

import static org.web3j.crypto.Bip32ECKeyPair.HARDENED_BIT;

@Slf4j
public class Bip44Util {

    public static List<Credentials> getHDCredentialsList(String seed, int from, int to) {
        Assert.isTrue(MnemonicUtils.validateMnemonic(seed), "seed格式不合法");

        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(seed, null));
        List<Credentials> credentialsList = Lists.newArrayList();
        for (int i = from; i < to; i++) {
            int[] path = {44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0, i};
            Bip32ECKeyPair x = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path);
            Credentials credentials = Credentials.create(x.getPrivateKey().toString(16));
            credentialsList.add(credentials);
        }
        return credentialsList;
    }

    public static List<String> getHDPrivateKeyList(String seed, int from, int to) {
        Assert.isTrue(MnemonicUtils.validateMnemonic(seed), "seed格式不合法");
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(seed, null));
        List<String> privateKeyList = Lists.newArrayList();
        for (int i = from; i < to; i++) {
            int[] path = {44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0, i};
            Bip32ECKeyPair x = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path);
            Credentials credentials = Credentials.create(x.getPrivateKey().toString(16));
            privateKeyList.add(padLeftZeros(credentials.getEcKeyPair().getPrivateKey().toString(16), 64));
        }
        return privateKeyList;
    }

    public static String getPrivateKey(Credentials credentials){
        return padLeftZeros(credentials.getEcKeyPair().getPrivateKey().toString(16), 64);
    }

    public static String getFixedHDPrivateKey(String seed, int index){
        Assert.isTrue(MnemonicUtils.validateMnemonic(seed), "seed格式不合法");

        return getHDPrivateKeyList(seed, index, index+1).get(0);
    }

    public static String getFixedHDAddress(String seed, int index){
        Assert.isTrue(MnemonicUtils.validateMnemonic(seed), "seed格式不合法");
        return getFixedHDCredentials(seed, index).getAddress();
    }

    public static List<String> getHDAddrList(String seed, int from, int to) {
        Assert.isTrue(MnemonicUtils.validateMnemonic(seed), "seed格式不合法");

        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(seed, null));
        List<String> addrList = Lists.newArrayList();
        for (int i = from; i < to; i++) {
            int[] path = {44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0, i};
            Bip32ECKeyPair x = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path);
            Credentials credentials = Credentials.create(x.getPrivateKey().toString(16));
            addrList.add(credentials.getAddress());
        }
        return addrList;
    }

    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
//        System.out.println("raw str " + inputString);
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }

    public static Credentials getFixedHDCredentials(String seed, int index) {
        Assert.isTrue(MnemonicUtils.validateMnemonic(seed), "seed格式不合法");

        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(seed, null));
        int[] path = {44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0, index};
        Bip32ECKeyPair x = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path);
        return Credentials.create(x.getPrivateKey().toString(16));
    }

    public static List<Credentials> getHDCredentialsList(String seed, int length) {
        Assert.isTrue(MnemonicUtils.validateMnemonic(seed), "seed格式不合法");
        return getHDCredentialsList(seed, 0, length);
    }

    public static String generateSeed(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] initialEntropy = new byte[16];//算法需要，必须是被4整除
        secureRandom.nextBytes(initialEntropy);
        String seed = MnemonicUtils.generateMnemonic(initialEntropy);
        Assert.isTrue(MnemonicUtils.validateMnemonic(seed), "seed格式不合法");
        return seed;
    }



    @SneakyThrows
    public static void main(String[] args) {
        Credentials credentials = Credentials.create("0xc07424aae86b9341712212d5b12f6a70eb963a93c07727862141c563356f1a35");
        String address = credentials.getAddress();
//        List<String> addressList = Bip44Util.getHDAddrList("", 1, 150);
//        System.out.println(String.join("\r\n", addressList));
        for (int i = 0; i < 1; i++) {
            String seed = Bip44Util.generateSeed();
            System.out.println(seed);
            List<String> addressList = Bip44Util.getHDAddrList(seed, 0, 10);
            System.out.println(String.join("\r\n", addressList));
        }
    }
}
