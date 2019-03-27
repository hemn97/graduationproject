CREATE DATABASE competitionplatform DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use competitionplatform;

/* 用户 */
create table t_users(
	id BIGINT NOT NULL AUTO_INCREMENT primary key,
	nickname VARCHAR(55),
	gender VARCHAR(10),
	realname VARCHAR(55),
	school VARCHAR(100),
	avatar VARCHAR(255),
	tel_number CHAR(11),
	skills VARCHAR(255),
	activate_code CHAR(6),
	register_ip CHAR(36),
	register_time DATETIME,
	role_type INT comment '0:未激活 1:普通用户 2:管理员 3:封禁',
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

create view v_users as (
select * from t_users a
left join (select user_id as id_email,identifier as email from t_user_auths where identity_type='EMAIL') b
on a.id=b.id_email
left join (select user_id as id_username,identifier as username from t_user_auths where identity_type='USERNAME') c
on a.id=c.id_username
                       );
					   
create table t_user_auths(
	id BIGINT NOT NULL AUTO_INCREMENT primary key,
	user_id INT NOT NULL,
	identity_type ENUM('USERNAME','EMAIL'),
	identifier VARCHAR(55),
	credential VARCHAR(55)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

/* 竞赛 */
create table t_competitions(
	id BIGINT NOT NULL AUTO_INCREMENT primary key,
	title VARCHAR(255) NOT NULL,
	logo VARCHAR(255),
	category varchar(10),
	start_time DATETIME,
	end_time DATETIME,
	status INT NOT NULL comment '0:可报名 1:进行中 2:已结束',
	tags VARCHAR(255),
	details LONGTEXT,
	link VARCHAR(255),
	heat LONG,
	register_time DATETIME,
	live INT comment '1:正常 0:已删除'
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

create view v_competitions as (
  select * from t_competitions a
left join (select count(*) as team_number, compt_id from t_teams group by t_teams.compt_id) b
on a.id=b.compt_id
left join
  (select count(*) as view_number, theme_id as theme_id_view from t_browse_logs where theme_type='竞赛' group by theme_id,theme_type) c
  on a.id=c.theme_id_view
left join
                  (select count(*) as comment_number, theme_id as theme_id_comment from t_comments where theme_type='竞赛' group by theme_id,theme_type) d
    on a.id=d.theme_id_comment
);

/* 帖子 */			   
create table t_posts (
	id BIGINT NOT NULL AUTO_INCREMENT primary key,
	title  VARCHAR(255),
	user_id BIGINT,
	content LONGTEXT,
	abstract_content VARCHAR(255),
	category VARCHAR(10),
	release_time DATETIME,
	live INT comment '1:正常 0:已删除'
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

/* 浏览日志 */
create table t_browse_logs(
	id BIGINT NOT NULL AUTO_INCREMENT primary key,
	theme_type VARCHAR(10),
	theme_id BIGINT,
	user_id BIGINT,
	log_time DATETIME
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

/* 评论 */
create table t_comments (
	id BIGINT NOT NULL AUTO_INCREMENT primary key,
	user_id BIGINT,
	reply_id BIGINT,
	theme_type VARCHAR(10),
	theme_id BIGINT,
	farther_id BIGINT,
	content varchar(255),
	release_time DATETIME
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

create view v_posts as (
  select * from t_posts a left join 
  (select count(*) as comment_number, theme_id as theme_id_comment from t_comments where theme_type='帖子' group by theme_id,theme_type) b 
  on a.id=b.theme_id_comment 
  left join 
  (select count(*) as view_number, theme_id as theme_id_view from t_browse_logs where theme_type='帖子' group by theme_id,theme_type) c 
  on a.id=c.theme_id_view 
  left join 
  (select id as id_user,nickname,avatar,school from t_users) d on a.user_id=d.id_user
);

create view v_comments as (select * from t_comments a
left join (select id as id_reply_id, user_id as id_reply_userid, content as reply_content from t_comments) b
		on a.reply_id=b.id_reply_id
left join (select id as id_user, nickname, avatar, school from t_users) c
		on a.user_id=c.id_user
left join (select id as id_user_reply, nickname as reply_nickname from t_users) d
		on b.id_reply_userid=d.id_user_reply);
		
/* 队伍 */
create table t_teams (
	id BIGINT NOT NULL AUTO_INCREMENT primary key,
	compt_id BIGINT,
	name VARCHAR(255) NOT NULL,
	captain_id BIGINT,
	motto VARCHAR(255),
	status varchar(10),
	register_time DATETIME
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

create table t_teammates (
	id BIGINT NOT NULL AUTO_INCREMENT primary key,
	team_id BIGINT,
	user_id BIGINT,
	status varchar(10),
	join_time DATETIME
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

create view v_teams as (
select * from t_teams a
left join (select id as id_compt,title as compt_title from t_competitions) b
on a.compt_id=b.id_compt
left join (select id as id_captain,nickname as captain_name from t_users) c
on a.captain_id=c.id_captain
                       );
					   
drop view v_myteam;
create view v_myteam as (
select * from t_teammates a
left join (select id as id_team,captain_id,captain_name,compt_id,compt_title,motto,status as team_status from v_teams) b
    on a.team_id=b.id_team
                        );
						
create view v_teammates as (
select * from t_teammates a
left join (select id as id_user,nickname,school,skills,email from v_users) b
on a.user_id=b.id_user
													 );

/* 私信 */													 
create table t_messages(
	id BIGINT NOT NULL AUTO_INCREMENT primary key,
	sender_id BIGINT,
	recipient_id BIGINT,
	title varchar(255),
	content VARCHAR(255),
	status varchar(10),
	send_time DATETIME
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

create view v_messages as (
select * from t_messages a
left join (select id as id_sender,nickname as sender_name from t_users) b
		on a.sender_id=b.id_sender
left join (select id as id_recipient,nickname as recipient_name from t_users) c
		on a.recipient_id=c.id_recipient
													);
													
/* 视频 */
create table t_videos (
	id BIGINT NOT NULL AUTO_INCREMENT primary key,
	title VARCHAR(255) NOT NULL,
	author VARCHAR(255),
	introduction VARCHAR(255),
	figure VARCHAR(255),
	category VARCHAR(10),
	tags VARCHAR(255),
	src VARCHAR(255),
	score DECIMAL(2,1),
	release_time DATETIME,
	live INT
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

create table t_video_evaluations (
	id BIGINT NOT NULL AUTO_INCREMENT primary key,
	video_id BIGINT,
	user_id BIGINT,
	score INT,
	comment VARCHAR(255),
	live INT,
	evaluate_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create view v_video_evaluations as (
select * from t_video_evaluations a
left join (select id as id_user,nickname,avatar from t_users) b
on a.user_id=b.id_user
                                   );
								   
create view v_videos as (
  select * from t_videos a
  left join
  (select count(*) as view_number, theme_id as theme_id_view from t_browse_logs where theme_type='视频' group by theme_id,theme_type) a
  on a.id=a.theme_id_view
	left join
	(select count(*) as evaluate_number, video_id from t_video_evaluations where live=1 group by video_id) b
  on a.id=b.video_id
                        );