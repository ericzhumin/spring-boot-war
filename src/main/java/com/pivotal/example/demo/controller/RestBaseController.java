package com.pivotal.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 欢迎控制器
 * @author wulinyun
 * @version 2017年9月8日下午3:28:30
 */
@RestController
public class RestBaseController {
	@RequestMapping("/")
	public String home(){
		return "Hello World";
	}
}
