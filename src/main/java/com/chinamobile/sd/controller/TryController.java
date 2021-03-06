package com.chinamobile.sd.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.*;
import com.chinamobile.sd.commonUtils.royasoft.TextPhotoPush;
import com.chinamobile.sd.model.*;
import com.chinamobile.sd.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/fanfou/debug")
public class TryController {

//    private Logger logger = LoggerFactory.getLogger(TryController.class);
//    private static final Logger logger = LoggerFactory.getLogger(TryController.class);
    /**
     * 单独使用log4j,与slf4j桥接浪费性能
     */
    private Logger logger = LogManager.getLogger(TryController.class);

    @Autowired
    private AndmuRestClientService restClient4Andmu;
    @Autowired
    private CameraAiService cameraAiService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private FoodItemService foodItemService;
    @Autowired
    private FoodCommentService foodCommentService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TextPhotoPush textPhotoPush;
    @Autowired
    private CrypUtil crypUtil;
    @Autowired
    private CountLandingService countLandingService;
    @Autowired
    private FoodExcelService foodExcelService;
    @Autowired
    private BookedUserService buserService;
    @Autowired
    private BookedRecordService recordService;

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
//    @RequestMapping("/alluser")
//    public List<User> allUser() {
//        return userService.findAllUsers();
//    }

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
        JSONArray jsonArray = restClient4Andmu.getDeviceList();
        return jsonArray.toJSONString();
//        return restClient4Andmu.getToken();

//        long st = System.currentTimeMillis();
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

//        cameraAiService.syncPicSendRedisCallAiTask();

//        logger.info("+++++++++++++++++++++>>>>" + Constant.getPeopleFlowRate());
//        return String.valueOf(System.currentTimeMillis() - st);
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
        return ResultUtil.successResult(stringRedisTemplate.opsForHash().get(Constant.REDIS_R0ATTENDPROB_PREFIX + "20190930", lkey));
    }

    @GetMapping("/dbainsert")
    public ResultModel testDb() {
//        FoodItem newItem = new FoodItem(null, "红烧排骨", 0, true, 1, DateUtil.getToday(), 3, 5, 1, "B1餐厅");
//        return foodItemService.addItem(newItem);
        List<FoodItem> items = new ArrayList<>();
//        FoodItem it1 = new FoodItem(null, "九转大肠", 0, false, 1, DateUtil.getToday(), 3, 5, 1, "B1餐厅");
//        FoodItem it2 = new FoodItem(null, "葱爆羊肉", 0, false, 1, DateUtil.getToday(), 3, 5, 1, 5, 0);
//        FoodItem it3 = new FoodItem(null, "酸菜鱼", 0, false, 1, DateUtil.getToday(), 3, 5, 1, 5, 0);
//        items.add(it1);
//        items.add(it2);
//        items.add(it3);
        try {
            return foodItemService.addItems(items);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        foodItemService.removeItemById(2);
//        return ResultUtil.successResult("ok");
        return null;
    }

    @GetMapping("/dbaget")
    public ResultModel testDbget() {
//        return foodItemService.getItemsByDayAndPeriod("2019-09-30", 1);
//        return foodItemService.getRecommendTodayPeriod(DateUtil.getToday(),1);
//        return foodItemService.getRecommendTodayPeriod("2019-09-30", 1);

//        return foodItemService.upItem(6);
//        return foodItemService.downItem(6);
        return null;

    }

    @GetMapping("/dbacomm")
    public ResultModel testComment() {
//        return foodCommentService.addComment("红烧排骨是最爱了，谢谢大厨！！", DateUtil.getToday());
//        foodCommentService.addComment("九转大肠太甜了。。。。不好吃", DateUtil.getToday());
//        return foodCommentService.addComment("酸菜鱼做的太专业了，老家的味道！", DateUtil.getToday());
        return foodCommentService.getCommentsByTime("2019-09-30", 0);
    }

    @GetMapping("/testa")
    public void testAsync() {
//        long st = System.currentTimeMillis();
//        cameraAiService.asyncPicSendRedisCallAiTask();
//        return String.valueOf(System.currentTimeMillis() - st);
        notifyService.notifyMobileRecipe();
    }

    /**
     * 上传excel文件
     */
    @PostMapping("/upload")
    public ResultModel uploadExcel(@RequestParam("file") MultipartFile multipartFile) {
//        InputStream inputStream = multipartFile.getInputStream();
        return foodExcelService.processRecipeExcel(multipartFile);
//        return ResultUtil.successResult(1);
    }

    @GetMapping("/processexcel")
    public ResultModel processExcel() {

//        String filepath = "C:\\zsxhome\\forAICT\\B1大餐厅菜谱sample.xlsx";
        String filepath = "D:\\tmp\\B1大餐厅菜谱sample.xlsx";

        String res = null;
        String[] resa = null;
        try {
            XSSFWorkbook workbook = (XSSFWorkbook) XSSFWorkbookFactory.create(new File(filepath));
            XSSFSheet breakfast = workbook.getSheetAt(0);
            XSSFRow xssfRow = breakfast.getRow(2);
            XSSFCell xssfCell = xssfRow.getCell(3);
            res = "老醋花生|凉拌双耳|猪头肉拌黄瓜|黄瓜拌猪肝";
//            res = xssfCell.getStringCellValue().trim();
            resa = res.split("\\|");
            logger.info(res);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return ResultUtil.successResult(resa);
    }


    /**
     * 预备服务器上联调接口，避免重新编译上产的时间损耗
     *
     * @return
     */
    @GetMapping("/t_picflow")
    public String testPicflow() {
        //R0_DEVICE_QUEUE
        String attJson = "{\"deviceId\":\"" + Constant.R0_DEVICE_ATTENDANCE + "\",\"size\": \"1080x720\"}";
//        String queJson1 = "{\"deviceId\":\"" + Constant.R1_DEVICE_QUEUE + "\",\"size\": \"1080x720\"}";
//        logger.info(queJson1);
        //作为key的时间戳精确到秒
        String nowTime = DateUtil.getCurrentSeconds();
        JSONObject picJsonQue = restClient4Andmu.requestApi(Constant.PIC_REALTIME_NEW, attJson, true);
//        JSONObject picJsonQue1 = restClient4Andmu.requestApi(Constant.PIC_REALTIME_NEW, queJson1, true);
//        logger.info(Constant.PIC_REALTIME_NEW);
        try {
            String queurl = picJsonQue.getJSONObject("data").getString("url");
//            String queurl1 = picJsonQue1.getJSONObject("data").getString("url");
//            logger.info("====que0==>> " + queurl + "==que1=====> " + queurl1);
//        CrypUtil.savePicFromUrl(queurl);
            String queBase = crypUtil.encodeUrlPicToBase64(queurl).get();
            String nowHkey = Constant.REDIS_R0ATTENDANCE_PREFIX + DateUtil.getToday();
            String timeKey = DateUtil.getCurrentSeconds();
            redisTemplate.opsForHash().put(nowHkey, timeKey, queBase);
            redisTemplate.expire(nowHkey, Constant.REDISKEY_EXPIRES, TimeUnit.MINUTES);
            notifyService.notifyAiService(Constant.AISERVICEURL, "{\"time_stamp\":\"" + timeKey + "\"}");
            return timeKey;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "error";
    }

    @GetMapping("/t_picdecode")
    public void testPicDecode() {
        String picbase = crypUtil.encodeUrlPic2BaseWithHc("http://oss-open.andmu.cn:2080/xxxxS_0012313a1fc1/1574043745898.jpg?e=1576635748&token=9GlNy3-FQnzzT9sNDnpdhBc_0cJDnktVx9MYD73p:OSJY-abfAeF8CPzS1_tmJTl3qJM=");
        CrypUtil.decodeBase64ToPic(picbase, "C:\\zsxhome\\testhc.jpg");
    }


    @GetMapping("/t_aiflow")
    public void testAiflow() {
        cameraAiService.asyncPicSendRedisCallAiTask();
    }

    @GetMapping("/t_frontend")
    public String testUnion() {
        logger.info("llllllllllllllllllllllllll");
        return "hello world";
    }

    @GetMapping("/t_foodflow")
    public String testFoodflow() {
        return foodItemService.getItemsByDayAndPeriod(DateUtil.getTodayWithSlash(), 1, 0).toString();
    }

    @GetMapping("/t_dblink")
    public String testDbLink() {
        return foodCommentService.getTodayComments(0).toString();
    }

    @GetMapping("/t_redislink")
    public String testRedisLink() {
        stringRedisTemplate.opsForValue().set("lalatest", "sdfsfsdfds");
        return stringRedisTemplate.opsForValue().get("lalatest");
    }

    @GetMapping("/t_notify")
    public void testNotify() {
        notifyService.notifyAiService(Constant.AISERVICEURL, "{\"time_stamp\":\"" + 123123123 + "\"}");
    }

    @GetMapping("/t_push")
    public void testPush() {
        textPhotoPush.sendText("mobileMsg");
    }

    @GetMapping("/t_hisque")
    public void testHisque() {
//        statisticService.initData();
        Map<String, String> hisque = new LinkedHashMap<>(30);
//        List<String> lastKeyList = redisTemplate.opsForList().range(Constant.REDISKEY_COMPLETED_LIST, 0, 0);
//        String lastKey = lastKeyList.get(0);
//        String queLen = (String) redisTemplate.opsForHash().get(Constant.REDIS_R0PEOPLECOUNT_PREFIX + DateUtil.getToday(), lastKey);
//        hisque.put(lastKey, queLen);

        statisticService.processHisQue(hisque, 0);
        logger.info(JSONObject.toJSONString(hisque));
    }

    @GetMapping("/t_updatetoken")
    public String updateToken() {
        return restClient4Andmu.getToken();
    }

    @GetMapping("/t_commens")
    public ResultModel<List<FoodComment>> comments() {
        String[] days = DateUtil.getCurrentWeekFirstLastDay();
        return foodCommentService.getCommentsBetweenTime(days[0], days[1], 0);
    }

    @GetMapping("t_landing")
    public void landing() {
        countLandingService.landingTask();
    }

    @GetMapping("t_buser_add")
    public void addBuser() {
//        BookedUser user1 = new BookedUser("aaaaa44444", "张三4", "18810102024", "信息技术部");
//        logger.info(buserService.newBuser(user1));
//        logger.info(user1.getBookUid());
    }
}
