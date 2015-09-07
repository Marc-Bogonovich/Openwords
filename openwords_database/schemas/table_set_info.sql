SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `set_info`
--

CREATE TABLE `set_info` (
`set_id` bigint(20) NOT NULL,
  `set_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `lang_one` int(11) NOT NULL,
  `lang_two` int(11) NOT NULL,
  `visibility` int(11) NOT NULL,
  `valid` tinyint(1) NOT NULL,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `set_info`
--
ALTER TABLE `set_info`
 ADD PRIMARY KEY (`set_id`), ADD UNIQUE KEY `set_name` (`set_name`,`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `set_info`
--
ALTER TABLE `set_info`
MODIFY `set_id` bigint(20) NOT NULL AUTO_INCREMENT;
