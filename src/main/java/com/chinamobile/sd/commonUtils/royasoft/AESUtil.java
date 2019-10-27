/************************************************
 *  Copyright © 2002-2015 上海若雅软件系统有限公司 *
 ************************************************/
package com.chinamobile.sd.commonUtils.royasoft;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * AES对称加密解密
 *
 * <p>
 * 一般由非对称密钥加密对称密钥，并非单独使用.<br>
 * 多数使用aesEncrypt和aesDecrypt加解密为base 64
 * </p>
 *
 * @author jxue
 * @Date 2014-9-17
 */
public class AESUtil {

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(encryptKey.getBytes());
        kgen.init(128, secureRandom);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(decryptKey.getBytes());
        kgen.init(128, secureRandom);

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes);
    }

    /**
     * base 64 encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        return new String(Base64Coder.encode(bytes));
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) {
        return Base64Coder.decode(base64Code);
    }

    /**
     * 获取byte[]的md5值
     *
     * @param bytes byte[]
     * @return md5
     * @throws Exception
     */
    public static byte[] md5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);

            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 获取字符串md5值
     *
     * @param msg
     * @return md5
     * @throws Exception
     */
    public static byte[] md5(String msg) {
        return md5(msg.getBytes());
    }

    /**
     * 结合base64实现md5加密
     *
     * @param msg 待加密字符串
     * @return 获取md5后转为base64
     * @throws Exception
     */
    public static String md5Encrypt(String msg) {
        return base64Encode(md5(msg));
    }

    /**
     * Encodes a String in AES-256 with a given key
     *
     * @param context
     * @param password
     * @param text
     * @return String Base64 and AES encoded String
     */
    public static String encode(String keyString, String stringToEncode) throws Exception {
        if (keyString.length() == 0 || keyString == null) {
            throw new NullPointerException("Please give Password");
        }

        if (stringToEncode.length() == 0 || stringToEncode == null) {
            throw new NullPointerException("Please give text");
        }

        SecretKeySpec skeySpec = getKey(keyString);
        byte[] clearText = stringToEncode.getBytes("UTF-8");

        // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
        final byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Cipher is not thread safe
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);

        String encrypedValue = Base64Android.encodeToString(cipher.doFinal(clearText), Base64Android.DEFAULT);
        return encrypedValue;
    }

    /**
     * AES加密
     *
     * @param keyString
     * @param byteToEncode
     * @return
     * @throws NullPointerException
     * @Description:
     */
    public static String encode(String keyString, byte[] byteToEncode) throws NullPointerException {
        if (keyString.length() == 0 || keyString == null) {
            throw new NullPointerException("Please give Password");
        }

        if (byteToEncode.length == 0 || byteToEncode == null) {
            throw new NullPointerException("Please give text");
        }

        try {
            SecretKeySpec skeySpec = getKey(keyString);
            byte[] clearText = byteToEncode;

            // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // Cipher is not thread safe
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);

            String encrypedValue = Base64Android.encodeToString(cipher.doFinal(clearText), Base64Android.DEFAULT);
            return encrypedValue;

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Decodes a String using AES-256 and Base64
     *
     * @param context
     * @param password
     * @param text
     * @return desoded String
     */
    public static String decode(String password, String text) throws Exception {

        if (password.length() == 0 || password == null) {
            throw new NullPointerException("Please give Password");
        }

        if (text.length() == 0 || text == null) {
            throw new NullPointerException("Please give text");
        }

        SecretKey key = getKey(password);

        // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
        final byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        byte[] encrypedPwdBytes = Base64Android.decode(text, Base64Android.NO_WRAP);
        // cipher is not thread safe
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

        String decrypedValue = new String(decrypedValueBytes, "UTF-8");
        return decrypedValue;
    }

    /**
     * Decodes a String using AES-256 and Base64
     *
     * @param context
     * @param password
     * @param text
     * @return desoded String
     */
    public static byte[] decodeByte(String password, String text) throws NullPointerException {

        if (password.length() == 0 || password == null) {
            throw new NullPointerException("Please give Password");
        }

        if (text.length() == 0 || text == null) {
            throw new NullPointerException("Please give text");
        }

        try {
            SecretKey key = getKey(password);

            // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            byte[] encrypedPwdBytes = Base64Android.decode(text, Base64Android.NO_WRAP);
            // cipher is not thread safe
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

            return decrypedValueBytes;

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对map进行解密
     *
     * @param password
     * @param paramMap
     * @return
     * @throws NullPointerException
     * @Description:
     */
    public static Map<String, String> decodeMap(String password, Map<String, String> paramMap) throws Exception {

        if (password.length() == 0 || password == null) {
            throw new NullPointerException("Please give Password");
        }

        if (null == paramMap || paramMap.size() == 0) {
            throw new NullPointerException("Please give text");
        }

        SecretKey key = getKey(password);

        // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
        final byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // cipher is not thread safe
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);

        Iterator<Entry<String, String>> iter = paramMap.entrySet().iterator();
        Map<String, String> resMap = new HashMap<String, String>();
        while (iter.hasNext()) {
            Entry<String, String> entry = (Entry<String, String>) iter.next();
            String paramKey = entry.getKey();
            String paramVal = entry.getValue();

            if (null != paramKey && !"".equals(paramKey) && null != paramVal && !"".equals(paramVal)) {
                String paramValStr = paramVal;
                String paramKeyStr = paramKey;
                byte[] encrypedPwdBytes = Base64Android.decode(paramValStr, Base64Android.NO_WRAP);
                byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

                String decrypedValue = new String(decrypedValueBytes, "UTF-8");
                resMap.put(paramKeyStr, decrypedValue);
            }
        }
        return resMap;
    }

    /**
     * Generates a SecretKeySpec for given password
     *
     * @param password
     * @return SecretKeySpec
     * @throws UnsupportedEncodingException
     */
    private static SecretKeySpec getKey(String password) throws UnsupportedEncodingException {

        // You can change it to 128 if you wish
        int keyLength = 256;
        byte[] keyBytes = new byte[keyLength / 8];
        // explicitly fill with zeros
        Arrays.fill(keyBytes, (byte) 0x0);

        // if password is shorter then key length, it will be zero-padded
        // to key length
        byte[] passwordBytes = password.getBytes("UTF-8");
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        return key;
    }
}
