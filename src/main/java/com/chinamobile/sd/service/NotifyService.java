package com.chinamobile.sd.service;

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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @Author: fengchen.zsx
 * @Date: 2019/10/18 16:44
 */
@Service
public class NotifyService {

    private Logger logger = LogManager.getLogger(NotifyService.class);


    @Autowired
    private TextPhotoPush textPhotoPush;
    @Value("${fanfou.mobile.msg}")
    private String mobileMsg;

    /**
     * 通知移动社区用户
     */
    public void notifyMobile() {
        logger.info("-----------notify_mobile: " + DateUtil.date2String(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        textPhotoPush.sendText(mobileMsg);
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
            logger.error(e.getMessage() + e.toString());
        } catch (ClientProtocolException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        } finally {
            try {
                restClient.close();
                httpPost.releaseConnection();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
        logger.info("--------------------notified_ai_service : " + requestBody);
    }

}
