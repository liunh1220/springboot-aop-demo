package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.annotation.EventMonitor;
import com.example.demo.annotation.SystemControllerLog;
import com.example.demo.annotation.SystemServiceLog;
import com.example.demo.constants.CommonConstant;
import com.example.demo.constants.LogTypes;
import com.example.demo.model.User;
import com.example.demo.service.TestService;
import com.example.demo.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liulanhua on 2018/2/6.
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    private static String json = "{\"password\":\"111111\",\"createTime\":\"2017-04-19 17:21:31.0\",\"name\":\"aaa\",\"id\":\"1\"}";

    @GetMapping("/testEventMonitor")
    @EventMonitor(name="testEventMonitor",desc = "测试EventMonitor")
    public void testEventMonitor() throws Exception{
        System.out.println("================ testEventMonitor==================");
        Thread.sleep(100);
    }

    @SystemControllerLog(moduleType= LogTypes.operateValue.select,operateValue= LogTypes.operateValue.login,description="controllerLog")
    @GetMapping("/controllerLog")
    public void controllerLog() {
        WebUtils.setSessionValue(CommonConstant.SESSION_USER,JSONObject.parseObject(json,User.class));
    }

    @GetMapping("/serviceLog")
    public void serviceLog() throws Exception{
        WebUtils.setSessionValue(CommonConstant.SESSION_USER,JSONObject.parseObject(json,User.class));
        testService.serviceMethod();
    }
}
