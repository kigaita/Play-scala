#
# Dumping data for table 'group'
#

INSERT INTO `group` (`id`, `name`, `description`) VALUES
    (1,'sysadmin','System Administrator'), 
    (2,'admin','Administrator'),
     (3,'member','General User');

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
# Insert for table 'news_category'
#

INSERT INTO  `news_category`  VALUES
(null,'World News',''),
(null,'Africa News',''),
(null,'Regional news',''),
(null,'Local News','');
