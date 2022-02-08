CREATE TABLE `user_sessions` (
  `session_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `customer_user_id` int(10) unsigned DEFAULT NULL,
  `session_attempt_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`session_id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=l

INSERT INTO dpa_user_dvel.user_sessions(customer_user_id, session_attempt_datetime)
VALUES(20, CURRENT_TIMESTAMP);
