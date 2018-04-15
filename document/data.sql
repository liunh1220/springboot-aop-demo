CREATE TABLE `t_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `reqSource` varchar(10) DEFAULT 'pc' COMMENT '请求来源，pc：pc端，wap：wap端 默认来源为pc',
  `type` varchar(10) DEFAULT NULL COMMENT '日志类型，‘operate’:操作日志，‘exception’:异常日志',
  `ip` varchar(20) NOT NULL COMMENT '操作电脑ip',
  `fullName` varchar(50) NOT NULL COMMENT '操作人员名字',
  `loginName` varchar(50) NOT NULL COMMENT '操作人员登录账号',
  `moduleType` varchar(50) NOT NULL COMMENT '模块代码',
  `operateCode` varchar(50) NOT NULL COMMENT '操作代码',
  `operateValue` varchar(50) DEFAULT NULL COMMENT '操作类型',
  `operateDateTime` datetime NOT NULL COMMENT '操作时间',
  `createDateTime` datetime NOT NULL COMMENT '创建时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '操作备注(记录参数)',
  `operateStatus` varchar(20) DEFAULT NULL COMMENT '操作状态（成功与否Y\\N）',
  `localAddr` varchar(20) DEFAULT NULL COMMENT '服务器IP',
  `method` varchar(100) DEFAULT NULL COMMENT '调用方法',
  `param` varchar(2000) DEFAULT NULL COMMENT '方法的请求参数',
  `exceptionDetail` varchar(1000) DEFAULT NULL COMMENT '异常信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='操作日志';



