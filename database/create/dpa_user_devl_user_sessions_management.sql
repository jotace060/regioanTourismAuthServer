CREATE TABLE `user_sessions_management` (
  `session_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `management_user_id` int(10) unsigned DEFAULT NULL,
  `session_attempt_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1