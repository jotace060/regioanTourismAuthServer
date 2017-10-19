package com.dparadig.auth_server.alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JLabarca
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseType {

    private int licenseTypeId;
    private int productId;
    private String licenseName;
    private int duration;
    private String durationUnit;
    private int price;
    private String priceCurrency;
    private int status;
    /*
    SELECT * FROM dpa_user_dvel.license_type;

CREATE TABLE `license_type` (
  `license_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) unsigned DEFAULT NULL COMMENT 'Id del producto al cual hace referencia la licencia.',
  `license_name` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'tipo de licencia (freemium, estándar, etc).',
  `duration` int(10) unsigned DEFAULT NULL COMMENT 'Duración de la licencia. El número determina la cantidad, pero durationUnit determina a qué corresponde (días, mes, año).',
  `duration_unit` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Unidad de la duración (días, meses, semestres, años).',
  `price` int(11) DEFAULT NULL COMMENT 'Precio de la licencia.',
  `price_currency` varchar(45) COLLATE utf8_spanish_ci DEFAULT NULL COMMENT 'Moneda en la cual está expresado el precio.',
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`license_type_id`),
  KEY `fk_pro_lty_id_idx` (`product_id`),
  CONSTRAINT `fk_pro_lty_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Listado de todos los tipos de licencia disponibles';

     */
}
