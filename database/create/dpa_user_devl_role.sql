CREATE TABLE `role` (
  `role_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `customer_company_id` int(10) unsigned DEFAULT NULL COMMENT 'Esto permite que el rol se ajuste al tipo de licencia.',
  `name` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Nombre del rol.',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del rol.',
  `modification_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de modificación del rol.',
  `status` tinyint(1) DEFAULT '1',
  `product_id` bigint(20) NOT NULL,
  `super_admin` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_UN` (`customer_company_id`,`name`,`product_id`),
  KEY `fk_lty_rol_id_idx` (`customer_company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Roles para privilegios en el sistema general.'