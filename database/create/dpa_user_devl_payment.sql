CREATE TABLE `payment` (
  `payment_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Columna auto-incremental, utilizada para identificar de manera única cada registro.',
  `license_company_id` int(10) unsigned DEFAULT NULL COMMENT 'Referencia al _id del license company a la cual se le esta realizando un pago',
  `customer_user_id` int(10) unsigned DEFAULT NULL COMMENT 'Referencia al _id del customer_user que realiza el pago.',
  `pay_type_id` int(10) unsigned DEFAULT NULL COMMENT 'Tipo de pago. Referencia al id de la tabla pay_type. EJ: paypal, webpay, TC, etc',
  `amount` decimal(10,2) DEFAULT NULL COMMENT 'Monto en dinero del pago',
  `response` json DEFAULT NULL COMMENT 'JSON respuesta de la api del medio de pago',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del pago.',
  `modification_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de modificación/actualización del pago.',
  `result` varchar(8) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Resultado del pago:  Created|Approved|Failed|Canceled|Expired.',
  `status` tinyint(1) DEFAULT NULL COMMENT 'Permite eliminar lógicamente un registro, sin perder la información.',
  PRIMARY KEY (`payment_id`),
  KEY `fk_lco_pay_id_idx` (`license_company_id`),
  KEY `fk_cus_pay_id_idx` (`customer_user_id`),
  KEY `fk_pty_pay_id_idx` (`pay_type_id`),
  CONSTRAINT `fk_cus_pay_id` FOREIGN KEY (`customer_user_id`) REFERENCES `customer_user` (`customer_user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_lco_pay_id` FOREIGN KEY (`license_company_id`) REFERENCES `license_company` (`license_company_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pty_pay_id` FOREIGN KEY (`pay_type_id`) REFERENCES `pay_type` (`pay_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Almacenamiento de los pagos de los clientes.'