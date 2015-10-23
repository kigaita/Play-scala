CREATE TABLE "group" (
  id int NOT NULL ,
  name varchar(20) NOT NULL,
  description varchar(100) NOT NULL,
  PRIMARY KEY (id)
) ;

-- user table
CREATE TABLE "user" (
  id int NOT NULL ,
  ip_address varchar(15) NOT NULL,
  username varchar(100) NOT NULL,
  password text NOT NULL,
  salt varchar(255) DEFAULT NULL,
  email varchar(100) NOT NULL,
  permission text DEFAULT NULL,
  activation_code text DEFAULT NULL,
  activation_time timestamp DEFAULT NULL,
  forgotten_password_code text DEFAULT NULL,
  forgotten_password_time timestamp DEFAULT NULL,
  remember_code varchar(40) DEFAULT NULL,
  created_on timestamp NOT NULL,
  last_login timestamp  DEFAULT NULL,
  active int DEFAULT 0,
  first_name varchar(50) DEFAULT NULL,
  last_name varchar(50) DEFAULT NULL,
  company varchar(100) DEFAULT NULL,
  phone varchar(20) DEFAULT NULL,
  PRIMARY KEY (id)
) ;

-- - Table structure for table user_group

CREATE TABLE "user_group" (
  id int NOT NULL ,
  user_id int NOT NULL,
  group_id int NOT NULL,
  PRIMARY KEY (id)) ;


-- - Table structure for table login_attempt

CREATE TABLE "login_attempt" (
  id int NOT NULL ,
  ip_address varchar(15) NOT NULL,
  login varchar(100) NOT NULL,
  time timestamp DEFAULT NULL,
  PRIMARY KEY (id)
) ;


-- - Table structure for table rating

CREATE TABLE "rating" (
  id int NOT NULL ,
  classification varchar(100) NOT NULL,
  description text NOT NULL,
  PRIMARY KEY (id)
) ;

-- - Table structure for table story_category

CREATE TABLE "story_category" (
  id int NOT NULL ,
  title varchar(100) NOT NULL,
  description text NOT NULL,
  PRIMARY KEY (id)
) ;


-- - Create for table story

CREATE TYPE statusenum AS ENUM ('UNPUBLISHED', 'PUBLISHED', 'DELETED','BANNED');
CREATE TABLE "story" (
  id int NOT NULL ,
  title varchar(200) NOT NULL,
  text text NOT NULL,
  keywords text DEFAULT NULL,
  status statusenum,
  story_category_id int NOT NULL,
  rating_id int NOT NULL,
  created_by int NOT NULL,
  created_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modified_on timestamp NULL DEFAULT NULL,
  PRIMARY KEY (id)) ;

#
# Create for table story_view_count
#

CREATE TABLE "story_view_count" (
  id int NOT NULL ,
  story_id int  NOT NULL,
  view_count bigint  NOT NULL,
  PRIMARY KEY (id)) ;


-- - Create for table story_comment
CREATE TABLE "story_comment" (
  id int NOT NULL ,
  text text NOT NULL,
  parent_id int  NULL,
  visible int DEFAULT 0,
  story_id int  NOT NULL,
  created_by int NOT NULL,
created_on timestamp  NOT NULL DEFAULT current_timestamp,
modified_on timestamp   NULL,
  PRIMARY KEY (id)) ;



-- - Table structure for table news_category

CREATE TABLE "news_category" (
  id int NOT NULL ,
  title varchar(100) NOT NULL,
  description text NOT NULL,
  PRIMARY KEY (id)
) ;

-- - Create for table news

CREATE TABLE "news" (
  id int NOT NULL ,
  title varchar(200) NOT NULL,
  text text NOT NULL,
  keywords text NOT NULL,
  status statusenum,
  news_category_id int NOT NULL,
  rating_id int NOT NULL,
  created_by int NOT NULL,
  created_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modified_on timestamp NULL DEFAULT NULL,
  PRIMARY KEY (id)) ;

-- Create for table news_view_count
CREATE TABLE "news_view_count" (
  id int NOT NULL ,
  news_id int  NOT NULL,
  view_count bigint  NOT NULL,
  PRIMARY KEY (id)) ;

-- - Create for table comment
CREATE TABLE "news_comment" (
  id int NOT NULL ,
  text text NOT NULL,
  parent_id int  NULL,
  visible int DEFAULT 0,
  news_id int  NOT NULL,
  created_by int NOT NULL,
created_on timestamp  NOT NULL DEFAULT current_timestamp,
modified_on timestamp   NULL,
  PRIMARY KEY (id)) ;
