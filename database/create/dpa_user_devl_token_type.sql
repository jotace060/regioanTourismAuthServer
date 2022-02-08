CREATE TABLE `token_type` (
  `token_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type_name` varchar(12) COLLATE utf8_spanish_ci NOT NULL COMMENT 'nombre del tipo de token',
  PRIMARY KEY (`token_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Tipos de token existentes'