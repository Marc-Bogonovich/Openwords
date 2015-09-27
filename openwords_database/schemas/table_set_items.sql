--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `set_content`
--

CREATE TABLE `set_items` (
  `set_id` bigint(20) NOT NULL,
  `word_one_id` bigint(20) NOT NULL,
  `word_two_id` bigint(20) NOT NULL,
  `word_one` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `word_two` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `item_order` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `set_content`
--
ALTER TABLE `set_items`
 ADD PRIMARY KEY (`set_id`,`word_one_id`,`word_two_id`);
