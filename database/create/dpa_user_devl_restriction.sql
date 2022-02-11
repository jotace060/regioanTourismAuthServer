CREATE TABLE `restriction` (
  `restriction_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `restriction_field` varchar(20) NOT NULL,
  `restriction_type` varchar(20) NOT NULL,
  PRIMARY KEY (`restriction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='Definición de restricción que se aplicarán a las diferentes licencias'