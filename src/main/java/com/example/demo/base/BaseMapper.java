package com.example.demo.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by liulanhua on 2018/3/9.
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
