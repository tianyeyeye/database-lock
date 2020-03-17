drop table if exists job;
create table job(
`id` int primary key AUTO_INCREMENT comment '主键',
`job_code` varchar(20) UNIQUE KEY not null comment '作业编码',
`job_name` varchar(50) comment '作业名称', 
description varchar(200) comment '作业描述',
`job_enable` bit not null comment '是否启用：0-禁用，1-启用',
`job_lock` bit not null comment '是否锁定：0-空闲，1-锁定',
time_out int not null comment '超时时间（分钟）',
create_time datetime not null comment '创建时间',
last_update_time datetime not null comment '最后更新时间',
`version` BIGINT not null default 0 comment '版本号'
)