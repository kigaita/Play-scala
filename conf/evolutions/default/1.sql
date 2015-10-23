drop database if exists kenyapatches;
create database if not exists kenyapatches;
use kenyapatches;

# --- !Ups

-- groups table
CREATE TABLE `group` (
  `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `description` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- user table
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ip_address` varchar(15) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` text NOT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `permission` blob DEFAULT NULL,
  `activation_code` text DEFAULT NULL,
  `activation_time` datetime DEFAULT NULL,
  `forgotten_password_code` text DEFAULT NULL,
  `forgotten_password_time` datetime DEFAULT NULL,
  `remember_code` varchar(40) DEFAULT NULL,
  `created_on` datetime NOT NULL,
  `last_login` datetime  DEFAULT NULL,
  `active` tinyint(1) unsigned DEFAULT 0,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- - Table structure for table user_group

CREATE TABLE `user_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `group_id` mediumint(8) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_group_user1_idx` (`user_id`),
  KEY `fk_user_group_group1_idx` (`group_id`),
  CONSTRAINT `uc_user_group` UNIQUE (`user_id`, `group_id`),
  CONSTRAINT `fk_user_group_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_group1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- - Table structure for table login_attempt

CREATE TABLE `login_attempt` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ip_address` varchar(15) NOT NULL,
  `login` varchar(100) NOT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- - Table structure for table rating

CREATE TABLE `rating` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `classification` varchar(100) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- - Table structure for table story_category

CREATE TABLE `story_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


-- - Create for table story

CREATE TABLE `story` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `text` text NOT NULL,
  `keywords` text DEFAULT NULL,
  `status` ENUM('UNPUBLISHED', 'PUBLISHED', 'DELETED','BANNED') NOT NULL,
  `story_category_id` int(11) unsigned NOT NULL,
  `rating_id` int(11) unsigned NOT NULL,
  `created_by` int(11) unsigned NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_story` (`title`),
  KEY `fk_story_category_id` (`story_category_id`),
  KEY `rating_id` (`rating_id`),
  CONSTRAINT `fk_story_category_id` FOREIGN KEY (`story_category_id`) REFERENCES `story_category` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
CONSTRAINT `fk_created_by` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`) 
ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `rating_id` FOREIGN KEY (`rating_id`) REFERENCES `rating` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Create for table story_view_count
#

CREATE TABLE `story_view_count` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `story_id` int(11)  unsigned NOT NULL,
  `view_count` bigint  NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_story_id` (`story_id`),
  CONSTRAINT `fk_story_id` FOREIGN KEY (`story_id`) REFERENCES `story` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- - Create for table story_comment
CREATE TABLE `story_comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `parent_id` int  NULL,
  `visible` tinyint(1) unsigned DEFAULT 0,
  `story_id` int(11)  unsigned NOT NULL,
  `created_by` int(11) unsigned NOT NULL,
`created_on` timestamp  NOT NULL DEFAULT current_timestamp,
`modified_on` timestamp   NULL,
  PRIMARY KEY (`id`),

  CONSTRAINT `fk_story_comment_created_by` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_story_comment_story_id` FOREIGN KEY (`story_id`) REFERENCES `story` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`parent_id`) REFERENCES `story_comment`(`id`) ON UPDATE CASCADE ON DELETE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- - Table structure for table news_category

CREATE TABLE `news_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- - Create for table news

CREATE TABLE `news` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `text` text NOT NULL,
  `keywords` text NOT NULL,
  `status` ENUM('UNPUBLISHED', 'PUBLISHED', 'DELETED','BANNED') NOT NULL,
  `news_category_id` int(11) unsigned NOT NULL,
  `rating_id` int(11) unsigned NOT NULL,
  `created_by` int(11) unsigned NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_news` (`title`),
  KEY `fk_news_category_id` (`news_category_id`),
  KEY `rating_id` (`rating_id`),
  CONSTRAINT `fk_news_category_id` FOREIGN KEY (`news_category_id`) REFERENCES `news_category` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
CONSTRAINT `fk_news_created_by` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`) 
ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `news_rating_id` FOREIGN KEY (`rating_id`) REFERENCES `rating` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create for table news_view_count
CREATE TABLE `news_view_count` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `news_id` int(11)  unsigned NOT NULL,
  `view_count` bigint  NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_news_id` (`news_id`),
  CONSTRAINT `fk_news_id` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- - Create for table comment
CREATE TABLE `news_comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `parent_id` int  NULL,
  `visible` tinyint(1) unsigned DEFAULT 0,
  `news_id` int(11)  unsigned NOT NULL,
  `created_by` int(11) unsigned NOT NULL,
`created_on` timestamp  NOT NULL DEFAULT current_timestamp,
`modified_on` timestamp   NULL,
  PRIMARY KEY (`id`),

  CONSTRAINT `fk_news_comment_created_by` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_news_comment_news_id` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`parent_id`) REFERENCES `news_comment`(`id`) ON UPDATE CASCADE ON DELETE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8;


# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

-- user tables
drop table if exists `group`;

drop table if exists `user`;

drop table if exists `user_group`;

drop table if exists `login_attempt`;

drop table if exists `rating`;


--  story tables

drop table if exists `story_category`;

drop table if exists `story`;

drop table if exists `story_view_count`;

drop table if exists `story_comment`;


-- news tables
drop table if exists `news_category`;

drop table if exists `news`;

drop table if exists `news_view_count`;

drop table if exists `news_comment`;


SET FOREIGN_KEY_CHECKS=1;

