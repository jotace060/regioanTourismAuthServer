/* Se creo la tabla user_session*/
CREATE TABLE `user_sessions` (
  `session_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `customer_user_id` int(10) unsigned DEFAULT NULL,
  `session_attempt_datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`session_id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=l

INSERT INTO dpa_user_dvel.user_sessions(customer_user_id, session_attempt_datetime)
VALUES(200, CURRENT_TIMESTAMP);

/* Se agrego la columna validate_access a la tabla customer_user*/
ALTER TABLE dpa_user_dvel.customer_user ADD validate_access tinyint(1);
UPDATE dpa_user_dvel.customer_user SET validate_access=0 WHERE customer_user_id = 184