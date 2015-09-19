--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `sentence_connections`
--

CREATE TABLE `sentence_connections` (
  `uni_sentence_id` bigint(20) NOT NULL,
  `sentence_id` bigint(20) NOT NULL,
  `connection_type` varchar(10) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `sentence_connections`
--
ALTER TABLE `sentence_connections`
 ADD PRIMARY KEY (`uni_sentence_id`,`sentence_id`);
