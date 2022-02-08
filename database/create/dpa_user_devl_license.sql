CREATE TABLE `license` (
  `license_company_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`license_company_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1