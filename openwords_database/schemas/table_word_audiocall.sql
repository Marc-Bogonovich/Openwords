SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `openwords_words`
--

-- --------------------------------------------------------

--
-- Table structure for table `word_audiocall`
--

CREATE TABLE `word_audiocall` (
  `word_id` bigint(20) NOT NULL,
  `type` int(2) NOT NULL,
  `language_id` int(11) NOT NULL,
  `file_name` varchar(200) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `word_audiocall`
--
ALTER TABLE `word_audiocall`
 ADD PRIMARY KEY (`word_id`,`type`);
