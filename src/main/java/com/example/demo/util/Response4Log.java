package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.enums.PerformanceLogLevel;
import com.example.demo.enums.PerformanceLogType;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liulanhua on 2018/3/19.
 */
public class Response4Log extends PerformanceLog {

    private static final Logger logger = LoggerFactory.getLogger(Request4Log.class);

    /**
     * 响应时间(ms)
     */
    private long time;

    /**
     * 请求URI
     */
    private String uri;

    /**
     * 返回的http状态码
     */
    private int status;

    /**
     * 返回消息头
     */
    private String header;

    /**
     * 异常信息
     */
    private String error;

    /**
     * 请求消息体, 只有在type是spring mvc, 并且请求是json消息的情况下才有值.
     * 由于servlet request input stream的局限性, 消息体只能在业务请求读取后才能被拦截器获得, 所以只能在response里打印
     */
    private String requestBody;

    public Response4Log() {}

    public Response4Log(PerformanceLogType type, PerformanceLogLevel performanceLogLevel) {
        super(type, performanceLogLevel);
    }


    public static Response4Log create4SpringMVC(PerformanceLogLevel performanceLogLevel, String requestURI, long elapsedTime,
                                                String error, String requestBody, HttpServletResponse response) {

        Response4Log response4Log = new Response4Log(PerformanceLogType.SPRING_RESP, performanceLogLevel);

        try {

            response4Log.time = elapsedTime;
            response4Log.uri = requestURI;
            response4Log.error = error;
            response4Log.status = response.getStatus();

            if(!PerformanceLogLevel.MINIMUM.equals(performanceLogLevel)) {
                Map<String, String> headerMap = extractHeaderToMap(response);
                if(headerMap != null) {
                    response4Log.header = JSONObject.toJSONString(headerMap);
                }

                response4Log.requestBody = requestBody;

                response4Log.simplifyLogIfNecessary(performanceLogLevel);
            }

        } catch (Exception e) {
            logger.warn("", e);
        }

        return response4Log;
    }

    public static Response4Log create4RestTemplate(PerformanceLogLevel performanceLogLevel,  String requestURI, long elapsedTime,
                                                   String error, ClientHttpResponse response) {

        Response4Log response4Log = new Response4Log(PerformanceLogType.REST_RESP, performanceLogLevel);

        try {

            response4Log.time = elapsedTime;
            response4Log.uri = requestURI;
            response4Log.error = error;
            if(response != null) {
                response4Log.status = response.getRawStatusCode();

                if(!PerformanceLogLevel.MINIMUM.equals(performanceLogLevel)) {
                    if(response.getHeaders() != null) {
                        response4Log.header = JSONObject.toJSONString(response.getHeaders());
                    }
                }
            }

            response4Log.simplifyLogIfNecessary(performanceLogLevel);

        } catch (Exception e) {
            logger.warn("", e);
        }

        return response4Log;
    }

    public static Response4Log create4Feign(PerformanceLogLevel performanceLogLevel,  String requestURI, long elapsedTime,
                                            String error, Response response) {

        Response4Log response4Log = new Response4Log(PerformanceLogType.FEIGN_RESP, performanceLogLevel);

        try {

            response4Log.time = elapsedTime;
            response4Log.uri = requestURI;
            response4Log.error = error;

            if(response != null) {
                response4Log.status = response.status();

                if(!PerformanceLogLevel.MINIMUM.equals(performanceLogLevel)) {
                    if(response.headers() != null) {
                        response4Log.header = JSONObject.toJSONString(response.headers());
                    }
                }
            }

            response4Log.simplifyLogIfNecessary(performanceLogLevel);

        } catch (Exception e) {
            logger.warn("", e);
        }

        return response4Log;
    }

    public static Response4Log create4OkHttp(PerformanceLogLevel performanceLogLevel,  String requestURI, long elapsedTime,
                                             String error, okhttp3.Response response) {

        Response4Log response4Log = new Response4Log(PerformanceLogType.OKHTTP_RESP, performanceLogLevel);

        try {

            response4Log.time = elapsedTime;
            response4Log.uri = requestURI;
            response4Log.error = error;

            if(response != null) {
                response4Log.status = response.code();

                if(!PerformanceLogLevel.MINIMUM.equals(performanceLogLevel)) {
                    if(response.headers() != null) {
                        response4Log.header = JSONObject.toJSONString(response.headers().toMultimap());
                    }
                }
            }

            response4Log.simplifyLogIfNecessary(performanceLogLevel);

        } catch (Exception e) {
            logger.warn("", e);
        }

        return response4Log;
    }


    private void simplifyLogIfNecessary(PerformanceLogLevel performanceLogLevel) {
        if(PerformanceLogLevel.SIMPLE.equals(performanceLogLevel)) {
            if(this.header != null && this.header.length() > SIMPLE_MAX_SIZE) {
                this.header = this.header.substring(0, SIMPLE_MAX_SIZE);
            }

            if(this.requestBody != null && this.requestBody.length() > SIMPLE_MAX_SIZE) {
                this.requestBody = this.requestBody.substring(0, SIMPLE_MAX_SIZE);
            }

        }
    }


    private static Map<String, String> extractHeaderToMap(HttpServletResponse response) {
        Collection<String> headerNames = response.getHeaderNames();
        if (headerNames == null) {
            return null;
        }
        Map<String, String> headerMap = new HashMap<>();
        for(String header : headerNames) {
            headerMap.put(header, response.getHeader(header));
        }
        return headerMap;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

}
