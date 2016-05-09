CREATE TABLE `courses` (
  `course_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `course_name` varchar(100) NOT NULL,
  `language_one` int NOT NULL,
  `language_two` int NOT NULL,
  `updated_time` bigint NOT NULL,
  `content` text NOT NULL
);

ALTER TABLE `courses`
 ADD PRIMARY KEY (`course_id`);
