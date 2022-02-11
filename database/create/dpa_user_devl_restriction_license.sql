CREATE TABLE `restriction_license` (
  `restriction_id` bigint(20) NOT NULL,
  `license_company_id` int(10) unsigned NOT NULL,
  `restriction_value` varchar(50) NOT NULL,
  `restriction_period` varchar(20) NOT NULL,
  PRIMARY KEY (`restriction_id`,`license_company_id`),
  KEY `restriction_license_FK` (`license_company_id`),
  CONSTRAINT `restriction_license_FK` FOREIGN KEY (`license_company_id`) REFERENCES `license_company` (`license_company_id`),
  CONSTRAINT `restriction_license_FK_1` FOREIGN KEY (`restriction_id`) REFERENCES `restriction` (`restriction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8