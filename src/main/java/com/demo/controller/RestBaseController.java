package com.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.entity.JgMessageEntity;
import com.demo.service.ShortMessageTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * 欢迎控制器
 *
 * @author wulinyun
 * @version 2017年9月8日下午3:28:30
 */
@RestController
public class RestBaseController {
    private static Logger log = LoggerFactory.getLogger(RestBaseController.class);

//	@Autowired
//	private ShortMessageTool shortMessageTool;

    @RequestMapping("/")
    public String home() {
        return "Hello World";
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public String send(@RequestBody JSONObject jsonObject, HttpServletResponse response) {
        String res = null;
        try {

            //极光短信接口-调用组件池的方法
            JgMessageEntity jgMessageEntity = new JgMessageEntity();
            jgMessageEntity.setSubject(jsonObject.getString("content"));
            String iphonenume = jsonObject.getString("phone");
            ShortMessageTool.sendMessage(iphonenume, jgMessageEntity);
            log.info("接收人222："+iphonenume+",接收内容："+jsonObject.getString("content"));
            res = "true";


            //未调用组件池的方法
//            String rtn = "";
//            SMSClient client = new SMSClient(masterSecret, appkey);
//            SMSPayload payload = null;
//            String templeteId = entity.getTemplateID();
//            payload = SMSPayload.newBuilder()
//                    .setMobileNumber(phone)
//                    .setTempId(166932)  //测试模板编号
//                    .addTempPara("subject", entity.getSubject())
//                    .build();
//            try {
//                SendSMSResult res = client.sendTemplateSMS(payload);
//                entity.setSuccess(true);
//                rtn = res.toString();
//                LOG.info(res.toString());
//            } catch (APIRequestException e) {
//                entity.setSuccess(false);
//                LOG.error("Error response from JPush server. Should review and fix it. ", e);
//                LOG.info("HTTP Status: " + e.getStatus());
//                LOG.info("Error Message: " + e.getMessage());
//                rtn = e.getMessage();
//            } catch (APIConnectionException e) {
//                entity.setSuccess(false);
//                LOG.error("Connection error. Should retry later. ", e);
//                rtn = e.getMessage();
//            }

        } catch (Exception ex) {
            log.info("出现异常,错误详细："+ex.getMessage());
            
            res = "false";
        }

        return res;
    }
}
