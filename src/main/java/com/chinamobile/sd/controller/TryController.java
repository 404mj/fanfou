package com.chinamobile.sd.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.*;
import com.chinamobile.sd.model.ResultModel;
import com.chinamobile.sd.model.User;
import com.chinamobile.sd.service.CameraAiService;
import com.chinamobile.sd.service.StatisticService;
import com.chinamobile.sd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/try")
public class TryController {

    private static final Logger logger = LoggerFactory.getLogger(TryController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RestClient4Andmu restClient4Andmu;
    @Autowired
    private CameraAiService cameraAiService;
    @Autowired
    private StatisticService statisticService;

    /**
     * 直接使用sring序列化，免去Config配置
     */
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
        //body数据按照规范构造顺序map，tojson。
        Map<String, String> req = new LinkedHashMap<>();
        req.put("page", "1");
        req.put("pageSize", "10");
        JSONObject obj = restClient4Andmu.requestApi(Constant.DEVICELIST_POST, JSONObject.toJSONString(req), true);
        return obj.toJSONString();
    }

    /**
     * Test
     */
    @GetMapping("/test")
    public String test() {
//        return restClient4Andmu.getToken();
        //测试rdis其他数据结构
//        stringRedisTemplate.opsForList().rightPush("fanfou_list","aaaaaaaaaaaaaa");
//        stringRedisTemplate.opsForList().rightPush("fanfou_list","bbbbbbbbbbbbbb");
//        stringRedisTemplate.opsForList().rightPush("fanfou_list","ccccccccccccccc");
//        stringRedisTemplate.opsForHash().put("fanfou_hash","20190926","base64picsdfsdfsdfd");

//到秒
//        logger.info(String.valueOf((System.currentTimeMillis() / 1000)));

        //20190929设备到位调试摄像头到服务端
        //        JSONArray jsonArray = restClient4Andmu.getDeviceList();


        long st = System.currentTimeMillis();
//        String queJson = "{\"deviceId\":\"" + Constant.DEVICE_QUEUE + "\"}";
//        logger.info(DateUtil.date2String(new Date(),DateUtil.YYYY_MM_DD_HH_MM_SS));
//        JSONObject picJsonQue = restClient4Andmu.requestApi(Constant.PIC_REALTIME, queJson, true);
//        String queurl = picJsonQue.get("data").toString();
//        return queurl;


        //store image base64 to hash
//        String basepic = CrypUtil.encodePicToBase64("C:\\zsxhome\\api_header.png");
//        long st = System.currentTimeMillis();
        //https://openapi.h.reservehemu.com/rest/service/camera/xxxxS_001231404901/thumbnail/current?size=320x320&token=fcd00a35f6d84b53822540dc0857ecfc&channelNo=
//        String basepic = CrypUtil.encodeUrlPicToBase64(queurl);
//        stringRedisTemplate.opsForHash().put("fanfou_hash", DateUtil.getCurrentSeconds(), basepic);
//
//        String s = stringRedisTemplate.opsForHash().get("fanfou_hash","1569486463").toString();
//        CrypUtil.decodeBase64ToPic(basepic,"C:\\zsxhome\\andmu_picget_3.png");

        cameraAiService.getPicSendRedisCallAiTask();

        return String.valueOf(System.currentTimeMillis() - st);
//
        //测试统计数据返沪resultmodel行不行。--可以的。
//        return statisticService.getStatistic();


//        stringRedisTemplate.opsForValue().set("test", "11111");
//        ResultUtil.successResult(stringRedisTemplate.opsForValue().get("test"))

//        return ;
    }

    @GetMapping("/very")
    public ResultModel very() {
        List<String> al = stringRedisTemplate.opsForList().range(Constant.REDISKEY_COMPLETED_LIST, 0, 0);
        String lkey = al.get(0);
        logger.info("--------->>>>>completed key:::" + lkey);
        return ResultUtil.successResult(stringRedisTemplate.opsForHash().get(Constant.REDISKEY_ATTENDANCEPROB_PREFIX + "20190930", lkey));
    }

}
