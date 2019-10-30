package com.chinamobile.sd.commonUtils;
/**
 * @Author: fengchen.zsx
 * @Date: 2019/10/27 19:01
 */

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Map;

public class HttpRequestUtil {


    private static Logger logger = LogManager.getLogger(HttpRequestUtil.class);

    /**
     * @param url
     * @param paramsMap
     */
    public static void postMethod(String url, Map<String, String> paramsMap) {

        JSONObject jsonObj = new JSONObject();
        if (paramsMap != null) {
            for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                jsonObj.put(param.getKey(), param.getValue());
            }
        }
        httpPost(url, jsonObj.toJSONString(), null);
    }

    /**
     * @param url
     * @param requestBody
     * @param headers
     * @return
     */
    public static JSONObject httpPost(String url, String requestBody, Map<String, String> headers) {
        CloseableHttpClient restClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        InputStream inStream = null;
        try {
            StringEntity entity = new StringEntity(requestBody);

            entity.setContentType("application/json");
            entity.setContentEncoding("utf-8");
            httpPost.setEntity(entity);

            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    httpPost.setHeader(header.getKey(), header.getValue());
                }
            }

            CloseableHttpResponse response = restClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                inStream = responseEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                StringBuilder sb = new StringBuilder();
                String resBuf = null;
                while ((resBuf = reader.readLine()) != null) {
                    sb.append(resBuf);
                }
                /*
                Header[] hs = response.getAllHeaders();
                for (int i = 0; i < hs.length; i++) {
                    logger.info(hs[i].toString());
                }
                */
                //logger.info(sb.toString());
                return JSONObject.parseObject(sb.toString());
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                restClient.close();
                httpPost.releaseConnection();
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

