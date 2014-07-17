-- phpMyAdmin SQL Dump
-- version 4.1.8
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 17, 2014 at 03:43 PM
-- Server version: 5.5.37-cll
-- PHP Version: 5.4.23

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `oworg_owr_1_0`
--
CREATE DATABASE IF NOT EXISTS `oworg_owr_1_0` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `oworg_owr_1_0`;

DELIMITER $$
--
-- Procedures
--
DROP PROCEDURE IF EXISTS `populate_learnable_lang_opt`$$
CREATE DEFINER=`oworg`@`localhost` PROCEDURE `populate_learnable_lang_opt`()
    NO SQL
    DETERMINISTIC
BEGIN
DECLARE no_more_rows BOOLEAN;
DECLARE num_rows INT DEFAULT 0;
DECLARE l1id INT;
DECLARE l1name INT;

DECLARE langlist CURSOR FOR
SELECT distinct id,language from languages;
	
DECLARE CONTINUE HANDLER FOR NOT FOUND
    SET no_more_rows = TRUE;
	
    DELETE FROM learnable_language_options;
    
	OPEN langlist;
	select FOUND_ROWS() into num_rows;

req_loop: LOOP	 
		FETCH  langlist
		INTO   l1id,l1name;

		-- break out of the loop if
		  -- 1) there were no records, or
		  -- 2) we've processed them all
		IF no_more_rows THEN
			CLOSE langlist;
			LEAVE req_loop;
		END IF;

		
		
		insert into  learnable_language_options
		select l1id, l.id, l.language 
		FROM languages l
		WHERE EXISTS (
		SELECT c.id
		FROM word_connections c, words w1, words w2
		WHERE w2.id = c.word1_id
		AND w1.id = c.word2_id
		AND w2.language_id =l1id
		AND w1.language_id = l.id
		);
			

END LOOP req_loop;	
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `connection_tags`
--

DROP TABLE IF EXISTS `connection_tags`;
CREATE TABLE IF NOT EXISTS `connection_tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tie_id` int(11) DEFAULT NULL,
  `tag` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tie_id` (`tie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `english_wiktionary_raw`
--

DROP TABLE IF EXISTS `english_wiktionary_raw`;
CREATE TABLE IF NOT EXISTS `english_wiktionary_raw` (
  `wordID` int(11) NOT NULL AUTO_INCREMENT,
  `enWord` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `grammerType` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `defID` int(11) DEFAULT NULL,
  `definition` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lang` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `word` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tr` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`wordID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=988411 ;

-- --------------------------------------------------------

--
-- Table structure for table `languages`
--

DROP TABLE IF EXISTS `languages`;
CREATE TABLE IF NOT EXISTS `languages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `language` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='Words will be tagged into a table.' AUTO_INCREMENT=7 ;

-- --------------------------------------------------------

--
-- Table structure for table `learnable_language_options`
--

DROP TABLE IF EXISTS `learnable_language_options`;
CREATE TABLE IF NOT EXISTS `learnable_language_options` (
  `l1_id` int(20) NOT NULL,
  `option_l2_id` int(20) NOT NULL,
  `option_l2_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`l1_id`,`option_l2_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='l2 language options for user depending on available translations';

-- --------------------------------------------------------

--
-- Table structure for table `rank_types`
--

DROP TABLE IF EXISTS `rank_types`;
CREATE TABLE IF NOT EXISTS `rank_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rank_type_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COMMENT='Table of Rank types' AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Table structure for table `words`
--

DROP TABLE IF EXISTS `words`;
CREATE TABLE IF NOT EXISTS `words` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `language_id` int(11) DEFAULT NULL,
  `word` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `language_id` (`language_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='This table is also to be known as the Tower of Babel. It con' AUTO_INCREMENT=14 ;

-- --------------------------------------------------------

--
-- Table structure for table `word_audiocall`
--

DROP TABLE IF EXISTS `word_audiocall`;
CREATE TABLE IF NOT EXISTS `word_audiocall` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word_id` int(11) NOT NULL,
  `url` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `word_id_2` (`word_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `word_connections`
--

DROP TABLE IF EXISTS `word_connections`;
CREATE TABLE IF NOT EXISTS `word_connections` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word1_id` int(11) DEFAULT NULL,
  `word2_id` int(11) DEFAULT NULL,
  `connection_type` mediumtext,
  PRIMARY KEY (`id`),
  KEY `word1_id` (`word1_id`),
  KEY `word2_id` (`word2_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='This is the core field of the database, and represents the L' AUTO_INCREMENT=9 ;

-- --------------------------------------------------------

--
-- Table structure for table `word_meaning`
--

DROP TABLE IF EXISTS `word_meaning`;
CREATE TABLE IF NOT EXISTS `word_meaning` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `meaning_text` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `word_id` (`word_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `word_rank`
--

DROP TABLE IF EXISTS `word_rank`;
CREATE TABLE IF NOT EXISTS `word_rank` (
  `id` int(20) NOT NULL,
  `word_id` int(20) NOT NULL,
  `rank` int(20) DEFAULT NULL COMMENT 'Ranks',
  `rank_type` int(20) DEFAULT NULL COMMENT 'Ran type id from rank type table',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Word Rankings';

-- --------------------------------------------------------

--
-- Table structure for table `word_tags`
--

DROP TABLE IF EXISTS `word_tags`;
CREATE TABLE IF NOT EXISTS `word_tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word_id` int(11) DEFAULT NULL,
  `tag` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `word_id` (`word_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `word_transcription`
--

DROP TABLE IF EXISTS `word_transcription`;
CREATE TABLE IF NOT EXISTS `word_transcription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word_id` int(11) DEFAULT NULL,
  `transcription` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `transcription_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `word_id` (`word_id`),
  KEY `transcription_type` (`transcription_type`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='This table will contain various kinds of transcriptions. The' AUTO_INCREMENT=9 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `connection_tags`
--
ALTER TABLE `connection_tags`
  ADD CONSTRAINT `connection_tags_ibfk_1` FOREIGN KEY (`tie_id`) REFERENCES `word_connections` (`id`);

--
-- Constraints for table `words`
--
ALTER TABLE `words`
  ADD CONSTRAINT `words_ibfk_1` FOREIGN KEY (`language_id`) REFERENCES `languages` (`id`);

--
-- Constraints for table `word_audiocall`
--
ALTER TABLE `word_audiocall`
  ADD CONSTRAINT `word_audiocall_ibfk_1` FOREIGN KEY (`word_id`) REFERENCES `words` (`id`);

--
-- Constraints for table `word_connections`
--
ALTER TABLE `word_connections`
  ADD CONSTRAINT `word_connections_ibfk_1` FOREIGN KEY (`word1_id`) REFERENCES `words` (`id`),
  ADD CONSTRAINT `word_connections_ibfk_2` FOREIGN KEY (`word2_id`) REFERENCES `words` (`id`);

--
-- Constraints for table `word_meaning`
--
ALTER TABLE `word_meaning`
  ADD CONSTRAINT `word_meaning_ibfk_1` FOREIGN KEY (`word_id`) REFERENCES `words` (`id`);

--
-- Constraints for table `word_tags`
--
ALTER TABLE `word_tags`
  ADD CONSTRAINT `word_tags_ibfk_1` FOREIGN KEY (`word_id`) REFERENCES `words` (`id`);

--
-- Constraints for table `word_transcription`
--
ALTER TABLE `word_transcription`
  ADD CONSTRAINT `word_transcription_ibfk_1` FOREIGN KEY (`id`) REFERENCES `words` (`id`);
SET FOREIGN_KEY_CHECKS=1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
