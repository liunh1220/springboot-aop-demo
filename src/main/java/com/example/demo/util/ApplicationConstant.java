package com.example.demo.util;

import com.example.demo.enums.PerformanceLogLevel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by liulanhua on 2018/3/19.
 */
public class ApplicationConstant {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConstant.class);

    @Value("${spring.application.name}")
    public String applicationName;


    @Value("${spring.profiles.active:dev}")
    public String profile;

    /**
     * NONE 不打印日志
     * MINIMUM 不打印请求消息体, 请求参数, 请求响应头
     * SIMPLE 打印精简的日志
     * ALL 打印所有日志
     */
    @Value("${app.performance.log:NOTSET}")
    public String performanceLogType;

    @Value("${app.performance.log.ignore.urls:}")
    public String[] performanceLogIgnoreUrls;


    /**
     * 确认用哪种性能日志级别
     *
     * @return
     */
    public PerformanceLogLevel determinePerformanceLogType() {
        PerformanceLogLevel type = PerformanceLogLevel.NOTSET;
        try {
            type = PerformanceLogLevel.valueOf(performanceLogType.toUpperCase());
        } catch (Exception e) {
            logger.error("", e);
        }
        if (type.equals(PerformanceLogLevel.NOTSET)) {
            if (isProdProfile()) {
                return PerformanceLogLevel.SIMPLE;
            } else {
                return PerformanceLogLevel.ALL;
            }
        } else {
            return type;
        }
    }

    public boolean isDevProfile() {
        return StringUtils.isBlank(profile) || "DEV".equalsIgnoreCase(profile);
    }

    public boolean isTestProfile() {
        return "TEST".equalsIgnoreCase(profile);
    }

    public boolean isPrevProfile() {
        return "PREV".equalsIgnoreCase(profile);
    }

    public boolean isProdProfile() {
        return "PROD".equalsIgnoreCase(profile);
    }

}
