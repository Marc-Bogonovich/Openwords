--
-- Table structure for table `sentence_items`
--

CREATE TABLE `sentence_items` (
  `sentence_id` bigint(20) NOT NULL,
  `item_index` int(11) NOT NULL,
  `item` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `item_type` varchar(10) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `sentence_items`
--
ALTER TABLE `sentence_items`
 ADD PRIMARY KEY (`sentence_id`,`item_index`);
