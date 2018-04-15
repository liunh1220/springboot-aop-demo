package com.example.demo.util;

import com.example.demo.constants.CommonConstant;

import java.util.List;
import java.util.Map;

/**
 * Created by liulanhua on 2018/3/9.
 */
public class WebResult {

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回数据
     */
    private Object data;

    private Map<?, ?> map;

    private List<Map<String, Object>> list;

    public WebResult() {
    }

    public WebResult(String msg, String code) {
        super();
        this.msg = msg;
        this.code = code;
    }

    public WebResult(String msg, String code, Object data) {
        super();
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<?, ?> getMap() {
        return map;
    }

    public void setMap(Map<?, ?> map) {
        this.map = map;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "WebResult [msg=" + msg + ", code=" + code + ", data=" + data + "]";
    }

    /**
     * 初始失败方法
     *
     * @author cc HSSD0473
     * @see [类、类#方法、类#成员]
     */
    public void invokeFail() {
        this.data = null;
        this.code = CommonConstant.BACK_FAIL;
        this.msg = "操作失败";
    }

    public void invokeFail(String msg) {
        this.data = null;
        this.code = CommonConstant.BACK_FAIL;
        if (msg != null && !msg.equals("")) {
            this.msg = msg;
        }
    }

    public void invokeSuccess() {
        this.code = CommonConstant.BACK_SUCCESS;
        this.msg = "操作成功";
    }

    public void invokeSuccess(String msg) {
        if (msg != null && !msg.equals("")) {
            this.msg = msg;
        }
        this.code = CommonConstant.BACK_SUCCESS;
    }

}
