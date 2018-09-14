-- MySQL dump 10.13  Distrib 5.7.23, for Linux (x86_64)
--
-- Host: localhost    Database: anywork
-- ------------------------------------------------------
-- Server version	5.7.23-0ubuntu0.18.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `relation`
--

DROP TABLE IF EXISTS `relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relation` (
  `relation_id` int(11) NOT NULL AUTO_INCREMENT,
  `organization_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `role` int(11) DEFAULT NULL,
  PRIMARY KEY (`relation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=261 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relation`
--

LOCK TABLES `relation` WRITE;
/*!40000 ALTER TABLE `relation` DISABLE KEYS */;
INSERT INTO `relation` VALUES (26,1,5,NULL),(31,13,42,NULL),(32,13,43,NULL),(33,13,44,NULL),(34,13,45,NULL),(35,13,46,NULL),(36,13,47,NULL),(37,13,48,NULL),(38,13,49,NULL),(39,13,50,NULL),(40,13,51,NULL),(41,13,52,NULL),(42,13,53,NULL),(43,13,55,NULL),(44,13,56,NULL),(45,13,54,NULL),(46,13,57,NULL),(47,13,58,NULL),(48,13,59,NULL),(49,13,60,NULL),(50,13,61,NULL),(51,13,62,NULL),(52,13,63,NULL),(53,13,64,NULL),(54,13,66,NULL),(55,13,65,NULL),(56,13,67,NULL),(57,13,68,NULL),(58,13,69,NULL),(59,13,70,NULL),(60,13,71,NULL),(61,13,72,NULL),(62,13,73,NULL),(63,13,74,NULL),(64,13,75,NULL),(65,13,76,NULL),(66,13,77,NULL),(67,13,78,NULL),(68,13,79,NULL),(69,13,80,NULL),(70,13,81,NULL),(71,14,82,NULL),(72,13,83,NULL),(73,13,84,NULL),(74,13,85,NULL),(75,13,86,NULL),(76,14,87,NULL),(77,14,94,NULL),(78,14,95,NULL),(79,14,93,NULL),(80,14,92,NULL),(81,14,91,NULL),(83,14,88,NULL),(86,14,98,NULL),(87,14,153,NULL),(88,14,154,NULL),(89,14,155,NULL),(90,14,156,NULL),(91,14,157,NULL),(93,14,158,NULL),(94,14,159,NULL),(138,14,172,NULL),(139,14,90,NULL),(140,14,185,NULL),(141,14,96,NULL),(142,14,173,NULL),(143,14,186,NULL),(149,13,189,NULL),(151,14,189,NULL),(152,17,190,NULL),(153,14,183,NULL),(154,14,193,NULL),(155,14,194,NULL),(156,14,97,NULL),(157,14,196,NULL),(158,13,206,NULL),(159,14,89,NULL),(160,14,207,NULL),(161,14,99,NULL),(162,14,208,NULL),(163,14,209,NULL),(164,14,210,NULL),(165,14,211,NULL),(166,14,212,NULL),(167,14,214,NULL),(168,15,205,NULL),(169,15,215,NULL),(170,15,217,NULL),(171,16,198,NULL),(172,16,219,NULL),(173,16,218,NULL),(174,16,223,NULL),(175,16,220,NULL),(176,16,222,NULL),(177,15,203,NULL),(178,15,224,NULL),(179,15,225,NULL),(180,16,226,NULL),(181,15,227,NULL),(182,15,213,NULL),(183,15,199,NULL),(184,15,202,NULL),(185,15,228,NULL),(186,16,229,NULL),(187,15,230,NULL),(188,16,231,NULL),(189,16,232,NULL),(190,16,233,NULL),(191,15,195,NULL),(192,15,234,NULL),(193,16,221,NULL),(194,15,235,NULL),(195,15,236,NULL),(196,16,237,NULL),(197,16,238,NULL),(198,15,216,NULL),(199,15,239,NULL),(200,15,240,NULL),(201,15,241,NULL),(202,16,242,NULL),(203,15,243,NULL),(204,15,244,NULL),(205,16,246,NULL),(206,16,245,NULL),(207,16,247,NULL),(208,16,248,NULL),(209,16,249,NULL),(210,16,251,NULL),(211,16,252,NULL),(212,15,253,NULL),(213,15,254,NULL),(214,16,250,NULL),(215,15,255,NULL),(216,15,256,NULL),(217,15,257,NULL),(218,16,258,NULL),(219,15,204,NULL),(220,14,174,NULL),(221,14,260,NULL),(222,14,259,NULL),(223,15,261,NULL),(224,15,262,NULL),(225,16,263,NULL),(226,16,264,NULL),(227,16,265,NULL),(228,16,266,NULL),(229,16,267,NULL),(230,16,268,NULL),(231,16,269,NULL),(232,16,270,NULL),(233,16,271,NULL),(234,16,272,NULL),(235,14,274,NULL),(236,15,276,NULL),(237,16,277,NULL),(238,16,278,NULL),(239,15,197,NULL),(240,15,200,NULL),(241,16,279,NULL),(242,15,282,NULL),(243,15,283,NULL),(244,15,284,NULL),(245,18,285,NULL),(246,19,285,NULL),(247,16,286,NULL),(248,19,287,NULL),(249,19,288,NULL),(250,14,289,NULL),(251,13,289,NULL),(252,20,289,NULL),(253,21,289,NULL),(254,13,290,NULL),(255,13,1996,NULL),(256,13,1988,NULL),(257,14,1988,NULL),(258,15,1988,NULL),(260,15,1970,NULL);
/*!40000 ALTER TABLE `relation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-20 15:17:12