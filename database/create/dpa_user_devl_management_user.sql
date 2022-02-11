CREATE TABLE `management_user` (
  `management_user_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Llave autoincremental',
  `email` varchar(250) CHARACTER SET latin1 NOT NULL COMMENT 'Identificador �nico del usuario. Un mismo e-mail no puede tener mas de un usuario.',
  `name` varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Nombre del usuario.',
  `country_id` int(10) unsigned DEFAULT '1' COMMENT 'Llave for�nea con el id de pa�s.',
  `password` varchar(60) CHARACTER SET latin1 DEFAULT NULL COMMENT 'Password actual',
  `pass_expr_date` datetime DEFAULT NULL COMMENT 'Fecha de expiraci�n de contrase�a.',
  `validation_status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT 'Estado de validaci�n de la cuenta (correo).',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creaci�n del usuario.',
  `modification_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de �ltima modificaci�n del usuario.',
  `status` tinyint(1) unsigned DEFAULT '1' COMMENT 'Permite eliminar l�gicamente una cuenta, sin eliminar la informaci�n.',
  `validate_access` tinyint(4) DEFAULT '1' COMMENT 'Valor para bloquear el acceso a la cuenta despu�s de los 3 intentos fallidos',
  PRIMARY KEY (`management_user_id`),
  UNIQUE KEY `management_user_UN` (`email`),
  KEY `management_user_FK_1` (`country_id`),
  CONSTRAINT `management_user_FK_1` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=186 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci