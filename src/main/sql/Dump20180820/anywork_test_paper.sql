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
-- Table structure for table `test_paper`
--

DROP TABLE IF EXISTS `test_paper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_paper` (
  `testpaper_id` int(11) NOT NULL AUTO_INCREMENT,
  `testpaper_title` varchar(255) DEFAULT NULL,
  `author_id` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `ending_time` timestamp NULL DEFAULT NULL,
  `chapter_id` int(11) DEFAULT NULL,
  `testpaper_score` int(11) DEFAULT NULL,
  `testpaper_type` int(11) DEFAULT NULL,
  `organization_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`testpaper_id`)
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_paper`
--

LOCK TABLES `test_paper` WRITE;
/*!40000 ALTER TABLE `test_paper` DISABLE KEYS */;
INSERT INTO `test_paper` VALUES (33,'1.1 什么是计算机程序',190,'2017-10-15 16:00:00','2018-09-03 16:00:00',11,12,0,17),(34,'1.2什么是计算机语言',190,'2017-10-15 16:00:00','2018-09-03 16:00:00',11,100,0,17),(35,'1.3C语言的发展及其特点 ',190,'2017-10-15 16:00:00','2018-09-03 16:00:00',11,100,0,17),(36,'1.4最简单的C语言程序',190,'2017-10-15 16:00:00','2018-09-03 16:00:00',11,100,0,17),(37,'1.5运行C程序的步骤与方法',190,'2017-10-15 16:00:00','2018-09-03 16:00:00',11,100,0,17),(38,'1.6程序设计的任务 ',190,'2017-10-15 16:00:00','2018-09-03 16:00:00',11,100,0,17),(39,'1.1什么是计算机程序',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',12,100,0,13),(40,'1.2什么是计算机语言',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',12,100,0,13),(41,'1.3C语言的发展及其特点 ',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',12,100,0,13),(42,'1.4最简单的C语言程序',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',12,100,0,13),(43,'1.5运行C程序的步骤与方法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',12,100,0,13),(44,'1.6程序设计的任务 ',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',12,100,0,13),(46,'2.1什么是算法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',13,100,0,13),(47,'2.2简单的算法举例',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',13,100,0,13),(48,'2.3 算法的特性',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',13,100,0,13),(49,'2.4怎样表示一个算法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',13,100,0,13),(50,'2.5 结构化程序设计方法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',13,100,0,13),(51,'3.1 顺序程序设计举例',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',14,50,0,13),(52,'3.2数据的表现形式及其运算',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',14,153,0,13),(53,'3.3 C语句',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',14,85,0,13),(54,'3.4数据的输入输出',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',14,152,0,13),(55,'4.1选择结构和条件判断',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',15,100,0,13),(56,'4.2用if语句实现选择结构',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',15,95,0,13),(57,'4.3关系运算符和关系表达式',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',15,100,0,13),(58,'4.4逻辑运算符和逻辑表达式',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',15,100,0,13),(59,'4.5条件运算符和条件表达式',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',15,100,0,13),(60,'4.6选择结构的嵌套',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',15,100,0,13),(61,'4.7用switch语句实现多分支选择结构',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',15,100,0,13),(62,'4.8选择结构程序综合举例',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',15,100,0,13),(63,'1.1什么是计算机程序',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',16,100,0,14),(64,'1.2什么是计算机语言',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',16,100,0,14),(65,'1.3C语言的发展及其特点 ',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',16,100,0,14),(66,'1.4最简单的C语言程序',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',16,100,0,14),(67,'1.5运行C程序的步骤与方法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',16,100,0,14),(68,'1.6程序设计的任务 ',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',16,100,0,14),(69,'2.1什么是算法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',17,100,0,14),(70,'2.2简单的算法举例',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',17,100,0,14),(71,'2.3 算法的特性',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',17,100,0,14),(72,'2.4怎样表示一个算法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',17,100,0,14),(73,'2.5 结构化程序设计方法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',17,100,0,14),(74,'3.1 顺序程序设计举例',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',18,6,0,14),(75,'3.2数据的表现形式及其运算',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',18,153,0,14),(76,'3.3 C语句',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',18,85,0,14),(77,'3.4数据的输入输出',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',18,152,0,14),(78,'4.1选择结构和条件判断',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',19,100,0,14),(79,'4.2用if语句实现选择结构',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',19,95,0,14),(80,'4.3关系运算符和关系表达式',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',19,100,0,14),(81,'4.4逻辑运算符和逻辑表达式',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',19,100,0,14),(82,'4.5条件运算符和条件表达式',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',19,100,0,14),(83,'4.6选择结构的嵌套',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',19,100,0,14),(84,'4.7用switch语句实现多分支选择结构',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',19,100,0,14),(85,'4.8选择结构程序综合举例',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',19,100,0,14),(86,'1.1什么是计算机程序',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',20,100,0,15),(87,'1.2什么是计算机语言',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',20,100,0,15),(88,'1.3C语言的发展及其特点 ',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',20,100,0,15),(89,'1.4最简单的C语言程序',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',20,100,0,15),(90,'1.5运行C程序的步骤与方法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',20,100,0,15),(91,'1.6程序设计的任务 ',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',20,100,0,15),(92,'2.1什么是算法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',21,100,0,15),(93,'2.2简单的算法举例',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',21,100,0,15),(94,'2.3 算法的特性',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',21,100,0,15),(95,'2.4怎样表示一个算法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',21,100,0,15),(96,'2.5 结构化程序设计方法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',21,100,0,15),(97,'3.1 顺序程序设计举例',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',22,60,0,15),(98,'3.2数据的表现形式及其运算',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',22,153,0,15),(99,'3.3 C语句',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',22,85,0,15),(100,'3.4数据的输入输出',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',22,152,0,15),(101,'4.1选择结构和条件判断',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',23,100,0,15),(102,'4.2用if语句实现选择结构',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',23,95,0,15),(103,'4.3关系运算符和关系表达式',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',23,100,0,15),(104,'4.4逻辑运算符和逻辑表达式',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',23,100,0,15),(105,'4.5条件运算符和条件表达式',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',23,100,0,15),(106,'4.6选择结构的嵌套',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',23,100,0,15),(107,'4.7用switch语句实现多分支选择结构',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',23,100,0,15),(108,'4.8选择结构程序综合举例',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',23,100,0,15),(109,'1.1什么是计算机程序',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',24,100,0,16),(110,'1.2什么是计算机语言',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',24,100,0,16),(111,'1.3C语言的发展及其特点 ',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',24,100,0,16),(112,'1.4最简单的C语言程序',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',24,100,0,16),(113,'1.5运行C程序的步骤与方法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',24,100,0,16),(114,'1.6程序设计的任务 ',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',24,100,0,16),(115,'2.1什么是算法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',25,100,0,16),(116,'2.2简单的算法举例',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',25,100,0,16),(117,'2.3 算法的特性',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',25,100,0,16),(118,'2.4怎样表示一个算法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',25,100,0,16),(119,'2.5 结构化程序设计方法',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',25,100,0,16),(120,'3.1 顺序程序设计举例',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',26,6,0,16),(121,'3.2数据的表现形式及其运算',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',26,153,0,16),(122,'3.3 C语句',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',26,85,0,16),(123,'3.4数据的输入输出',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',26,152,0,16),(124,'4.1选择结构和条件判断',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',27,100,0,16),(125,'4.2用if语句实现选择结构',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',27,95,0,16),(126,'4.3关系运算符和关系表达式',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',27,100,0,16),(127,'4.4逻辑运算符和逻辑表达式',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',27,100,0,16),(128,'4.5条件运算符和条件表达式',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',27,100,0,16),(129,'4.6选择结构的嵌套',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',27,100,0,16),(130,'4.7用switch语句实现多分支选择结构',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',27,100,0,16),(131,'4.8选择结构程序综合举例',41,'2017-10-31 16:00:00','2018-09-03 16:00:00',27,100,0,16),(132,'5.1为什么需要循环控制',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',28,90,0,13),(133,'5.2用while语句实现循环',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',28,100,0,13),(134,'5.3 用do……while语句实现循环',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',28,90,0,13),(135,'5.4用for 语句实现循环',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',28,100,0,13),(136,'5.5循环的嵌套',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',28,100,0,13),(137,'5.6 几种循环的比较',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',28,100,0,13),(138,'5.7改变循环执行的状态',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',28,100,0,13),(139,'5.8循环程序举例',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',28,100,0,13),(140,'5.1为什么需要循环控制',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',29,90,0,14),(141,'5.2用while语句实现循环',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',29,100,0,14),(142,'5.3 用do……while语句实现循环',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',29,90,0,14),(143,'5.4用for 语句实现循环',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',29,100,0,14),(144,'5.5循环的嵌套',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',29,100,0,14),(145,'5.6 几种循环的比较',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',29,100,0,14),(146,'5.7改变循环执行的状态',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',29,100,0,14),(147,'5.8循环程序举例',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',29,100,0,14),(148,'5.1为什么需要循环控制',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',30,90,0,15),(149,'5.2用while语句实现循环',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',30,100,0,15),(150,'5.3 用do……while语句实现循环',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',30,90,0,15),(151,'5.4用for 语句实现循环',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',30,100,0,15),(152,'5.5循环的嵌套',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',30,100,0,15),(153,'5.6 几种循环的比较',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',30,100,0,15),(154,'5.7改变循环执行的状态',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',30,100,0,15),(155,'5.8循环程序举例',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',30,100,0,15),(156,'5.1为什么需要循环控制',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',31,90,0,16),(157,'5.2用while语句实现循环',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',31,100,0,16),(158,'5.3 用do……while语句实现循环',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',31,90,0,16),(159,'5.4用for 语句实现循环',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',31,100,0,16),(160,'5.5循环的嵌套',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',31,100,0,16),(161,'5.6 几种循环的比较',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',31,100,0,16),(162,'5.7改变循环执行的状态',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',31,100,0,16),(163,'5.8循环程序举例',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',31,100,0,16),(164,'6.1 怎样定义和引用一维数组',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',32,106,0,13),(165,'6.2怎样定义和引用二维数组',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',32,100,0,13),(166,'6.3 字符数组',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',32,100,0,13),(167,'6.1 怎样定义和引用一维数组',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',33,106,0,14),(168,'6.2怎样定义和引用二维数组',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',33,100,0,14),(169,'6.3 字符数组',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',33,100,0,14),(170,'6.1 怎样定义和引用一维数组',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',34,106,0,15),(171,'6.2怎样定义和引用二维数组',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',34,100,0,15),(172,'6.3 字符数组',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',34,100,0,15),(173,'6.1 怎样定义和引用一维数组',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',35,106,0,16),(174,'6.2怎样定义和引用二维数组',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',35,100,0,16),(175,'6.3 字符数组',41,'2017-11-30 16:00:00','2018-09-03 16:00:00',35,100,0,16),(176,'一份试卷',285,'2018-01-07 16:00:00','2018-09-03 16:00:00',0,60,1,19),(177,'一个联系',285,'2018-01-07 16:00:00','2018-09-03 16:00:00',36,35,0,19),(178,'aaa',285,'2018-01-08 16:00:00','2018-09-03 16:00:00',36,5,0,19),(179,'bbb',285,'2018-01-08 16:00:00','2018-09-03 16:00:00',37,5,0,19),(180,'c',285,'2018-01-08 16:00:00','2018-09-03 16:00:00',0,5,1,19);
/*!40000 ALTER TABLE `test_paper` ENABLE KEYS */;
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