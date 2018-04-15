package com.example.demo.intercept;

import com.alibaba.fastjson.JSON;
import com.example.demo.annotation.SystemControllerLog;
import com.example.demo.annotation.SystemServiceLog;
import com.example.demo.constants.CommonConstant;
import com.example.demo.constants.LogTypes;
import com.example.demo.dao.LogMapper;
import com.example.demo.model.Log;
import com.example.demo.model.User;
import com.example.demo.util.WebResult;
import com.example.demo.util.WebUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by liulanhua on 2018/3/9.
 */
@Aspect
@Component
public class SystemLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

    //  队列
    private static BlockingQueue<Log> queue = new LinkedBlockingQueue<Log>();

    //  缓存线程池
    private static ExecutorService threadPool = Executors.newFixedThreadPool(3);

    //  任务数
    private static int taskSize = 6;

    //  线程是否已启动
    boolean isStartThread = false;

    //  用来启动或停止线程
    static boolean run = true;

    @Autowired
    private LogMapper logMapper;

    //  Service层切点
    @Pointcut("@annotation(com.example.demo.annotation.SystemServiceLog)")
    public void serviceAspect() {
    }

    //  Controller层切点
    @Pointcut("@annotation(com.example.demo.annotation.SystemControllerLog)")
    public void controllerAspect() {
    }

    public static BlockingQueue<Log> getQueue() {
        return queue;
    }

    public static void setQueue(BlockingQueue<Log> queue) {
        SystemLogAspect.queue = queue;
    }

    public static boolean isRun() {
        return run;
    }

    public static void setRun(boolean run) {
        SystemLogAspect.run = run;
    }

    /**
     * 返回通知  用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     * @param result    返回值
     * @see [类、类#方法、类#成员]
     */
    @AfterReturning(value = "controllerAspect()", returning = "result")
    public void afterReturn(JoinPoint joinPoint, Object result) {
        //  请求的IP
        User user = WebUtils.getSessionValue(CommonConstant.SESSION_USER);
        WebResult webResult = new WebResult();
        webResult.setCode(CommonConstant.BACK_SUCCESS);
        try {
            if (WebResult.class.isInstance(result)) {
                webResult = (WebResult) result;
            }
            InnnerBean innnerBean = getControllerMethodDescription(joinPoint);
            Object[] arguments = innnerBean.getArguments();
            String remark = innnerBean.getDescription();

            Log log = new Log.Builder().type(LogTypes.type.operate)
                    .moduleType(innnerBean.getModuleType())
                    .operateCode(joinPoint.getSignature().getName())
                    .operateValue(innnerBean.getOperateValue())
                    .remark(remark)
                    .operateStatus(webResult.getCode().equals(CommonConstant.BACK_SUCCESS) ? LogTypes.operateStatus.Y
                            : LogTypes.operateStatus.N)//  返回值1操作成功，否则失败
                    .method((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"))
                    .param(arguments.toString())
                    .loginName(user.getName())
                    .fullName(user.getName())
                    .build();
            //  放入队列
            queue.put(log);
            if (!isStartThread) {
                for (int i = 0; i < taskSize; i++) {
                    threadPool.execute(new saveLogThread());
                }
                isStartThread = true;
            }
        } catch (Exception e) {
            logger.error("异常信息:{}", e.toString());
        }
    }

    /**
     * 异常通知  用于拦截service层记录异常日志
     *
     * @param joinPoint
     * @param e
     * @see [类、类#方法、类#成员]
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        //  读取session中的用户
        User user = WebUtils.getSessionValue(CommonConstant.SESSION_USER);
        String params = "";

        try {
            if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    params += JSON.toJSONString(joinPoint.getArgs()[i].toString()) + ";";
                }
            }

            InnnerBean innnerBean = getServiceMthodDescription(joinPoint);
            String loginName = "";

            Log log =
                    new Log.Builder().type(LogTypes.type.exception)
                            .moduleType(innnerBean.getModuleType())
                            .operateCode(joinPoint.getSignature().getName())
                            .operateValue(innnerBean.getOperateValue())
                            .remark(innnerBean.getDescription())
                            .operateStatus(LogTypes.operateStatus.N)
                            .method(
                                    (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"))
                            .param(params)
                            .exceptionDetail(e.toString())
                            .build();
            //  放入队列
            queue.put(log);
            if (!isStartThread) {
                new Thread(new saveLogThread()).start();
                isStartThread = true;
            }
        } catch (Exception ex) {
            logger.error("异常信息:{}", ex.toString());
        } finally {
            logger.error("异常方法:{" + joinPoint.getTarget().getClass().getName() + "}异常代码:{"
                    + joinPoint.getSignature().getName() + "}异常信息:{" + e.toString() + "}参数:{" + params + "}");
        }

    }

    /**
     * 获取注解中对方法的描述信息  用于service层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("rawtypes")
    public static InnnerBean getServiceMthodDescription(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String moduleType = "";
        String operateValue = "";
        String description = "";
        InnnerBean innnerBean = new InnnerBean(moduleType, operateValue, description);
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    SystemServiceLog annotation = method.getAnnotation(SystemServiceLog.class);
                    moduleType = annotation.moduleType();
                    operateValue = annotation.operateValue();
                    description = annotation.description();
                    innnerBean = new InnnerBean(moduleType, operateValue, description);
                    break;
                }
            }
        }
        innnerBean.setArguments(arguments);
        return innnerBean;
    }

    /**
     * 获取注解中对方法的描述信息  用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("rawtypes")
    public static InnnerBean getControllerMethodDescription(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String moduleType = "";
        String operateValue = "";
        String description = "";
        boolean firstParamName = false;
        InnnerBean innnerBean = new InnnerBean(moduleType, operateValue, description);
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    SystemControllerLog annotation = method.getAnnotation(SystemControllerLog.class);
                    moduleType = annotation.moduleType();
                    operateValue = annotation.operateValue();
                    description = annotation.description();
                    firstParamName = annotation.firstParamName();
                    innnerBean = new InnnerBean(moduleType, operateValue, description);
                    innnerBean.setFirstParamName(firstParamName);
                    break;
                }
            }
        }
        innnerBean.setArguments(arguments);
        return innnerBean;
    }

    /**
     * 内部类封装注入信息
     *
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    static class InnnerBean {
        private String moduleType;//  模块代码

        private String description;//  描述

        private String operateValue;//  操作类型

        private boolean firstParamName;

        private Object[] arguments;

        public InnnerBean(String moduleType, String operateValue, String description) {
            super();
            this.moduleType = moduleType;
            this.description = description;
            this.operateValue = operateValue;
        }

        public String getOperateValue() {
            return operateValue;
        }

        public void setOperateValue(String operateValue) {
            this.operateValue = operateValue;
        }

        public String getModuleType() {
            return moduleType;
        }

        public void setModuleType(String moduleType) {
            this.moduleType = moduleType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Object[] getArguments() {
            return arguments;
        }

        public void setArguments(Object[] arguments) {
            this.arguments = arguments;
        }

        public boolean isFirstParamName() {
            return firstParamName;
        }

        public void setFirstParamName(boolean firstParamName) {
            this.firstParamName = firstParamName;
        }
    }

    /**
     * 异步保存日志
     *
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    class saveLogThread implements Runnable {
        Lock lock = new ReentrantLock();

        @Override
        public void run() {
            try {
                while (run) {
                    while (queue.size() != 0) {
                        //  如果对插入顺序无要求，此处不需要同步可提升效率
                        lock.lock();
                        Log log = queue.take();
                        logMapper.insert(log);
                        lock.unlock();
                    }
                    Thread.sleep(3000);
                }
            } catch (InterruptedException e) {
                logger.error("saveLogThread被唤醒：" + e.toString());
            } catch (Exception e) {
                logger.error("saveLogThread异常：" + e.toString());
            }
        }
    }

}
