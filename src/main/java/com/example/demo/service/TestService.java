package com.example.demo.service;

import com.example.demo.annotation.SystemServiceLog;
import com.example.demo.constants.LogTypes;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/4/15.
 */
@Service
public class TestService {

    @SystemServiceLog(moduleType= LogTypes.operateValue.select,operateValue=LogTypes.operateValue.login,description=" serviceLog ")
    public void serviceMethod() throws Exception{
        System.out.println("================  test SystemServiceLog ==================");
        // 模拟异常
        throw new  Exception("test SystemServiceLog ");
    }
}
