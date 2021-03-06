show databases;
create database fanfou;
use fanfou;

create table `user`
(
  `id`      int(13) not null primary key auto_increment comment '主键',
  `name`    varchar(30)  default null comment '姓名',
  `age`     int(3)       default null comment '年龄',
  `company` varchar(100) default null comment '公司'
) engine = InnoDB
  auto_increment = 1
  default charset = utf8;

-- 食物详情
create table `food_item`
(
  `food_id`    int not null primary key auto_increment comment '主键',
  `food_desc`  varchar(30) default null comment '食物名称',
  `kind`       tinyint     default null comment '食物的种类:0-水果;1-凉菜;2-热菜;3-面食;4-汤粥;5-现场制作;',
  `recommend`  tinyint     default 0 comment '是否推荐:0-不是推荐的;1-今日推荐',
  `period`     tinyint     default null comment '食物所属时段:0-早餐;1-午餐;2-晚餐',
  `food_time`  date        default null comment '食物是哪一天的',
  `food_week`  varchar(5)  default null comment '食物是周几的',
  `stars`      tinyint     default 5 comment '每个食物的星级',
  `up`         int         default 0 comment '赞的数量',
  `down`       int         default 0 comment '踩的数量',
  `food_belng` tinyint     default null comment '属于哪个餐厅:0-B1大餐厅;1-B1小餐厅',
  KEY `index_get` (`period`, `food_time`, `recommend`, `food_belng`),
  UNIQUE `unique_food` (`food_desc`, `period`, `food_time`, `food_belng`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8;


-- 每日食物评论
create table `food_comment`
(
  `comment_id`   int not null primary key auto_increment comment '主键',
  `content`      varchar(512) default null comment '评论内容',
  `comment_time` date         default null comment '评论的时间',
  `food_time`    date         default null comment '食物是哪一天的',
  `restaurant`   tinyint      default null comment '属于哪个餐厅:0-B1大餐厅;1-B1小餐厅',
  `discusser`    varchar(20)  default null comment '评论人',
  `visible`      tinyint      default 1 comment '评论是否可见m默认1可见,0不可见',
  UNIQUE `index_day` (`content`, `food_time`, `restaurant`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8;

-- 开发过程修改
alter table food_item
  change reconmmend recommend tinyint default null comment '是否推荐:0-不是推荐的;1-今日推荐';
alter table food_item
  modify column food_belng tinyint default null comment '属于哪个餐厅:0-B1大餐厅;1-B1小餐厅';
alter table food_comment
  add `restaurant` tinyint default null comment '属于哪个餐厅:0-B1大餐厅;1-B1小餐厅';
alter table food_item
  modify column food_week varchar(5) default null comment '食物是周几的';
alter table food_comment
  add `food_time` date default null comment '食物是哪一天的';
alter table food_comment
  add `visible` tinyint default 1;

delete *
from food_item
where 1 = 1;
delete *
from food_comment
where 1 = 1;
truncate table food_item;

alter table food_item
  add UNIQUE `index_food` (`food_desc`, `period`, `food_time`, `food_belng`);

-- 每日排队和上座数据落地便于分析整理
create table `count_data`
(
  `data_id`     int not null primary key auto_increment comment '主键',
  `restaurant`  tinyint  default null comment '所属餐厅0|1',
  `count_type`  tinyint  default null comment '数据统计类型，1：排队数据;2：上座数据',
  `count_key`   datetime default null comment '年月日时分秒 redis键值',
  `count_value` float    default 0 comment '对应类型数据',
  key `index_count` (`restaurant`, `count_type`, `count_key`, `count_value`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8;

-- 加班餐预约
create table `booked_user`
(
  `book_uid`  int          not null primary key auto_increment,
  `memid`     varchar(100) not null,
  `name`      varchar(30),
  `msisdn`    varchar(15)  not null comment '手机号',
  `dept_name` varchar(100),
  unique `index_bookuser` (`memid`, `msisdn`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8;

create table `booked_record`
(
  `book_id`     int  not null primary key auto_increment,
  `book_uid`    int  not null,
  `book_time`   date not null,
  `book_week`   varchar(1),
  `book_period` tinyint comment '0 1 2',
  `book_rest`   tinyint comment '0机关餐厅 2七里山餐厅 3德亨餐厅',
  unique `idx_book_record` (`book_uid`, `book_time`, `book_rest`, `book_period`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8;