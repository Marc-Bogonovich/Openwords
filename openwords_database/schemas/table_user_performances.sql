--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `user_performances`
--

CREATE TABLE `user_performances` (
  `user_id` bigint(20) NOT NULL,
  `word_connection_id` bigint(20) NOT NULL,
  `learning_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `performance` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `version` int(11) NOT NULL,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user_performances`
--
ALTER TABLE `user_performances`
 ADD PRIMARY KEY (`user_id`,`word_connection_id`,`learning_type`);
