package com.example.demo.annotation;

import java.lang.annotation.*;

/**
 * Created by liulanhua on 2018/3/9.
 */
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemServiceLog {
    String description() default "";//描述  
    String moduleType() default "";//模块代码  
    String operateValue() default "";//操作类型  
    boolean firstParamName() default false;
}
