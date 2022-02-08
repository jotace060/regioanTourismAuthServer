CREATE TABLE `license_company_option` (
  `license_company_id` int(10) unsigned DEFAULT NULL COMMENT 'Asocia el número de licencia de una empresa en particular.',
  `product_option_id` int(10) unsigned DEFAULT NULL COMMENT 'Asocia las opciones del producto, para poder configurar la licencia, desde product_option.',
  `active` tinyint(1) DEFAULT NULL COMMENT 'Indica si la relación se encuentra activa.',
  `option_value` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Valor dependiente de la licencia contratada',
  KEY `fk_lco_lop_id_idx` (`license_company_id`),
  KEY `fk_pop_lop_id_idx` (`product_option_id`),
  CONSTRAINT `fk_lco_lop_id` FOREIGN KEY (`license_company_id`) REFERENCES `license_company` (`license_company_id`),
  CONSTRAINT `fk_pop_lop_id` FOREIGN KEY (`product_option_id`) REFERENCES `product_option` (`product_option_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Permite configurar la licencia de una empresa, a través de las opciones disponibles.'