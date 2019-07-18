package com.pivotal.example.demo.controller;
/**
 * 获取应用容器一些基本信息
 * @author wulinyun
 * 2018年1月23日下午1:23:07
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class InfoController {
	@Autowired(required=false)
	private Cloud cloud;
	private Environment springEnvironment;
	@Autowired
	public InfoController(Environment springEnvironment)
	{
	     this.springEnvironment = springEnvironment;
	}
	 @ResponseBody
	 @RequestMapping({"/info"})
	 public Map<String,Object> info() {
		 		 Map<String,Object> resultMap = new HashMap<String,Object>();
		 		 resultMap.put("profiles", this.springEnvironment.getActiveProfiles());
		 		 resultMap.put("services", getServiceNames());
	      return resultMap;
	  }
	 @RequestMapping({"/env"})
	 @ResponseBody
	 public Map<String, String> showEnvironment() {
	      return System.getenv();
	 }
	 @RequestMapping({"/spring_env"})
	 @ResponseBody
	 public Map<String, Object> showSpringEnvironment() {
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("VCAP_SERVICES", this.springEnvironment.getProperty("VCAP_SERVICES"));
		 return map;
	 }
	 @RequestMapping({"/service"})
	 @ResponseBody
	 public List<ServiceInfo> showServiceInfo() {
		 if (this.cloud != null) {
			 return this.cloud.getServiceInfos();
		 }
		 return new ArrayList();
	}
	 
    private String[] getServiceNames()
    {
      if (this.cloud != null) {
        List<ServiceInfo> serviceInfos = this.cloud.getServiceInfos();
  
        List names = new ArrayList();
        for (ServiceInfo serviceInfo : serviceInfos) {
          names.add(serviceInfo.getId());
        }
        return (String[])names.toArray(new String[names.size()]);
      }
      return new String[0];
    }
}
