CREATE TABLE `log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_type` varchar(20) COLLATE utf8_spanish_ci DEFAULT NULL,
  `message` text COLLATE utf8_spanish_ci NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `license` text COLLATE utf8_spanish_ci,
  `source_name` varchar(100) COLLATE utf8_spanish_ci DEFAULT NULL,
  `source_ip` varchar(50) COLLATE utf8_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`log_id`),
  KEY `log_log_id_IDX` (`log_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2702 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci