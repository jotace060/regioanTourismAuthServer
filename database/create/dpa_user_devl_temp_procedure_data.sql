CREATE TABLE `temp_procedure_data` (
  `procedure_name` varchar(20) COLLATE utf8_spanish_ci NOT NULL,
  `start_time` datetime(6) NOT NULL COMMENT 'hora de inicio',
  `stop_time` datetime(6) NOT NULL COMMENT 'hora de termino',
  `elapsed_time` varchar(45) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='tabla temporal para tener información de tiempo de funcionamiento de los procedimientos almacenados.'