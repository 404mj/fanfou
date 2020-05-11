package com.chinamobile.sd.service;

import com.chinamobile.sd.commonUtils.Constant;
import com.chinamobile.sd.commonUtils.DateUtil;
import com.chinamobile.sd.commonUtils.royasoft.TextPhotoPush;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Date;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/10/18 16:44
 */
@Component
public class NotifyService {

    private Logger logger = LogManager.getLogger(NotifyService.class);
    public static String[] WEEKMAP = {"", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    public static String RECIPEURL = "https://ydsq.sd.chinamobile.com/vue-h5/resturant/index.html#/weekDelicacy?week=";

    @Autowired
    private TextPhotoPush textPhotoPush;

    /**
     * 通知移动社区用户
     */
    public void notifyMobile() {
        textPhotoPush.sendText(Constant.MOBILE_PUSH_MSG);
        logger.info("--------notify_mobile: " + DateUtil.date2String(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));
    }

    /**
     * 根据周几生成食谱访问链接，通知移动用户
     */
    public void notifyMobileRecipe() {
        String wkurl = RECIPEURL + WEEKMAP[LocalDate.now().getDayOfWeek().getValue()];
        textPhotoPush.sendText(Constant.MOBILE_PUSH_RECIPEMSG + wkurl);
        logger.info("--------notify_mobile_receipe: " + DateUtil.date2String(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));
    }


    /**
     * 通知模型服务
     *
     * @param url
     * @param requestBody
     */
    public void notifyAiService(String url, String requestBody) {
        CloseableHttpClient restClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        try {
            StringEntity entity = new StringEntity(requestBody);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            restClient.execute(httpPost);
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
                logger.error(e.toString());
            }
        }
        logger.info("-------notified_ai_service : " + requestBody);
    }

}
