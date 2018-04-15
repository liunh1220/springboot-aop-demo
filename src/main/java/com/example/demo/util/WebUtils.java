package com.example.demo.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;

/**
 * Created by liulanhua on 2018/3/9.
 */
public class WebUtils {
    /**
     * 描述：获取request对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 描述：获取responce对象
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

    }

    /**
     * 描述：获取session
     *
     * @return
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 描述：设置session值
     *
     * @param key
     * @param val
     */
    public static <T> void setSessionValue(String key, T val) {
        getSession().setAttribute(key, val);
    }

    /**
     * 描述：获取session值
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSessionValue(String key) {
        return (T) getSession().getAttribute(key);
    }

    /**
     * 描述：移除session
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T removeSessionValue(String key) {
        Object obj = getSession().getAttribute(key);
        getSession().removeAttribute(key);
        return (T) obj;
    }

    /**
     * 描述：获取客户端ip
     *
     * @param request
     * @return
     */
    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");


        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    /**
     * 获得本机IP
     *
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public final static String getLocalAddr() {
        String hostAddress = "";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
        }
        return hostAddress;
    }

    /**
     * 描述：获取客户端ip
     *
     * @return
     */
    public static String getRemoteIP() {
        HttpServletRequest request = getRequest();
        return getRemoteIP(request);
    }
}
