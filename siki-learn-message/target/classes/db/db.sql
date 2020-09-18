CREATE TABLE t_message_channel(
  id BIGINT(20) UNSIGNED PRIMARY KEY comment '消息通道id',
  channel_no VARCHAR(32) NOT NULL comment '消息通道编号',
  name VARCHAR(32) NOT NULL UNIQUE comment '消息通道名称',
  type TINYINT UNSIGNED NOT NULL comment '消息通道类型 0-短信,1-邮件,2-即时通讯',
  is_batch TINYINT UNSIGNED NOT NULL comment '是否支持批量发送，0-支持，1-不支持',
  status TINYINT UNSIGNED NOT NULL default 1 comment '消息通道状态 0-上线,1-下线',
  template_range TINYINT UNSIGNED NOT NULL comment '模板类型，0-内部模板,1-外部模板, ',
  match_templates VARCHAR(32) comment '适用模板类型-内部模板必须 数组 ; 分隔',
  limited_str INT UNSIGNED NOT NULL comment '内容限制长度',
  mark VARCHAR(255) comment '描述',
  create_time  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  update_time  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


CREATE TABLE t_message_template(
  id BIGINT(20) UNSIGNED PRIMARY KEY comment '消息模板id' ,
  name VARCHAR(32) NOT NULL UNIQUE comment '消息模板名称',
  type TINYINT UNSIGNED NOT NULL comment '消息模板类型 0-字符，1-HTML',
  content VARCHAR(10000) NOT NULL comment '消息模板内容',
  status TINYINT UNSIGNED NOT NULL comment '消息模板状态',
  params VARCHAR(255) NOT NULL comment '消息模板参数',
  mark VARCHAR(255) comment '描述',
  create_time  timestamp  NOT NULL default current_timestamp,
  update_time  timestamp  NOT NULL default current_timestamp
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE m_message_order(
  id BIGINT(20) UNSIGNED PRIMARY KEY comment '消息订单号' ,
  message_no VARCHAR(32)  NOT NULL comment '上游消息编号',
  serial_num VARCHAR(64)  NOT NULL comment '流水号-可能下游有规则要求',
  channel_id BIGINT(20) UNSIGNED comment '消息通道编号',
  template_id VARCHAR(64) comment '模板号-外部或内部',
  target VARCHAR(10000) NOT NULL comment '发送目标地址',
  notic_addr VARCHAR(1024) comment '结果通知地址',
  notic_status TINYINT UNSIGNED NOT NULL default 0 comment '消息发送结果 0-pendding，1-success，2-faile',
  result TINYINT UNSIGNED NOT NULL comment '消息发送结果 0-init，1-sending，2-sended 3-faile，4-success',
  mark VARCHAR(1024) comment '描述',
  create_time  timestamp   NOT NULL default current_timestamp comment '创建时间',
  update_time  timestamp   NOT NULL default current_timestamp comment '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_message_consume(
  id BIGINT(20) UNSIGNED PRIMARY KEY
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

