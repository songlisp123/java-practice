DROP DATABASE IF EXISTS `micro_demo_db`;
CREATE DATABASE `micro_demo_db`;
USE `micro_demo_db`;
DROP TABLE IF EXISTS `gun_info`;
CREATE TABLE gun_info (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(64) NOT NULL COMMENT '枪名称',
  `status` INT NOT NULL DEFAULT 0 COMMENT '状态:0-离线,1-空闲,2-使用中,3-故障',
  `type` INT NOT NULL COMMENT '类型:1-直流快充,2-交流慢充',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充电枪信息表';

insert into gun_info(name,status,type) values ('充电枪1号',1,1);
insert into gun_info(name,status,type) values ('充电枪2号',1,2);

DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `bill_id` varchar(255) NOT NULL COMMENT '订单号',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `gun_id` int(11) DEFAULT NULL COMMENT '设备ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;