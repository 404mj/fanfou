package com.chinamobile.sd.commonUtils;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/24 15:47
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 根据 https://open.andmu.cn/doc/api 规范
 */
@Component
public class RestClient4Andmu {
    private static Logger logger = LoggerFactory.getLogger(RestClient4Andmu.class);

    @Autowired
    private StringRedisTemplate redisTemplate;


    private static final String APPID = "6e4268766a5c4445b6d1ec16d0f24636";
    private static final String SECRET = "uxG8HrcoMDWu0eqZ";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJwcm9mZXNzaW9uIjoxLCJzdWIiOiI2ZTQyNjg3NjZhNWM0NDQ1YjZkMW" +
            "VjMTZkMGYyNDYzNiIsImFwcGlkIjoiNmU0MjY4NzY2YTVjNDQ0NWI2ZDFlYzE2ZDBmMjQ2MzYiLCJvcGVyYXRvclR5cGUiOjEsImV4cCI6M" +
            "TU2OTk0NTAxNSwiaWF0IjoxNTY5MzQwMjE1LCJvcGVyYXRvciI6IjZlNDI2ODc2NmE1YzQ0NDViNmQxZWMxNmQwZjI0NjM2IiwianRpIjoi" +
            "MTU2OTM0MDIxNTkzMiJ9.isl5spzHnFEuJdR6UHMk-8kUGQpKGRjQOHcQx2h1B7o";
    private static final String VERSION = "1.0.0";

    /**
     * api list
     */
    public static final String TOKEN_POST = "https://open.andmu.cn/v3/open/api/token";
    public static final String DEVICELIST_POST = "https://open.andmu.cn/v3/open/api/pro/device/list";
    public static final String VIDEO_PLAY = "https://open.andmu.cn/v3/open/api/websdk/live";
    public static final String PIC_REALTIME = "https://open.andmu.cn/v3/open/api/pro/camera/thumbnail/realtime";

    /**
     * 构造Header
     */
    private Map<String, String> getHeader(String md, Boolean needToken) {
        Map<String, String> header = new LinkedHashMap<>(16);

        header.put("appid", APPID);
        header.put("md5", md);
        header.put("timestamp", String.valueOf(System.currentTimeMillis()));
        if (needToken) {
            header.put("token", this.getToken());
        }
        header.put("version", VERSION);
        //签名header
        header.put("signature", CrypUtil.rsaSign(JSON.toJSONString(header)));
        return header;
    }


    /**
     * 请求第一步,获取token。
     * 取redis，不存在重新请求并放进去。
     */
    public String getToken() {

        redisTemplate.opsForValue().set(StringUtil.REDISKEY_TOKEN,TOKEN,604800);
        String token = redisTemplate.opsForValue().get(StringUtil.REDISKEY_TOKEN);
        return JSON.toJSONString(token);
       /*
        if (!StringUtils.isEmpty(token)) {
            return token;
        }

        Map<String, String> req = new LinkedHashMap<>();
        req.put("operatorType", "1");
        req.put("sig", CrypUtil.MD5Sum(APPID + SECRET));
        JSONObject res = this.requestApi(TOKEN_POST, JSON.toJSONString(req), false);
        if (!res.getString("resultCode").equals("000000")) {
            logger.error(res.toJSONString());
            return null;
        }
        token = JSONObject.parseObject(res.get("data").toString()).getString("token");
        String expiresIn = JSONObject.parseObject(res.get("data").toString()).getString("expires_in");
        redisTemplate.opsForValue().set(StringUtil.REDISKEY_TOKEN, token, Long.valueOf(expiresIn));
        return token;
        */

    }


    /**
     * 向开发者平台发送请求
     *
     * @param url         请求api
     * @param requestBody 请求体
     * @param needToken   是否需要token鉴权
     * @return 响应json
     */
    public JSONObject requestApi(String url, String requestBody, Boolean needToken) {
        CloseableHttpClient restClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        InputStream inStream = null;
        try {
            StringEntity entity = new StringEntity(requestBody);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            Map<String, String> headers = getHeader(CrypUtil.MD5Sum(requestBody), needToken);
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
                logger.info(sb.toString());
                return JSONObject.parseObject(sb.toString());
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
        return null;
    }

}