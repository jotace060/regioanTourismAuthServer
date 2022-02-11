CREATE TABLE `customer_user_role` (
  `customer_user_id` int(10) unsigned DEFAULT NULL COMMENT 'Numero para asociar al usuario. Llave foránea de la tabla customer_user.',
  `role_id` int(10) unsigned DEFAULT NULL COMMENT 'Permite asociar un rol. Vinculado a la tabla role.',
  `license_company_id` int(10) unsigned DEFAULT NULL COMMENT 'Asociada a una licencia de la empresa. Viculada a la tabla license_company.',
  KEY `fk_cus_cur_id_idx` (`customer_user_id`),
  KEY `fk_rol_cur_id_idx` (`role_id`),
  KEY `fk_lco_cur_id_idx` (`license_company_id`),
  CONSTRAINT `fk_cus_cur_id` FOREIGN KEY (`customer_user_id`) REFERENCES `customer_user` (`customer_user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_lco_cur_id` FOREIGN KEY (`license_company_id`) REFERENCES `license_company` (`license_company_id`),
  CONSTRAINT `fk_rol_cur_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Asocia el usuario, con un rol y la licencia de la empresa.'