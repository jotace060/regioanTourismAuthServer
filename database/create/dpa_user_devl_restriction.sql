CREATE TABLE `restriction` (
  `restriction_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `restriction_field` varchar(20) NOT NULL,
  `restriction_type` varchar(20) NOT NULL,
  PRIMARY KEY (`restriction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='Definici�n de restricci�n que se aplicar�n a las diferentes licencias'