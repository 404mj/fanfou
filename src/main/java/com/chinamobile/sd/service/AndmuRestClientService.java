package com.chinamobile.sd.service;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/9/24 15:47
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.Constant;
import com.chinamobile.sd.commonUtils.CrypUtil;
import com.chinamobile.sd.commonUtils.HttpRequestUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 根据 https://open.andmu.cn/doc/api 规范
 */
@Component
public class AndmuRestClientService {
    private static Logger logger = LogManager.getLogger(AndmuRestClientService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;


    private final String APPID = "6e4268766a5c4445b6d1ec16d0f24636";
    private final String SECRET = "uxG8HrcoMDWu0eqZ";
    private final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJwcm9mZXNzaW9uIjoxLCJzdWIiOiI2ZTQyNjg3NjZhNWM0NDQ1" +
            "YjZkMWVjMTZkMGYyNDYzNiIsImFwcGlkIjoiNmU0MjY4NzY2YTVjNDQ0NWI2ZDFlYzE2ZDBmMjQ2MzYiLCJvcGVyYXRvclR5cGUiO" +
            "jEsImV4cCI6MTU3MDAyODcwNiwiaWF0IjoxNTY5NDIzOTA2LCJvcGVyYXRvciI6IjZlNDI2ODc2NmE1YzQ0NDViNmQxZWMxNmQwZj" +
            "I0NjM2IiwianRpIjoiMTU2OTQyMzkwNjQ0NyJ9.--BqqkEowVVXh-pU3qhjXtHdWKOSJbYX6NF4zj9X7pQ";
    private final String VERSION = "1.0.0";


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

        String token = redisTemplate.opsForValue().get(Constant.REDISKEY_TOKEN);
        if (!StringUtils.isEmpty(token)) {
            return token;
        }

        Map<String, String> req = new LinkedHashMap<>();
        req.put("operatorType", "1");
        req.put("sig", CrypUtil.MD5Sum(APPID + SECRET));
        JSONObject res = this.requestApi(Constant.TOKEN_POST, JSON.toJSONString(req), false);
        if (!res.getString("resultCode").equals("000000")) {
            logger.error(res.toJSONString());
            return null;
        }
        token = JSONObject.parseObject(res.get("data").toString()).getString("token");
        logger.info("----response   token--------" + token);
        Long expiresIn = JSONObject.parseObject(res.get("data").toString()).getLong("expires_in") - 10;
        redisTemplate.opsForValue().set(Constant.REDISKEY_TOKEN, token, expiresIn, TimeUnit.SECONDS);
        return token;

    }

    /**
     * 设备列表
     *
     * @return
     */
    public JSONArray getDeviceList() {
        Map<String, String> req = new LinkedHashMap<>();
        req.put("page", "1");
        req.put("pageSize", "10");
        JSONObject res = this.requestApi(Constant.DEVICELIST_POST, JSON.toJSONString(req), true);
        if (!res.getString("resultCode").equals("000000")) {
            logger.error(res.toJSONString());
            return null;
        }
        JSONArray jsons = JSONArray.parseArray(res.get("data").toString());
        logger.info(jsons.toJSONString());
        return jsons;
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
        Map<String, String> headers = getHeader(CrypUtil.MD5Sum(requestBody), needToken);
        return HttpRequestUtil.httpPost(url, requestBody, headers);
    }
}
