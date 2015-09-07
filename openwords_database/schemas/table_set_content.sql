--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `set_content`
--

CREATE TABLE `set_content` (
`auto_id` bigint(20) NOT NULL,
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
 ADD PRIMARY KEY (`auto_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `set_content`
--
ALTER TABLE `set_content`
MODIFY `auto_id` bigint(20) NOT NULL AUTO_INCREMENT;
