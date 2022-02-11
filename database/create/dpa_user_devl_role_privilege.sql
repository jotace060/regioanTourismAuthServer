CREATE TABLE `role_privilege` (
  `role_id` int(10) unsigned NOT NULL COMMENT 'Se asocia a la tabla role.',
  `privilege_id` int(10) unsigned NOT NULL COMMENT 'Se asocia a la tabla privilege.',
  `active` tinyint(1) DEFAULT NULL COMMENT 'Indica si el rol tiene activo el privilegio.',
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creacción de la asociación entre rol y privilegio.',
  `modification_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de modificación de la asociación entre rol y privilegio.',
  `status` tinyint(1) DEFAULT NULL,
  UNIQUE KEY `idx_rol_pri` (`role_id`,`privilege_id`) USING BTREE,
  KEY `fk_rol_rpr_id_idx` (`role_id`),
  KEY `fk_pri_rpr_id_idx` (`privilege_id`),
  CONSTRAINT `fk_pri_rpr_id` FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`privilege_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_rol_rpr_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Asocia los distintos roles, con los distintos privilegios disponibles. Por ejemplo podrían haber 3 administradores, con distintos tipos de privilegios.'