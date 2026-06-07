-- MySQL dump 10.13  Distrib 8.0.46, for Linux (aarch64)
--
-- Host: localhost    Database: worldcup_event_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_city`
--

DROP TABLE IF EXISTS `t_city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_city` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name_cn` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '城市中文名',
  `name_en` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '城市英文名',
  `country` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '所属国家',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '简介',
  `image_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片地址',
  `source` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据来源',
  `source_updated_at` datetime DEFAULT NULL COMMENT '来源更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_city_name_en` (`name_en`),
  KEY `idx_city_country` (`country`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='主办城市表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_city`
--

LOCK TABLES `t_city` WRITE;
/*!40000 ALTER TABLE `t_city` DISABLE KEYS */;
INSERT INTO `t_city` VALUES (1,'墨西哥城','Mexico City','Mexico','2026 世界杯开幕战主办城市。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(2,'瓜达拉哈拉','Guadalajara','Mexico','墨西哥主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(3,'蒙特雷','Monterrey','Mexico','墨西哥主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(4,'多伦多','Toronto','Canada','加拿大主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(5,'温哥华','Vancouver','Canada','加拿大主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(6,'纽约新泽西','New York New Jersey','United States','2026 世界杯决赛主办地。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(7,'洛杉矶','Los Angeles','United States','美国主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(8,'达拉斯','Dallas','United States','美国主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(9,'堪萨斯城','Kansas City','United States','美国主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(10,'休斯敦','Houston','United States','美国主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(11,'亚特兰大','Atlanta','United States','美国主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(12,'迈阿密','Miami','United States','美国主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(13,'费城','Philadelphia','United States','美国主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(14,'西雅图','Seattle','United States','美国主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(15,'旧金山湾区','San Francisco Bay Area','United States','美国主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(16,'波士顿','Boston','United States','美国主办城市之一。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02');
/*!40000 ALTER TABLE `t_city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_comment`
--

DROP TABLE IF EXISTS `t_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `match_id` bigint NOT NULL COMMENT '比赛ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `content` varchar(800) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `audit_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING APPROVED REJECTED',
  `audit_user_id` bigint DEFAULT NULL COMMENT '审核人ID',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_comment_match` (`match_id`),
  KEY `idx_comment_user` (`user_id`),
  KEY `idx_comment_status` (`audit_status`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='比赛评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_comment`
--

LOCK TABLES `t_comment` WRITE;
/*!40000 ALTER TABLE `t_comment` DISABLE KEYS */;
INSERT INTO `t_comment` VALUES (1,1,2,'期待揭幕战，墨西哥主场氛围一定很好。','APPROVED',NULL,NULL,'2026-06-06 15:41:02','2026-06-06 15:41:02'),(2,7,2,'巴西和摩洛哥这场很值得关注。','PENDING',NULL,NULL,'2026-06-06 15:41:02','2026-06-06 15:41:02'),(3,2,2,'联调测试评论','APPROVED',1,'2026-06-06 15:42:27','2026-06-06 15:42:17','2026-06-06 15:42:17');
/*!40000 ALTER TABLE `t_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_data_maintenance`
--

DROP TABLE IF EXISTS `t_data_maintenance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_data_maintenance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_type` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据类型',
  `source` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据来源',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `action_type` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_data_maintenance_type` (`data_type`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据维护记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_data_maintenance`
--

LOCK TABLES `t_data_maintenance` WRITE;
/*!40000 ALTER TABLE `t_data_maintenance` DISABLE KEYS */;
INSERT INTO `t_data_maintenance` VALUES (1,'SEED','FIFA public data',1,'INIT','初始化世界杯赛事信息系统基础数据','2026-06-06 15:41:02'),(2,'MATCH','FIFA official schedule page; FourFourTwo fixtures 2026-06-05',1,'SOURCE_VERIFY','补齐 48 支球队、12 个小组、104 场比赛；FIFA 官方页面为主来源，FourFourTwo 用于静态赛程文本核对。','2026-06-06 16:08:26');
/*!40000 ALTER TABLE `t_data_maintenance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_favorite`
--

DROP TABLE IF EXISTS `t_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `object_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'TEAM MATCH',
  `object_id` bigint NOT NULL COMMENT '收藏对象ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_favorite_user_object` (`user_id`,`object_type`,`object_id`),
  KEY `idx_favorite_object` (`object_type`,`object_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_favorite`
--

LOCK TABLES `t_favorite` WRITE;
/*!40000 ALTER TABLE `t_favorite` DISABLE KEYS */;
INSERT INTO `t_favorite` VALUES (1,2,'TEAM',11,'2026-06-06 15:41:02'),(2,2,'MATCH',1,'2026-06-06 15:41:02'),(3,2,'TEAM',12,'2026-06-06 15:42:17');
/*!40000 ALTER TABLE `t_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_match`
--

DROP TABLE IF EXISTS `t_match`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_match` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `match_no` int NOT NULL COMMENT '比赛编号',
  `stage` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '阶段',
  `group_name` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '小组',
  `home_team_id` bigint DEFAULT NULL COMMENT '主队ID',
  `away_team_id` bigint DEFAULT NULL COMMENT '客队ID',
  `match_time` datetime DEFAULT NULL COMMENT '比赛时间',
  `city_id` bigint DEFAULT NULL COMMENT '城市ID',
  `stadium_id` bigint DEFAULT NULL COMMENT '场馆ID',
  `home_score` int DEFAULT NULL COMMENT '主队比分',
  `away_score` int DEFAULT NULL COMMENT '客队比分',
  `status` varchar(24) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NOT_STARTED' COMMENT 'NOT_STARTED LIVE FINISHED CANCELLED',
  `winner_team_id` bigint DEFAULT NULL COMMENT '胜者球队ID',
  `source` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据来源',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_match_no` (`match_no`),
  KEY `idx_match_stage` (`stage`),
  KEY `idx_match_group` (`group_name`),
  KEY `idx_match_time` (`match_time`),
  KEY `idx_match_city` (`city_id`),
  KEY `idx_match_home_team` (`home_team_id`),
  KEY `idx_match_away_team` (`away_team_id`)
) ENGINE=InnoDB AUTO_INCREMENT=210 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='比赛表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_match`
--

LOCK TABLES `t_match` WRITE;
/*!40000 ALTER TABLE `t_match` DISABLE KEYS */;
INSERT INTO `t_match` VALUES (1,1,'GROUP','A',1,2,'2026-06-11 13:00:00',1,1,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(2,2,'GROUP','A',3,4,'2026-06-11 20:00:00',2,2,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(3,3,'GROUP','A',4,2,'2026-06-18 12:00:00',11,11,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(4,4,'GROUP','A',1,3,'2026-06-18 19:00:00',2,2,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(5,5,'GROUP','A',4,1,'2026-06-24 19:00:00',1,1,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(6,6,'GROUP','A',2,3,'2026-06-24 19:00:00',3,3,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(7,7,'GROUP','B',5,6,'2026-06-12 15:00:00',4,4,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(8,8,'GROUP','B',7,8,'2026-06-13 12:00:00',15,15,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(9,9,'GROUP','B',8,6,'2026-06-18 12:00:00',7,7,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(10,10,'GROUP','B',5,7,'2026-06-18 15:00:00',5,5,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(11,11,'GROUP','B',8,5,'2026-06-24 12:00:00',5,5,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(12,12,'GROUP','B',6,7,'2026-06-24 12:00:00',14,14,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(13,13,'GROUP','C',11,12,'2026-06-13 18:00:00',16,16,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(14,14,'GROUP','C',9,10,'2026-06-13 21:00:00',6,6,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(15,15,'GROUP','C',10,12,'2026-06-19 18:00:00',13,13,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(16,16,'GROUP','C',11,9,'2026-06-19 21:00:00',16,16,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 15:41:02','2026-06-06 16:08:26'),(73,73,'ROUND_OF_32',NULL,NULL,NULL,'2026-06-28 12:00:00',7,7,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Runner-up Group A vs Runner-up Group B','2026-06-06 15:41:02','2026-06-06 16:08:26'),(89,89,'ROUND_OF_16',NULL,NULL,NULL,'2026-07-04 12:00:00',13,13,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 74 vs Winner Match 77','2026-06-06 15:41:02','2026-06-06 16:08:26'),(97,97,'QUARTER_FINAL',NULL,NULL,NULL,'2026-07-09 12:00:00',16,16,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 89 vs Winner Match 90','2026-06-06 15:41:02','2026-06-06 16:08:26'),(101,101,'SEMI_FINAL',NULL,NULL,NULL,'2026-07-14 12:00:00',8,8,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 97 vs Winner Match 98','2026-06-06 15:41:02','2026-06-06 16:08:26'),(103,103,'THIRD_PLACE',NULL,NULL,NULL,'2026-07-18 12:00:00',12,12,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Loser Match 101 vs Loser Match 102','2026-06-06 15:41:02','2026-06-06 16:08:26'),(104,104,'FINAL',NULL,NULL,NULL,'2026-07-19 12:00:00',6,6,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 101 vs Winner Match 102','2026-06-06 15:41:02','2026-06-06 16:08:26'),(122,17,'GROUP','C',10,11,'2026-06-24 18:00:00',12,12,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(123,18,'GROUP','C',12,9,'2026-06-24 18:00:00',11,11,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(124,19,'GROUP','D',13,14,'2026-06-12 18:00:00',7,7,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(125,20,'GROUP','D',15,16,'2026-06-13 21:00:00',5,5,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(126,21,'GROUP','D',16,14,'2026-06-19 21:00:00',15,15,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(127,22,'GROUP','D',13,15,'2026-06-19 12:00:00',14,14,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(128,23,'GROUP','D',16,13,'2026-06-25 19:00:00',7,7,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(129,24,'GROUP','D',14,15,'2026-06-25 19:00:00',15,15,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(130,25,'GROUP','E',17,18,'2026-06-14 19:00:00',13,13,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(131,26,'GROUP','E',19,20,'2026-06-14 12:00:00',10,10,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(132,27,'GROUP','E',19,17,'2026-06-20 16:00:00',4,4,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(133,28,'GROUP','E',18,20,'2026-06-20 19:00:00',9,9,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(134,29,'GROUP','E',20,17,'2026-06-25 16:00:00',13,13,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(135,30,'GROUP','E',18,19,'2026-06-25 16:00:00',6,6,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(136,31,'GROUP','F',21,22,'2026-06-14 15:00:00',8,8,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(137,32,'GROUP','F',23,24,'2026-06-14 20:00:00',3,3,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(138,33,'GROUP','F',21,23,'2026-06-20 12:00:00',10,10,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(139,34,'GROUP','F',24,22,'2026-06-20 22:00:00',3,3,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(140,35,'GROUP','F',22,23,'2026-06-25 18:00:00',8,8,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(141,36,'GROUP','F',24,21,'2026-06-25 18:00:00',9,9,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(142,37,'GROUP','G',25,26,'2026-06-15 18:00:00',7,7,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(143,38,'GROUP','G',27,28,'2026-06-15 12:00:00',14,14,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(144,39,'GROUP','G',27,25,'2026-06-21 12:00:00',7,7,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(145,40,'GROUP','G',26,28,'2026-06-21 18:00:00',5,5,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(146,41,'GROUP','G',28,25,'2026-06-26 20:00:00',14,14,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(147,42,'GROUP','G',26,27,'2026-06-26 20:00:00',5,5,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(148,43,'GROUP','H',29,30,'2026-06-15 18:00:00',12,12,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(149,44,'GROUP','H',31,32,'2026-06-15 12:00:00',11,11,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(150,45,'GROUP','H',30,32,'2026-06-21 18:00:00',12,12,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(151,46,'GROUP','H',31,29,'2026-06-21 12:00:00',11,11,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(152,47,'GROUP','H',32,29,'2026-06-26 19:00:00',10,10,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(153,48,'GROUP','H',30,31,'2026-06-26 18:00:00',2,2,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(154,49,'GROUP','I',33,37,'2026-06-16 15:00:00',6,6,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(155,50,'GROUP','I',76,75,'2026-06-16 18:00:00',16,16,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(156,51,'GROUP','I',75,37,'2026-06-22 20:00:00',6,6,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(157,52,'GROUP','I',33,76,'2026-06-22 17:00:00',13,13,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(158,53,'GROUP','I',75,33,'2026-06-26 15:00:00',16,16,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(159,54,'GROUP','I',37,76,'2026-06-26 15:00:00',4,4,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(160,55,'GROUP','J',35,78,'2026-06-16 20:00:00',9,9,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(161,56,'GROUP','J',79,80,'2026-06-16 21:00:00',15,15,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(162,57,'GROUP','J',35,79,'2026-06-22 12:00:00',8,8,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(163,58,'GROUP','J',80,78,'2026-06-22 20:00:00',15,15,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(164,59,'GROUP','J',78,79,'2026-06-27 21:00:00',9,9,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(165,60,'GROUP','J',80,35,'2026-06-27 21:00:00',8,8,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(166,61,'GROUP','K',36,84,'2026-06-17 12:00:00',10,10,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(167,62,'GROUP','K',82,38,'2026-06-17 20:00:00',1,1,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(168,63,'GROUP','K',36,82,'2026-06-23 12:00:00',10,10,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(169,64,'GROUP','K',38,84,'2026-06-23 20:00:00',2,2,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(170,65,'GROUP','K',38,36,'2026-06-27 19:30:00',12,12,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(171,66,'GROUP','K',84,82,'2026-06-27 19:30:00',11,11,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(172,67,'GROUP','L',87,88,'2026-06-17 19:00:00',4,4,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(173,68,'GROUP','L',34,86,'2026-06-17 15:00:00',8,8,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(174,69,'GROUP','L',34,87,'2026-06-23 16:00:00',16,16,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(175,70,'GROUP','L',88,86,'2026-06-23 19:00:00',4,4,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(176,71,'GROUP','L',88,34,'2026-06-27 17:00:00',6,6,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(177,72,'GROUP','L',86,87,'2026-06-27 17:00:00',13,13,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26'),(179,74,'ROUND_OF_32',NULL,NULL,NULL,'2026-06-29 12:00:00',16,16,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Group E vs 3rd Group A/B/C/D/F','2026-06-06 16:08:26','2026-06-06 16:08:26'),(180,75,'ROUND_OF_32',NULL,NULL,NULL,'2026-06-29 12:00:00',3,3,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Group F vs Runner-up Group C','2026-06-06 16:08:26','2026-06-06 16:08:26'),(181,76,'ROUND_OF_32',NULL,NULL,NULL,'2026-06-29 12:00:00',10,10,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Group C vs Runner-up Group F','2026-06-06 16:08:26','2026-06-06 16:08:26'),(182,77,'ROUND_OF_32',NULL,NULL,NULL,'2026-06-30 12:00:00',6,6,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Group I vs 3rd Group C/D/F/G/H','2026-06-06 16:08:26','2026-06-06 16:08:26'),(183,78,'ROUND_OF_32',NULL,NULL,NULL,'2026-06-30 12:00:00',8,8,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Runner-up Group E vs Runner-up Group I','2026-06-06 16:08:26','2026-06-06 16:08:26'),(184,79,'ROUND_OF_32',NULL,NULL,NULL,'2026-06-30 12:00:00',1,1,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Group A vs 3rd Group C/E/F/H/I','2026-06-06 16:08:26','2026-06-06 16:08:26'),(185,80,'ROUND_OF_32',NULL,NULL,NULL,'2026-07-01 12:00:00',11,11,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Group L vs 3rd Group E/H/I/J/K','2026-06-06 16:08:26','2026-06-06 16:08:26'),(186,81,'ROUND_OF_32',NULL,NULL,NULL,'2026-07-01 12:00:00',15,15,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Group D vs 3rd Group B/E/F/I/J','2026-06-06 16:08:26','2026-06-06 16:08:26'),(187,82,'ROUND_OF_32',NULL,NULL,NULL,'2026-07-01 12:00:00',14,14,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Group G vs 3rd Group A/E/H/I/J','2026-06-06 16:08:26','2026-06-06 16:08:26'),(188,83,'ROUND_OF_32',NULL,NULL,NULL,'2026-07-02 12:00:00',4,4,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Runner-up Group K vs Runner-up Group L','2026-06-06 16:08:26','2026-06-06 16:08:26'),(189,84,'ROUND_OF_32',NULL,NULL,NULL,'2026-07-02 12:00:00',7,7,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Group H vs Runner-up Group J','2026-06-06 16:08:26','2026-06-06 16:08:26'),(190,85,'ROUND_OF_32',NULL,NULL,NULL,'2026-07-02 12:00:00',5,5,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Group B vs 3rd Group E/F/G/I/J','2026-06-06 16:08:26','2026-06-06 16:08:26'),(191,86,'ROUND_OF_32',NULL,NULL,NULL,'2026-07-03 12:00:00',12,12,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Group J vs Runner-up Group H','2026-06-06 16:08:26','2026-06-06 16:08:26'),(192,87,'ROUND_OF_32',NULL,NULL,NULL,'2026-07-03 12:00:00',9,9,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Group K vs 3rd Group D/E/I/J/L','2026-06-06 16:08:26','2026-06-06 16:08:26'),(193,88,'ROUND_OF_32',NULL,NULL,NULL,'2026-07-03 12:00:00',8,8,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Runner-up Group D vs Runner-up Group G','2026-06-06 16:08:26','2026-06-06 16:08:26'),(195,90,'ROUND_OF_16',NULL,NULL,NULL,'2026-07-04 12:00:00',10,10,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 73 vs Winner Match 75','2026-06-06 16:08:26','2026-06-06 16:08:26'),(196,91,'ROUND_OF_16',NULL,NULL,NULL,'2026-07-05 12:00:00',6,6,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 76 vs Winner Match 78','2026-06-06 16:08:26','2026-06-06 16:08:26'),(197,92,'ROUND_OF_16',NULL,NULL,NULL,'2026-07-05 12:00:00',1,1,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 79 vs Winner Match 80','2026-06-06 16:08:26','2026-06-06 16:08:26'),(198,93,'ROUND_OF_16',NULL,NULL,NULL,'2026-07-06 12:00:00',8,8,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 83 vs Winner Match 84','2026-06-06 16:08:26','2026-06-06 16:08:26'),(199,94,'ROUND_OF_16',NULL,NULL,NULL,'2026-07-06 12:00:00',14,14,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 81 vs Winner Match 82','2026-06-06 16:08:26','2026-06-06 16:08:26'),(200,95,'ROUND_OF_16',NULL,NULL,NULL,'2026-07-07 12:00:00',11,11,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 86 vs Winner Match 88','2026-06-06 16:08:26','2026-06-06 16:08:26'),(201,96,'ROUND_OF_16',NULL,NULL,NULL,'2026-07-07 12:00:00',5,5,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 85 vs Winner Match 87','2026-06-06 16:08:26','2026-06-06 16:08:26'),(203,98,'QUARTER_FINAL',NULL,NULL,NULL,'2026-07-10 12:00:00',7,7,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 93 vs Winner Match 94','2026-06-06 16:08:26','2026-06-06 16:08:26'),(204,99,'QUARTER_FINAL',NULL,NULL,NULL,'2026-07-11 12:00:00',12,12,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 91 vs Winner Match 92','2026-06-06 16:08:26','2026-06-06 16:08:26'),(205,100,'QUARTER_FINAL',NULL,NULL,NULL,'2026-07-11 12:00:00',9,9,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 95 vs Winner Match 96','2026-06-06 16:08:26','2026-06-06 16:08:26'),(207,102,'SEMI_FINAL',NULL,NULL,NULL,'2026-07-15 12:00:00',11,11,NULL,NULL,'NOT_STARTED',NULL,'FIFA official schedule page; FourFourTwo fixtures 2026-06-05 | Winner Match 99 vs Winner Match 100','2026-06-06 16:08:26','2026-06-06 16:08:26');
/*!40000 ALTER TABLE `t_match` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_stadium`
--

DROP TABLE IF EXISTS `t_stadium`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_stadium` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name_cn` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '场馆中文名',
  `name_en` varchar(160) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '场馆英文名',
  `city_id` bigint NOT NULL COMMENT '城市ID',
  `capacity` int DEFAULT NULL COMMENT '容量',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '简介',
  `image_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片地址',
  `source` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据来源',
  `source_updated_at` datetime DEFAULT NULL COMMENT '来源更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_stadium_name_en` (`name_en`),
  KEY `idx_stadium_city` (`city_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='场馆表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_stadium`
--

LOCK TABLES `t_stadium` WRITE;
/*!40000 ALTER TABLE `t_stadium` DISABLE KEYS */;
INSERT INTO `t_stadium` VALUES (1,'墨西哥城体育场','Mexico City Stadium',1,87523,'开幕战场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(2,'瓜达拉哈拉体育场','Estadio Guadalajara',2,49850,'瓜达拉哈拉比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(3,'蒙特雷体育场','Estadio Monterrey',3,53500,'蒙特雷比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(4,'多伦多体育场','Toronto Stadium',4,45000,'多伦多比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(5,'温哥华 BC Place','BC Place Vancouver',5,54500,'温哥华比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(6,'纽约新泽西体育场','New York New Jersey Stadium',6,82500,'决赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(7,'洛杉矶体育场','Los Angeles Stadium',7,70240,'洛杉矶比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(8,'达拉斯体育场','Dallas Stadium',8,80000,'达拉斯比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(9,'堪萨斯城体育场','Kansas City Stadium',9,76416,'堪萨斯城比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(10,'休斯敦体育场','Houston Stadium',10,72220,'休斯敦比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(11,'亚特兰大体育场','Atlanta Stadium',11,71000,'亚特兰大比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(12,'迈阿密体育场','Miami Stadium',12,65326,'迈阿密比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(13,'费城体育场','Philadelphia Stadium',13,69879,'费城比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(14,'西雅图体育场','Seattle Stadium',14,68740,'西雅图比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(15,'旧金山湾区体育场','San Francisco Bay Area Stadium',15,68500,'旧金山湾区比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02'),(16,'波士顿体育场','Boston Stadium',16,65878,'波士顿比赛场馆。','','FIFA public data','2026-06-06 15:41:02','2026-06-06 15:41:02','2026-06-06 15:41:02');
/*!40000 ALTER TABLE `t_stadium` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_standing`
--

DROP TABLE IF EXISTS `t_standing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_standing` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_name` varchar(8) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '小组',
  `team_id` bigint NOT NULL COMMENT '球队ID',
  `played` int NOT NULL DEFAULT '0' COMMENT '场次',
  `wins` int NOT NULL DEFAULT '0' COMMENT '胜',
  `draws` int NOT NULL DEFAULT '0' COMMENT '平',
  `losses` int NOT NULL DEFAULT '0' COMMENT '负',
  `goals_for` int NOT NULL DEFAULT '0' COMMENT '进球',
  `goals_against` int NOT NULL DEFAULT '0' COMMENT '失球',
  `goal_diff` int NOT NULL DEFAULT '0' COMMENT '净胜球',
  `points` int NOT NULL DEFAULT '0' COMMENT '积分',
  `rank_no` int DEFAULT NULL COMMENT '排名',
  `qualify_status` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING QUALIFIED ELIMINATED',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_standing_group_team` (`group_name`,`team_id`),
  KEY `idx_standing_group_rank` (`group_name`,`rank_no`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分榜表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_standing`
--

LOCK TABLES `t_standing` WRITE;
/*!40000 ALTER TABLE `t_standing` DISABLE KEYS */;
INSERT INTO `t_standing` VALUES (1,'A',1,0,0,0,0,0,0,0,0,1,'QUALIFIED','2026-06-06 15:41:02'),(2,'A',2,0,0,0,0,0,0,0,0,2,'QUALIFIED','2026-06-06 15:41:02'),(3,'A',3,0,0,0,0,0,0,0,0,3,'BEST_THIRD_QUALIFIED','2026-06-06 15:41:02'),(4,'A',4,0,0,0,0,0,0,0,0,4,'ELIMINATED','2026-06-06 15:41:02'),(5,'B',5,0,0,0,0,0,0,0,0,1,'QUALIFIED','2026-06-06 15:41:02'),(6,'B',6,0,0,0,0,0,0,0,0,2,'QUALIFIED','2026-06-06 15:41:02'),(7,'B',7,0,0,0,0,0,0,0,0,3,'BEST_THIRD_QUALIFIED','2026-06-06 15:41:02'),(8,'B',8,0,0,0,0,0,0,0,0,4,'ELIMINATED','2026-06-06 15:41:02'),(9,'C',9,0,0,0,0,0,0,0,0,1,'QUALIFIED','2026-06-06 15:41:02'),(10,'C',10,0,0,0,0,0,0,0,0,2,'QUALIFIED','2026-06-06 15:41:02'),(11,'C',11,0,0,0,0,0,0,0,0,3,'BEST_THIRD_QUALIFIED','2026-06-06 15:41:02'),(12,'C',12,0,0,0,0,0,0,0,0,4,'ELIMINATED','2026-06-06 15:41:02'),(13,'D',13,0,0,0,0,0,0,0,0,1,'QUALIFIED','2026-06-06 15:41:02'),(14,'D',14,0,0,0,0,0,0,0,0,2,'QUALIFIED','2026-06-06 15:41:02'),(15,'D',15,0,0,0,0,0,0,0,0,3,'BEST_THIRD_QUALIFIED','2026-06-06 15:41:02'),(16,'D',16,0,0,0,0,0,0,0,0,4,'ELIMINATED','2026-06-06 15:41:02'),(17,'E',17,0,0,0,0,0,0,0,0,1,'QUALIFIED','2026-06-06 15:41:02'),(18,'E',18,0,0,0,0,0,0,0,0,2,'QUALIFIED','2026-06-06 15:41:02'),(19,'E',19,0,0,0,0,0,0,0,0,3,'BEST_THIRD_QUALIFIED','2026-06-06 15:41:02'),(20,'E',20,0,0,0,0,0,0,0,0,4,'ELIMINATED','2026-06-06 15:41:02'),(21,'F',21,0,0,0,0,0,0,0,0,1,'QUALIFIED','2026-06-06 15:41:02'),(22,'F',22,0,0,0,0,0,0,0,0,2,'QUALIFIED','2026-06-06 15:41:02'),(23,'F',23,0,0,0,0,0,0,0,0,3,'BEST_THIRD_QUALIFIED','2026-06-06 15:41:02'),(24,'F',24,0,0,0,0,0,0,0,0,4,'ELIMINATED','2026-06-06 15:41:02'),(25,'G',25,0,0,0,0,0,0,0,0,1,'QUALIFIED','2026-06-06 15:41:02'),(26,'G',26,0,0,0,0,0,0,0,0,2,'QUALIFIED','2026-06-06 15:41:02'),(27,'G',27,0,0,0,0,0,0,0,0,3,'BEST_THIRD_QUALIFIED','2026-06-06 15:41:02'),(28,'G',28,0,0,0,0,0,0,0,0,4,'ELIMINATED','2026-06-06 15:41:02'),(29,'H',29,0,0,0,0,0,0,0,0,1,'QUALIFIED','2026-06-06 15:41:02'),(30,'H',30,0,0,0,0,0,0,0,0,2,'QUALIFIED','2026-06-06 15:41:02'),(31,'H',31,0,0,0,0,0,0,0,0,3,'BEST_THIRD_QUALIFIED','2026-06-06 15:41:02'),(32,'H',32,0,0,0,0,0,0,0,0,4,'ELIMINATED','2026-06-06 15:41:02'),(33,'I',33,0,0,0,0,0,0,0,0,1,'QUALIFIED','2026-06-06 15:41:02'),(35,'J',35,0,0,0,0,0,0,0,0,1,'QUALIFIED','2026-06-06 15:41:02'),(36,'K',36,0,0,0,0,0,0,0,0,1,'QUALIFIED','2026-06-06 15:41:02'),(66,'L',34,0,0,0,0,0,0,0,0,1,'QUALIFIED','2026-06-06 16:08:27'),(67,'I',37,0,0,0,0,0,0,0,0,2,'QUALIFIED','2026-06-06 16:08:27'),(68,'K',38,0,0,0,0,0,0,0,0,2,'QUALIFIED','2026-06-06 16:08:27'),(69,'I',75,0,0,0,0,0,0,0,0,3,'ELIMINATED','2026-06-06 16:08:27'),(70,'I',76,0,0,0,0,0,0,0,0,4,'ELIMINATED','2026-06-06 16:08:27'),(71,'J',78,0,0,0,0,0,0,0,0,2,'QUALIFIED','2026-06-06 16:08:27'),(72,'J',79,0,0,0,0,0,0,0,0,3,'ELIMINATED','2026-06-06 16:08:27'),(73,'J',80,0,0,0,0,0,0,0,0,4,'ELIMINATED','2026-06-06 16:08:27'),(74,'K',82,0,0,0,0,0,0,0,0,3,'ELIMINATED','2026-06-06 16:08:27'),(75,'K',84,0,0,0,0,0,0,0,0,4,'ELIMINATED','2026-06-06 16:08:27'),(76,'L',86,0,0,0,0,0,0,0,0,2,'QUALIFIED','2026-06-06 16:08:27'),(77,'L',87,0,0,0,0,0,0,0,0,3,'ELIMINATED','2026-06-06 16:08:27'),(78,'L',88,0,0,0,0,0,0,0,0,4,'ELIMINATED','2026-06-06 16:08:27');
/*!40000 ALTER TABLE `t_standing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_team`
--

DROP TABLE IF EXISTS `t_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_team` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name_cn` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '球队中文名',
  `name_en` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '球队英文名',
  `country` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '国家或地区',
  `confederation` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '洲际足联',
  `group_name` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '小组',
  `flag_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '队徽或国旗地址',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '简介',
  `source` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据来源',
  `source_updated_at` datetime DEFAULT NULL COMMENT '来源更新时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_team_name_en` (`name_en`),
  KEY `idx_team_group` (`group_name`),
  KEY `idx_team_confederation` (`confederation`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='球队表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_team`
--

LOCK TABLES `t_team` WRITE;
/*!40000 ALTER TABLE `t_team` DISABLE KEYS */;
INSERT INTO `t_team` VALUES (1,'墨西哥','Mexico','Mexico','CONCACAF','A','','墨西哥 / Mexico，2026 FIFA 世界杯 A 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(2,'南非','South Africa','South Africa','CAF','A','','南非 / South Africa，2026 FIFA 世界杯 A 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(3,'韩国','South Korea','South Korea','AFC','A','','韩国 / South Korea，2026 FIFA 世界杯 A 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(4,'捷克','Czechia','Czechia','UEFA','A','','捷克 / Czechia，2026 FIFA 世界杯 A 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(5,'加拿大','Canada','Canada','CONCACAF','B','','加拿大 / Canada，2026 FIFA 世界杯 B 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(6,'波黑','Bosnia and Herzegovina','Bosnia and Herzegovina','UEFA','B','','波黑 / Bosnia and Herzegovina，2026 FIFA 世界杯 B 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(7,'卡塔尔','Qatar','Qatar','AFC','B','','卡塔尔 / Qatar，2026 FIFA 世界杯 B 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(8,'瑞士','Switzerland','Switzerland','UEFA','B','','瑞士 / Switzerland，2026 FIFA 世界杯 B 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(9,'海地','Haiti','Haiti','CONCACAF','C','','海地 / Haiti，2026 FIFA 世界杯 C 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(10,'苏格兰','Scotland','Scotland','UEFA','C','','苏格兰 / Scotland，2026 FIFA 世界杯 C 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(11,'巴西','Brazil','Brazil','CONMEBOL','C','','巴西 / Brazil，2026 FIFA 世界杯 C 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(12,'摩洛哥','Morocco','Morocco','CAF','C','','摩洛哥 / Morocco，2026 FIFA 世界杯 C 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(13,'美国','United States','United States','CONCACAF','D','','美国 / United States，2026 FIFA 世界杯 D 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(14,'巴拉圭','Paraguay','Paraguay','CONMEBOL','D','','巴拉圭 / Paraguay，2026 FIFA 世界杯 D 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(15,'澳大利亚','Australia','Australia','AFC','D','','澳大利亚 / Australia，2026 FIFA 世界杯 D 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(16,'土耳其','Turkiye','Turkiye','UEFA','D','','土耳其 / Turkiye，2026 FIFA 世界杯 D 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(17,'科特迪瓦','Ivory Coast','Ivory Coast','CAF','E','','科特迪瓦 / Ivory Coast，2026 FIFA 世界杯 E 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(18,'厄瓜多尔','Ecuador','Ecuador','CONMEBOL','E','','厄瓜多尔 / Ecuador，2026 FIFA 世界杯 E 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(19,'德国','Germany','Germany','UEFA','E','','德国 / Germany，2026 FIFA 世界杯 E 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(20,'库拉索','Curacao','Curacao','CONCACAF','E','','库拉索 / Curacao，2026 FIFA 世界杯 E 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(21,'荷兰','Netherlands','Netherlands','UEFA','F','','荷兰 / Netherlands，2026 FIFA 世界杯 F 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(22,'日本','Japan','Japan','AFC','F','','日本 / Japan，2026 FIFA 世界杯 F 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(23,'瑞典','Sweden','Sweden','UEFA','F','','瑞典 / Sweden，2026 FIFA 世界杯 F 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(24,'突尼斯','Tunisia','Tunisia','CAF','F','','突尼斯 / Tunisia，2026 FIFA 世界杯 F 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(25,'伊朗','Iran','Iran','AFC','G','','伊朗 / Iran，2026 FIFA 世界杯 G 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(26,'新西兰','New Zealand','New Zealand','OFC','G','','新西兰 / New Zealand，2026 FIFA 世界杯 G 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(27,'比利时','Belgium','Belgium','UEFA','G','','比利时 / Belgium，2026 FIFA 世界杯 G 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(28,'埃及','Egypt','Egypt','CAF','G','','埃及 / Egypt，2026 FIFA 世界杯 G 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(29,'沙特阿拉伯','Saudi Arabia','Saudi Arabia','AFC','H','','沙特阿拉伯 / Saudi Arabia，2026 FIFA 世界杯 H 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(30,'乌拉圭','Uruguay','Uruguay','CONMEBOL','H','','乌拉圭 / Uruguay，2026 FIFA 世界杯 H 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(31,'西班牙','Spain','Spain','UEFA','H','','西班牙 / Spain，2026 FIFA 世界杯 H 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(32,'佛得角','Cape Verde','Cape Verde','CAF','H','','佛得角 / Cape Verde，2026 FIFA 世界杯 H 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(33,'法国','France','France','UEFA','I','','法国 / France，2026 FIFA 世界杯 I 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(34,'英格兰','England','England','UEFA','L','','英格兰 / England，2026 FIFA 世界杯 L 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(35,'阿根廷','Argentina','Argentina','CONMEBOL','J','','阿根廷 / Argentina，2026 FIFA 世界杯 J 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(36,'葡萄牙','Portugal','Portugal','UEFA','K','','葡萄牙 / Portugal，2026 FIFA 世界杯 K 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(37,'塞内加尔','Senegal','Senegal','CAF','I','','塞内加尔 / Senegal，2026 FIFA 世界杯 I 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(38,'哥伦比亚','Colombia','Colombia','CONMEBOL','K','','哥伦比亚 / Colombia，2026 FIFA 世界杯 K 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 15:41:02','2026-06-06 16:08:26'),(75,'挪威','Norway','Norway','UEFA','I',NULL,'挪威 / Norway，2026 FIFA 世界杯 I 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26','2026-06-06 16:08:26'),(76,'伊拉克','Iraq','Iraq','AFC','I',NULL,'伊拉克 / Iraq，2026 FIFA 世界杯 I 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26','2026-06-06 16:08:26'),(78,'阿尔及利亚','Algeria','Algeria','CAF','J',NULL,'阿尔及利亚 / Algeria，2026 FIFA 世界杯 J 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26','2026-06-06 16:08:26'),(79,'奥地利','Austria','Austria','UEFA','J',NULL,'奥地利 / Austria，2026 FIFA 世界杯 J 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26','2026-06-06 16:08:26'),(80,'约旦','Jordan','Jordan','AFC','J',NULL,'约旦 / Jordan，2026 FIFA 世界杯 J 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26','2026-06-06 16:08:26'),(82,'乌兹别克斯坦','Uzbekistan','Uzbekistan','AFC','K',NULL,'乌兹别克斯坦 / Uzbekistan，2026 FIFA 世界杯 K 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26','2026-06-06 16:08:26'),(84,'刚果民主共和国','DR Congo','DR Congo','CAF','K',NULL,'刚果民主共和国 / DR Congo，2026 FIFA 世界杯 K 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26','2026-06-06 16:08:26'),(86,'克罗地亚','Croatia','Croatia','UEFA','L',NULL,'克罗地亚 / Croatia，2026 FIFA 世界杯 L 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26','2026-06-06 16:08:26'),(87,'加纳','Ghana','Ghana','CAF','L',NULL,'加纳 / Ghana，2026 FIFA 世界杯 L 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26','2026-06-06 16:08:26'),(88,'巴拿马','Panama','Panama','CONCACAF','L',NULL,'巴拿马 / Panama，2026 FIFA 世界杯 L 组参赛球队。','FIFA official schedule page; FourFourTwo fixtures 2026-06-05','2026-06-06 16:08:26','2026-06-06 16:08:26','2026-06-06 16:08:26');
/*!40000 ALTER TABLE `t_team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'MD5密码',
  `email` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱',
  `phone` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `role` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USER' COMMENT '角色: ADMIN-管理员 USER-普通用户',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 0-禁用 1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (1,'admin','0192023a7bbd73250516f069df18b500','admin@example.com','13800000000','ADMIN',1,'2026-06-06 15:41:02','2026-06-06 15:41:02'),(2,'user','e10adc3949ba59abbe56e057f20f883e','user@example.com','13900000000','USER',1,'2026-06-06 15:41:02','2026-06-06 15:41:02');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-06 16:10:13
