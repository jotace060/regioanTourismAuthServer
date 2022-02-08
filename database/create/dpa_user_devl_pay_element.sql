CREATE TABLE `pay_element` (
  `pay_element_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Columna auto-incremental, utilizada para identificar de manera única cada registro.',
  `customer_company_id` int(10) unsigned DEFAULT NULL COMMENT 'Referencia al _id de la empresa, desde la tabla customer_company.',
  `pay_type_id` int(10) unsigned DEFAULT NULL COMMENT 'Tipo de pago. Referencia al id de la tabla pay_type. EJ: paypal, webpay, TC, etc',
  `name` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Nombre del medio de pago, asignado directamente por el usuario, para diferenciar sus distintos medios de pago.',
  `pay_information` varchar(100) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Datos para conexión API paypal, webpay. Se debe investigar si con esta información basta.',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del medio de pago.',
  `modification_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de modificación/actualización del medio de pago.',
  `expiration_date` datetime DEFAULT NULL COMMENT 'Fecha de expiración del método de pago.',
  `status` tinyint(1) DEFAULT NULL COMMENT 'Permite eliminar lógicamente un registro, sin perder la información.',
  PRIMARY KEY (`pay_element_id`),
  KEY `fk_cco_pel_id_idx` (`customer_company_id`),
  KEY `fk_pty_pel_id_idx` (`pay_type_id`),
  CONSTRAINT `fk_cco_pel_id` FOREIGN KEY (`customer_company_id`) REFERENCES `customer_company` (`customer_company_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pty_pel_id` FOREIGN KEY (`pay_type_id`) REFERENCES `pay_type` (`pay_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Almacenamiento de los métodos de pago de los clientes.'