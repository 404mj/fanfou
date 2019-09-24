package com.chinamobile.sd.commonUtils;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/24 15:47
 */

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 根据 https://open.andmu.cn/doc/api 规范
 */
public class RestClient4Andmu {
    private Logger logger = LoggerFactory.getLogger(RestClient4Andmu.class);
    private static final String APPID = "6e4268766a5c4445b6d1ec16d0f24636";
    private static final String SECRET = "uxG8HrcoMDWu0eqZ";
    private static final String VERSION = "1.0.0";

    /**
     * 构造Header
     */
    private Map<String, String> getHeader(String md) {
        Map<String, String> header = new LinkedHashMap<>(16);
        header.put("appid", APPID);
        header.put("md5", md);
        header.put("timestamp", String.valueOf(Instant.now()));
        header.put("token",StringUtil.EMPTYSTR);
        header.put("version",VERSION);
        return header;
    }


    /**
     * 构造body
     */
    private String getBody() {

        return "";
    }

    /**
     * 发起请求
     */
    public void requestApi(String url, String jsonData, Boolean needToken) {
        CloseableHttpClient restClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        try {
            StringEntity entity = new StringEntity(jsonData);
            httpPost.setEntity(entity);

            Map<String, String> headers = getHeader(CrypUtil.MD5Sum(jsonData));
            for (Map.Entry<String, String> header : headers.entrySet()) {

            }
            httpPost.setHeader("signature",CrypUtil.rsaSign());

            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = restClient.execute(httpPost);
            logger.info("+++++++++++++" + response.toString());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        } finally {
            try {
                restClient.close();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }

    }

}
