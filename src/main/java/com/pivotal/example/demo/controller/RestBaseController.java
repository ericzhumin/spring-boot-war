package com.pivotal.example.demo.controller;

import com.pivotal.example.demo.entity.JgMessageEntity;
import com.pivotal.example.demo.service.ShortMessageTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 欢迎控制器
 *
 * @author wulinyun
 * @version 2017年9月8日下午3:28:30
 */
@RestController
public class RestBaseController {

//	@Autowired
//	private ShortMessageTool shortMessageTool;

    @RequestMapping("/")
    public String home() {
        return "Hello World";
    }

    @RequestMapping("/send")
    public String send() {
        String res=null;
        try {
            JgMessageEntity jgMessageEntity = new JgMessageEntity();
            jgMessageEntity.setSubject("晚上好");
            String iphonenume = "13311705786";
            ShortMessageTool.sendMessage(iphonenume, jgMessageEntity);
            res ="发送内容："+jgMessageEntity.getSubject()+",接收人手机："+iphonenume;

        } catch (Exception ex) {

        }

        return res;
    }
}
