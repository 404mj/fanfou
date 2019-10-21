package com.chinamobile.sd.commonUtils;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/24 14:24
 */

import com.google.common.io.ByteStreams;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * 签名编码工具类
 */
public class CrypUtil {

    private static final Logger logger = LogManager.getLogger(CrypUtil.class);
    private static final String RSASecret = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMmDL1RFq+KJMyRxlr0Iz2XSGW" +
            "lHq2v0yzD5VgP23gYQyum1cSialGGO/wdraWjL2JFQLBFzwYXHO0UgWlbDLHaue59qk4rIVhO9y/ItwjxZbsL4rH5Jq6pD7s/UftcYPayb" +
            "Ezq053QRvtwkxKd8ZXSw8pUxVkvgo0R0BzbP1qBTAgMBAAECgYEAhtBqKKIX9Y2+lmIb68nqHv+pmoKlT6tNCS22ex58uWggqKyH0Rdhr8" +
            "cgHZGhysed0tHlOaPM07hPkdasOaGHq4rBWemxdt8HhJZWM1VP168QvOD/hSbgdTMJOc/99SKVJH4ODovME0M6IUBfLHTRVSl53FudYw4m" +
            "t4cbGI8BzjkCQQDrn4zT1Qv7iZ0BKQ2KeTc3mYB2T1YpiPJ7ANnmvI7Nw1l5O20Q3k1cad3Qhs5mFkbmxdj3zywCFdrL4vu4ID/3AkEA2v" +
            "B1Jwvv7ELtr0pHw+BI2bvTl4/jhhOeUzZGQ0lJBIKt9uhOUOTnZaF6ckAS1Rp1/isu4KJktH9I6gQ5ifaDhQJBANs4u/8O9jIW7CP7PST7" +
            "+JrbT8FDuWe1id1VgmcCQl9BBMsdtjD+k72Kphzwf7JNwbPOmjqd31UlXkdaLVc3cisCQApyRLS7PhRg0SR2/9uZvQAelSb7J5kDIJ/JzC" +
            "pzubcsYQ6R4QK9GUlhHg2LfxqJ661X9CGlvfZayGjaEMDDcV0CQQC0G5KHdkYtPeGpj3hMOUsODNPuSJgrLT47O56owIWHJJ/Iyn/uCDgD" +
            "OFBeazarypMT2ooMYaonR7YBxMur7Rl1";


    /**
     * @param clearText 明文
     * @return
     */
    public static String rsaSign(String clearText) {
        if (null == clearText || clearText.length() <= 0) {
            return Constant.EMPTYSTR;
        }

        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(RSASecret));
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(privateKey);
            signature.update(clearText.getBytes("utf-8"));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            logger.error(e.getStackTrace() + e.toString());
        }

        return Constant.EMPTYSTR;
    }

    /**
     * md5哈希
     *
     * @param source 明文
     */
    public static String MD5Sum(String source) {
        if (null == source || source.length() <= 0) {
            return Constant.EMPTYSTR;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(StringUtils.getBytesUtf8(source));
            return Hex.encodeHexString(messageDigest);

        } catch (Exception e) {
            logger.error(e.getStackTrace());
        }

        return Constant.EMPTYSTR;
    }


    public static void savePicFromUrl(String picUrl) {
        try (InputStream in = new URL(picUrl).openStream()) {
            Files.copy(in, Paths.get("C:/zsxhome/t_aiflow/image.jpg"));
        } catch (IOException e) {
            logger.error(e.getStackTrace());
        }
    }


    public static String encodeUrlPicToBase64(String picUrl) {
        try {
            URL imgURL = new URL(picUrl);
            InputStream is = imgURL.openStream();
            byte[] bytes = ByteStreams.toByteArray(is);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (MalformedURLException e) {
            logger.error(e.getMessage() + e.getStackTrace());
        } catch (IOException e) {
            logger.error(e.getMessage() + e.getStackTrace());
        }

        /*
        try {
            URL imgURL = new URL(picUrl);
            BufferedImage netPic = ImageIO.read(imgURL);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(netPic, "jpg", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        /*
        try {
//            File fpic = new File(picUrl);
            FileInputStream fis = new FileInputStream(fpic);
            byte[] bytes = new byte[(int) fpic.length()];
            fis.read(bytes);
            fis.close();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return Constant.EMPTYSTR;
    }

    public static void decodeBase64ToPic(String base64, String pathName) {
        try {
            FileOutputStream fos = new FileOutputStream(pathName);
            byte[] decoded = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
            fos.write(decoded);
            fos.close();
        } catch (FileNotFoundException e) {
            logger.error(e.getStackTrace());
        } catch (IOException e) {
            logger.error(e.getStackTrace());
        }
    }
}
