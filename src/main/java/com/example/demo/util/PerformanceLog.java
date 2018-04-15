package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.enums.PerformanceLogLevel;
import com.example.demo.enums.PerformanceLogType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by liulanhua on 2018/3/19.
 */
public class PerformanceLog implements Serializable {


    private static final Logger logger = LoggerFactory.getLogger(PerformanceLog.class);

    public static final int SIMPLE_MAX_SIZE = 1000;

    public static final String LOG_PREFIX = "PerfLog";

    /**
     * 日志类型
     */
    protected PerformanceLogType type;

    //暂时不生成uuid
    @JsonIgnore
    protected String uuid;

    @JsonIgnore
    protected PerformanceLogLevel performanceLogLevel;

    public PerformanceLog() {}

    public PerformanceLog(PerformanceLogType type, PerformanceLogLevel performanceLogLevel) {
        this.type = type;
        this.performanceLogLevel = performanceLogLevel;
    }

    public PerformanceLogType getType() {
        return type;
    }

    public void setType(PerformanceLogType type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public PerformanceLogLevel getPerformanceLogLevel() {
        return performanceLogLevel;
    }

    public void setPerformanceLogLevel(PerformanceLogLevel performanceLogLevel) {
        this.performanceLogLevel = performanceLogLevel;
    }

    @Override
    public String toString() {
        return String.format("%s_%s:%s", LOG_PREFIX, typeToToken(type), JSONObject.toJSON(this));
    }

    private String typeToToken(PerformanceLogType type) {
        String token = "0";
        if(type == null) {
            logger.error("PerformanceLogType is null");
        } else if(type.name().endsWith("REQ")){
            token = "0";
        } else if(type.name().endsWith("RESP")){
            token = "1";
        }
        return token;
    }

    protected static String tryTrimToSimple(String str, PerformanceLogLevel performanceLogLevel) {

        if(performanceLogLevel.equals(PerformanceLogLevel.SIMPLE) && str != null && str.length() > SIMPLE_MAX_SIZE) {
            return str.substring(0, SIMPLE_MAX_SIZE);
        }

        return str;

    }

}
