CREATE TABLE `trial_history` (
  `customer_company_id` int(10) unsigned DEFAULT NULL COMMENT 'Asocia el número de una empresa en particular.',
  `license_type_id` int(10) unsigned DEFAULT NULL COMMENT 'Tipo de licencia, vinculado a la tabla license_type.',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del registro.',
  `status` tinyint(1) DEFAULT NULL COMMENT 'Permite eliminar lógicamente una limitación, sin perder la información de la bd.',
  KEY `fk_cco_thi_id_idx` (`customer_company_id`),
  KEY `fk_lty_thi_id_idx` (`license_type_id`),
  CONSTRAINT `fk_cco_lop_id` FOREIGN KEY (`customer_company_id`) REFERENCES `customer_company` (`customer_company_id`),
  CONSTRAINT `fk_lty_lop_id` FOREIGN KEY (`license_type_id`) REFERENCES `license_type` (`license_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Permite configurar la licencia de una empresa, a través de las opciones disponibles.'