CREATE TABLE `token` (
  `customer_user_id` int(10) unsigned NOT NULL COMMENT 'Referencia al usuario.',
  `token` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT 'El token mismo.',
  `expr_date` datetime DEFAULT NULL COMMENT 'Fecha de expiraci�n del token\n',
  `token_type` int(11) DEFAULT NULL COMMENT 'Tipo de token. 1 = Validaci�n de cuenta, 2 = cambio de password.',
  KEY `fk_cus_tok_id_idx` (`customer_user_id`),
  CONSTRAINT `fk_cus_tok_id` FOREIGN KEY (`customer_user_id`) REFERENCES `customer_user` (`customer_user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Almacena los token de validaci�n de cuenta y cambio de contrase�a. Debe tener una mantenci�n regular, en la cual se eliminen los registros expirados.'