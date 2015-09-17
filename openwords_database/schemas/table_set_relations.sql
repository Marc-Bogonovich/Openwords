--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `set_relations`
--

CREATE TABLE `set_relations` (
  `set_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `relation_type` int(11) NOT NULL,
  `relation_meta` mediumtext COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `set_relations`
--
ALTER TABLE `set_relations`
 ADD PRIMARY KEY (`set_id`,`user_id`);
