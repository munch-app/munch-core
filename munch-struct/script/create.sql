CREATE DATABASE  IF NOT EXISTS `munch` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `munch`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 139.59.240.51    Database: munch
-- ------------------------------------------------------
-- Server version	5.7.15

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
-- Table structure for table `AdminAccount`
--

DROP TABLE IF EXISTS `AdminAccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AdminAccount` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  `sort` bigint(20) NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `hashSaltPassword` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lastActiveDate` datetime NOT NULL,
  `lastLoginDate` datetime NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `phoneNumber` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `secretToken` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `SECRET_TOKEN_INDEX` (`secretToken`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AdminAccount`
--

LOCK TABLES `AdminAccount` WRITE;
/*!40000 ALTER TABLE `AdminAccount` DISABLE KEYS */;
/*!40000 ALTER TABLE `AdminAccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Brand`
--

DROP TABLE IF EXISTS `Brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Brand` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `phoneNumber` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `websiteUrl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Brand`
--

LOCK TABLES `Brand` WRITE;
/*!40000 ALTER TABLE `Brand` DISABLE KEYS */;
/*!40000 ALTER TABLE `Brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Container`
--

DROP TABLE IF EXISTS `Container`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Container` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `radius` double DEFAULT NULL,
  `type` int(11) NOT NULL,
  `websiteUrl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `neighborhood_id` char(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9b2ifgpe4e0ia2jb5biyda362` (`location_id`),
  KEY `FKlffus2y49kmaf65xk9yohsvrc` (`neighborhood_id`),
  CONSTRAINT `FKlffus2y49kmaf65xk9yohsvrc` FOREIGN KEY (`neighborhood_id`) REFERENCES `Neighborhood` (`id`),
  CONSTRAINT `FKpn8d3seu4thff5ck32h3oclos` FOREIGN KEY (`location_id`) REFERENCES `Location` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Container`
--

LOCK TABLES `Container` WRITE;
/*!40000 ALTER TABLE `Container` DISABLE KEYS */;
/*!40000 ALTER TABLE `Container` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Container_SortedImage`
--

DROP TABLE IF EXISTS `Container_SortedImage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Container_SortedImage` (
  `Container_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `images_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  UNIQUE KEY `UK_8wniha7a18h95i2rh261yyqjl` (`images_id`),
  KEY `FKi394eif8huqajsfnv03d2f5e3` (`Container_id`),
  CONSTRAINT `FKi394eif8huqajsfnv03d2f5e3` FOREIGN KEY (`Container_id`) REFERENCES `Container` (`id`),
  CONSTRAINT `FKp3cqdnuuhyn3sbulqn5g3jcdp` FOREIGN KEY (`images_id`) REFERENCES `SortedImage` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Container_SortedImage`
--

LOCK TABLES `Container_SortedImage` WRITE;
/*!40000 ALTER TABLE `Container_SortedImage` DISABLE KEYS */;
/*!40000 ALTER TABLE `Container_SortedImage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Country`
--

DROP TABLE IF EXISTS `Country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Country` (
  `id` bigint(20) NOT NULL,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  `sort` bigint(20) NOT NULL,
  `code` varchar(3) COLLATE utf8_unicode_ci NOT NULL,
  `iso` varchar(3) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(80) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_qyh4l70f9l5k5jcv876rb4j89` (`code`),
  UNIQUE KEY `UK_5u0fi84kbbwq8a7j3u4nfc0a6` (`iso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Country`
--

LOCK TABLES `Country` WRITE;
/*!40000 ALTER TABLE `Country` DISABLE KEYS */;
/*!40000 ALTER TABLE `Country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KeyValuePair`
--

DROP TABLE IF EXISTS `KeyValuePair`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `KeyValuePair` (
  `keyId` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  `value` longtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`keyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KeyValuePair`
--

LOCK TABLES `KeyValuePair` WRITE;
/*!40000 ALTER TABLE `KeyValuePair` DISABLE KEYS */;
/*!40000 ALTER TABLE `KeyValuePair` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Location`
--

DROP TABLE IF EXISTS `Location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Location` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `address` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `city` varchar(70) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `postal` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `state` varchar(70) COLLATE utf8_unicode_ci DEFAULT NULL,
  `street` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unitNumber` varchar(70) COLLATE utf8_unicode_ci DEFAULT NULL,
  `country_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf3jd4adefvfwt55g20tg85uih` (`country_id`),
  CONSTRAINT `FKf3jd4adefvfwt55g20tg85uih` FOREIGN KEY (`country_id`) REFERENCES `Country` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Location`
--

LOCK TABLES `Location` WRITE;
/*!40000 ALTER TABLE `Location` DISABLE KEYS */;
/*!40000 ALTER TABLE `Location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Neighborhood`
--

DROP TABLE IF EXISTS `Neighborhood`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Neighborhood` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  `sort` bigint(20) NOT NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `radius` double DEFAULT NULL,
  `websiteUrl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bh7kw8k3d3b1pgwds5jwuf1jw` (`location_id`),
  CONSTRAINT `FKqj5ft62o5s5219u90ubnxe51s` FOREIGN KEY (`location_id`) REFERENCES `Location` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Neighborhood`
--

LOCK TABLES `Neighborhood` WRITE;
/*!40000 ALTER TABLE `Neighborhood` DISABLE KEYS */;
/*!40000 ALTER TABLE `Neighborhood` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Neighborhood_SortedImage`
--

DROP TABLE IF EXISTS `Neighborhood_SortedImage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Neighborhood_SortedImage` (
  `Neighborhood_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `images_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  UNIQUE KEY `UK_dncb5h86e5tbvrxboatym7y5a` (`images_id`),
  KEY `FKp7e5kv2s6yw0w4py7tfo0gnrf` (`Neighborhood_id`),
  CONSTRAINT `FKligqvfccyjqgxu4uy148a4kcd` FOREIGN KEY (`images_id`) REFERENCES `SortedImage` (`id`),
  CONSTRAINT `FKp7e5kv2s6yw0w4py7tfo0gnrf` FOREIGN KEY (`Neighborhood_id`) REFERENCES `Neighborhood` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Neighborhood_SortedImage`
--

LOCK TABLES `Neighborhood_SortedImage` WRITE;
/*!40000 ALTER TABLE `Neighborhood_SortedImage` DISABLE KEYS */;
/*!40000 ALTER TABLE `Neighborhood_SortedImage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Place`
--

DROP TABLE IF EXISTS `Place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Place` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `description` longtext COLLATE utf8_unicode_ci,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `phoneNumber` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `priceEnd` double DEFAULT NULL,
  `priceStart` double DEFAULT NULL,
  `status` int(11) NOT NULL,
  `websiteUrl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `brand_id` char(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `container_id` char(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `neighbourhood_id` char(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `placeLink_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `placeLog_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `summary_id` char(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jx6vrilg8ilg3qrlrr3ihhlp0` (`location_id`),
  UNIQUE KEY `UK_74ss2myamyori8vtu07q83me0` (`placeLink_id`),
  UNIQUE KEY `UK_1lk1r0ac2unylqul88q7rt8hf` (`placeLog_id`),
  KEY `FKhvnqh9f2imywvwu3pi92yuv8w` (`brand_id`),
  KEY `FKadjte534hhl44k339gnk00pux` (`container_id`),
  KEY `FKsc2t4ycfokn6sw6ghcsw7xdmw` (`neighbourhood_id`),
  KEY `FK7wspblfgadyf7f1ote2atjyeg` (`summary_id`),
  CONSTRAINT `FK4ghq1jx30tnj1murfqocc35wx` FOREIGN KEY (`placeLog_id`) REFERENCES `PlaceLog` (`id`),
  CONSTRAINT `FK7wspblfgadyf7f1ote2atjyeg` FOREIGN KEY (`summary_id`) REFERENCES `ReviewSummary` (`id`),
  CONSTRAINT `FKadjte534hhl44k339gnk00pux` FOREIGN KEY (`container_id`) REFERENCES `Container` (`id`),
  CONSTRAINT `FKhvnqh9f2imywvwu3pi92yuv8w` FOREIGN KEY (`brand_id`) REFERENCES `Brand` (`id`),
  CONSTRAINT `FKl960unik04l12aql1cij8wum9` FOREIGN KEY (`placeLink_id`) REFERENCES `PlaceLink` (`id`),
  CONSTRAINT `FKmyim7a2hj8hyvdy6cg89evkk4` FOREIGN KEY (`location_id`) REFERENCES `Location` (`id`),
  CONSTRAINT `FKsc2t4ycfokn6sw6ghcsw7xdmw` FOREIGN KEY (`neighbourhood_id`) REFERENCES `Neighborhood` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Place`
--

LOCK TABLES `Place` WRITE;
/*!40000 ALTER TABLE `Place` DISABLE KEYS */;
/*!40000 ALTER TABLE `Place` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PlaceHour`
--

DROP TABLE IF EXISTS `PlaceHour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PlaceHour` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  `close` tinyblob NOT NULL,
  `day` int(11) NOT NULL,
  `open` tinyblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PlaceHour`
--

LOCK TABLES `PlaceHour` WRITE;
/*!40000 ALTER TABLE `PlaceHour` DISABLE KEYS */;
/*!40000 ALTER TABLE `PlaceHour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PlaceLink`
--

DROP TABLE IF EXISTS `PlaceLink`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PlaceLink` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `facebookPageId` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `facebookPlaceId` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `factualId` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `googlePlaceId` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `instagramPlaceId` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `instagramUserId` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PlaceLink`
--

LOCK TABLES `PlaceLink` WRITE;
/*!40000 ALTER TABLE `PlaceLink` DISABLE KEYS */;
/*!40000 ALTER TABLE `PlaceLink` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PlaceLog`
--

DROP TABLE IF EXISTS `PlaceLog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PlaceLog` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  `addedBy` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `addedHow` int(11) NOT NULL,
  `addedThrough` int(11) NOT NULL,
  `humanUpdateDate` datetime DEFAULT NULL,
  `humanVersion` int(11) NOT NULL,
  `integrityCheckDate` datetime DEFAULT NULL,
  `machineUpdateDate` datetime DEFAULT NULL,
  `machineVersion` int(11) NOT NULL,
  `munchUpdateDate` datetime DEFAULT NULL,
  `munchVersion` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PlaceLog`
--

LOCK TABLES `PlaceLog` WRITE;
/*!40000 ALTER TABLE `PlaceLog` DISABLE KEYS */;
/*!40000 ALTER TABLE `PlaceLog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PlaceMenu`
--

DROP TABLE IF EXISTS `PlaceMenu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PlaceMenu` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  `sort` bigint(20) NOT NULL,
  `caption` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `keyId` varchar(60) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(75) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` int(11) NOT NULL,
  `url` longtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dvlpv25c9e0bdwvymiofyx7so` (`keyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PlaceMenu`
--

LOCK TABLES `PlaceMenu` WRITE;
/*!40000 ALTER TABLE `PlaceMenu` DISABLE KEYS */;
/*!40000 ALTER TABLE `PlaceMenu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PlaceType`
--

DROP TABLE IF EXISTS `PlaceType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PlaceType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  `sort` bigint(20) NOT NULL,
  `name` varchar(80) COLLATE utf8_unicode_ci NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PlaceType`
--

LOCK TABLES `PlaceType` WRITE;
/*!40000 ALTER TABLE `PlaceType` DISABLE KEYS */;
/*!40000 ALTER TABLE `PlaceType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Place_PlaceHour`
--

DROP TABLE IF EXISTS `Place_PlaceHour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Place_PlaceHour` (
  `Place_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `placeHours_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`Place_id`,`placeHours_id`),
  UNIQUE KEY `UK_3si3eywjyooacqpu991q16eey` (`placeHours_id`),
  CONSTRAINT `FK5asejo6rnrrurlgn7o01lu2yh` FOREIGN KEY (`Place_id`) REFERENCES `Place` (`id`),
  CONSTRAINT `FKfi6d0w9tc9dp6qnwibmaa2dh6` FOREIGN KEY (`placeHours_id`) REFERENCES `PlaceHour` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Place_PlaceHour`
--

LOCK TABLES `Place_PlaceHour` WRITE;
/*!40000 ALTER TABLE `Place_PlaceHour` DISABLE KEYS */;
/*!40000 ALTER TABLE `Place_PlaceHour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Place_PlaceMenu`
--

DROP TABLE IF EXISTS `Place_PlaceMenu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Place_PlaceMenu` (
  `Place_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `menus_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`Place_id`,`menus_id`),
  UNIQUE KEY `UK_8g8fx5rlewcd8elwb6qeynf5m` (`menus_id`),
  CONSTRAINT `FK8g86ouqxk0a5tpyyuo4a2jjbt` FOREIGN KEY (`menus_id`) REFERENCES `PlaceMenu` (`id`),
  CONSTRAINT `FKigqrkhk9isnyhuf9uil8rxf50` FOREIGN KEY (`Place_id`) REFERENCES `Place` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Place_PlaceMenu`
--

LOCK TABLES `Place_PlaceMenu` WRITE;
/*!40000 ALTER TABLE `Place_PlaceMenu` DISABLE KEYS */;
/*!40000 ALTER TABLE `Place_PlaceMenu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Place_PlaceType`
--

DROP TABLE IF EXISTS `Place_PlaceType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Place_PlaceType` (
  `Place_id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `types_id` bigint(20) NOT NULL,
  PRIMARY KEY (`Place_id`,`types_id`),
  KEY `FK4kmxjmbar1f7sfnv7xvf5neaj` (`types_id`),
  CONSTRAINT `FK4kmxjmbar1f7sfnv7xvf5neaj` FOREIGN KEY (`types_id`) REFERENCES `PlaceType` (`id`),
  CONSTRAINT `FK9t410bs9vhvw4bx2qni7h069s` FOREIGN KEY (`Place_id`) REFERENCES `Place` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Place_PlaceType`
--

LOCK TABLES `Place_PlaceType` WRITE;
/*!40000 ALTER TABLE `Place_PlaceType` DISABLE KEYS */;
/*!40000 ALTER TABLE `Place_PlaceType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ReviewSummary`
--

DROP TABLE IF EXISTS `ReviewSummary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ReviewSummary` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `atmosphere` double DEFAULT NULL,
  `overallExperience` double NOT NULL,
  `price` double DEFAULT NULL,
  `qualityOfFood` double DEFAULT NULL,
  `qualityOfService` double DEFAULT NULL,
  `reviewCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ReviewSummary`
--

LOCK TABLES `ReviewSummary` WRITE;
/*!40000 ALTER TABLE `ReviewSummary` DISABLE KEYS */;
/*!40000 ALTER TABLE `ReviewSummary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SeedPlaceTrack`
--

DROP TABLE IF EXISTS `SeedPlaceTrack`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SeedPlaceTrack` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  `sort` bigint(20) NOT NULL,
  `address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `changes` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `source` int(11) NOT NULL,
  `sourceId` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sourceUrl` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sourceUser` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `stageExtractDate` datetime DEFAULT NULL,
  `stageFinalEntryDate` datetime DEFAULT NULL,
  `stageFinalSaveDate` datetime DEFAULT NULL,
  `stageLocationDate` datetime DEFAULT NULL,
  `stageProbeDate` datetime DEFAULT NULL,
  `stageSourceDate` datetime DEFAULT NULL,
  `status` int(11) NOT NULL,
  `place_id` char(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjdx74raximhdvqr2612bnqois` (`place_id`),
  CONSTRAINT `FKjdx74raximhdvqr2612bnqois` FOREIGN KEY (`place_id`) REFERENCES `Place` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SeedPlaceTrack`
--

LOCK TABLES `SeedPlaceTrack` WRITE;
/*!40000 ALTER TABLE `SeedPlaceTrack` DISABLE KEYS */;
/*!40000 ALTER TABLE `SeedPlaceTrack` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SortedImage`
--

DROP TABLE IF EXISTS `SortedImage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SortedImage` (
  `id` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  `sort` bigint(20) NOT NULL,
  `caption` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `keyId` varchar(60) COLLATE utf8_unicode_ci NOT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_c544sgndonqo0hqbqcwg4fvit` (`keyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SortedImage`
--

LOCK TABLES `SortedImage` WRITE;
/*!40000 ALTER TABLE `SortedImage` DISABLE KEYS */;
/*!40000 ALTER TABLE `SortedImage` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-19 19:49:05
