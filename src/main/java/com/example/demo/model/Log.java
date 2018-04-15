package com.example.demo.model;

import com.example.demo.constants.CommonConstant;
import com.example.demo.util.WebUtils;

import java.util.Date;

/**
 * Created by liulanhua on 2018/3/9.
 */
public class Log {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private static final String reqSource;//   请求来源，pc：pc端，wap：wap端   默认来源为pc

    private static final String localAddr;//   服务器IP

    private String ip;//   操作电脑ip

    private String fullName;//   操作人员名字

    private String loginName;//   操作人员登录账号

    private Date operateDateTime;//   操作时间

    private Date createDateTime;//   创建时间

    private Long id;

    private String type;//   日志类型，‘operate’:操作日志，‘exception’:异常日志

    private String moduleType;//   模块代码

    private String operateCode;//   操作代码

    private String operateValue;//   操作类型

    private String remark;//   操作备注(记录参数)

    private String operateStatus;//   操作状态（成功与否Y\\N）

    private String method;//   调用方法

    private String param;//   方法的请求参数

    private String exceptionDetail;//   异常信息

    static {
        reqSource = "wap";
        localAddr = WebUtils.getLocalAddr();
    }

    public void init() {
        User user = WebUtils.getSessionValue(CommonConstant.SESSION_USER);
        ip = WebUtils.getRemoteIP();
        loginName = user != null ? user.getName() : getLoginName();
        fullName = user != null ? user.getName() : getFullName();
        operateDateTime = new Date();
        createDateTime = new Date();
    }

    public Log() {
        init();
    }

    public Log(Log origin) {
        init();
        this.id = origin.id;
        this.type = origin.type;
        this.moduleType = origin.moduleType;
        this.operateCode = origin.operateCode;
        this.operateValue = origin.operateValue;
        this.remark = origin.remark;
        this.operateStatus = origin.operateStatus;
        this.method = origin.method;
        this.param = origin.param;
        this.exceptionDetail = origin.exceptionDetail;
        this.fullName = origin.fullName;
        this.loginName = origin.loginName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getOperateDateTime() {
        return operateDateTime;
    }

    public void setOperateDateTime(Date operateDateTime) {
        this.operateDateTime = operateDateTime;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    public String getOperateValue() {
        return operateValue;
    }

    public void setOperateValue(String operateValue) {
        this.operateValue = operateValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(String operateStatus) {
        this.operateStatus = operateStatus;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getExceptionDetail() {
        return exceptionDetail;
    }

    public void setExceptionDetail(String exceptionDetail) {
        this.exceptionDetail = exceptionDetail;
    }

    public String getLocalAddr() {
        return localAddr;
    }

    public static class Builder {

        private Log target;

        public Builder() {
            target = new Log();
        }

        public Builder id(Long id) {
            target.id = id;
            return this;
        }

        public Builder type(String type) {
            target.type = type;
            return this;
        }

        public Builder moduleType(String moduleType) {
            target.moduleType = moduleType;
            return this;
        }

        public Builder operateCode(String operateCode) {
            target.operateCode = operateCode;
            return this;
        }

        public Builder operateValue(String operateValue) {
            target.operateValue = operateValue;
            return this;
        }

        public Builder remark(String remark) {
            target.remark = remark;
            return this;
        }

        public Builder operateStatus(String operateStatus) {
            target.operateStatus = operateStatus;
            return this;
        }

        public Builder method(String method) {
            target.method = method;
            return this;
        }

        public Builder param(String param) {
            target.param = param;
            return this;
        }

        public Builder exceptionDetail(String exceptionDetail) {
            target.exceptionDetail = exceptionDetail;
            return this;
        }

        public Builder loginName(String loginName) {
            target.loginName = loginName;
            return this;
        }

        public Builder fullName(String fullName) {
            target.fullName = fullName;
            return this;
        }

        public Log build() {
            return new Log(target);
        }

    }
}
