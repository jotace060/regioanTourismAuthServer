CREATE TABLE `language_list` (
  `language_list_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `long_name` char(49) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Nombre largo del idioma.',
  `short_name` char(2) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Nombre corto del idioma, según estándar ISO-639-1',
  `valid` tinyint(1) DEFAULT '0' COMMENT 'Permite eliminar lógicamente una idioma, sin perder la información de la bd.',
  PRIMARY KEY (`language_list_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Lista de idiomas.'