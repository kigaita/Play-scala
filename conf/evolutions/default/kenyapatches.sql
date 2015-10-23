#create database kenyapatches;
use kenyapatches;

DROP TABLE IF EXISTS `group`;

#
# Table structure for table 'group'
#

CREATE TABLE `group` (
  `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `description` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table 'group'
#

INSERT INTO `group` (`id`, `name`, `description`) VALUES
    (1,'sysadmin','System Administrator'), 
    (2,'admin','Administrator'),
     (3,'member','General User');



DROP TABLE IF EXISTS `user`;

#
# Table structure for table 'user'
#

CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ip_address` varchar(15) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` text NOT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
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



DROP TABLE IF EXISTS `user_group`;

#
# Table structure for table 'user_group'
#

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


DROP TABLE IF EXISTS `login_attempt`;

#
# Table structure for table 'login_attempt'
#

CREATE TABLE `login_attempt` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ip_address` varchar(15) NOT NULL,
  `login` varchar(100) NOT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Table structure for table 'rating'
#

CREATE TABLE `rating` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `classification` varchar(100) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Insert into table 'rating'
#

INSERT INTO `rating`  VALUES
    (null,'G','Story contains no strong language, no violence, no sexual references and no references to nudity.'), 
    (null,'PG','May be read by anyone, but readers should be aware that there may be occasional mild swear-words, oblique reference to sex or some kissing scenes, and brief and in passing reference to nudity.'),
    (null,'PG-13','Not recommended for anyone under the age of 13. These stories may contain some stronger language, violence, references to lovemaking and some more descriptive -- though not explicit -- sexual scenes.'),
    (null,'R','Parental consent required for reading by anyone under the age of 18; stories may contain descriptions of sexual intercourse but will probably not be fully explicit.'),
    (null,'NC-17','May not be read by anyone under the age of 18. Stories may contain full and explicit descriptions of sexual intercourse, explicit descriptions of nudity and/or strong language.');

#
# Table structure for table 'story_category'
#

CREATE TABLE `story_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
#
# Insert for table 'story_category'
#

INSERT INTO  `story_category`  VALUES
(null,'Adult Stories',''),
(null,'Children stories',''),
(null,'General Literary',''),
(null,'Poetry',''),
(null,'Song Lyrics',''),
(null,'Travel',''),
(null,'True Stories','');

#
# Create for table 'story'
#

CREATE TABLE `story` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `text` text NOT NULL,
  `keywords` text NOT NULL,
  `status` tinyint(1) NOT NULL,
  `banned` tinyint(1) NOT NULL,
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
# Create for table 'story_view_count'
#

CREATE TABLE `story_view_count` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `story_id` int(11)  unsigned NOT NULL,
  `view_count` bigint  NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_story_id` (`story_id`),
  CONSTRAINT `fk_story_id` FOREIGN KEY (`story_id`) REFERENCES `story` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Create for table 'comment'
#
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `parent_id` int  NULL,
  `story_id` int(11)  unsigned NOT NULL,
  `created_by` int(11) unsigned NOT NULL,
`created_on` timestamp  NOT NULL DEFAULT current_timestamp,
`modified_on` timestamp   NULL,
  PRIMARY KEY (`id`),

  CONSTRAINT `fk_comment_created_by` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_comment_story_id` FOREIGN KEY (`story_id`) REFERENCES `story` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`parent_id`) REFERENCES `comment`(`id`) ON UPDATE CASCADE ON DELETE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#
# Table structure for table 'news_category'
#

CREATE TABLE `news_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
#
# Insert for table 'news_category'
#

INSERT INTO  `news_category`  VALUES
(null,'World News',''),
(null,'Africa News',''),
(null,'Regional news',''),
(null,'Local News','');

#
# Create for table 'news'
#

CREATE TABLE `news` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `text` text NOT NULL,
  `keywords` text NOT NULL,
  `status` tinyint(1) NOT NULL,
  `banned` tinyint(1) NOT NULL,
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

#
# Create for table 'news_view_count'
#

CREATE TABLE `news_view_count` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `news_id` int(11)  unsigned NOT NULL,
  `view_count` bigint  NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_news_id` (`news_id`),
  CONSTRAINT `fk_news_id` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Create for table 'comment'
#
CREATE TABLE `news_comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `parent_id` int  NULL,
  `news_id` int(11)  unsigned NOT NULL,
  `created_by` int(11) unsigned NOT NULL,
`created_on` timestamp  NOT NULL DEFAULT current_timestamp,
`modified_on` timestamp   NULL,
  PRIMARY KEY (`id`),

  CONSTRAINT `fk_news_comment_created_by` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_news_comment_news_id` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`parent_id`) REFERENCES `news_comment`(`id`) ON UPDATE CASCADE ON DELETE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

