package com.nest.diamond.common.util;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class AES {
    private static String charset = "utf-8";
    // 偏移量
    private static int offset = 16;
    private static String transformation = "AES/CBC/PKCS5Padding";
    private static String algorithm = "AES";


    public static String encryptWithPassword(String content, String password) {
        try {
            if (content == null) return null;

            // 1. 生成密钥：依然使用 MD5(password) 得到 16字节密钥
            byte[] rawKey = DigestUtils.md5(password);
            SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");

            // 2. 生成随机 IV (16字节)
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // 3. 初始化 Cipher
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);

            // 4. 执行加密
            byte[] encryptedBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));

            // 5. 拼接 IV 和 密文 (IV在前，密文在后)
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encryptedBytes.length);
            byteBuffer.put(iv);
            byteBuffer.put(encryptedBytes);

            // 6. 转 Base64
            return new Base64().encodeToString(byteBuffer.array());

        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }
    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param key     加密密码
     * @return
     */
    @SneakyThrows
    public static String encrypt(String content, String key) {
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), algorithm);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, offset);
            Cipher cipher = Cipher.getInstance(transformation);
            byte[] byteContent = content.getBytes(charset);
            cipher.init(Cipher.ENCRYPT_MODE, skey, iv);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return new Base64().encodeToString(result); // 加密
        } catch (Exception e) {
            // LogUtil.exception(e);
        }
        return null;
    }
 
    /**
     * AES（256）解密
     *
     * @param content 待解密内容
     * @param key     解密密钥
     * @return 解密之后
     * @throws Exception
     */
    @SneakyThrows
    public static String decrypt(String content, String key) {
        try {
 
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), algorithm);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, offset);
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, skey, iv);// 初始化
            byte[] result = cipher.doFinal(new Base64().decode(content));
            return new String(result); // 解密
        } catch (Exception e) {
            //LogUtil.exception(e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(AES.encrypt("heloxvwfwefwefwefwefwefwefwefwefwefwefwefwefwefwefwefw", "0123456789012345"));
    }
}