package com.chinamobile.sd.commonUtils;
/**
 * @Author: fengchen.zsx
 * @Date: 2019/10/27 19:01
 */

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequestUtil {


    private static Logger logger = LogManager.getLogger(HttpRequestUtil.class);

    /**
     * without json
     *
     * @param url
     * @param paramsMap
     */
    public static void postMethod(String url, Map<String, String> paramsMap) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost postMethod = new HttpPost(url);
        postMethod.setHeader(HTTP.CONTENT_ENCODING, "utf-8");
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (paramsMap != null) {
            for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
        }
        try {
            postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(postMethod);
            //EntityUtils.toString(response.getEntity())
            logger.info("======>> "+getStringRes(response));
            response.close();
            postMethod.releaseConnection();
            httpClient.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * with json
     *
     * @param url
     * @param requestBody
     * @param headers
     * @return
     */
    public static JSONObject httpPost(String url, String requestBody, Map<String, String> headers) {
        CloseableHttpClient restClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
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
            String res = getStringRes(response);
            response.close();
            logger.info(res);
            return JSONObject.parseObject(res);

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                restClient.close();
                httpPost.releaseConnection();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    private static String getStringRes(HttpResponse response) throws IOException {
        HttpEntity responseEntity = response.getEntity();
        if (responseEntity != null) {
            InputStream inStream = responseEntity.getContent();
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            String resBuf = null;
            while ((resBuf = reader.readLine()) != null) {
                sb.append(resBuf);
            }
            inStream.close();
            return sb.toString();
        }
        return Constant.EMPTYSTR;
    }

}