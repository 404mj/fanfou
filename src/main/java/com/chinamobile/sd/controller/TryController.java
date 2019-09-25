package com.chinamobile.sd.controller;

import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.CrypUtil;
import com.chinamobile.sd.commonUtils.RestClient4Andmu;
import com.chinamobile.sd.model.User;
import com.chinamobile.sd.service.UserService;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/try")
public class TryController {

    private static final Logger logger = LoggerFactory.getLogger(TryController.class);

    @Autowired
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate redisTemplate;


    //    测试环境配置
    //@GetMapping
    @RequestMapping("/index")
    public Map<String, String> index() {
        Map<String, String> a = new HashMap<>();
        a.put("1", "hello");
        a.put("2", "world");
        return a;
    }

    //    测试json返回
    @RequestMapping("/array")
    public List<String> array() {
        List<String> b = new ArrayList<>();
        b.add("I");
        b.add("love");
        b.add("java");
        return b;
    }

    //    测试mysql+mybatis
    @RequestMapping("/alluser")
    public List<User> allUser() {
        return userService.findAllUsers();
    }

    //    测试普通请求参数
    @RequestMapping("/testparam")
    public String testparam(@RequestParam(value = "userName") String userName) {
        return userName;
    }

    //    测试json请求参数
    // @PostMapping
    @RequestMapping("/jsonparam")
    public String jsonParam(@RequestBody String jsonData) {
        return jsonData;
    }

    @RequestMapping("/jsonparam2")
    public String jsonParam2(@RequestBody Map<String, Object> jsonData) {
        logger.info(jsonData.get("k1").toString());
        return "success";
    }


    /**
     * 测试日志组件
     * 日志级别
     * OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL
     */
    @RequestMapping("/printLog")
    public String printLog() {
        logger.error("ERROR 级别日志");
        logger.warn("WARN 级别日志");
        logger.info("INFO 级别日志");
        logger.debug("DEBUG 级别日志");
        logger.trace("TRACE 级别日志");
        return "success";
    }

    /**
     * 测试redis
     * 10s过期时间
     */
    @RequestMapping("/setget")
    public String setThenGet() {
        stringRedisTemplate.opsForValue().set("ping", "Pong!", 10, TimeUnit.SECONDS);
        return stringRedisTemplate.opsForValue().get("ping");
    }

    /**
     * 使用alibaba fastjson
     */
    @PostMapping("/tryjson")
    public void parseJson(@RequestBody String jsonstring) {
        JSONObject jsonOb = JSONObject.parseObject(jsonstring);
        String jstring = jsonOb.toJSONString();
        logger.info("=======>" + jsonstring + "<=======");
    }


    /**
     * 使用httpclient请求开发者接口
     */
    @GetMapping("/sendapi")
    public String getPic() {
        //todo 调用接口的body数据怎么弄？
        RestClient4Andmu.requestApi("https://open.andmu.cn/v3/open/api/token", "{\"sig\":\"3ea4ef228abe9fb7098fcaaa24598abf\",\"operatorType\":1}", false);
        return "headers";
    }

    /**
     * Test
     */
    @GetMapping("/test")
    public void test() {


    }

}
