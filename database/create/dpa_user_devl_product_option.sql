CREATE TABLE `product_option` (
  `product_option_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) unsigned DEFAULT NULL,
  `name` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Nombre de la opción, confiurable por el administrador.',
  `option_type` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'secciones disponibles, conectores activados, etc.',
  `description` varchar(200) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Descripción de la opción.',
  `option_default` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Almacena las opciones sin un valor definido',
  PRIMARY KEY (`product_option_id`),
  KEY `fk_pro_pop_id_idx` (`product_id`),
  CONSTRAINT `fk_pro_pop_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Opciones de productos, como que tipo de bd, o las secciones activas.'