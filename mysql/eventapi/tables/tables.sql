-- ここにテーブルの定義を配置する
-- 例：
-- create table sample_id (
--   id bigint NOT NULL,
--   primary key(id)
-- );

create table event (
   id bigint NOT NULL AUTO_INCREMENT,
   name varchar(255) NOT NULL,
   content text,
   primary key(id)
);