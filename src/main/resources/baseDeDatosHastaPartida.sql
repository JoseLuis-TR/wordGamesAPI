-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: wordgamesdb
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `equipo`
--

DROP TABLE IF EXISTS `equipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `puntos` int NOT NULL,
  `logo` varchar(45) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipo`
--

LOCK TABLES `equipo` WRITE;
/*!40000 ALTER TABLE `equipo` DISABLE KEYS */;
INSERT INTO `equipo` VALUES (1,'Equipo 33',20,''),(2,'Equipo 99',0,''),(3,'Equipo 120',0,''),(4,'Equipo 100',0,'');
/*!40000 ALTER TABLE `equipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `juego`
--

DROP TABLE IF EXISTS `juego`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `juego` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dificultad` enum('FACIL','NORMAL','DIFICIL') COLLATE utf8mb4_spanish_ci NOT NULL,
  `nombre` varchar(75) COLLATE utf8mb4_spanish_ci NOT NULL,
  `instrucciones` varchar(500) COLLATE utf8mb4_spanish_ci NOT NULL,
  `intentosmax` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `juego`
--

LOCK TABLES `juego` WRITE;
/*!40000 ALTER TABLE `juego` DISABLE KEYS */;
INSERT INTO `juego` VALUES (1,'FACIL','Ahorcado','Lorem Ipsum',7),(3,'NORMAL','Wordle','Lorem Ipsum',6);
/*!40000 ALTER TABLE `juego` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jugador`
--

DROP TABLE IF EXISTS `jugador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jugador` (
  `id` int NOT NULL AUTO_INCREMENT,
  `admin` tinyint NOT NULL,
  `nombreusu` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `clave` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `puntos` int DEFAULT '0',
  `id_equipo` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_jugador_Equipo_idx` (`id_equipo`),
  CONSTRAINT `fk_jugador_Equipo` FOREIGN KEY (`id_equipo`) REFERENCES `equipo` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jugador`
--

LOCK TABLES `jugador` WRITE;
/*!40000 ALTER TABLE `jugador` DISABLE KEYS */;
INSERT INTO `jugador` VALUES (1,0,'Nano 1','Puerta.123','',20,1),(6,1,'Nano','Pestillo.123','',NULL,NULL);
/*!40000 ALTER TABLE `jugador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partida`
--

DROP TABLE IF EXISTS `partida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partida` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_juego` int NOT NULL,
  `id_jugador` int NOT NULL,
  `intentos` int NOT NULL,
  `puntos` int NOT NULL,
  `palabra` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `Datetime` date NOT NULL,
  PRIMARY KEY (`id`,`id_juego`,`id_jugador`),
  KEY `fk_juego_has_jugador_jugador1_idx` (`id_jugador`),
  KEY `fk_juego_has_jugador_juego1_idx` (`id_juego`),
  CONSTRAINT `fk_juego_has_jugador_juego1` FOREIGN KEY (`id_juego`) REFERENCES `juego` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_juego_has_jugador_jugador1` FOREIGN KEY (`id_jugador`) REFERENCES `jugador` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partida`
--

LOCK TABLES `partida` WRITE;
/*!40000 ALTER TABLE `partida` DISABLE KEYS */;
INSERT INTO `partida` VALUES (1,3,1,2,12,'Pisto','2023-03-09'),(2,1,1,2,8,'Aburrido','2023-03-09');
/*!40000 ALTER TABLE `partida` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-03-09  0:45:54
