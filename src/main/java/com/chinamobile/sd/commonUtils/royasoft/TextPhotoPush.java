package com.chinamobile.sd.commonUtils.royasoft;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.sd.commonUtils.HttpRequestUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TextPhotoPush {

    private static Logger logger = LogManager.getLogger(TextPhotoPush.class);
    /**
     * 备注：
     * 单独下载JCE 扩展：需单独从Oracle官网下载对应JDK版本的Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files
     * JCEPolicyJDK中的local_policy.jar US_export_policy.jar两个文件替换到\jdk\jre\lib\security目录下
     * <p>
     * 1、在OMC中创建服务号
     * 2、在OMC中对此服务号设置第三方允许访问IP地址
     * 3、获取OMC中服务号的三个信息：服务号ID（serviceID）/安全身份ID（securityID）/安全密钥（securityKey）
     */
    private static String serviceID = "7d30d5ff-ab74-47fa-9e75-9834b784b0d9";
    private static String securityID = "ID__221744_1572252259843";
    private static String securityKey = "VDJ87VDV7EKF10T77B53";

    @Value("${service.url.esip.push}")
    private String ESIP_services_url;

    //发送图文方法
    public void sendMessage(String json) {
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

    /**
     * @param text
     */
    public void sendText(String text) {
        try {
            JSONObject json = new JSONObject();
            json.put("fromType", "1");
            //图文接收者范围:01(预置服务号为全部用户、其他服务号为关注此服务号的用户) 02(单个或多个手机号) 03(单个或多个用户ID)
//            json.put("receiverType", "01");
            json.put("receiverType", "02");
//            多个发送对象以逗号分割
            json.put("receiverPerson", "18853195179");

            json.put("serviceID", serviceID);
            json.put("securityID", securityID);
            json.put("securityKey", securityKey);


            //文本消息--start
            json.put("messageType", "01");//文本类型为01
            json.put("messageContent", Base64.encodeBytes(text.getBytes("utf-8")));
            //文本消息--end

            sendMessage(json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送图文消息
     */
    public void sendPhotoText() {
        try {
            JSONObject json = new JSONObject();
            json.put("fromType", "1");
            //图文接收者范围:01(预置服务号为全部用户、其他服务号为关注此服务号的用户) 02(单个或多个手机号) 03(单个或多个用户ID)
            json.put("receiverType", "01");
            //多个发送对象以逗号分割
            json.put("receiverPerson", "135XXXXXXXX");

            json.put("serviceID", serviceID);
            json.put("securityID", securityID);
            json.put("securityKey", securityKey);


            //图文类型为02-start
            json.put("messageType", "02");

            JSONArray photo_text_array = new JSONArray();
            JSONObject photo_text_json;

            photo_text_json = new JSONObject();
            //mainTitle表示主节点、sc表示子节点
            photo_text_json.put("type", "");
            //标题内容
            photo_text_json.put("titleDesc", Base64.encodeBytes("".getBytes()));
            //标题背景图片url
            photo_text_json.put("titlePicUrl", "");
            //标题内容链接地址
            photo_text_json.put("clickUrl", "");
            photo_text_array.add(photo_text_json);

            photo_text_json = new JSONObject();
            //mainTitle表示主节点、sc表示子节点
            photo_text_json.put("type", "sc");
            //标题内容
            photo_text_json.put("titleDesc", Base64.encodeBytes("".getBytes()));
            //标题背景图片url
            photo_text_json.put("titlePicUrl", "");
            //标题内容链接地址
            photo_text_json.put("clickUrl", "");
            photo_text_array.add(photo_text_json);

            photo_text_json = new JSONObject();
            photo_text_json.put("type", "sc");
            photo_text_json.put("titleDesc", Base64.encodeBytes("".getBytes()));
            photo_text_json.put("titlePicUrl", "");
            photo_text_json.put("clickUrl", "");
            photo_text_array.add(photo_text_json);

            photo_text_json = new JSONObject();
            //mainTitle表示主节点、sc表示子节点
            photo_text_json.put("type", "");
            //标题内容
            photo_text_json.put("titleDesc", Base64.encodeBytes("".getBytes()));
            //标题背景图片url
            photo_text_json.put("titlePicUrl", "");
            //标题内容链接地址
            photo_text_json.put("clickUrl", "");
            photo_text_array.add(photo_text_json);


            json.put("messageContent", photo_text_array.toJSONString());
            //图文消息-end

            sendMessage(json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
