package com.chinamobile.sd.commonUtils;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/24 15:47
 */

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 根据 https://open.andmu.cn/doc/api 规范
 */
public class RestClient4Andmu {
    private static Logger logger = LoggerFactory.getLogger(RestClient4Andmu.class);
    private static final String APPID = "6e4268766a5c4445b6d1ec16d0f24636";
    private static final String SECRET = "uxG8HrcoMDWu0eqZ";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJwcm9mZXNzaW9uIjoxLCJzdWIiOiI2ZTQyNjg3NjZhNWM0NDQ1YjZkMW" +
            "VjMTZkMGYyNDYzNiIsImFwcGlkIjoiNmU0MjY4NzY2YTVjNDQ0NWI2ZDFlYzE2ZDBmMjQ2MzYiLCJvcGVyYXRvclR5cGUiOjEsImV4cCI6M" +
            "TU2OTk0NTAxNSwiaWF0IjoxNTY5MzQwMjE1LCJvcGVyYXRvciI6IjZlNDI2ODc2NmE1YzQ0NDViNmQxZWMxNmQwZjI0NjM2IiwianRpIjoi" +
            "MTU2OTM0MDIxNTkzMiJ9.isl5spzHnFEuJdR6UHMk-8kUGQpKGRjQOHcQx2h1B7o";
    private static final String VERSION = "1.0.0";

    /**
     * 构造Header
     */
    public static Map<String, String> getHeader(String md, Boolean needToken) {
        Map<String, String> header = new LinkedHashMap<>(16);

        header.put("appid", APPID);
        header.put("md5", md);
        header.put("timestamp", String.valueOf(System.currentTimeMillis()));
        if (needToken) {
            //TODO: 把token放到缓存中，设置上失效时间。从redis中取token
            header.put("token", StringUtil.EMPTYSTR);
        }
        header.put("version", VERSION);
        //签名header
        header.put("signature", CrypUtil.rsaSign(JSON.toJSONString(header)));
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
    public static void requestApi(String url, String jsonData, Boolean needToken) {
        CloseableHttpClient restClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        InputStream inStream = null;
        try {
            StringEntity entity = new StringEntity(jsonData);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            Map<String, String> headers = getHeader(CrypUtil.MD5Sum(jsonData), needToken);
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpPost.setHeader(header.getKey(), header.getValue());
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
                //todo resbuf是获取到的json返回结果。后续怎么处理？
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        } finally {
            try {
                inStream.close();
                restClient.close();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
    }
    /**
     * {"resultCode":"000000","resultMsg":"成功","data":{"expires_in":604800,"token":"eyJhbGciOiJIUzI1NiJ9.eyJwcm9mZXNzaW9uIjoxLCJzdWIiOiI2ZTQyNjg3NjZhNWM0NDQ1YjZkMWVjMTZkMGYyNDYzNiIsImFwcGlkIjoiNmU0MjY4NzY2YTVjNDQ0NWI2ZDFlYzE2ZDBmMjQ2MzYiLCJvcGVyYXRvclR5cGUiOjEsImV4cCI6MTU2OTk0Njk3NywiaWF0IjoxNTY5MzQyMTc3LCJvcGVyYXRvciI6IjZlNDI2ODc2NmE1YzQ0NDViNmQxZWMxNmQwZjI0NjM2IiwianRpIjoiMTU2OTM0MjE3NzYxNSJ9.vq12OvBqsZE6x8ni5RPtOARrvDBzo3ScbEVQUTPQ7Ls"}}
     */

}
