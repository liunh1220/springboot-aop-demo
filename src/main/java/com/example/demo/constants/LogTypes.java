package com.example.demo.constants;

/**
 * Created by liulanhua on 2018/3/9.
 */
public interface LogTypes {
    /**
     * 操作状态（成功与否Y\\N）
     *
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    static interface operateStatus {
        //  成功
        final String Y = "Y";

        //  失败
        final String N = "N";
    }

    /**
     * 日志类型
     *
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    static interface type {
        //  操作日志
        final String operate = "operate";

        //  异常日志
        final String exception = "exception";
    }

    /**
     * 模块类型
     *
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    static interface moduleType {
        //  登录模块
        final String LOGIN = "LOGIN";

        //  项目模块
        final String PROJECT = "PROJECT";

        //  客户模块
        final String CUSTOMER = "CUSTOMER";

        //  用户模块
        final String SYS_USER = "SYS_USER";
    }

    /**
     * 操作类型
     *
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    static interface operateValue {
        //  查询
        final String select = "select";

        //  登录
        final String login = "login";

        //  保存
        final String save = "save";

        //  新增
        final String add = "add";

        //  修改
        final String edit = "edit";

        //  删除
        final String delete = "delete";

        //  查看
        final String view = "view";

        //  修改密码
        final String editPassword = "editPassword";

        //  上传
        final String upload = "upload";

        //  下载
        final String down = "down";

        //  下载
        final String packagedown = "packagedown";

    }

    /**
     * 保存描述的前缀
     * 方便于批量修改该备注
     *
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    static interface Prefix {
        //  查询
        final String savePrefix = "新增/编辑";
    }
}
