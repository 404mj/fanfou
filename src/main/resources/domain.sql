show databases;
create database fanfou;
use fanfou;

create table `user` (
  `id` int(13) not null primary key  auto_increment comment '主键',
  `name` varchar(30) default null comment '姓名',
  `age` int(3) default null comment '年龄',
  `company` varchar(100) default null comment '公司'
) engine=InnoDB auto_increment=1 default charset=utf8


