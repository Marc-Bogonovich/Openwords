-- phpMyAdmin SQL Dump
-- version 2.8.0.1
-- http://www.phpmyadmin.net
--
-- Host: custsql-ipg03.eigbox.net
-- Generation Time: May 19, 2014 at 02:40 PM
-- Server version: 5.5.32
-- PHP Version: 4.4.9
--
-- Database: `owr_1_0`
--

-- --------------------------------------------------------

--
-- Table structure for table `connection_tags`
--

CREATE TABLE `connection_tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tie_id` int(11) DEFAULT NULL,
  `tag` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tie_id` (`tie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `connections`
--

CREATE TABLE `connections` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word1_id` int(11) DEFAULT NULL,
  `word2_id` int(11) DEFAULT NULL,
  `connection_type` mediumtext,
  PRIMARY KEY (`id`),
  KEY `word1_id` (`word1_id`),
  KEY `word2_id` (`word2_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='This is the core field of the database, and represents the L';

-- --------------------------------------------------------

--
-- Table structure for table `languages`
--

CREATE TABLE `languages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `language` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Words will be tagged into a table.';

-- --------------------------------------------------------

--
-- Table structure for table `learnable_language_options`
--

CREATE TABLE `learnable_language_options` (
  `l1_id` int(20) NOT NULL,
  `option_l2_id` int(20) NOT NULL,
  `option_l2_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`l1_id`,`option_l2_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='l2 language options for user depending on available translations';

-- --------------------------------------------------------

--
-- Table structure for table `rank_types`
--

CREATE TABLE `rank_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rank_type_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Table of Rank types';

-- --------------------------------------------------------

--
-- Table structure for table `word_audiocall`
--

CREATE TABLE `word_audiocall` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word_id` int(11) NOT NULL,
  `url` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `word_id` (`word_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `word_meaning`
--

CREATE TABLE `word_meaning` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `meaning_text` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `word_id` (`word_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `word_rank`
--

CREATE TABLE `word_rank` (
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

CREATE TABLE `word_tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word_id` int(11) DEFAULT NULL,
  `tag` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `word_id` (`word_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `word_transcription`
--

CREATE TABLE `word_transcription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word_id` int(11) DEFAULT NULL,
  `transcription` varchar(200) DEFAULT NULL,
  `transcription_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='This table will contain various kinds of transcriptions. The';

-- --------------------------------------------------------

--
-- Table structure for table `words`
--

CREATE TABLE `words` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `language_id` int(11) DEFAULT NULL,
  `word` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `language_id` (`language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='This table is also to be known as the Tower of Babel. It con';
