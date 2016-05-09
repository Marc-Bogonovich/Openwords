CREATE TABLE `courses` (
  `course_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `course_name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `language_one` int(11) NOT NULL,
  `language_two` int(11) NOT NULL,
  `updated_time` bigint NOT NULL,
  `content` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `courses`
 ADD PRIMARY KEY (`course_id`);

ALTER TABLE `courses`
MODIFY `course_id` bigint(20) NOT NULL AUTO_INCREMENT;
