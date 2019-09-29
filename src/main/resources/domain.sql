show databases;

create database fanfou;

use fanfou;

-- 食物详情
create table `food_detail`
(
  `food_id`    int not null primary key auto_increment comment '主键',
  `desc`       varchar(30) default null comment '食物名称',
  `kind`       tinyint     default null comment '食物的种类:0-菜品;1-面点;2-汤食;3-水果',
  `reconmmend` tinyint     default null comment '是否推荐:0-不是推荐的;1-今日推荐',
  `period`     tinyint     default null comment '食物所属时段:0-早餐;1-午餐;2-晚餐',
  `food_time`  date        default null comment '食物是哪一天的',
  `food_week`  tinyint     default null comment '食物是周几的',
  `up`         int         default 0 comment '赞的数量',
  `down`       int         default 0 comment '踩的数量',
  `food_belng` varchar(30) default null comment '属于哪个餐厅',
  KEY `index_get` (`period`,`food_time`, `food_week`,`reconmmend`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8;


-- 每日食物评论
create table `food_discuss`
(
  `discuss_id`      int not null primary key auto_increment comment '主键',
  `discuss_content` varchar(512) default null comment '评论内容',
  `discuss_time`    date         default null comment '针对某天食物的评论',
  KEY `index_day` (`discuss_time`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8;

