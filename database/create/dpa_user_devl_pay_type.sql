CREATE TABLE `pay_type` (
  `pay_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'nombre del medio de pago: tarjeta de credito, paypal, webpay, etc.',
  `description` varchar(100) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'descripción del medio de pago: mastercard, visa signature, etc.',
  PRIMARY KEY (`pay_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Todos los medios de pago disponibles'