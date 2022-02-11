CREATE TABLE `license_limitation` (
  `license_limitation_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `license_type_id` int(10) unsigned DEFAULT NULL COMMENT 'Tipo de licencia, vinculado a la tabla license_type.',
  `name` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Nombre de la limitaci�n.',
  `threshold_type` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Tipo de l�mite (usuarios, conectores, etc).',
  `threshold` int(10) unsigned DEFAULT NULL COMMENT 'Cantidad de asociada al l�mite descrito en treshold_type.',
  `status` tinyint(1) DEFAULT NULL COMMENT 'Permite eliminar l�gicamente una limitaci�n, sin perder la informaci�n de la bd.',
  PRIMARY KEY (`license_limitation_id`),
  KEY `fk_lty_lli,id_idx` (`license_type_id`),
  CONSTRAINT `fk_lty_lli_id` FOREIGN KEY (`license_type_id`) REFERENCES `license_type` (`license_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Las limitaciones del tipo de licencia (freemium, est�ndar, etc)., respecto del producto (SGI, OTRS, etc).'