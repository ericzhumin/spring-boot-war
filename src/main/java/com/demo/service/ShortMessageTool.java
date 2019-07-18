package com.demo.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
//import com.associationcloud.system.schdule.schdule.domain.JgMessageEntity;
//import com.associationcloud.system.schdule.schdule.domain.PaymentNoticeEntity;
import com.demo.entity.JgMessageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShortMessageTool {

    protected static final Logger LOG = LoggerFactory.getLogger(ShortMessageTool.class);
    @Value("${masterSecret}")
    private static String masterSecret = "381a78804aadb80fad046c83";
    @Value("${appkey}")
    private static String appkey = "95de08380ba9d478167af884";
    @Value("${jPushUrl}")
    private static String uri = "https://api.sms.jpush.cn/v1/messages";

    public static String sendMessage(String phone, JgMessageEntity entity) {
        String rtn = "";
        SMSClient client = new SMSClient(masterSecret, appkey);
        SMSPayload payload = null;
        String templeteId = entity.getTemplateID();
            payload = SMSPayload.newBuilder()
                    .setMobileNumber(phone)
                    .setTempId(167001)  //测试模板编号
                    .addTempPara("subject", entity.getSubject())
                    .build();
        try {
            SendSMSResult res = client.sendTemplateSMS(payload);
            entity.setSuccess(true);
            rtn = res.toString();
            LOG.info(res.toString());
        } catch (APIRequestException e) {
            entity.setSuccess(false);
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
            rtn = e.getMessage();
        } catch (APIConnectionException e) {
            entity.setSuccess(false);
            LOG.error("Connection error. Should retry later. ", e);
            rtn = e.getMessage();
        }
        return rtn;
    }

    /***
    public static String sendPaymentMessage(String phone, PaymentNoticeEntity entity) {
        String rtn = "";
        SMSClient client = new SMSClient(masterSecret, appkey);
        SMSPayload payload = null;


        payload = SMSPayload.newBuilder()
                .setMobileNumber(phone)
                .setTempId(152739)
                .addTempPara("category", entity.getCategory())
                .addTempPara("stipulate", entity.getStipulate())
                .addTempPara("year", entity.getYear())
                .addTempPara("money", entity.getMoney() + "")
                .addTempPara("account_name", entity.getAccount_name())
                .addTempPara("account_number", entity.getAccount_number())
                .addTempPara("link_address", entity.getLink_address())
                .addTempPara("bank", entity.getBank())
                .addTempPara("sender", entity.getSender())
                .addTempPara("send_date", entity.getSend_date())
                .build();

        try {
            SendSMSResult res = client.sendTemplateSMS(payload);
            entity.setSuccess(true);
            rtn = res.toString();
            LOG.info(res.toString());
        } catch (APIRequestException e) {
            entity.setSuccess(false);
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
            rtn = e.getMessage();
        } catch (APIConnectionException e) {
            entity.setSuccess(false);
            LOG.error("Connection error. Should retry later. ", e);
            rtn = e.getMessage();
        }
        return rtn;
    }
     **/
}
