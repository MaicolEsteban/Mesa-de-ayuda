-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: mesa_digital
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` tinyint(1) DEFAULT '1',
  `cedula` varchar(15) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `departamento` varchar(100) DEFAULT NULL,
  `email` varchar(150) NOT NULL,
  `facultad` varchar(100) DEFAULT NULL,
  `fecha_creacion` datetime DEFAULT CURRENT_TIMESTAMP,
  `fecha_verificacion` datetime DEFAULT NULL,
  `nombre` varchar(150) NOT NULL,
  `observaciones` text,
  `rol` varchar(50) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `tipo_usuario` varchar(20) NOT NULL,
  `ultima_modificacion` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ultimo_acceso` datetime DEFAULT NULL,
  `verificado` tinyint(1) DEFAULT '0',
  `primer_login` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_efovjjo5q5jlsa0f9eoptdjly` (`cedula`),
  UNIQUE KEY `UK_kfsp0s1tflm1cwlj8idhqsad0` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (3,1,'1087654321','$2a$10$yc7we3B8PTStBfLsiliYN.a.AFT0G8.e3/wxlSOxuQ5nzp51bGIDC',NULL,'juan@unipanamericana.edu.co','Ingenieria','2025-11-12 21:40:34',NULL,'Juan Pérez García',NULL,'ROL_ADMIN',NULL,'PERSONAL','2025-11-13 21:18:37','2025-11-19 14:51:58',0,_binary '\0'),(9,1,'123445566','$2a$10$b5r31fAM/He2r1aKiPj2S.Hwvio/TlF5QUCnh8I9dtDXA8iDW2z2i',NULL,'casosresponsable@ucompensar@gmail',NULL,'2025-11-16 13:50:06',NULL,'casos',NULL,'REVISOR',NULL,'PERSONAL',NULL,'2025-11-17 15:20:05',1,_binary '\0'),(16,1,'789654321','$2a$10$h12fkbY/Rd6t.R9S/u82cui6qaWOw9Bp6fLNyEwnr.UzXqJAhG8U2',NULL,'mesaucompesar@unipanamericana.edu.co',NULL,'2025-11-19 08:38:57',NULL,'ucompensar',NULL,'ROL_REVISOR',NULL,'REVISOR','2025-11-19 08:46:41','2025-11-19 14:52:37',1,_binary '\0'),(17,1,'1019876578','$2a$10$T6XiojLrbqn31hyUVVcIr.xqYYCH2GAranMJZb4fR/P4f6Kqtyz/a',NULL,'crodriguez@unipanamericana.edu.co','Administración de Empresas','2025-11-19 08:40:27',NULL,'Carlos rodriguez flores',NULL,'ESTUDIANTE',NULL,'ESTUDIANTE','2025-11-19 08:41:12','2025-11-19 10:54:37',0,_binary '\0'),(18,1,'1111223444','$2a$10$hnOM91gMankgD2SZAExQ7u5QlB/8HTt.Djk7mnFhLSj9d0wJIR/Q.',NULL,'carlos@unipanamericana.edu.co','Psicología','2025-11-19 14:49:57',NULL,'carlos gomez',NULL,'ESTUDIANTE',NULL,'ESTUDIANTE','2025-11-19 14:50:40','2025-11-19 14:50:40',0,_binary '\0');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-20 21:08:06
