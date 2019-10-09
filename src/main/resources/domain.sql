show databases;

create database fanfou;

use fanfou;

-- 食物详情
create table `food_item`
(
  `food_id`    int not null primary key auto_increment comment '主键',
  `food_desc`  varchar(30) default null comment '食物名称',
  `kind`       tinyint     default null comment '食物的种类:0-菜品;1-面点;2-汤食;3-水果',
  `recommend`  tinyint     default null comment '是否推荐:0-不是推荐的;1-今日推荐',
  `period`     tinyint     default null comment '食物所属时段:0-早餐;1-午餐;2-晚餐',
  `food_time`  date        default null comment '食物是哪一天的',
  `food_week`  tinyint     default null comment '食物是周几的',
  `stars`      tinyint     default 5 comment '每个食物的星级',
  `up`         int         default 0 comment '赞的数量',
  `down`       int         default 0 comment '踩的数量',
  `food_belng` tinyint     default null comment '属于哪个餐厅:0-B1大餐厅;1-B1小餐厅',
  KEY `index_get` (`period`, `food_time`, `recommend`, `food_belng`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8;


-- 每日食物评论
create table `food_comment`
(
  `comment_id`      int not null primary key auto_increment comment '主键',
  `content`         varchar(512) default null comment '评论内容',
  `comment_time`    date         default null comment '针对某天食物的评论',
  `restaurant`      tinyint      default null comment '属于哪个餐厅:0-B1大餐厅;1-B1小餐厅',
  `discusser`       varchar(20)  default null comment '评论人',
  UNIQUE `index_day` (`content`, `comment_time`,`restaurant`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8;

-- 开发过程修改
insert into food_item (`desc`, kind, reconmmend, period, food_time, food_week, up, down, food_belng)
values ('红烧排骨', 0, 1, 1, '2019-09-30', 1, 0, 0, 0),
       ('馒头', 1, 0, 1, '2019-09-30', 1, 0, 0, 0),
       ('老厨白菜', 1, 1, 1, '2019-09-30', 1, 0, 0, 0),
       ('小米粥', 2, 0, 2, '2019-09-30', 1, 0, 0, 0);
insert into food_item (`desc`, kind, reconmmend, period, food_time, food_week, up, down, food_belng)
values ('豆腐脑', 2, 1, 0, '2019-09-30', 1, 0, 0, 0),
       ('煎饼果子', 1, 0, 0, '2019-09-30', 1, 0, 0, 0),
       ('西葫芦水饺', 1, 0, 2, '2019-09-30', 1, 0, 0, 0),
       ('火龙果', 3, 0, 2, '2019-09-30', 1, 0, 0, 0);

insert into food_comment (content, comment_time, discusser)
VALUES ('白菜有点儿烂了，也可能是焖的有点久了', '2019-09-30', ''),
       ('小米粥一直是我的最爱！！！~~~', '2019-09-30', ''),
       ('红烧排骨太硬了了！赞赞赞！', '2019-09-30', '');

insert into food_comment (content, comment_time, discusser)
VALUES ('永远抢不到的煎饼果子和水饺', '2019-09-30', '');

alter table food_item
  change reconmmend recommend tinyint default null comment '是否推荐:0-不是推荐的;1-今日推荐';
alter table food_item
  modify column food_belng tinyint default null comment '属于哪个餐厅:0-B1大餐厅;1-B1小餐厅';
alter table food_comment add `restaurant` tinyint      default null comment '属于哪个餐厅:0-B1大餐厅;1-B1小餐厅';