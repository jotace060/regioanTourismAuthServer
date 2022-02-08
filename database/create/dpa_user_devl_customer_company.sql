CREATE TABLE `customer_company` (
  `customer_company_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Numero identificador unico para cada empresa.',
  `company_name` varchar(100) COLLATE utf8_spanish_ci NOT NULL COMMENT 'Nombre de la empresa',
  `identification_number` varchar(30) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Numero de identificacion de la empresa. En el caso chileno, sería el RUT de la misma.',
  `area` varchar(100) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Area de la empresa que contrata o utiliza el servicio. Una empresa puede tener diversos registros, por cada area distinta para la cual haya adquerido un producto.',
  `country_id` int(10) unsigned DEFAULT '1' COMMENT 'Numero de País. El _id debe ser extraído desde la tabla country.',
  `company_parent_id` int(10) unsigned DEFAULT NULL COMMENT 'Id que apunta a hacia la misma tabla (customer_company). Permite relacionar varias areas como una misma empresa.',
  `validation` tinyint(1) DEFAULT '1' COMMENT '1 si una empresa está validada (sus datos han sido revisados y la empresa existe), sino es cero 0.',
  `status` tinyint(1) DEFAULT '1' COMMENT 'Permite eliminar lógicamente un registro, sin perder la información.',
  PRIMARY KEY (`customer_company_id`),
  KEY `fk_cco_cco_id_idx` (`company_parent_id`),
  KEY `fk_ctr_cco_id_idx` (`country_id`),
  CONSTRAINT `fk_cco_cco_id` FOREIGN KEY (`company_parent_id`) REFERENCES `customer_company` (`customer_company_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ctr_cco_id` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1131 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Almacena las empresas que han contratado productos.\nSi el cliente no registra empresa, se crea una ficticia con sus datos (por ejemplo para versiones trial o productos contratados por particulares).\nSi se confirman los datos de una empresa, la columna validation tiene valor 1, sino es 0.'