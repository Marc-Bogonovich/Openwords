--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `user_languages`
--

CREATE TABLE `user_languages` (
  `user_id` bigint(20) NOT NULL,
  `base_language` int(11) NOT NULL,
  `learning_language` int(11) NOT NULL,
  `page` int(11) NOT NULL,
  `meta_info` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `under_use` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user_languages`
--
ALTER TABLE `user_languages`
 ADD PRIMARY KEY (`user_id`,`base_language`,`learning_language`);
