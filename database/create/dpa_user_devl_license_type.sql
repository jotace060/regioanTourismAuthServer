CREATE TABLE `license_type` (
  `license_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) unsigned DEFAULT NULL COMMENT 'Id del producto al cual hace referencia la licencia.',
  `license_name` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'tipo de licencia (freemium, est�ndar, etc).',
  `duration` int(10) unsigned DEFAULT NULL COMMENT 'Duraci�n de la licencia. El n�mero determina la cantidad, pero durationUnit determina a qu� corresponde (d�as, mes, a�o).',
  `duration_unit` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Unidad de la duraci�n (d�as, meses, semestres, a�os).',
  `price` decimal(19,4) DEFAULT NULL COMMENT 'Precio de la licencia.',
  `price_currency` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Moneda en la cual est� expresado el precio.',
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`license_type_id`),
  KEY `fk_pro_lty_id_idx` (`product_id`),
  CONSTRAINT `fk_pro_lty_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Listado de todos los tipos de licencia disponibles'