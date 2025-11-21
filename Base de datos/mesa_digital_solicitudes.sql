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
-- Table structure for table `solicitudes`
--

DROP TABLE IF EXISTS `solicitudes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitudes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activa` tinyint(1) DEFAULT '1',
  `cedula_estudiante` varchar(20) NOT NULL,
  `codigo` varchar(20) NOT NULL,
  `comentario_satisfaccion` text,
  `dentro_de_plazo` bit(1) DEFAULT NULL,
  `descripcion` text NOT NULL,
  `email_estudiante` varchar(100) NOT NULL,
  `estado` varchar(30) NOT NULL,
  `fecha_primera_respuesta` datetime DEFAULT NULL,
  `fecha_radicacion` datetime DEFAULT CURRENT_TIMESTAMP,
  `fecha_resolucion` datetime DEFAULT NULL,
  `nombre_estudiante` varchar(150) NOT NULL,
  `observaciones` text,
  `prioridad` int DEFAULT '3',
  `responsable_actual` varchar(100) DEFAULT NULL,
  `satisfaccion` varchar(5) DEFAULT NULL,
  `tiempo_resolucion_horas` int DEFAULT NULL,
  `tiempo_respuesta_horas` int DEFAULT NULL,
  `tipo_solicitud` varchar(50) NOT NULL,
  `calificacion_satisfaccion` int DEFAULT NULL,
  `dias_transcurridos` int DEFAULT NULL,
  `email_responsable` varchar(255) DEFAULT NULL,
  `fecha_asignacion` datetime DEFAULT NULL,
  `fecha_limite_solucion` datetime(6) DEFAULT NULL,
  `horas_restantes` int DEFAULT NULL,
  `vencida` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_gxiujk9xjkr2ml4pk0pcvdihf` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `solicitudes`
--

LOCK TABLES `solicitudes` WRITE;
/*!40000 ALTER TABLE `solicitudes` DISABLE KEYS */;
INSERT INTO `solicitudes` VALUES (11,1,'1019876578','SOL-20251119-1817',NULL,NULL,'Solicitud de certficado  academico semestre actual ','crodriguez@unipanamericana.edu.co','EN_REVISION',NULL,'2025-11-19 08:45:25',NULL,'Carlos rodriguez flores',NULL,3,NULL,NULL,NULL,NULL,'CERTIFICADO_ACADEMICO',NULL,NULL,'mesaucompesar@unipanamericana.edu.co',NULL,NULL,NULL,NULL),(12,1,'1019876578','SOL-20251119-5858',NULL,NULL,'quiero homologacion','crodriguez@unipanamericana.edu.co','RADICADA',NULL,'2025-11-19 09:38:10',NULL,'Carlos rodriguez flores',NULL,3,NULL,NULL,NULL,NULL,'HOMOLOGACION',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(13,1,'1019876578','SOL-20251119-3833',NULL,NULL,'quiero cambiar','crodriguez@unipanamericana.edu.co','RADICADA',NULL,'2025-11-19 11:02:27',NULL,'Carlos rodriguez flores',NULL,3,NULL,NULL,NULL,NULL,'CAMBIO_PROGRAMA',NULL,NULL,NULL,NULL,'2025-11-22 11:02:27.418704',72,_binary '\0'),(14,1,'1111223444','SOL-20251119-7842',NULL,NULL,'requiero homolagion de materia calculo','carlos@unipanamericana.edu.co','RADICADA',NULL,'2025-11-19 14:51:27',NULL,'carlos gomez',NULL,3,NULL,NULL,NULL,NULL,'HOMOLOGACION',NULL,NULL,'mesaucompesar@unipanamericana.edu.co',NULL,'2025-11-22 14:51:26.675280',71,_binary '\0');
/*!40000 ALTER TABLE `solicitudes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-20 21:08:05
