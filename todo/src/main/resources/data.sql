--DROP DATEBASE IF EXISTS vue02;
--CREATE DATABASE IF NOT EXISTS vue02;
USE vue02;


--DROP TABLE IF NOT EXISTS users;
--
--CREATE TABLE IF NOT EXISTS users(
--id BIGINT PRIMARY KEY AUTO_INCREMENT,
--username VARCHAR(30) NOT NULL UNIQUE,
--password VARCHAR(30) NOT NULL,
--email VARCHAR(30)
--);


CREATE TABLE tag
(
    id                   bigint auto_increment comment '车辆id' primary key,
    brand                varchar(30)  null comment '车辆品牌',
    license              varchar(20)  null comment '车牌号',
    model                varchar(100) null comment '车辆型号',
    code                 varchar(100) null comment '车辆识别码',
    displacement         varchar(20)  null comment '车辆排量',
    status               varchar(10)  null comment '车辆状态：空闲1 占用2',
    type                 varchar(50)  null comment '车辆类型',
    color                varchar(10)  null comment '车辆颜色',
    kilometers           varchar(10)  null comment '里程数',
    reg_time             datetime     null comment '上牌时间',
    buy_time             datetime     null comment '购买时间',
    price                varchar(10)  null comment '购买价格',
    battery_type         varchar(50)  null comment '电池类型',
    create_time          datetime     null comment '创建时间',
    update_time          datetime     null comment '更新时间',
    geofence_bind_status varchar(10)  null comment '电子围栏车辆绑定状态 已绑定1 未绑定0',
    geofence_id          bigint       null comment '电子围栏id',
    constraint license unique (license)
) comment '车辆表' auto_increment = 101;




