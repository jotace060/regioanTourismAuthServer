CREATE TABLE `product` (
  `product_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Nombre del producto',
  `version` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Versión de producto. Pueden haber varios productos con el mismo nombre, pero en diferentes versiones.',
  `status` tinyint(1) DEFAULT NULL COMMENT 'Permite eliminar lóginamente un producto, sin eliminar el registro de la bd.',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Listado de los productos disponibles.'