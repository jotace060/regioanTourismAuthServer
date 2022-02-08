CREATE TABLE `customer_user_suscription` (
  `customer_user_id` int(10) unsigned NOT NULL,
  `product_id` int(10) unsigned NOT NULL,
  `suscription_type` varchar(50) NOT NULL,
  PRIMARY KEY (`customer_user_id`,`product_id`),
  KEY `customer_user_suscription_FK_1` (`product_id`),
  CONSTRAINT `customer_user_suscription_FK` FOREIGN KEY (`customer_user_id`) REFERENCES `customer_user` (`customer_user_id`),
  CONSTRAINT `customer_user_suscription_FK_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8