--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `word_connections`
--

CREATE TABLE `word_connections` (
`connection_id` bigint(20) NOT NULL,
  `word1_id` bigint(20) NOT NULL,
  `word1_language` int(11) NOT NULL,
  `word2_id` bigint(20) NOT NULL,
  `word2_language` int(11) NOT NULL,
  `connection_type` smallint(6) NOT NULL,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `contributor_id` char(4) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `word_connections`
--
ALTER TABLE `word_connections`
 ADD PRIMARY KEY (`connection_id`), ADD UNIQUE KEY `unique_connection` (`word1_id`,`word2_id`,`connection_type`,`contributor_id`), ADD KEY `word1_id` (`word1_id`), ADD KEY `word1_language` (`word1_language`), ADD KEY `word2_id` (`word2_id`), ADD KEY `word2_language` (`word2_language`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `word_connections`
--
ALTER TABLE `word_connections`
MODIFY `connection_id` bigint(20) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `word_connections`
--
ALTER TABLE `word_connections`
ADD CONSTRAINT `word1_fk` FOREIGN KEY (`word1_id`) REFERENCES `words` (`word_id`),
ADD CONSTRAINT `word2_fk` FOREIGN KEY (`word2_id`) REFERENCES `words` (`word_id`),
ADD CONSTRAINT `word1_lang_fk` FOREIGN KEY (`word1_language`) REFERENCES `languages` (`language_id`),
ADD CONSTRAINT `word2_lang_fk` FOREIGN KEY (`word2_language`) REFERENCES `languages` (`language_id`);
