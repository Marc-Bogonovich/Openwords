--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `words`
--

CREATE TABLE `words` (
`word_id` bigint(20) NOT NULL,
  `language_id` int(11) NOT NULL,
  `word` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `meta_info` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `contributor_id` char(4) COLLATE utf8_unicode_ci NOT NULL,
  `translation_md5` binary(16) NOT NULL COMMENT 'the sig for global translation or clarification'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `words`
--
ALTER TABLE `words`
 ADD PRIMARY KEY (`word_id`), ADD UNIQUE KEY `unique_translation` (`language_id`,`word`,`translation_md5`), ADD KEY `word` (`word`), ADD KEY `language_id` (`language_id`), ADD KEY `translation_md5` (`translation_md5`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `words`
--
ALTER TABLE `words`
MODIFY `word_id` bigint(20) NOT NULL AUTO_INCREMENT;
