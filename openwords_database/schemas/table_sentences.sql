--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `sentences`
--

CREATE TABLE `sentences` (
`sentence_id` bigint(20) NOT NULL,
  `language_id` int(11) NOT NULL,
  `text` text COLLATE utf8_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `meta_info` text COLLATE utf8_unicode_ci NOT NULL,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `sentences`
--
ALTER TABLE `sentences`
 ADD PRIMARY KEY (`sentence_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `sentences`
--
ALTER TABLE `sentences`
MODIFY `sentence_id` bigint(20) NOT NULL AUTO_INCREMENT;
