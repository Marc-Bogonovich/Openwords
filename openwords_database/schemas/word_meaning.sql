CREATE TABLE IF NOT EXISTS `word_meaning` (
  `auto_id` int(11) NOT NULL AUTO_INCREMENT,
  `word_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `meaning_text` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  PRIMARY KEY (`auto_id`)
);
