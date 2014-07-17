-- phpMyAdmin SQL Dump
-- version 4.1.8
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 17, 2014 at 03:42 PM
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
-- Database: `oworg_openwords`
--
CREATE DATABASE IF NOT EXISTS `oworg_openwords` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `oworg_openwords`;

-- --------------------------------------------------------

--
-- Table structure for table `personal_db_language_pref`
--

DROP TABLE IF EXISTS `personal_db_language_pref`;
CREATE TABLE IF NOT EXISTS `personal_db_language_pref` (
  `user_id` int(20) NOT NULL,
  `l2_pref_id` int(20) NOT NULL DEFAULT '0',
  `l2_pref_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`,`l2_pref_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='personal language learning preference of user';

-- --------------------------------------------------------

--
-- Table structure for table `personal_db_user_details`
--

DROP TABLE IF EXISTS `personal_db_user_details`;
CREATE TABLE IF NOT EXISTS `personal_db_user_details` (
  `user_id` int(20) NOT NULL,
  `about_u1` varchar(100) DEFAULT NULL,
  `about_u2` varchar(100) DEFAULT NULL,
  `l1_pref_id` int(20) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `role_user`
--

DROP TABLE IF EXISTS `role_user`;
CREATE TABLE IF NOT EXISTS `role_user` (
  `user_id` int(20) NOT NULL,
  `role_id` int(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user_db`
--

DROP TABLE IF EXISTS `user_db`;
CREATE TABLE IF NOT EXISTS `user_db` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `email` varchar(20) NOT NULL,
  `password` char(32) NOT NULL,
  PRIMARY KEY (`email`),
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=317 ;

-- --------------------------------------------------------

--
-- Table structure for table `user_performance`
--

DROP TABLE IF EXISTS `user_performance`;
CREATE TABLE IF NOT EXISTS `user_performance` (
  `id` int(100) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) NOT NULL,
  `connection_id` int(20) NOT NULL,
  `type` int(11) DEFAULT NULL COMMENT '0-review,1-self,2-type-3-hearing',
  `performance` int(11) DEFAULT NULL COMMENT '0-skipped,1-wrong,2-close,3-correct',
  `time` int(100) DEFAULT NULL,
  `user_exclude` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9391 ;

-- --------------------------------------------------------

--
-- Table structure for table `user_words`
--

DROP TABLE IF EXISTS `user_words`;
CREATE TABLE IF NOT EXISTS `user_words` (
  `user_id` int(11) NOT NULL,
  `connection_id` int(11) NOT NULL,
  `wordl2_id` int(11) DEFAULT NULL,
  `wordl1_id` int(11) DEFAULT NULL,
  `l2_id` int(11) DEFAULT NULL,
  `download_time` int(11) DEFAULT NULL,
  `fresh` int(11) DEFAULT '1',
  PRIMARY KEY (`user_id`,`connection_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET FOREIGN_KEY_CHECKS=1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
