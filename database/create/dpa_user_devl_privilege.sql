CREATE TABLE `privilege` (
  `privilege_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Nombre del privilegio',
  `description` varchar(200) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Dscripción del privilegio.',
  `privilege_type` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Tipo  de privilegio (lectura, escritura, solo lectura, etc). Se ha cambiado el antiguo nombre (type), pues era una palabra reservada del sistema',
  `category` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Categoría del privilegio (portal web, procedimiento, sección).',
  `status` tinyint(1) DEFAULT NULL COMMENT 'Permite eliminar de manera lógica un privilegio, sin eliminar el registro.',
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`privilege_id`),
  UNIQUE KEY `privilege_UN` (`name`,`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Lista de privilegios que pueden adquerir los diversos roles.'