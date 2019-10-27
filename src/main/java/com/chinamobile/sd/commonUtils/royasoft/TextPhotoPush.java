package com.chinamobile.sd.commonUtils.royasoft;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.HttpRequestUtil;

import java.util.HashMap;
import java.util.Map;

public class TextPhotoPush {
    /**
     * 备注：
     * 单独下载JCE 扩展：需单独从Oracle官网下载对应JDK版本的Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files
     * JCEPolicyJDK中的local_policy.jar US_export_policy.jar两个文件替换到\jdk\jre\lib\security目录下
     * <p>
     * 1、在OMC中创建服务号
     * 2、在OMC中对此服务号设置第三方允许访问IP地址
     * 3、获取OMC中服务号的三个信息：服务号ID（serviceID）/安全身份ID（securityID）/安全密钥（securityKey）
     */
    private static String serviceID = "e42de1a7-2460-45b6-8222-823267d33f0a";
    private static String securityID = "ID__347823_1472470377255";
    private static String securityKey = "YLCUJMN9KULQYYGB5GOH";

    //测试环境-公网地址：http://223.99.142.5/esip/ 内网地址：http://10.19.110.72/esip/
    //生产环境-公网地址：http://223.99.142.2/esip/ 内网地址：http://10.19.110.66/esip/
    private static String ESIP_services_url = "http://223.99.142.5/esip/";


    public static void main(String[] args) {
        //发送文本信息示例
        sendText();
        //发送图文消息示例
        sendPhotoText();
    }

    //发送图文方法
    public static void sendMessage(String json) {
        try {
            String aes_encode_body = AESUtil.encode(securityKey, json);

            //解密测试
            //System.out.println("解密测试:"+AESUtil.decode(securityKey, aes_encode_body));

            Map<String, String> sendParams = new HashMap<String, String>();
            sendParams.put("function_id", "1001");//必须为1001
            sendParams.put("service_id", serviceID);
            sendParams.put("request_body", aes_encode_body);
            HttpRequestUtil httpRequestUtil = new HttpRequestUtil();
            httpRequestUtil.postMethod(ESIP_services_url, sendParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送文本信息示例
    public static void sendText() {
        try {
            JSONObject json = new JSONObject();
            json.put("fromType", "1");
            json.put("receiverType", "01");//图文接收者范围:01(预置服务号为全部用户、其他服务号为关注此服务号的用户) 02(单个或多个手机号) 03(单个或多个用户ID)
            json.put("receiverPerson", "135XXXXXXXX");//多个发送对象以逗号分割

            json.put("serviceID", serviceID);
            json.put("securityID", securityID);
            json.put("securityKey", securityKey);


            //文本消息--示例-start
            json.put("messageType", "01");//文本类型为01
            json.put("messageContent", Base64.encodeBytes("移动社区-应用接口-文本消息推送示例".getBytes()));
            //文本消息--示例-end

            sendMessage(json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送图文消息示例
    public static void sendPhotoText() {
        try {
            JSONObject json = new JSONObject();
            json.put("fromType", "1");
            json.put("receiverType", "01");//图文接收者范围:01(预置服务号为全部用户、其他服务号为关注此服务号的用户) 02(单个或多个手机号) 03(单个或多个用户ID)
            json.put("receiverPerson", "135XXXXXXXX");//多个发送对象以逗号分割

            json.put("serviceID", serviceID);
            json.put("securityID", securityID);
            json.put("securityKey", securityKey);


            //图文消息-示例-start           
            json.put("messageType", "02");//图文类型为02

            JSONArray photo_text_array = new JSONArray();
            JSONObject photo_text_json;

            photo_text_json = new JSONObject();
            photo_text_json.put("type", "mainTitle");//mainTitle表示主节点、sc表示子节点
            photo_text_json.put("titleDesc", Base64.encodeBytes("移动社区-应用接口-图文推送接口-主标题".getBytes()));//标题内容
            photo_text_json.put("titlePicUrl", "http://223.99.142.2/v_help/images/1.png"); //标题背景图片url
            photo_text_json.put("clickUrl", "http://223.99.142.2/h5/html/material/index.html?gid=170&serviceid=8cb38b7c-5029-4d5b-889c-6ea3519f8d8a");//标题内容链接地址
            photo_text_array.add(photo_text_json);

            photo_text_json = new JSONObject();
            photo_text_json.put("type", "sc");//mainTitle表示主节点、sc表示子节点
            photo_text_json.put("titleDesc", Base64.encodeBytes("移动社区-应用接口-图文推送接口-子标题1".getBytes()));//标题内容
            photo_text_json.put("titlePicUrl", "http://223.99.142.2/v_help/images/2.png"); //标题背景图片url
            photo_text_json.put("clickUrl", "http://223.99.142.2/h5/html/material/index.html?gid=223&serviceid=8cb38b7c-5029-4d5b-889c-6ea3519f8d8a");//标题内容链接地址
            photo_text_array.add(photo_text_json);

            photo_text_json = new JSONObject();
            photo_text_json.put("type", "sc");//mainTitle表示主节点、sc表示子节点
            photo_text_json.put("titleDesc", Base64.encodeBytes("移动社区-应用接口-图文推送接口-子标题2".getBytes()));//标题内容
            photo_text_json.put("titlePicUrl", "http://223.99.142.2/v_help/images/2.png"); //标题背景图片url
            photo_text_json.put("clickUrl", "http://223.99.142.2/h5/html/material/index.html?gid=223&serviceid=8cb38b7c-5029-4d5b-889c-6ea3519f8d8a");//标题内容链接地址
            photo_text_array.add(photo_text_json);

            photo_text_json = new JSONObject();
            photo_text_json.put("type", "sc");//mainTitle表示主节点、sc表示子节点
            photo_text_json.put("titleDesc", Base64.encodeBytes("移动社区-应用接口-图文推送接口-子标题3".getBytes()));//标题内容
            photo_text_json.put("titlePicUrl", "http://223.99.142.2/v_help/images/2.png"); //标题背景图片url
            photo_text_json.put("clickUrl", "http://223.99.142.2/h5/html/material/index.html?gid=223&serviceid=8cb38b7c-5029-4d5b-889c-6ea3519f8d8a");//标题内容链接地址
            photo_text_array.add(photo_text_json);


            json.put("messageContent", photo_text_array.toJSONString());
            //图文消息-示例-end

            sendMessage(json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
