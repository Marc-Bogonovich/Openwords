--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `set_content`
--

CREATE TABLE `set_content` (
  `set_id` bigint(20) NOT NULL,
  `word_connection_id` bigint(20) NOT NULL,
  `direction` int(11) NOT NULL,
  `item_order` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `set_content`
--
ALTER TABLE `set_content`
 ADD PRIMARY KEY (`set_id`,`word_connection_id`);
