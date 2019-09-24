package com.chinamobile.sd.commonUtils;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/24 14:24
 */

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * RSA签名与md5哈希工具类
 */
public class CrypUtil {

    private static final Logger logger = LoggerFactory.getLogger(CrypUtil.class);
    private static final String RSASecret = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMmDL1RFq+KJMyRxlr0Iz2XSGWlHq2v0yzD5VgP23gYQyum1cSialGGO/wdraWjL2JFQLBFzwYXHO0UgWlbDLHaue59qk4rIVhO9y/ItwjxZbsL4rH5Jq6pD7s/UftcYPaybEzq053QRvtwkxKd8ZXSw8pUxVkvgo0R0BzbP1qBTAgMBAAECgYEAhtBqKKIX9Y2+lmIb68nqHv+pmoKlT6tNCS22ex58uWggqKyH0Rdhr8cgHZGhysed0tHlOaPM07hPkdasOaGHq4rBWemxdt8HhJZWM1VP168QvOD/hSbgdTMJOc/99SKVJH4ODovME0M6IUBfLHTRVSl53FudYw4mt4cbGI8BzjkCQQDrn4zT1Qv7iZ0BKQ2KeTc3mYB2T1YpiPJ7ANnmvI7Nw1l5O20Q3k1cad3Qhs5mFkbmxdj3zywCFdrL4vu4ID/3AkEA2vB1Jwvv7ELtr0pHw+BI2bvTl4/jhhOeUzZGQ0lJBIKt9uhOUOTnZaF6ckAS1Rp1/isu4KJktH9I6gQ5ifaDhQJBANs4u/8O9jIW7CP7PST7+JrbT8FDuWe1id1VgmcCQl9BBMsdtjD+k72Kphzwf7JNwbPOmjqd31UlXkdaLVc3cisCQApyRLS7PhRg0SR2/9uZvQAelSb7J5kDIJ/JzCpzubcsYQ6R4QK9GUlhHg2LfxqJ661X9CGlvfZayGjaEMDDcV0CQQC0G5KHdkYtPeGpj3hMOUsODNPuSJgrLT47O56owIWHJJ/Iyn/uCDgDOFBeazarypMT2ooMYaonR7YBxMur7Rl1";


    /**
     * @param clearText 明文
     * @return
     */
    public static String rsaSign(String clearText) {
        if (null == clearText || clearText.length() <= 0) {
            return StringUtil.EMPTYSTR;
        }

        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(RSASecret));
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(privateKey);
            signature.update(clearText.getBytes("utf-8"));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            logger.error(e.toString());
        }

        return StringUtil.EMPTYSTR;
    }

    /**
     * md5哈希
     *
     * @param source 明文
     */
    public static String MD5Sum(String source) {
        if (null == source || source.length() <= 0) {
            return StringUtil.EMPTYSTR;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(StringUtils.getBytesUtf8(source));
            return Hex.encodeHexString(messageDigest);

        } catch (Exception e) {
            logger.error(e.toString());
        }

        return StringUtil.EMPTYSTR;
    }
}
