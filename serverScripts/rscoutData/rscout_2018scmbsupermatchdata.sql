-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: rscout
-- ------------------------------------------------------
-- Server version	5.7.21-log

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
-- Table structure for table `2018scmbsupermatchdata`
--

DROP TABLE IF EXISTS `2018scmbsupermatchdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `2018scmbsupermatchdata` (
  `matchNumber` int(11) DEFAULT NULL,
  `scoutName` varchar(255) DEFAULT NULL,
  `forceRed` int(11) DEFAULT NULL,
  `levitateRed` int(11) DEFAULT NULL,
  `boostRed` int(11) DEFAULT NULL,
  `forceBlue` int(11) DEFAULT NULL,
  `levitateBlue` int(11) DEFAULT NULL,
  `boostBlue` int(11) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `2018scmbsupermatchdata`
--

LOCK TABLES `2018scmbsupermatchdata` WRITE;
/*!40000 ALTER TABLE `2018scmbsupermatchdata` DISABLE KEYS */;
INSERT INTO `2018scmbsupermatchdata` VALUES (3,'Abigail B',0,2,0,0,3,2,'blue stockpiled until end of match and almost was unable to play levitate because because of time'),(4,'Abigail B',3,0,0,3,3,3,'blue filled whole vault quickly and played them all as soon as possible, red stockpiled'),(5,'Abigail B',0,0,0,0,0,0,'no cubes delivered mostly just a scale battle the entire time'),(6,'Evan Boswell',0,0,0,1,0,0,'Neither team were very coordinated and they had one cube in the vault between them'),(7,'Evan Boswell',0,0,0,0,3,1,'Blue team was very accurate with their cubes and kept the scale and switch locked down and red team did a bad or two'),(8,'Evan Boswell',0,2,0,0,3,0,'both teams focused solely on the scale but red alliance kept getting in each others way Blue was very strong'),(10,'Evan Boswell',0,2,0,0,0,3,'the teams were not very coordinated and had difficulty with cubes on the scale and switch and getting cubes in the vault');
/*!40000 ALTER TABLE `2018scmbsupermatchdata` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-02 11:16:51
