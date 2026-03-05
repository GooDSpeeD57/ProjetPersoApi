CREATE DATABASE  IF NOT EXISTS `micromania` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `micromania`;
-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: micromania
-- ------------------------------------------------------
-- Server version	8.4.8

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
-- Table structure for table `abonnement_client`
--

DROP TABLE IF EXISTS `abonnement_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `abonnement_client` (
  `id_abonnement` bigint NOT NULL AUTO_INCREMENT,
  `id_client` bigint NOT NULL,
  `id_statut_abonnement` bigint NOT NULL,
  `date_debut` date NOT NULL,
  `date_fin` date NOT NULL,
  `montant_paye` decimal(10,2) NOT NULL,
  `date_paiement` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `renouvellement_auto` tinyint(1) NOT NULL DEFAULT '1',
  `date_resiliation` datetime DEFAULT NULL,
  PRIMARY KEY (`id_abonnement`),
  KEY `idx_abonnement_client` (`id_client`),
  KEY `idx_abonnement_date_fin` (`date_fin`),
  KEY `idx_abonnement_statut` (`id_statut_abonnement`),
  CONSTRAINT `fk_abo_client` FOREIGN KEY (`id_client`) REFERENCES `client` (`id_client`),
  CONSTRAINT `fk_abo_statut` FOREIGN KEY (`id_statut_abonnement`) REFERENCES `statut_abonnement` (`id_statut_abonnement`),
  CONSTRAINT `chk_abo_dates` CHECK ((`date_fin` > `date_debut`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `abonnement_client`
--

LOCK TABLES `abonnement_client` WRITE;
/*!40000 ALTER TABLE `abonnement_client` DISABLE KEYS */;
/*!40000 ALTER TABLE `abonnement_client` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_expiration_abonnement` BEFORE UPDATE ON `abonnement_client` FOR EACH ROW BEGIN
    DECLARE v_id_premium BIGINT;
    IF NEW.id_statut_abonnement != OLD.id_statut_abonnement THEN
        IF (SELECT code FROM statut_abonnement
            WHERE id_statut_abonnement = NEW.id_statut_abonnement) = 'EXPIRE' THEN
            SELECT id_type_fidelite INTO v_id_premium
                FROM type_fidelite WHERE code = 'PREMIUM' LIMIT 1;
            UPDATE client SET id_type_fidelite = v_id_premium WHERE id_client = NEW.id_client;
        END IF;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `adresse`
--

DROP TABLE IF EXISTS `adresse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `adresse` (
  `id_adresse` bigint NOT NULL AUTO_INCREMENT,
  `id_client` bigint DEFAULT NULL,
  `id_magasin` bigint DEFAULT NULL,
  `id_type_adresse` bigint NOT NULL,
  `rue` varchar(255) NOT NULL,
  `ville` varchar(100) NOT NULL,
  `code_postal` varchar(10) NOT NULL,
  `pays` varchar(100) NOT NULL DEFAULT 'France',
  PRIMARY KEY (`id_adresse`),
  KEY `fk_adresse_client` (`id_client`),
  KEY `fk_adresse_magasin` (`id_magasin`),
  KEY `fk_adresse_type_adresse` (`id_type_adresse`),
  CONSTRAINT `fk_adresse_client` FOREIGN KEY (`id_client`) REFERENCES `client` (`id_client`) ON DELETE CASCADE,
  CONSTRAINT `fk_adresse_magasin` FOREIGN KEY (`id_magasin`) REFERENCES `magasin` (`id_magasin`) ON DELETE CASCADE,
  CONSTRAINT `fk_adresse_type_adresse` FOREIGN KEY (`id_type_adresse`) REFERENCES `type_adresse` (`id_type_adresse`),
  CONSTRAINT `chk_adresse_owner` CHECK ((((`id_client` is not null) and (`id_magasin` is null)) or ((`id_client` is null) and (`id_magasin` is not null))))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adresse`
--

LOCK TABLES `adresse` WRITE;
/*!40000 ALTER TABLE `adresse` DISABLE KEYS */;
INSERT INTO `adresse` VALUES (1,NULL,1,2,'Centre Commercial Cora — Route de Metz','Moulins-lès-Metz','57160','France'),(2,NULL,1,2,'Centre Commercial Cora — Route de Metz','Moulins-lès-Metz','57160','France'),(3,NULL,1,2,'Centre Commercial Cora — Route de Metz','Moulins-lès-Metz','57160','France'),(4,NULL,1,2,'Centre Commercial Cora — Route de Metz','Moulins-lès-Metz','57160','France');
/*!40000 ALTER TABLE `adresse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_log`
--

DROP TABLE IF EXISTS `audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_log` (
  `id_audit` bigint NOT NULL AUTO_INCREMENT,
  `table_name` varchar(100) NOT NULL,
  `operation_type` varchar(20) NOT NULL,
  `record_id` bigint NOT NULL,
  `donnees_avant` json DEFAULT NULL,
  `donnees_apres` json DEFAULT NULL,
  `user_identifier` varchar(100) DEFAULT NULL,
  `date_operation` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_audit`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

LOCK TABLES `audit_log` WRITE;
/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
INSERT INTO `audit_log` VALUES (1,'client','UPDATE',3,'{\"email\": \"john.Rambo@gmalo.cul\", \"niveau\": 1, \"pseudo\": \"Rambo\", \"deleted\": 0, \"rgpd_consent\": 1}','{\"email\": \"john.Rambo@gmalo.cul\", \"niveau\": 1, \"pseudo\": \"Rambo\", \"deleted\": 0, \"rgpd_consent\": 1}',NULL,'2026-03-05 16:08:25'),(2,'client','UPDATE',2,'{\"email\": \"juju.taesch@gmail.com\", \"niveau\": 1, \"pseudo\": \"GooDSpeeD57\", \"deleted\": 0, \"rgpd_consent\": 1}','{\"email\": \"juju.taesch@gmail.com\", \"niveau\": 1, \"pseudo\": \"GooDSpeeD57\", \"deleted\": 0, \"rgpd_consent\": 1}',NULL,'2026-03-05 16:08:25'),(3,'client','UPDATE',4,'{\"email\": \"Clarc.Kent@dayliplanet.com\", \"niveau\": 1, \"pseudo\": \"Superman\", \"deleted\": 0, \"rgpd_consent\": 1}','{\"email\": \"Clarc.Kent@dayliplanet.com\", \"niveau\": 1, \"pseudo\": \"Superman\", \"deleted\": 0, \"rgpd_consent\": 1}',NULL,'2026-03-05 16:24:46'),(4,'client','UPDATE',4,'{\"email\": \"Clarc.Kent@dayliplanet.com\", \"niveau\": 1, \"pseudo\": \"Superman\", \"deleted\": 0, \"rgpd_consent\": 1}','{\"email\": \"Clarc.Kent@dayliplanet.com\", \"niveau\": 1, \"pseudo\": \"Superman\", \"deleted\": 0, \"rgpd_consent\": 1}',NULL,'2026-03-05 16:50:41'),(5,'client','UPDATE',5,'{\"email\": \"Bruce.Wayne@tmalo.cul\", \"niveau\": 1, \"pseudo\": \"Batman\", \"deleted\": 0, \"rgpd_consent\": 1}','{\"email\": \"Bruce.Wayne@tmalo.cul\", \"niveau\": 1, \"pseudo\": \"Batman\", \"deleted\": 0, \"rgpd_consent\": 1}',NULL,'2026-03-05 16:50:41'),(6,'client','UPDATE',5,'{\"email\": \"Bruce.Wayne@tmalo.cul\", \"niveau\": 1, \"pseudo\": \"Batman\", \"deleted\": 0, \"rgpd_consent\": 1}','{\"email\": \"Bruce.Wayne@tmalo.cul\", \"niveau\": 1, \"pseudo\": \"Batman\", \"deleted\": 0, \"rgpd_consent\": 1}',NULL,'2026-03-05 16:52:32');
/*!40000 ALTER TABLE `audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `avis_produit`
--

DROP TABLE IF EXISTS `avis_produit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `avis_produit` (
  `id_avis` bigint NOT NULL AUTO_INCREMENT,
  `id_client` bigint NOT NULL,
  `id_produit` bigint NOT NULL,
  `note` tinyint NOT NULL,
  `commentaire` text,
  `date_creation` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_modification` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_avis`),
  UNIQUE KEY `uq_avis_client_produit` (`id_client`,`id_produit`),
  KEY `idx_avis_produit` (`id_produit`),
  KEY `idx_avis_client` (`id_client`),
  KEY `idx_avis_note` (`note`),
  CONSTRAINT `fk_avis_client` FOREIGN KEY (`id_client`) REFERENCES `client` (`id_client`) ON DELETE CASCADE,
  CONSTRAINT `fk_avis_produit` FOREIGN KEY (`id_produit`) REFERENCES `produit` (`id_produit`) ON DELETE CASCADE,
  CONSTRAINT `chk_note` CHECK ((`note` between 1 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `avis_produit`
--

LOCK TABLES `avis_produit` WRITE;
/*!40000 ALTER TABLE `avis_produit` DISABLE KEYS */;
/*!40000 ALTER TABLE `avis_produit` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_avis_achat_verifie` BEFORE INSERT ON `avis_produit` FOR EACH ROW BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM ligne_facture lf
        JOIN facture f ON f.id_facture = lf.id_facture
        WHERE f.id_client = NEW.id_client AND lf.id_produit = NEW.id_produit
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Le client doit avoir achete le produit pour laisser un avis';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `bon_achat`
--

DROP TABLE IF EXISTS `bon_achat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bon_achat` (
  `id_bon_achat` bigint NOT NULL AUTO_INCREMENT,
  `id_client` bigint NOT NULL,
  `code_bon` varchar(50) NOT NULL,
  `valeur` decimal(10,2) NOT NULL,
  `points_utilises` int NOT NULL,
  `utilise` tinyint(1) NOT NULL DEFAULT '0',
  `id_facture` bigint DEFAULT NULL,
  `date_creation` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_utilisation` datetime DEFAULT NULL,
  PRIMARY KEY (`id_bon_achat`),
  UNIQUE KEY `code_bon` (`code_bon`),
  KEY `fk_bon_facture` (`id_facture`),
  KEY `idx_bon_achat_client` (`id_client`),
  KEY `idx_bon_achat_utilise` (`utilise`),
  CONSTRAINT `fk_bon_client` FOREIGN KEY (`id_client`) REFERENCES `client` (`id_client`),
  CONSTRAINT `fk_bon_facture` FOREIGN KEY (`id_facture`) REFERENCES `facture` (`id_facture`),
  CONSTRAINT `chk_bon_points` CHECK ((`points_utilises` in (2000,8000))),
  CONSTRAINT `chk_bon_valeur` CHECK ((`valeur` in (10.00,20.00)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bon_achat`
--

LOCK TABLES `bon_achat` WRITE;
/*!40000 ALTER TABLE `bon_achat` DISABLE KEYS */;
/*!40000 ALTER TABLE `bon_achat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorie`
--

DROP TABLE IF EXISTS `categorie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorie` (
  `id_categorie` bigint NOT NULL AUTO_INCREMENT,
  `id_type_categorie` bigint NOT NULL,
  `nom` varchar(100) NOT NULL,
  `description` text,
  `actif` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_categorie`),
  UNIQUE KEY `nom` (`nom`),
  KEY `fk_categorie_type` (`id_type_categorie`),
  CONSTRAINT `fk_categorie_type` FOREIGN KEY (`id_type_categorie`) REFERENCES `type_categorie` (`id_type_categorie`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorie`
--

LOCK TABLES `categorie` WRITE;
/*!40000 ALTER TABLE `categorie` DISABLE KEYS */;
INSERT INTO `categorie` VALUES (1,1,'Jeux PS5',NULL,1),(2,1,'Jeux Xbox Series',NULL,1),(3,1,'Jeux Nintendo Switch',NULL,1),(4,1,'Jeux PC',NULL,1),(5,2,'Consoles',NULL,1),(6,3,'Accessoires',NULL,1),(7,4,'Goodies',NULL,1);
/*!40000 ALTER TABLE `categorie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `id_client` bigint NOT NULL AUTO_INCREMENT,
  `pseudo` varchar(50) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `date_naissance` date NOT NULL,
  `email` varchar(150) NOT NULL,
  `telephone` varchar(20) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `numero_carte_fidelite` varchar(50) DEFAULT NULL,
  `id_type_fidelite` bigint NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `rgpd_consent` tinyint(1) NOT NULL DEFAULT '0',
  `rgpd_consent_date` datetime DEFAULT NULL,
  `rgpd_consent_ip` varchar(45) DEFAULT NULL,
  `demande_suppression` tinyint(1) NOT NULL DEFAULT '0',
  `date_suppression` datetime DEFAULT NULL,
  `date_creation` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_modification` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_client`),
  UNIQUE KEY `pseudo` (`pseudo`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `telephone_UNIQUE` (`telephone`),
  UNIQUE KEY `numero_carte_fidelite` (`numero_carte_fidelite`),
  KEY `idx_client_email` (`email`),
  KEY `idx_client_fidelite` (`id_type_fidelite`),
  KEY `idx_client_deleted` (`deleted`),
  KEY `idx_client_demande_suppression` (`demande_suppression`),
  KEY `idx_client_rgpd_consent` (`rgpd_consent`),
  CONSTRAINT `fk_client_fidelite` FOREIGN KEY (`id_type_fidelite`) REFERENCES `type_fidelite` (`id_type_fidelite`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (2,'GooDSpeeD57','Taesch','julien','2026-06-05','juju.taesch@gmail.com','0612345678','$2a$10$aOtfqngKKjezTL9tbOkQ7eMKx4GYF3gGUm6psaCTzpPFllSwOi7z.','DEA2311D-F6C7-44',1,0,1,'2026-03-05 11:57:30','127.0.0.1',0,NULL,'2026-03-05 11:57:30','2026-03-05 16:08:25'),(3,'Rambo','John','Rambo','1982-05-14','john.Rambo@gmalo.cul','0610454894','$2a$10$x8XRO36Ga1HIrHfT4MI/2O9cJvHCB20kXC.bs5b4iaaVhi4taksXu','FF04EA51-08D7-40',1,0,1,'2026-03-05 15:46:01','127.0.0.1',0,NULL,'2026-03-05 15:46:01','2026-03-05 16:08:25'),(4,'Superman','Kent','Clarc','2026-03-11','Clarc.Kent@dayliplanet.com','0612345677','$2a$10$dCXMmKSkwwEWm2OF/KiRzOOdLkraQb1XqwKnYDscTzIyVJ/3DgGQW','F7634A43-00C7-4C',1,0,1,'2026-03-05 16:11:58','127.0.0.1',0,NULL,'2026-03-05 16:11:58','2026-03-05 16:50:41'),(5,'Batman','Wayne','Bruce','2026-03-05','Bruce.Wayne@tmalo.cul','0612345679','$2a$10$ZeXC.uUbO8pxFsTAWLODwO8UPASVKheQ3mMUOS8CRGn3xOeIJLw1q','28852DE0-F1EB-47',1,0,1,'2026-03-05 16:31:38','127.0.0.1',0,NULL,'2026-03-05 16:31:38','2026-03-05 16:52:32');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_audit_client_update` AFTER UPDATE ON `client` FOR EACH ROW BEGIN
    INSERT INTO audit_log (table_name, operation_type, record_id, donnees_avant, donnees_apres)
    VALUES ('client', 'UPDATE', OLD.id_client,
        JSON_OBJECT('email', OLD.email, 'pseudo', OLD.pseudo, 'niveau', OLD.id_type_fidelite,
                    'rgpd_consent', OLD.rgpd_consent, 'deleted', OLD.deleted),
        JSON_OBJECT('email', NEW.email, 'pseudo', NEW.pseudo, 'niveau', NEW.id_type_fidelite,
                    'rgpd_consent', NEW.rgpd_consent, 'deleted', NEW.deleted));
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `employe`
--

DROP TABLE IF EXISTS `employe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employe` (
  `id_employe` bigint NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `id_role` bigint NOT NULL,
  `id_magasin` bigint NOT NULL,
  `date_embauche` date DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `date_creation` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_modification` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_employe`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_employe_role` (`id_role`),
  KEY `fk_employe_magasin` (`id_magasin`),
  KEY `idx_employe_deleted` (`deleted`),
  CONSTRAINT `fk_employe_magasin` FOREIGN KEY (`id_magasin`) REFERENCES `magasin` (`id_magasin`),
  CONSTRAINT `fk_employe_role` FOREIGN KEY (`id_role`) REFERENCES `role` (`id_role`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employe`
--

LOCK TABLES `employe` WRITE;
/*!40000 ALTER TABLE `employe` DISABLE KEYS */;
INSERT INTO `employe` VALUES (1,'Administrateur','Super','admin@micromania.fr','$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LkTSBzy4mNS',3,1,'2026-03-05',0,'2026-03-05 10:35:33','2026-03-05 10:35:33'),(2,'Martin','Sophie','manager@micromania.fr','$2a$10$8K1p/a0dR1xqM2LtPSQtOeIl7NKSSVerIOjJHiJ0NnwWMSFTIBNGi',2,1,'2026-03-05',0,'2026-03-05 10:35:33','2026-03-05 10:35:33'),(3,'Dupont','Lucas','vendeur@micromania.fr','$2a$10$ByqhLUvlEzPCEFBPdUnFmODM3YpTAmHDXtxJN4hHxFJoYd.ywM4yy',1,1,'2026-03-05',0,'2026-03-05 10:35:33','2026-03-05 10:35:33');
/*!40000 ALTER TABLE `employe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extension_garantie`
--

DROP TABLE IF EXISTS `extension_garantie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `extension_garantie` (
  `id_extension` bigint NOT NULL AUTO_INCREMENT,
  `id_garantie` bigint NOT NULL,
  `id_type_garantie` bigint NOT NULL,
  `date_achat` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_fin_etendue` date NOT NULL,
  PRIMARY KEY (`id_extension`),
  KEY `fk_extension_type_garantie` (`id_type_garantie`),
  KEY `idx_extension_garantie` (`id_garantie`),
  CONSTRAINT `fk_extension_garantie` FOREIGN KEY (`id_garantie`) REFERENCES `garantie` (`id_garantie`),
  CONSTRAINT `fk_extension_type_garantie` FOREIGN KEY (`id_type_garantie`) REFERENCES `type_garantie` (`id_type_garantie`),
  CONSTRAINT `chk_dates_extension` CHECK ((`date_fin_etendue` > `date_achat`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extension_garantie`
--

LOCK TABLES `extension_garantie` WRITE;
/*!40000 ALTER TABLE `extension_garantie` DISABLE KEYS */;
/*!40000 ALTER TABLE `extension_garantie` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_extension_garantie_update` AFTER INSERT ON `extension_garantie` FOR EACH ROW BEGIN
    UPDATE garantie
    SET date_fin      = NEW.date_fin_etendue,
        est_etendue   = TRUE,
        date_extension = NEW.date_achat
    WHERE id_garantie = NEW.id_garantie;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `facture`
--

DROP TABLE IF EXISTS `facture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facture` (
  `id_facture` bigint NOT NULL AUTO_INCREMENT,
  `id_client` bigint DEFAULT NULL COMMENT 'V9: NULL pour ventes anonymes en magasin',
  `id_magasin` bigint NOT NULL,
  `id_employe` bigint DEFAULT NULL,
  `id_mode_paiement` bigint NOT NULL,
  `id_bon_achat` bigint DEFAULT NULL,
  `date_facture` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `montant_total` decimal(10,2) NOT NULL DEFAULT '0.00',
  `montant_remise` decimal(10,2) NOT NULL DEFAULT '0.00',
  `montant_final` decimal(10,2) NOT NULL DEFAULT '0.00',
  `nom_client` varchar(100) DEFAULT NULL COMMENT 'V9: Pour les ventes anonymes en magasin',
  `email_client` varchar(150) DEFAULT NULL COMMENT 'V9: Pour les retours/SAV',
  `telephone_client` varchar(20) DEFAULT NULL COMMENT 'V9: Pour les retours/SAV',
  `contexte_vente` enum('EN_LIGNE','EN_MAGASIN') NOT NULL DEFAULT 'EN_MAGASIN' COMMENT 'V9: EN_LIGNE = client obligatoire, EN_MAGASIN = anonyme possible',
  PRIMARY KEY (`id_facture`),
  KEY `fk_facture_mode_paiement` (`id_mode_paiement`),
  KEY `fk_facture_bon_achat` (`id_bon_achat`),
  KEY `idx_facture_client` (`id_client`),
  KEY `idx_facture_employe` (`id_employe`),
  KEY `idx_facture_date` (`date_facture`),
  KEY `idx_facture_magasin` (`id_magasin`),
  KEY `idx_facture_date_magasin` (`date_facture`,`id_magasin`),
  KEY `idx_facture_contexte` (`contexte_vente`),
  CONSTRAINT `fk_facture_bon_achat` FOREIGN KEY (`id_bon_achat`) REFERENCES `bon_achat` (`id_bon_achat`),
  CONSTRAINT `fk_facture_client` FOREIGN KEY (`id_client`) REFERENCES `client` (`id_client`),
  CONSTRAINT `fk_facture_employe` FOREIGN KEY (`id_employe`) REFERENCES `employe` (`id_employe`),
  CONSTRAINT `fk_facture_magasin` FOREIGN KEY (`id_magasin`) REFERENCES `magasin` (`id_magasin`),
  CONSTRAINT `fk_facture_mode_paiement` FOREIGN KEY (`id_mode_paiement`) REFERENCES `mode_paiement` (`id_mode_paiement`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facture`
--

LOCK TABLES `facture` WRITE;
/*!40000 ALTER TABLE `facture` DISABLE KEYS */;
/*!40000 ALTER TABLE `facture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garantie`
--

DROP TABLE IF EXISTS `garantie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garantie` (
  `id_garantie` bigint NOT NULL AUTO_INCREMENT,
  `id_produit` bigint NOT NULL,
  `id_facture` bigint NOT NULL,
  `numero_serie` varchar(100) NOT NULL,
  `date_debut` date NOT NULL,
  `date_fin` date NOT NULL,
  `est_etendue` tinyint(1) NOT NULL DEFAULT '0',
  `date_extension` date DEFAULT NULL,
  PRIMARY KEY (`id_garantie`),
  UNIQUE KEY `numero_serie` (`numero_serie`),
  KEY `idx_garantie_numero_serie` (`numero_serie`),
  KEY `idx_garantie_produit` (`id_produit`),
  KEY `idx_garantie_facture` (`id_facture`),
  KEY `idx_garantie_date_fin` (`date_fin`),
  CONSTRAINT `fk_garantie_facture` FOREIGN KEY (`id_facture`) REFERENCES `facture` (`id_facture`),
  CONSTRAINT `fk_garantie_produit` FOREIGN KEY (`id_produit`) REFERENCES `produit` (`id_produit`),
  CONSTRAINT `chk_dates_garantie` CHECK ((`date_fin` > `date_debut`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garantie`
--

LOCK TABLES `garantie` WRITE;
/*!40000 ALTER TABLE `garantie` DISABLE KEYS */;
/*!40000 ALTER TABLE `garantie` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_garantie_numero_serie_unique` BEFORE INSERT ON `garantie` FOR EACH ROW BEGIN
    IF EXISTS (SELECT 1 FROM garantie WHERE numero_serie = NEW.numero_serie) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ce numero de serie est deja enregistre';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `historique_points`
--

DROP TABLE IF EXISTS `historique_points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historique_points` (
  `id_historique` bigint NOT NULL AUTO_INCREMENT,
  `id_client` bigint NOT NULL,
  `id_facture` bigint DEFAULT NULL,
  `type_operation` varchar(20) NOT NULL,
  `points` int NOT NULL,
  `commentaire` varchar(255) DEFAULT NULL,
  `date_operation` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_historique`),
  KEY `fk_histo_facture` (`id_facture`),
  KEY `idx_histo_points_client` (`id_client`),
  KEY `idx_histo_points_date` (`date_operation`),
  CONSTRAINT `fk_histo_client` FOREIGN KEY (`id_client`) REFERENCES `client` (`id_client`),
  CONSTRAINT `fk_histo_facture` FOREIGN KEY (`id_facture`) REFERENCES `facture` (`id_facture`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historique_points`
--

LOCK TABLES `historique_points` WRITE;
/*!40000 ALTER TABLE `historique_points` DISABLE KEYS */;
/*!40000 ALTER TABLE `historique_points` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ligne_facture`
--

DROP TABLE IF EXISTS `ligne_facture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ligne_facture` (
  `id_ligne` bigint NOT NULL AUTO_INCREMENT,
  `id_facture` bigint NOT NULL,
  `id_produit` bigint NOT NULL,
  `id_prix` bigint NOT NULL,
  `quantite` int NOT NULL,
  `prix_unitaire` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_ligne`),
  KEY `fk_lf_prix` (`id_prix`),
  KEY `idx_ligne_facture_facture` (`id_facture`),
  KEY `idx_ligne_facture_produit` (`id_produit`),
  CONSTRAINT `fk_lf_facture` FOREIGN KEY (`id_facture`) REFERENCES `facture` (`id_facture`) ON DELETE CASCADE,
  CONSTRAINT `fk_lf_prix` FOREIGN KEY (`id_prix`) REFERENCES `produit_prix` (`id_prix`),
  CONSTRAINT `fk_lf_produit` FOREIGN KEY (`id_produit`) REFERENCES `produit` (`id_produit`),
  CONSTRAINT `chk_lf_quantite_positive` CHECK ((`quantite` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ligne_facture`
--

LOCK TABLES `ligne_facture` WRITE;
/*!40000 ALTER TABLE `ligne_facture` DISABLE KEYS */;
/*!40000 ALTER TABLE `ligne_facture` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_after_insert_ligne_facture` AFTER INSERT ON `ligne_facture` FOR EACH ROW BEGIN
    DECLARE v_stock_actuel  INT;
    DECLARE v_id_magasin    BIGINT;
    DECLARE v_id_mvt_sortie BIGINT;
    SELECT id_magasin INTO v_id_magasin FROM facture WHERE id_facture = NEW.id_facture;
    SELECT quantite INTO v_stock_actuel FROM stockage
        WHERE id_produit = NEW.id_produit AND id_magasin = v_id_magasin;
    IF v_stock_actuel IS NULL OR v_stock_actuel < NEW.quantite THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Stock insuffisant pour effectuer cette vente';
    END IF;
    UPDATE stockage SET quantite = quantite - NEW.quantite
        WHERE id_produit = NEW.id_produit AND id_magasin = v_id_magasin;
    SELECT id_type_mouvement INTO v_id_mvt_sortie FROM type_mouvement WHERE code = 'SORTIE' LIMIT 1;
    INSERT INTO mouvement_stock (id_produit, id_magasin, id_type_mouvement, quantite, commentaire)
        VALUES (NEW.id_produit, v_id_magasin, v_id_mvt_sortie, NEW.quantite,
                CONCAT('Vente - Facture ID ', NEW.id_facture));
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_update_total_after_insert` AFTER INSERT ON `ligne_facture` FOR EACH ROW BEGIN
    UPDATE facture
    SET montant_total = (SELECT IFNULL(SUM(quantite * prix_unitaire), 0) FROM ligne_facture WHERE id_facture = NEW.id_facture),
        montant_final = (SELECT IFNULL(SUM(quantite * prix_unitaire), 0) FROM ligne_facture WHERE id_facture = NEW.id_facture) - montant_remise
    WHERE id_facture = NEW.id_facture;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_update_total_after_delete` AFTER DELETE ON `ligne_facture` FOR EACH ROW BEGIN
    UPDATE facture
    SET montant_total = (SELECT IFNULL(SUM(quantite * prix_unitaire), 0) FROM ligne_facture WHERE id_facture = OLD.id_facture),
        montant_final = (SELECT IFNULL(SUM(quantite * prix_unitaire), 0) FROM ligne_facture WHERE id_facture = OLD.id_facture) - montant_remise
    WHERE id_facture = OLD.id_facture;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `ligne_panier`
--

DROP TABLE IF EXISTS `ligne_panier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ligne_panier` (
  `id_ligne_panier` bigint NOT NULL AUTO_INCREMENT,
  `id_panier` bigint NOT NULL,
  `id_produit` bigint NOT NULL,
  `id_prix` bigint NOT NULL,
  `quantite` int NOT NULL,
  `prix_unitaire` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_ligne_panier`),
  KEY `fk_lp_panier` (`id_panier`),
  KEY `fk_lp_produit` (`id_produit`),
  KEY `fk_lp_prix` (`id_prix`),
  CONSTRAINT `fk_lp_panier` FOREIGN KEY (`id_panier`) REFERENCES `panier` (`id_panier`) ON DELETE CASCADE,
  CONSTRAINT `fk_lp_prix` FOREIGN KEY (`id_prix`) REFERENCES `produit_prix` (`id_prix`),
  CONSTRAINT `fk_lp_produit` FOREIGN KEY (`id_produit`) REFERENCES `produit` (`id_produit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ligne_panier`
--

LOCK TABLES `ligne_panier` WRITE;
/*!40000 ALTER TABLE `ligne_panier` DISABLE KEYS */;
/*!40000 ALTER TABLE `ligne_panier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `magasin`
--

DROP TABLE IF EXISTS `magasin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magasin` (
  `id_magasin` bigint NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `date_creation` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_modification` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_magasin`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magasin`
--

LOCK TABLES `magasin` WRITE;
/*!40000 ALTER TABLE `magasin` DISABLE KEYS */;
INSERT INTO `magasin` VALUES (1,'Micromania-Zing Moulins-lès-Metz','0387152750','moulins-les-metz@micromania.fr','2026-03-05 10:35:33','2026-03-05 10:35:33'),(2,'Micromania-Zing Moulins-lès-Metz','0387152750','moulins-les-metz@micromania.fr','2026-03-05 10:46:10','2026-03-05 10:46:10'),(3,'Micromania-Zing Moulins-lès-Metz','0387152750','moulins-les-metz@micromania.fr','2026-03-05 10:48:04','2026-03-05 10:48:04'),(4,'Micromania-Zing Moulins-lès-Metz','0387152750','moulins-les-metz@micromania.fr','2026-03-05 10:50:17','2026-03-05 10:50:17');
/*!40000 ALTER TABLE `magasin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mode_paiement`
--

DROP TABLE IF EXISTS `mode_paiement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mode_paiement` (
  `id_mode_paiement` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  PRIMARY KEY (`id_mode_paiement`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mode_paiement`
--

LOCK TABLES `mode_paiement` WRITE;
/*!40000 ALTER TABLE `mode_paiement` DISABLE KEYS */;
INSERT INTO `mode_paiement` VALUES (1,'CB'),(2,'ESPECES'),(3,'PAYPAL');
/*!40000 ALTER TABLE `mode_paiement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mouvement_stock`
--

DROP TABLE IF EXISTS `mouvement_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mouvement_stock` (
  `id_mouvement` bigint NOT NULL AUTO_INCREMENT,
  `id_produit` bigint NOT NULL,
  `id_magasin` bigint NOT NULL,
  `id_type_mouvement` bigint NOT NULL,
  `quantite` int NOT NULL,
  `date_mouvement` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `commentaire` text,
  PRIMARY KEY (`id_mouvement`),
  KEY `fk_mvt_type_mouvement` (`id_type_mouvement`),
  KEY `idx_mouvement_produit` (`id_produit`),
  KEY `idx_mouvement_magasin` (`id_magasin`),
  KEY `idx_mouvement_date` (`date_mouvement`),
  CONSTRAINT `fk_mvt_magasin` FOREIGN KEY (`id_magasin`) REFERENCES `magasin` (`id_magasin`),
  CONSTRAINT `fk_mvt_produit` FOREIGN KEY (`id_produit`) REFERENCES `produit` (`id_produit`),
  CONSTRAINT `fk_mvt_type_mouvement` FOREIGN KEY (`id_type_mouvement`) REFERENCES `type_mouvement` (`id_type_mouvement`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mouvement_stock`
--

LOCK TABLES `mouvement_stock` WRITE;
/*!40000 ALTER TABLE `mouvement_stock` DISABLE KEYS */;
/*!40000 ALTER TABLE `mouvement_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `panier`
--

DROP TABLE IF EXISTS `panier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `panier` (
  `id_panier` bigint NOT NULL AUTO_INCREMENT,
  `id_client` bigint NOT NULL,
  `id_statut_panier` bigint NOT NULL,
  `date_creation` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_panier`),
  KEY `fk_panier_statut_panier` (`id_statut_panier`),
  KEY `idx_panier_client` (`id_client`),
  CONSTRAINT `fk_panier_client` FOREIGN KEY (`id_client`) REFERENCES `client` (`id_client`) ON DELETE CASCADE,
  CONSTRAINT `fk_panier_statut_panier` FOREIGN KEY (`id_statut_panier`) REFERENCES `statut_panier` (`id_statut_panier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `panier`
--

LOCK TABLES `panier` WRITE;
/*!40000 ALTER TABLE `panier` DISABLE KEYS */;
/*!40000 ALTER TABLE `panier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `planning_employe`
--

DROP TABLE IF EXISTS `planning_employe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `planning_employe` (
  `id_planning` bigint NOT NULL AUTO_INCREMENT,
  `id_employe` bigint NOT NULL,
  `id_statut_planning` bigint NOT NULL,
  `date_travail` date NOT NULL,
  `heure_debut` time NOT NULL,
  `heure_fin` time NOT NULL,
  PRIMARY KEY (`id_planning`),
  KEY `fk_planning_statut_planning` (`id_statut_planning`),
  KEY `idx_planning_employe` (`id_employe`),
  KEY `idx_planning_date` (`date_travail`),
  CONSTRAINT `fk_planning_employe` FOREIGN KEY (`id_employe`) REFERENCES `employe` (`id_employe`) ON DELETE CASCADE,
  CONSTRAINT `fk_planning_statut_planning` FOREIGN KEY (`id_statut_planning`) REFERENCES `statut_planning` (`id_statut_planning`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `planning_employe`
--

LOCK TABLES `planning_employe` WRITE;
/*!40000 ALTER TABLE `planning_employe` DISABLE KEYS */;
/*!40000 ALTER TABLE `planning_employe` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_check_planning` BEFORE INSERT ON `planning_employe` FOR EACH ROW BEGIN
    IF NEW.heure_fin <= NEW.heure_debut THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'L heure de fin doit etre strictement superieure a l heure de debut';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `points_fidelite`
--

DROP TABLE IF EXISTS `points_fidelite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `points_fidelite` (
  `id_points_fidelite` bigint NOT NULL AUTO_INCREMENT,
  `id_client` bigint NOT NULL,
  `solde_points` int NOT NULL DEFAULT '0',
  `total_achats_annuel` decimal(10,2) NOT NULL DEFAULT '0.00',
  `date_debut_periode` date NOT NULL,
  `date_modification` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_points_fidelite`),
  UNIQUE KEY `id_client` (`id_client`),
  CONSTRAINT `fk_points_client` FOREIGN KEY (`id_client`) REFERENCES `client` (`id_client`) ON DELETE CASCADE,
  CONSTRAINT `chk_achats_positif` CHECK ((`total_achats_annuel` >= 0)),
  CONSTRAINT `chk_solde_positif` CHECK ((`solde_points` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `points_fidelite`
--

LOCK TABLES `points_fidelite` WRITE;
/*!40000 ALTER TABLE `points_fidelite` DISABLE KEYS */;
/*!40000 ALTER TABLE `points_fidelite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produit`
--

DROP TABLE IF EXISTS `produit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produit` (
  `id_produit` bigint NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `description` text,
  `id_categorie` bigint NOT NULL,
  `date_sortie` date DEFAULT NULL,
  `editeur` varchar(150) DEFAULT NULL,
  `constructeur` varchar(150) DEFAULT NULL,
  `pegi` int DEFAULT NULL,
  `plateforme` varchar(100) DEFAULT NULL,
  `actif` tinyint(1) NOT NULL DEFAULT '1',
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `niveau_acces_min` varchar(20) NOT NULL DEFAULT 'NORMAL',
  `langue` varchar(10) NOT NULL DEFAULT 'fr',
  `necessite_numero_serie` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'V9: Si TRUE, numéro de série obligatoire pour garantie',
  `date_creation` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_modification` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_produit`),
  KEY `idx_produit_nom` (`nom`),
  KEY `idx_produit_categorie` (`id_categorie`),
  KEY `idx_produit_deleted` (`deleted`),
  KEY `idx_produit_categorie_actif` (`id_categorie`,`actif`),
  KEY `idx_produit_niveau_acces` (`niveau_acces_min`),
  KEY `idx_produit_langue` (`langue`),
  KEY `idx_produit_numero_serie` (`necessite_numero_serie`),
  CONSTRAINT `fk_produit_categorie` FOREIGN KEY (`id_categorie`) REFERENCES `categorie` (`id_categorie`),
  CONSTRAINT `chk_niveau_acces_min` CHECK ((`niveau_acces_min` in (_utf8mb4'NORMAL',_utf8mb4'PREMIUM',_utf8mb4'ULTIMATE'))),
  CONSTRAINT `chk_pegi` CHECK ((`pegi` between 3 and 18))
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produit`
--

LOCK TABLES `produit` WRITE;
/*!40000 ALTER TABLE `produit` DISABLE KEYS */;
INSERT INTO `produit` VALUES (1,'Marvel Spider-Man 2','Aventure de Peter Parker et Miles Morales dans New York.',1,NULL,'Sony Interactive Entertainment',NULL,18,'PS5',1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(2,'God of War Ragnarok','Kratos et Atreus affrontent le Ragnarok.',1,NULL,'Sony Interactive Entertainment',NULL,18,'PS5',1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(3,'Final Fantasy VII Rebirth','La suite de Remake. Cloud quitte Midgar dans une aventure epique.',1,NULL,'Square Enix',NULL,16,'PS5',1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(4,'Helldivers 2','TPS cooperatif 4 joueurs. Defendez la Super-Terre.',1,NULL,'Sony Interactive Entertainment',NULL,18,'PS5',1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(5,'Stellar Blade','Action RPG exclusif PS5. Eve combat pour liberer la Terre.',1,NULL,'Sony Interactive Entertainment',NULL,18,'PS5',1,0,'PREMIUM','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(6,'Forza Horizon 5','Course en monde ouvert au Mexique. Plus de 500 voitures.',2,NULL,'Microsoft',NULL,3,'Xbox Series',1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(7,'Halo Infinite','Master Chief est de retour. FPS emblematique Xbox.',2,NULL,'Microsoft',NULL,16,'Xbox Series',1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(8,'Zelda Tears of the Kingdom','Link explore Hyrule et les iles celestes.',3,NULL,'Nintendo',NULL,12,'Nintendo Switch',1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(9,'Mario Kart 8 Deluxe','Jeu de course Nintendo. 48 circuits, multijoueur.',3,NULL,'Nintendo',NULL,3,'Nintendo Switch',1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(10,'Pokemon Ecarlate','Explorez la region de Paldea en monde ouvert.',3,NULL,'Nintendo',NULL,7,'Nintendo Switch',1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(11,'Super Mario Bros Wonder','Mario 2D revolutionnaire avec les Fleurs Merveille.',3,NULL,'Nintendo',NULL,3,'Nintendo Switch',1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(12,'Baldurs Gate 3','RPG ultime D&D 5e. Liberte totale, co-op 4 joueurs.',4,NULL,'Larian Studios',NULL,18,'PC',1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(13,'Cyberpunk 2077 Phantom Liberty','RPG futuriste dans Night City. Inclut extension.',4,NULL,'CD Projekt Red',NULL,18,'PC',1,0,'ULTIMATE','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(14,'PlayStation 5 Standard','SSD ultra-rapide, DualSense, ray-tracing natif.',5,NULL,'Sony Interactive Entertainment',NULL,NULL,'PS5',1,0,'NORMAL','fr',1,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(15,'Xbox Series X','4K, 120fps, Game Pass compatible.',5,NULL,'Microsoft',NULL,NULL,'Xbox Series',1,0,'NORMAL','fr',1,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(16,'Nintendo Switch OLED','Ecran OLED 7 pouces. Mode portable et TV.',5,NULL,'Nintendo',NULL,NULL,'Nintendo Switch',1,0,'NORMAL','fr',1,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(17,'Manette DualSense Blanc','Retour haptique et gachettes adaptatives.',6,NULL,'Sony Interactive Entertainment',NULL,NULL,'PS5',1,0,'NORMAL','fr',1,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(18,'Manette DualSense Noir','Version noire de la DualSense.',6,NULL,'Sony Interactive Entertainment',NULL,NULL,'PS5',1,0,'NORMAL','fr',1,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(19,'Casque Pulse 3D PS5','Audio 3D Tempest, sans fil, officiel Sony.',6,NULL,'Sony Interactive Entertainment',NULL,NULL,'PS5',1,0,'NORMAL','fr',1,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(20,'Joy-Con Switch Bleu Rouge','Paire officielle Nintendo. Gyroscope, NFC.',6,NULL,'Nintendo',NULL,NULL,'Nintendo Switch',1,0,'NORMAL','fr',1,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(21,'Figurine Amiibo Link','Figurine NFC compatible Zelda Tears of the Kingdom.',7,NULL,'Nintendo',NULL,NULL,NULL,1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(22,'Steelbook God of War','Boitier collector edition limitee.',7,NULL,'Sony Interactive Entertainment',NULL,NULL,NULL,1,0,'NORMAL','fr',0,'2026-03-05 10:48:04','2026-03-05 10:48:04'),(23,'Marvel Spider-Man 2','Aventure de Peter Parker et Miles Morales dans New York.',1,NULL,'Sony Interactive Entertainment',NULL,18,'PS5',1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(24,'God of War Ragnarok','Kratos et Atreus affrontent le Ragnarok.',1,NULL,'Sony Interactive Entertainment',NULL,18,'PS5',1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(25,'Final Fantasy VII Rebirth','La suite de Remake. Cloud quitte Midgar dans une aventure epique.',1,NULL,'Square Enix',NULL,16,'PS5',1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(26,'Helldivers 2','TPS cooperatif 4 joueurs. Defendez la Super-Terre.',1,NULL,'Sony Interactive Entertainment',NULL,18,'PS5',1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(27,'Stellar Blade','Action RPG exclusif PS5. Eve combat pour liberer la Terre.',1,NULL,'Sony Interactive Entertainment',NULL,18,'PS5',1,0,'PREMIUM','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(28,'Forza Horizon 5','Course en monde ouvert au Mexique. Plus de 500 voitures.',2,NULL,'Microsoft',NULL,3,'Xbox Series',1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(29,'Halo Infinite','Master Chief est de retour. FPS emblematique Xbox.',2,NULL,'Microsoft',NULL,16,'Xbox Series',1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(30,'Zelda Tears of the Kingdom','Link explore Hyrule et les iles celestes.',3,NULL,'Nintendo',NULL,12,'Nintendo Switch',1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(31,'Mario Kart 8 Deluxe','Jeu de course Nintendo. 48 circuits, multijoueur.',3,NULL,'Nintendo',NULL,3,'Nintendo Switch',1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(32,'Pokemon Ecarlate','Explorez la region de Paldea en monde ouvert.',3,NULL,'Nintendo',NULL,7,'Nintendo Switch',1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(33,'Super Mario Bros Wonder','Mario 2D revolutionnaire avec les Fleurs Merveille.',3,NULL,'Nintendo',NULL,3,'Nintendo Switch',1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(34,'Baldurs Gate 3','RPG ultime D&D 5e. Liberte totale, co-op 4 joueurs.',4,NULL,'Larian Studios',NULL,18,'PC',1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(35,'Cyberpunk 2077 Phantom Liberty','RPG futuriste dans Night City. Inclut extension.',4,NULL,'CD Projekt Red',NULL,18,'PC',1,0,'ULTIMATE','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(36,'PlayStation 5 Standard','SSD ultra-rapide, DualSense, ray-tracing natif.',5,NULL,'Sony Interactive Entertainment',NULL,NULL,'PS5',1,0,'NORMAL','fr',1,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(37,'Xbox Series X','4K, 120fps, Game Pass compatible.',5,NULL,'Microsoft',NULL,NULL,'Xbox Series',1,0,'NORMAL','fr',1,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(38,'Nintendo Switch OLED','Ecran OLED 7 pouces. Mode portable et TV.',5,NULL,'Nintendo',NULL,NULL,'Nintendo Switch',1,0,'NORMAL','fr',1,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(39,'Manette DualSense Blanc','Retour haptique et gachettes adaptatives.',6,NULL,'Sony Interactive Entertainment',NULL,NULL,'PS5',1,0,'NORMAL','fr',1,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(40,'Manette DualSense Noir','Version noire de la DualSense.',6,NULL,'Sony Interactive Entertainment',NULL,NULL,'PS5',1,0,'NORMAL','fr',1,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(41,'Casque Pulse 3D PS5','Audio 3D Tempest, sans fil, officiel Sony.',6,NULL,'Sony Interactive Entertainment',NULL,NULL,'PS5',1,0,'NORMAL','fr',1,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(42,'Joy-Con Switch Bleu Rouge','Paire officielle Nintendo. Gyroscope, NFC.',6,NULL,'Nintendo',NULL,NULL,'Nintendo Switch',1,0,'NORMAL','fr',1,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(43,'Figurine Amiibo Link','Figurine NFC compatible Zelda Tears of the Kingdom.',7,NULL,'Nintendo',NULL,NULL,NULL,1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18'),(44,'Steelbook God of War','Boitier collector edition limitee.',7,NULL,'Sony Interactive Entertainment',NULL,NULL,NULL,1,0,'NORMAL','fr',0,'2026-03-05 10:50:18','2026-03-05 10:50:18');
/*!40000 ALTER TABLE `produit` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_produit_numero_serie_auto` BEFORE INSERT ON `produit` FOR EACH ROW BEGIN
    DECLARE v_type_code VARCHAR(50);
    SELECT tc.code INTO v_type_code
    FROM categorie c
    JOIN type_categorie tc ON tc.id_type_categorie = c.id_type_categorie
    WHERE c.id_categorie = NEW.id_categorie;
    IF v_type_code IN ('CONSOLE', 'ACCESSOIRE') THEN
        SET NEW.necessite_numero_serie = TRUE;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `produit_image`
--

DROP TABLE IF EXISTS `produit_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produit_image` (
  `id_image` bigint NOT NULL AUTO_INCREMENT,
  `id_produit` bigint NOT NULL,
  `url` varchar(255) NOT NULL,
  `alt` varchar(255) NOT NULL DEFAULT '',
  `decorative` tinyint(1) NOT NULL DEFAULT '0',
  `principale` tinyint(1) NOT NULL DEFAULT '0',
  `ordre` int NOT NULL DEFAULT '0',
  `date_creation` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_image`),
  KEY `idx_image_produit` (`id_produit`),
  KEY `idx_image_principale` (`id_produit`,`principale`),
  KEY `idx_image_decorative` (`id_produit`,`decorative`),
  CONSTRAINT `fk_image_produit` FOREIGN KEY (`id_produit`) REFERENCES `produit` (`id_produit`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produit_image`
--

LOCK TABLES `produit_image` WRITE;
/*!40000 ALTER TABLE `produit_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `produit_image` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_image_principale_before_insert` BEFORE INSERT ON `produit_image` FOR EACH ROW BEGIN
    IF NEW.principale = TRUE THEN
        UPDATE produit_image SET principale = FALSE WHERE id_produit = NEW.id_produit;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_image_principale_before_update` BEFORE UPDATE ON `produit_image` FOR EACH ROW BEGIN
    IF NEW.principale = TRUE AND OLD.principale = FALSE THEN
        UPDATE produit_image SET principale = FALSE
        WHERE id_produit = NEW.id_produit AND id_image != NEW.id_image;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `produit_prix`
--

DROP TABLE IF EXISTS `produit_prix`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produit_prix` (
  `id_prix` bigint NOT NULL AUTO_INCREMENT,
  `id_produit` bigint NOT NULL,
  `id_statut_produit` bigint NOT NULL,
  `prix` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_prix`),
  KEY `fk_produit_prix_produit` (`id_produit`),
  KEY `fk_produit_prix_statut_produit` (`id_statut_produit`),
  CONSTRAINT `fk_produit_prix_produit` FOREIGN KEY (`id_produit`) REFERENCES `produit` (`id_produit`) ON DELETE CASCADE,
  CONSTRAINT `fk_produit_prix_statut_produit` FOREIGN KEY (`id_statut_produit`) REFERENCES `statut_produit` (`id_statut_produit`),
  CONSTRAINT `chk_prix_positif` CHECK ((`prix` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produit_prix`
--

LOCK TABLES `produit_prix` WRITE;
/*!40000 ALTER TABLE `produit_prix` DISABLE KEYS */;
INSERT INTO `produit_prix` VALUES (1,1,1,79.99),(2,2,1,69.99),(3,2,2,34.99),(4,3,1,79.99),(5,4,1,39.99),(6,5,1,79.99),(7,6,1,59.99),(8,7,1,49.99),(9,7,2,19.99),(10,8,1,59.99),(11,9,1,59.99),(12,10,1,59.99),(13,11,1,59.99),(14,12,1,59.99),(15,13,1,49.99),(16,14,1,549.99),(17,15,1,499.99),(18,16,1,349.99),(19,17,1,74.99),(20,18,1,74.99),(21,19,1,99.99),(22,20,1,79.99),(23,21,1,24.99),(24,22,1,19.99),(25,23,1,79.99),(26,24,1,69.99),(27,24,2,34.99),(28,25,1,79.99),(29,26,1,39.99),(30,27,1,79.99),(31,28,1,59.99),(32,29,1,49.99),(33,29,2,19.99),(34,30,1,59.99),(35,31,1,59.99),(36,32,1,59.99),(37,33,1,59.99),(38,34,1,59.99),(39,35,1,49.99),(40,36,1,549.99),(41,37,1,499.99),(42,38,1,349.99),(43,39,1,74.99),(44,40,1,74.99),(45,41,1,99.99),(46,42,1,79.99),(47,43,1,24.99),(48,44,1,19.99);
/*!40000 ALTER TABLE `produit_prix` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produit_video`
--

DROP TABLE IF EXISTS `produit_video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produit_video` (
  `id_video` bigint NOT NULL AUTO_INCREMENT,
  `id_produit` bigint NOT NULL,
  `url` varchar(255) NOT NULL,
  `titre` varchar(255) NOT NULL,
  `ordre` int NOT NULL DEFAULT '0',
  `langue` varchar(10) NOT NULL DEFAULT 'fr',
  `sous_titres_url` varchar(255) DEFAULT NULL,
  `audio_desc_url` varchar(255) DEFAULT NULL,
  `transcription` text,
  `date_creation` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_video`),
  KEY `idx_video_produit` (`id_produit`),
  CONSTRAINT `fk_video_produit` FOREIGN KEY (`id_produit`) REFERENCES `produit` (`id_produit`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produit_video`
--

LOCK TABLES `produit_video` WRITE;
/*!40000 ALTER TABLE `produit_video` DISABLE KEYS */;
/*!40000 ALTER TABLE `produit_video` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotion` (
  `id_promotion` bigint NOT NULL AUTO_INCREMENT,
  `code_promo` varchar(50) NOT NULL,
  `description` text,
  `id_type_reduction` bigint NOT NULL,
  `valeur` decimal(10,2) NOT NULL,
  `date_debut` datetime NOT NULL,
  `date_fin` datetime NOT NULL,
  `actif` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_promotion`),
  UNIQUE KEY `code_promo` (`code_promo`),
  KEY `fk_promotion_type_reduction` (`id_type_reduction`),
  CONSTRAINT `fk_promotion_type_reduction` FOREIGN KEY (`id_type_reduction`) REFERENCES `type_reduction` (`id_type_reduction`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion`
--

LOCK TABLES `promotion` WRITE;
/*!40000 ALTER TABLE `promotion` DISABLE KEYS */;
/*!40000 ALTER TABLE `promotion` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_check_promotion` BEFORE INSERT ON `promotion` FOR EACH ROW BEGIN
    IF NEW.date_fin <= NEW.date_debut THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La date de fin doit etre strictement superieure a la date de debut';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `ratio_points`
--

DROP TABLE IF EXISTS `ratio_points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ratio_points` (
  `id_ratio` bigint NOT NULL AUTO_INCREMENT,
  `id_type_categorie` bigint NOT NULL,
  `id_type_fidelite` bigint NOT NULL,
  `ratio` decimal(4,2) NOT NULL,
  PRIMARY KEY (`id_ratio`),
  UNIQUE KEY `uq_ratio` (`id_type_categorie`,`id_type_fidelite`),
  KEY `idx_ratio_categorie` (`id_type_categorie`),
  KEY `idx_ratio_fidelite` (`id_type_fidelite`),
  CONSTRAINT `fk_ratio_type_categorie` FOREIGN KEY (`id_type_categorie`) REFERENCES `type_categorie` (`id_type_categorie`),
  CONSTRAINT `fk_ratio_type_fidelite` FOREIGN KEY (`id_type_fidelite`) REFERENCES `type_fidelite` (`id_type_fidelite`),
  CONSTRAINT `chk_ratio_positif` CHECK ((`ratio` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ratio_points`
--

LOCK TABLES `ratio_points` WRITE;
/*!40000 ALTER TABLE `ratio_points` DISABLE KEYS */;
INSERT INTO `ratio_points` VALUES (1,1,1,1.00),(2,2,1,0.50),(3,3,1,0.80),(4,4,1,1.00),(5,1,2,1.20),(6,2,2,0.60),(7,3,2,1.00),(8,4,2,1.20),(9,1,3,1.50),(10,2,3,0.80),(11,3,3,1.20),(12,4,3,1.50);
/*!40000 ALTER TABLE `ratio_points` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `remember_me_token`
--

DROP TABLE IF EXISTS `remember_me_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `remember_me_token` (
  `serie` varchar(64) NOT NULL,
  `token_value` varchar(64) NOT NULL,
  `date_derniere` datetime NOT NULL,
  `username` varchar(150) NOT NULL,
  `user_type` varchar(10) NOT NULL DEFAULT 'CLIENT',
  PRIMARY KEY (`serie`),
  KEY `idx_remember_me_username` (`username`),
  KEY `idx_remember_me_user_type` (`user_type`),
  CONSTRAINT `chk_user_type` CHECK ((`user_type` in (_utf8mb4'CLIENT',_utf8mb4'EMPLOYE')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `remember_me_token`
--

LOCK TABLES `remember_me_token` WRITE;
/*!40000 ALTER TABLE `remember_me_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `remember_me_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id_role` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  PRIMARY KEY (`id_role`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (3,'ADMIN'),(2,'MANAGER'),(1,'VENDEUR');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statut_abonnement`
--

DROP TABLE IF EXISTS `statut_abonnement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statut_abonnement` (
  `id_statut_abonnement` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  PRIMARY KEY (`id_statut_abonnement`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statut_abonnement`
--

LOCK TABLES `statut_abonnement` WRITE;
/*!40000 ALTER TABLE `statut_abonnement` DISABLE KEYS */;
INSERT INTO `statut_abonnement` VALUES (1,'ACTIF'),(4,'EN_ATTENTE'),(2,'EXPIRE'),(3,'RESILIE');
/*!40000 ALTER TABLE `statut_abonnement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statut_panier`
--

DROP TABLE IF EXISTS `statut_panier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statut_panier` (
  `id_statut_panier` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  PRIMARY KEY (`id_statut_panier`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statut_panier`
--

LOCK TABLES `statut_panier` WRITE;
/*!40000 ALTER TABLE `statut_panier` DISABLE KEYS */;
INSERT INTO `statut_panier` VALUES (3,'ABANDONNE'),(1,'ACTIF'),(2,'VALIDE');
/*!40000 ALTER TABLE `statut_panier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statut_planning`
--

DROP TABLE IF EXISTS `statut_planning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statut_planning` (
  `id_statut_planning` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  PRIMARY KEY (`id_statut_planning`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statut_planning`
--

LOCK TABLES `statut_planning` WRITE;
/*!40000 ALTER TABLE `statut_planning` DISABLE KEYS */;
INSERT INTO `statut_planning` VALUES (3,'ABSENT'),(4,'CONGE'),(2,'PRESENT'),(1,'PREVU');
/*!40000 ALTER TABLE `statut_planning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statut_produit`
--

DROP TABLE IF EXISTS `statut_produit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `statut_produit` (
  `id_statut_produit` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  PRIMARY KEY (`id_statut_produit`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statut_produit`
--

LOCK TABLES `statut_produit` WRITE;
/*!40000 ALTER TABLE `statut_produit` DISABLE KEYS */;
INSERT INTO `statut_produit` VALUES (3,'LOCATION'),(1,'NEUF'),(2,'OCCASION');
/*!40000 ALTER TABLE `statut_produit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stockage`
--

DROP TABLE IF EXISTS `stockage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stockage` (
  `id_stockage` bigint NOT NULL AUTO_INCREMENT,
  `id_produit` bigint NOT NULL,
  `id_magasin` bigint NOT NULL,
  `quantite` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_stockage`),
  UNIQUE KEY `uq_stockage_produit_magasin` (`id_produit`,`id_magasin`),
  KEY `idx_stockage_magasin` (`id_magasin`),
  KEY `idx_stockage_produit` (`id_produit`),
  CONSTRAINT `fk_stockage_magasin` FOREIGN KEY (`id_magasin`) REFERENCES `magasin` (`id_magasin`),
  CONSTRAINT `fk_stockage_produit` FOREIGN KEY (`id_produit`) REFERENCES `produit` (`id_produit`),
  CONSTRAINT `chk_stock_positive` CHECK ((`quantite` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=255 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stockage`
--

LOCK TABLES `stockage` WRITE;
/*!40000 ALTER TABLE `stockage` DISABLE KEYS */;
INSERT INTO `stockage` VALUES (1,1,3,8),(2,1,2,8),(3,1,1,8),(4,2,3,8),(5,2,2,8),(6,2,1,8),(7,3,3,8),(8,3,2,8),(9,3,1,8),(10,4,3,15),(11,4,2,15),(12,4,1,15),(13,5,3,8),(14,5,2,8),(15,5,1,8),(16,6,3,15),(17,6,2,15),(18,6,1,15),(19,7,3,15),(20,7,2,15),(21,7,1,15),(22,8,3,15),(23,8,2,15),(24,8,1,15),(25,9,3,15),(26,9,2,15),(27,9,1,15),(28,10,3,15),(29,10,2,15),(30,10,1,15),(31,11,3,15),(32,11,2,15),(33,11,1,15),(34,12,3,15),(35,12,2,15),(36,12,1,15),(37,13,3,15),(38,13,2,15),(39,13,1,15),(40,14,3,3),(41,14,2,3),(42,14,1,3),(43,15,3,3),(44,15,2,3),(45,15,1,3),(46,16,3,8),(47,16,2,8),(48,16,1,8),(49,17,3,8),(50,17,2,8),(51,17,1,8),(52,18,3,8),(53,18,2,8),(54,18,1,8),(55,19,3,8),(56,19,2,8),(57,19,1,8),(58,20,3,8),(59,20,2,8),(60,20,1,8),(61,21,3,15),(62,21,2,15),(63,21,1,15),(64,22,3,15),(65,22,2,15),(66,22,1,15),(128,1,4,8),(129,2,4,8),(130,3,4,8),(131,4,4,15),(132,5,4,8),(133,6,4,15),(134,7,4,15),(135,8,4,15),(136,9,4,15),(137,10,4,15),(138,11,4,15),(139,12,4,15),(140,13,4,15),(141,14,4,3),(142,15,4,3),(143,16,4,8),(144,17,4,8),(145,18,4,8),(146,19,4,8),(147,20,4,8),(148,21,4,15),(149,22,4,15),(150,23,4,8),(151,23,3,8),(152,23,2,8),(153,23,1,8),(154,24,4,8),(155,24,3,8),(156,24,2,8),(157,24,1,8),(158,25,4,8),(159,25,3,8),(160,25,2,8),(161,25,1,8),(162,26,4,15),(163,26,3,15),(164,26,2,15),(165,26,1,15),(166,27,4,8),(167,27,3,8),(168,27,2,8),(169,27,1,8),(170,28,4,15),(171,28,3,15),(172,28,2,15),(173,28,1,15),(174,29,4,15),(175,29,3,15),(176,29,2,15),(177,29,1,15),(178,30,4,15),(179,30,3,15),(180,30,2,15),(181,30,1,15),(182,31,4,15),(183,31,3,15),(184,31,2,15),(185,31,1,15),(186,32,4,15),(187,32,3,15),(188,32,2,15),(189,32,1,15),(190,33,4,15),(191,33,3,15),(192,33,2,15),(193,33,1,15),(194,34,4,15),(195,34,3,15),(196,34,2,15),(197,34,1,15),(198,35,4,15),(199,35,3,15),(200,35,2,15),(201,35,1,15),(202,36,4,3),(203,36,3,3),(204,36,2,3),(205,36,1,3),(206,37,4,3),(207,37,3,3),(208,37,2,3),(209,37,1,3),(210,38,4,8),(211,38,3,8),(212,38,2,8),(213,38,1,8),(214,39,4,8),(215,39,3,8),(216,39,2,8),(217,39,1,8),(218,40,4,8),(219,40,3,8),(220,40,2,8),(221,40,1,8),(222,41,4,8),(223,41,3,8),(224,41,2,8),(225,41,1,8),(226,42,4,8),(227,42,3,8),(228,42,2,8),(229,42,1,8),(230,43,4,15),(231,43,3,15),(232,43,2,15),(233,43,1,15),(234,44,4,15),(235,44,3,15),(236,44,2,15),(237,44,1,15);
/*!40000 ALTER TABLE `stockage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_adresse`
--

DROP TABLE IF EXISTS `type_adresse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_adresse` (
  `id_type_adresse` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  PRIMARY KEY (`id_type_adresse`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_adresse`
--

LOCK TABLES `type_adresse` WRITE;
/*!40000 ALTER TABLE `type_adresse` DISABLE KEYS */;
INSERT INTO `type_adresse` VALUES (2,'DOMICILE'),(1,'FACTURATION'),(3,'LIVRAISON');
/*!40000 ALTER TABLE `type_adresse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_categorie`
--

DROP TABLE IF EXISTS `type_categorie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_categorie` (
  `id_type_categorie` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_type_categorie`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_categorie`
--

LOCK TABLES `type_categorie` WRITE;
/*!40000 ALTER TABLE `type_categorie` DISABLE KEYS */;
INSERT INTO `type_categorie` VALUES (1,'JEU','Jeux vidéo'),(2,'CONSOLE','Consoles de jeu'),(3,'ACCESSOIRE','Accessoires gaming'),(4,'GOODIES','Produits dérivés et goodies');
/*!40000 ALTER TABLE `type_categorie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_fidelite`
--

DROP TABLE IF EXISTS `type_fidelite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_fidelite` (
  `id_type_fidelite` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `points_par_euro` decimal(4,2) NOT NULL DEFAULT '1.00',
  `seuil_upgrade_euro` decimal(10,2) DEFAULT NULL,
  `prix_abonnement` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id_type_fidelite`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_fidelite`
--

LOCK TABLES `type_fidelite` WRITE;
/*!40000 ALTER TABLE `type_fidelite` DISABLE KEYS */;
INSERT INTO `type_fidelite` VALUES (1,'NORMAL','Client standard',1.00,200.00,NULL),(2,'PREMIUM','Client premium',1.20,NULL,NULL),(3,'ULTIMATE','Abonnement Ultimate',1.50,NULL,9.99);
/*!40000 ALTER TABLE `type_fidelite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_garantie`
--

DROP TABLE IF EXISTS `type_garantie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_garantie` (
  `id_type_garantie` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `duree_mois` int NOT NULL COMMENT 'Durée en mois',
  `prix_extension` decimal(10,2) DEFAULT NULL COMMENT 'Prix pour extension',
  PRIMARY KEY (`id_type_garantie`),
  UNIQUE KEY `code` (`code`),
  CONSTRAINT `chk_duree_positive` CHECK ((`duree_mois` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_garantie`
--

LOCK TABLES `type_garantie` WRITE;
/*!40000 ALTER TABLE `type_garantie` DISABLE KEYS */;
INSERT INTO `type_garantie` VALUES (1,'STANDARD_CONSOLE','Garantie standard pour console',24,49.99),(2,'STANDARD_ACCESSOIRE','Garantie standard pour accessoire',12,19.99),(3,'ETENDUE_CONSOLE','Extension de garantie pour console',12,39.99),(4,'ETENDUE_ACCESSOIRE','Extension de garantie pour accessoire',12,9.99);
/*!40000 ALTER TABLE `type_garantie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_mouvement`
--

DROP TABLE IF EXISTS `type_mouvement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_mouvement` (
  `id_type_mouvement` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  PRIMARY KEY (`id_type_mouvement`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_mouvement`
--

LOCK TABLES `type_mouvement` WRITE;
/*!40000 ALTER TABLE `type_mouvement` DISABLE KEYS */;
INSERT INTO `type_mouvement` VALUES (1,'ENTREE'),(2,'SORTIE'),(3,'TRANSFERT');
/*!40000 ALTER TABLE `type_mouvement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_reduction`
--

DROP TABLE IF EXISTS `type_reduction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_reduction` (
  `id_type_reduction` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  PRIMARY KEY (`id_type_reduction`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_reduction`
--

LOCK TABLES `type_reduction` WRITE;
/*!40000 ALTER TABLE `type_reduction` DISABLE KEYS */;
INSERT INTO `type_reduction` VALUES (2,'MONTANT_FIXE'),(1,'POURCENTAGE');
/*!40000 ALTER TABLE `type_reduction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_abonnements_actifs`
--

DROP TABLE IF EXISTS `v_abonnements_actifs`;
/*!50001 DROP VIEW IF EXISTS `v_abonnements_actifs`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_abonnements_actifs` AS SELECT 
 1 AS `id_abonnement`,
 1 AS `pseudo`,
 1 AS `email`,
 1 AS `date_debut`,
 1 AS `date_fin`,
 1 AS `montant_paye`,
 1 AS `renouvellement_auto`,
 1 AS `jours_restants`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_avis_produit`
--

DROP TABLE IF EXISTS `v_avis_produit`;
/*!50001 DROP VIEW IF EXISTS `v_avis_produit`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_avis_produit` AS SELECT 
 1 AS `id_avis`,
 1 AS `note`,
 1 AS `commentaire`,
 1 AS `date_creation`,
 1 AS `auteur`,
 1 AS `produit`,
 1 AS `id_produit`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_bons_disponibles`
--

DROP TABLE IF EXISTS `v_bons_disponibles`;
/*!50001 DROP VIEW IF EXISTS `v_bons_disponibles`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_bons_disponibles` AS SELECT 
 1 AS `id_bon_achat`,
 1 AS `pseudo`,
 1 AS `code_bon`,
 1 AS `valeur`,
 1 AS `points_utilises`,
 1 AS `date_creation`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_client_actif`
--

DROP TABLE IF EXISTS `v_client_actif`;
/*!50001 DROP VIEW IF EXISTS `v_client_actif`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_client_actif` AS SELECT 
 1 AS `id_client`,
 1 AS `pseudo`,
 1 AS `nom`,
 1 AS `prenom`,
 1 AS `date_naissance`,
 1 AS `email`,
 1 AS `telephone`,
 1 AS `mot_de_passe`,
 1 AS `numero_carte_fidelite`,
 1 AS `id_type_fidelite`,
 1 AS `deleted`,
 1 AS `rgpd_consent`,
 1 AS `rgpd_consent_date`,
 1 AS `rgpd_consent_ip`,
 1 AS `demande_suppression`,
 1 AS `date_suppression`,
 1 AS `date_creation`,
 1 AS `date_modification`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_clients_a_supprimer`
--

DROP TABLE IF EXISTS `v_clients_a_supprimer`;
/*!50001 DROP VIEW IF EXISTS `v_clients_a_supprimer`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_clients_a_supprimer` AS SELECT 
 1 AS `id_client`,
 1 AS `pseudo`,
 1 AS `email`,
 1 AS `date_suppression`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_clients_sans_consentement`
--

DROP TABLE IF EXISTS `v_clients_sans_consentement`;
/*!50001 DROP VIEW IF EXISTS `v_clients_sans_consentement`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_clients_sans_consentement` AS SELECT 
 1 AS `id_client`,
 1 AS `pseudo`,
 1 AS `email`,
 1 AS `date_creation`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_employe_actif`
--

DROP TABLE IF EXISTS `v_employe_actif`;
/*!50001 DROP VIEW IF EXISTS `v_employe_actif`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_employe_actif` AS SELECT 
 1 AS `id_employe`,
 1 AS `nom`,
 1 AS `prenom`,
 1 AS `email`,
 1 AS `mot_de_passe`,
 1 AS `id_role`,
 1 AS `id_magasin`,
 1 AS `date_embauche`,
 1 AS `deleted`,
 1 AS `date_creation`,
 1 AS `date_modification`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_extensions_garantie`
--

DROP TABLE IF EXISTS `v_extensions_garantie`;
/*!50001 DROP VIEW IF EXISTS `v_extensions_garantie`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_extensions_garantie` AS SELECT 
 1 AS `id_extension`,
 1 AS `date_achat`,
 1 AS `date_fin_etendue`,
 1 AS `numero_serie`,
 1 AS `produit`,
 1 AS `type_extension`,
 1 AS `prix_extension`,
 1 AS `client`,
 1 AS `client_email`,
 1 AS `contexte_vente`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_facture_detail`
--

DROP TABLE IF EXISTS `v_facture_detail`;
/*!50001 DROP VIEW IF EXISTS `v_facture_detail`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_facture_detail` AS SELECT 
 1 AS `id_facture`,
 1 AS `date_facture`,
 1 AS `montant_total`,
 1 AS `montant_remise`,
 1 AS `montant_final`,
 1 AS `contexte_vente`,
 1 AS `client_pseudo`,
 1 AS `client_email`,
 1 AS `nom_client_affiche`,
 1 AS `email_client_affiche`,
 1 AS `telephone_client_affiche`,
 1 AS `magasin`,
 1 AS `mode_paiement`,
 1 AS `vendeur`,
 1 AS `id_employe`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_garanties_actives`
--

DROP TABLE IF EXISTS `v_garanties_actives`;
/*!50001 DROP VIEW IF EXISTS `v_garanties_actives`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_garanties_actives` AS SELECT 
 1 AS `id_garantie`,
 1 AS `numero_serie`,
 1 AS `date_debut`,
 1 AS `date_fin`,
 1 AS `est_etendue`,
 1 AS `produit`,
 1 AS `id_produit`,
 1 AS `client`,
 1 AS `client_email`,
 1 AS `id_facture`,
 1 AS `date_achat`,
 1 AS `contexte_vente`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_garanties_expirees`
--

DROP TABLE IF EXISTS `v_garanties_expirees`;
/*!50001 DROP VIEW IF EXISTS `v_garanties_expirees`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_garanties_expirees` AS SELECT 
 1 AS `id_garantie`,
 1 AS `numero_serie`,
 1 AS `date_debut`,
 1 AS `date_fin`,
 1 AS `produit`,
 1 AS `id_produit`,
 1 AS `id_facture`,
 1 AS `date_achat`,
 1 AS `contexte_vente`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_note_moyenne_produit`
--

DROP TABLE IF EXISTS `v_note_moyenne_produit`;
/*!50001 DROP VIEW IF EXISTS `v_note_moyenne_produit`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_note_moyenne_produit` AS SELECT 
 1 AS `id_produit`,
 1 AS `produit`,
 1 AS `nb_avis`,
 1 AS `note_moyenne`,
 1 AS `nb_5_etoiles`,
 1 AS `nb_4_etoiles`,
 1 AS `nb_3_etoiles`,
 1 AS `nb_2_etoiles`,
 1 AS `nb_1_etoile`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_points_client`
--

DROP TABLE IF EXISTS `v_points_client`;
/*!50001 DROP VIEW IF EXISTS `v_points_client`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_points_client` AS SELECT 
 1 AS `id_client`,
 1 AS `pseudo`,
 1 AS `nom`,
 1 AS `prenom`,
 1 AS `niveau_fidelite`,
 1 AS `solde_points`,
 1 AS `total_achats_annuel`,
 1 AS `date_debut_periode`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_produit_actif`
--

DROP TABLE IF EXISTS `v_produit_actif`;
/*!50001 DROP VIEW IF EXISTS `v_produit_actif`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_produit_actif` AS SELECT 
 1 AS `id_produit`,
 1 AS `nom`,
 1 AS `description`,
 1 AS `id_categorie`,
 1 AS `date_sortie`,
 1 AS `editeur`,
 1 AS `constructeur`,
 1 AS `pegi`,
 1 AS `plateforme`,
 1 AS `actif`,
 1 AS `deleted`,
 1 AS `niveau_acces_min`,
 1 AS `langue`,
 1 AS `necessite_numero_serie`,
 1 AS `date_creation`,
 1 AS `date_modification`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_produit_avec_acces`
--

DROP TABLE IF EXISTS `v_produit_avec_acces`;
/*!50001 DROP VIEW IF EXISTS `v_produit_avec_acces`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_produit_avec_acces` AS SELECT 
 1 AS `id_produit`,
 1 AS `nom`,
 1 AS `description`,
 1 AS `plateforme`,
 1 AS `pegi`,
 1 AS `actif`,
 1 AS `niveau_acces_min`,
 1 AS `langue_produit`,
 1 AS `necessite_numero_serie`,
 1 AS `categorie`,
 1 AS `type_categorie`,
 1 AS `prix_neuf`,
 1 AS `image_principale_url`,
 1 AS `image_principale_alt`,
 1 AS `image_decorative`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_produit_media`
--

DROP TABLE IF EXISTS `v_produit_media`;
/*!50001 DROP VIEW IF EXISTS `v_produit_media`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_produit_media` AS SELECT 
 1 AS `id_produit`,
 1 AS `produit`,
 1 AS `type_media`,
 1 AS `id_media`,
 1 AS `url`,
 1 AS `description_accessible`,
 1 AS `decorative`,
 1 AS `langue`,
 1 AS `sous_titres_url`,
 1 AS `audio_desc_url`,
 1 AS `transcription`,
 1 AS `ordre`,
 1 AS `principale`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_ratios_points`
--

DROP TABLE IF EXISTS `v_ratios_points`;
/*!50001 DROP VIEW IF EXISTS `v_ratios_points`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_ratios_points` AS SELECT 
 1 AS `type_categorie`,
 1 AS `niveau_fidelite`,
 1 AS `pts_par_euro`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_rgaa_audit_media`
--

DROP TABLE IF EXISTS `v_rgaa_audit_media`;
/*!50001 DROP VIEW IF EXISTS `v_rgaa_audit_media`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_rgaa_audit_media` AS SELECT 
 1 AS `id_produit`,
 1 AS `produit`,
 1 AS `nb_images`,
 1 AS `images_sans_alt`,
 1 AS `nb_videos`,
 1 AS `videos_sans_sous_titres`,
 1 AS `videos_sans_transcription`,
 1 AS `videos_sans_audio_desc`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_stock_magasin`
--

DROP TABLE IF EXISTS `v_stock_magasin`;
/*!50001 DROP VIEW IF EXISTS `v_stock_magasin`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_stock_magasin` AS SELECT 
 1 AS `id_stockage`,
 1 AS `magasin`,
 1 AS `produit`,
 1 AS `quantite`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping events for database 'micromania'
--

--
-- Dumping routines for database 'micromania'
--
/*!50003 DROP PROCEDURE IF EXISTS `sp_attribuer_points` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_attribuer_points`(IN p_id_facture BIGINT)
BEGIN
    DECLARE v_id_client         BIGINT;
    DECLARE v_code_niveau       VARCHAR(50);
    DECLARE v_seuil_upgrade     DECIMAL(10,2);
    DECLARE v_solde_actuel      INT;
    DECLARE v_nouveau_solde     INT;
    DECLARE v_total_annuel      DECIMAL(10,2);
    DECLARE v_nouveau_total     DECIMAL(10,2);
    DECLARE v_date_debut        DATE;
    DECLARE v_id_premium        BIGINT;
    DECLARE v_points_total      INT DEFAULT 0;
    DECLARE v_montant_final     DECIMAL(10,2);
    DECLARE v_prix_ligne        DECIMAL(10,2);
    DECLARE v_qte_ligne         INT;
    DECLARE v_id_type_categorie BIGINT;
    DECLARE v_id_type_fidelite  BIGINT;
    DECLARE v_ratio             DECIMAL(4,2);
    DECLARE v_points_ligne      INT;
    DECLARE v_done              BOOLEAN DEFAULT FALSE;

    DECLARE cur_lignes CURSOR FOR
        SELECT lf.prix_unitaire, lf.quantite, c.id_type_categorie
        FROM ligne_facture lf
        JOIN produit   p ON p.id_produit   = lf.id_produit
        JOIN categorie c ON c.id_categorie = p.id_categorie
        WHERE lf.id_facture = p_id_facture;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = TRUE;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;

    START TRANSACTION;

    SELECT f.id_client, f.montant_final INTO v_id_client, v_montant_final
    FROM facture f WHERE f.id_facture = p_id_facture;

    -- Facture anonyme : pas de points à attribuer
    IF v_id_client IS NULL THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Impossible d attribuer des points a une facture anonyme';
    END IF;

    SELECT tf.code, tf.seuil_upgrade_euro, c.id_type_fidelite
    INTO v_code_niveau, v_seuil_upgrade, v_id_type_fidelite
    FROM client c JOIN type_fidelite tf ON tf.id_type_fidelite = c.id_type_fidelite
    WHERE c.id_client = v_id_client;

    IF NOT EXISTS (SELECT 1 FROM points_fidelite WHERE id_client = v_id_client) THEN
        INSERT INTO points_fidelite (id_client, solde_points, total_achats_annuel, date_debut_periode)
        VALUES (v_id_client, 0, 0, CURDATE());
    END IF;

    SELECT solde_points, total_achats_annuel, date_debut_periode
    INTO v_solde_actuel, v_total_annuel, v_date_debut
    FROM points_fidelite WHERE id_client = v_id_client;

    IF DATEDIFF(CURDATE(), v_date_debut) >= 365 THEN
        SET v_total_annuel = 0;
        UPDATE points_fidelite
        SET total_achats_annuel = 0, date_debut_periode = CURDATE()
        WHERE id_client = v_id_client;
    END IF;

    -- Calcul ligne par ligne avec ratio catégorie × niveau fidélité
    OPEN cur_lignes;
    lignes_loop: LOOP
        FETCH cur_lignes INTO v_prix_ligne, v_qte_ligne, v_id_type_categorie;
        IF v_done THEN LEAVE lignes_loop; END IF;
        SELECT IFNULL(rp.ratio, tf.points_par_euro) INTO v_ratio
        FROM type_fidelite tf
        LEFT JOIN ratio_points rp ON rp.id_type_fidelite  = tf.id_type_fidelite
                                  AND rp.id_type_categorie = v_id_type_categorie
        WHERE tf.id_type_fidelite = v_id_type_fidelite;
        SET v_points_ligne = FLOOR(v_prix_ligne * v_qte_ligne * v_ratio);
        SET v_points_total = v_points_total + v_points_ligne;
    END LOOP;
    CLOSE cur_lignes;

    SET v_nouveau_solde = v_solde_actuel + v_points_total;
    SET v_nouveau_total = v_total_annuel + v_montant_final;

    UPDATE points_fidelite
    SET solde_points = v_nouveau_solde, total_achats_annuel = v_nouveau_total
    WHERE id_client = v_id_client;

    INSERT INTO historique_points (id_client, id_facture, type_operation, points, commentaire)
    VALUES (v_id_client, p_id_facture, 'GAIN', v_points_total,
            CONCAT('Achat facture #', p_id_facture, ' - ratio variable par categorie'));

    -- Upgrade NORMAL → PREMIUM si seuil annuel atteint
    IF v_code_niveau = 'NORMAL' AND v_seuil_upgrade IS NOT NULL AND v_nouveau_total >= v_seuil_upgrade THEN
        SELECT id_type_fidelite INTO v_id_premium FROM type_fidelite WHERE code = 'PREMIUM' LIMIT 1;
        UPDATE client SET id_type_fidelite = v_id_premium WHERE id_client = v_id_client;
        INSERT INTO historique_points (id_client, id_facture, type_operation, points, commentaire)
        VALUES (v_id_client, p_id_facture, 'UPGRADE', 0, 'Passage automatique au niveau PREMIUM');
    END IF;

    -- Génération automatique des bons d'achat
    IF v_nouveau_solde >= 8000 THEN
        UPDATE points_fidelite SET solde_points = solde_points - 8000 WHERE id_client = v_id_client;
        INSERT INTO bon_achat (id_client, code_bon, valeur, points_utilises)
        VALUES (v_id_client, CONCAT('BON-', v_id_client, '-', UNIX_TIMESTAMP()), 20.00, 8000);
        INSERT INTO historique_points (id_client, id_facture, type_operation, points, commentaire)
        VALUES (v_id_client, p_id_facture, 'UTILISATION', -8000, 'Generation bon achat 20 euros');
    ELSEIF v_nouveau_solde >= 2000 THEN
        UPDATE points_fidelite SET solde_points = solde_points - 2000 WHERE id_client = v_id_client;
        INSERT INTO bon_achat (id_client, code_bon, valeur, points_utilises)
        VALUES (v_id_client, CONCAT('BON-', v_id_client, '-', UNIX_TIMESTAMP()), 10.00, 2000);
        INSERT INTO historique_points (id_client, id_facture, type_operation, points, commentaire)
        VALUES (v_id_client, p_id_facture, 'UTILISATION', -2000, 'Generation bon achat 10 euros');
    END IF;

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_creer_facture` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_creer_facture`(
    IN p_id_client       BIGINT,
    IN p_id_magasin      BIGINT,
    IN p_id_employe      BIGINT,
    IN p_id_mode_paiement BIGINT,
    IN p_id_produit      BIGINT,
    IN p_id_prix         BIGINT,
    IN p_quantite        INT,
    IN p_prix            DECIMAL(10,2)
)
BEGIN
    DECLARE v_id_facture BIGINT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;
    START TRANSACTION;
    INSERT INTO facture (id_client, id_magasin, id_employe, id_mode_paiement,
                         montant_total, montant_remise, montant_final)
    VALUES (p_id_client, p_id_magasin, p_id_employe, p_id_mode_paiement, 0, 0, 0);
    SET v_id_facture = LAST_INSERT_ID();
    INSERT INTO ligne_facture (id_facture, id_produit, id_prix, quantite, prix_unitaire)
    VALUES (v_id_facture, p_id_produit, p_id_prix, p_quantite, p_prix);
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_enregistrer_garantie` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_enregistrer_garantie`(
    IN p_id_produit    BIGINT,
    IN p_id_facture    BIGINT,
    IN p_numero_serie  VARCHAR(100),
    IN p_duree_mois    INT
)
BEGIN
    DECLARE v_date_debut DATE DEFAULT CURDATE();
    DECLARE v_date_fin   DATE;
    SET v_date_fin = DATE_ADD(v_date_debut, INTERVAL p_duree_mois MONTH);
    INSERT INTO garantie (id_produit, id_facture, numero_serie, date_debut, date_fin)
    VALUES (p_id_produit, p_id_facture, p_numero_serie, v_date_debut, v_date_fin);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_etendre_garantie` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_etendre_garantie`(
    IN p_id_garantie      BIGINT,
    IN p_id_type_garantie BIGINT
)
BEGIN
    DECLARE v_duree_mois      INT;
    DECLARE v_date_fin_etendue DATE;
    SELECT duree_mois INTO v_duree_mois
    FROM type_garantie WHERE id_type_garantie = p_id_type_garantie;
    SELECT DATE_ADD(date_fin, INTERVAL v_duree_mois MONTH) INTO v_date_fin_etendue
    FROM garantie WHERE id_garantie = p_id_garantie;
    INSERT INTO extension_garantie (id_garantie, id_type_garantie, date_fin_etendue)
    VALUES (p_id_garantie, p_id_type_garantie, v_date_fin_etendue);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_souscrire_ultimate` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_souscrire_ultimate`(
    IN p_id_client      BIGINT,
    IN p_montant_paye   DECIMAL(10,2),
    IN p_renouvellement BOOLEAN
)
BEGIN
    DECLARE v_id_ultimate    BIGINT;
    DECLARE v_id_statut_actif BIGINT;
    DECLARE v_niveau_actuel  VARCHAR(50);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;
    START TRANSACTION;
    SELECT tf.code INTO v_niveau_actuel
    FROM client c JOIN type_fidelite tf ON tf.id_type_fidelite = c.id_type_fidelite
    WHERE c.id_client = p_id_client;
    IF v_niveau_actuel = 'ULTIMATE' THEN
        IF EXISTS (
            SELECT 1 FROM abonnement_client a
            JOIN statut_abonnement sa ON sa.id_statut_abonnement = a.id_statut_abonnement
            WHERE a.id_client = p_id_client AND sa.code = 'ACTIF' AND a.date_fin >= CURDATE()
        ) THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Le client possede deja un abonnement ULTIMATE actif';
        END IF;
    END IF;
    SELECT id_type_fidelite     INTO v_id_ultimate     FROM type_fidelite     WHERE code = 'ULTIMATE' LIMIT 1;
    SELECT id_statut_abonnement INTO v_id_statut_actif FROM statut_abonnement WHERE code = 'ACTIF'    LIMIT 1;
    UPDATE client SET id_type_fidelite = v_id_ultimate WHERE id_client = p_id_client;
    INSERT INTO abonnement_client (id_client, id_statut_abonnement, date_debut, date_fin,
                                   montant_paye, renouvellement_auto)
    VALUES (p_id_client, v_id_statut_actif, CURDATE(),
            DATE_ADD(CURDATE(), INTERVAL 1 YEAR), p_montant_paye, p_renouvellement);
    INSERT INTO historique_points (id_client, id_facture, type_operation, points, commentaire)
    VALUES (p_id_client, NULL, 'UPGRADE', 0, 'Souscription abonnement ULTIMATE annuel');
    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_verifier_acces_produit` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_verifier_acces_produit`(
    IN  p_id_client  BIGINT,
    IN  p_id_produit BIGINT,
    OUT p_acces      TINYINT
)
BEGIN
    DECLARE v_niveau_client  VARCHAR(20);
    DECLARE v_niveau_produit VARCHAR(20);
    DECLARE v_abo_actif      INT;
    SELECT tf.code INTO v_niveau_client
    FROM client c JOIN type_fidelite tf ON tf.id_type_fidelite = c.id_type_fidelite
    WHERE c.id_client = p_id_client;
    SELECT niveau_acces_min INTO v_niveau_produit FROM produit WHERE id_produit = p_id_produit;
    SET v_abo_actif = 0;
    IF v_niveau_client = 'ULTIMATE' THEN
        SELECT COUNT(*) INTO v_abo_actif FROM abonnement_client a
        JOIN statut_abonnement sa ON sa.id_statut_abonnement = a.id_statut_abonnement
        WHERE a.id_client = p_id_client AND sa.code = 'ACTIF' AND a.date_fin >= CURDATE();
    END IF;
    SET p_acces = CASE
        WHEN v_niveau_produit = 'NORMAL'   THEN 1
        WHEN v_niveau_produit = 'PREMIUM'
             AND v_niveau_client IN ('PREMIUM','ULTIMATE')
             AND (v_niveau_client != 'ULTIMATE' OR v_abo_actif > 0) THEN 1
        WHEN v_niveau_produit = 'ULTIMATE'
             AND v_niveau_client = 'ULTIMATE' AND v_abo_actif > 0   THEN 1
        ELSE 0
    END;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_verifier_garantie` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_verifier_garantie`(
    IN  p_numero_serie VARCHAR(100),
    IN  p_id_produit   BIGINT,
    OUT p_valide       BOOLEAN,
    OUT p_message      VARCHAR(255)
)
BEGIN
    DECLARE v_count INT;
    SELECT COUNT(*) INTO v_count
    FROM garantie g
    WHERE g.numero_serie = p_numero_serie
      AND g.id_produit   = p_id_produit
      AND g.date_fin     >= CURDATE();
    IF v_count > 0 THEN
        SET p_valide  = TRUE;
        SET p_message = 'Numero de serie valide. Garantie active.';
    ELSE
        SET p_valide  = FALSE;
        SET p_message = 'Numero de serie invalide ou garantie expiree.';
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `v_abonnements_actifs`
--

/*!50001 DROP VIEW IF EXISTS `v_abonnements_actifs`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_abonnements_actifs` AS select `a`.`id_abonnement` AS `id_abonnement`,`c`.`pseudo` AS `pseudo`,`c`.`email` AS `email`,`a`.`date_debut` AS `date_debut`,`a`.`date_fin` AS `date_fin`,`a`.`montant_paye` AS `montant_paye`,`a`.`renouvellement_auto` AS `renouvellement_auto`,(to_days(`a`.`date_fin`) - to_days(curdate())) AS `jours_restants` from ((`abonnement_client` `a` join `client` `c` on((`c`.`id_client` = `a`.`id_client`))) join `statut_abonnement` `sa` on((`sa`.`id_statut_abonnement` = `a`.`id_statut_abonnement`))) where ((`sa`.`code` = 'ACTIF') and (`a`.`date_fin` >= curdate())) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_avis_produit`
--

/*!50001 DROP VIEW IF EXISTS `v_avis_produit`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_avis_produit` AS select `a`.`id_avis` AS `id_avis`,`a`.`note` AS `note`,`a`.`commentaire` AS `commentaire`,`a`.`date_creation` AS `date_creation`,`c`.`pseudo` AS `auteur`,`p`.`nom` AS `produit`,`p`.`id_produit` AS `id_produit` from ((`avis_produit` `a` join `client` `c` on((`c`.`id_client` = `a`.`id_client`))) join `produit` `p` on((`p`.`id_produit` = `a`.`id_produit`))) where ((`c`.`deleted` = false) and (`p`.`deleted` = false)) order by `a`.`date_creation` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_bons_disponibles`
--

/*!50001 DROP VIEW IF EXISTS `v_bons_disponibles`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_bons_disponibles` AS select `b`.`id_bon_achat` AS `id_bon_achat`,`c`.`pseudo` AS `pseudo`,`b`.`code_bon` AS `code_bon`,`b`.`valeur` AS `valeur`,`b`.`points_utilises` AS `points_utilises`,`b`.`date_creation` AS `date_creation` from (`bon_achat` `b` join `client` `c` on((`c`.`id_client` = `b`.`id_client`))) where (`b`.`utilise` = false) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_client_actif`
--

/*!50001 DROP VIEW IF EXISTS `v_client_actif`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_client_actif` AS select `client`.`id_client` AS `id_client`,`client`.`pseudo` AS `pseudo`,`client`.`nom` AS `nom`,`client`.`prenom` AS `prenom`,`client`.`date_naissance` AS `date_naissance`,`client`.`email` AS `email`,`client`.`telephone` AS `telephone`,`client`.`mot_de_passe` AS `mot_de_passe`,`client`.`numero_carte_fidelite` AS `numero_carte_fidelite`,`client`.`id_type_fidelite` AS `id_type_fidelite`,`client`.`deleted` AS `deleted`,`client`.`rgpd_consent` AS `rgpd_consent`,`client`.`rgpd_consent_date` AS `rgpd_consent_date`,`client`.`rgpd_consent_ip` AS `rgpd_consent_ip`,`client`.`demande_suppression` AS `demande_suppression`,`client`.`date_suppression` AS `date_suppression`,`client`.`date_creation` AS `date_creation`,`client`.`date_modification` AS `date_modification` from `client` where (`client`.`deleted` = false) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_clients_a_supprimer`
--

/*!50001 DROP VIEW IF EXISTS `v_clients_a_supprimer`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_clients_a_supprimer` AS select `client`.`id_client` AS `id_client`,`client`.`pseudo` AS `pseudo`,`client`.`email` AS `email`,`client`.`date_suppression` AS `date_suppression` from `client` where ((`client`.`demande_suppression` = true) and (`client`.`deleted` = false)) order by `client`.`date_suppression` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_clients_sans_consentement`
--

/*!50001 DROP VIEW IF EXISTS `v_clients_sans_consentement`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_clients_sans_consentement` AS select `client`.`id_client` AS `id_client`,`client`.`pseudo` AS `pseudo`,`client`.`email` AS `email`,`client`.`date_creation` AS `date_creation` from `client` where ((`client`.`rgpd_consent` = false) and (`client`.`deleted` = false)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_employe_actif`
--

/*!50001 DROP VIEW IF EXISTS `v_employe_actif`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_employe_actif` AS select `employe`.`id_employe` AS `id_employe`,`employe`.`nom` AS `nom`,`employe`.`prenom` AS `prenom`,`employe`.`email` AS `email`,`employe`.`mot_de_passe` AS `mot_de_passe`,`employe`.`id_role` AS `id_role`,`employe`.`id_magasin` AS `id_magasin`,`employe`.`date_embauche` AS `date_embauche`,`employe`.`deleted` AS `deleted`,`employe`.`date_creation` AS `date_creation`,`employe`.`date_modification` AS `date_modification` from `employe` where (`employe`.`deleted` = false) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_extensions_garantie`
--

/*!50001 DROP VIEW IF EXISTS `v_extensions_garantie`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_extensions_garantie` AS select `e`.`id_extension` AS `id_extension`,`e`.`date_achat` AS `date_achat`,`e`.`date_fin_etendue` AS `date_fin_etendue`,`g`.`numero_serie` AS `numero_serie`,`p`.`nom` AS `produit`,`t`.`code` AS `type_extension`,`t`.`prix_extension` AS `prix_extension`,coalesce(`c`.`pseudo`,`f`.`nom_client`) AS `client`,coalesce(`c`.`email`,`f`.`email_client`) AS `client_email`,`f`.`contexte_vente` AS `contexte_vente` from (((((`extension_garantie` `e` join `garantie` `g` on((`g`.`id_garantie` = `e`.`id_garantie`))) join `produit` `p` on((`p`.`id_produit` = `g`.`id_produit`))) join `type_garantie` `t` on((`t`.`id_type_garantie` = `e`.`id_type_garantie`))) join `facture` `f` on((`f`.`id_facture` = `g`.`id_facture`))) left join `client` `c` on((`c`.`id_client` = `f`.`id_client`))) order by `e`.`date_achat` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_facture_detail`
--

/*!50001 DROP VIEW IF EXISTS `v_facture_detail`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_facture_detail` AS select `f`.`id_facture` AS `id_facture`,`f`.`date_facture` AS `date_facture`,`f`.`montant_total` AS `montant_total`,`f`.`montant_remise` AS `montant_remise`,`f`.`montant_final` AS `montant_final`,`f`.`contexte_vente` AS `contexte_vente`,`c`.`pseudo` AS `client_pseudo`,`c`.`email` AS `client_email`,coalesce(`c`.`pseudo`,`f`.`nom_client`) AS `nom_client_affiche`,coalesce(`c`.`email`,`f`.`email_client`) AS `email_client_affiche`,coalesce(`c`.`telephone`,`f`.`telephone_client`) AS `telephone_client_affiche`,`m`.`nom` AS `magasin`,`mp`.`code` AS `mode_paiement`,concat(`e`.`prenom`,' ',`e`.`nom`) AS `vendeur`,`e`.`id_employe` AS `id_employe` from ((((`facture` `f` left join `client` `c` on((`c`.`id_client` = `f`.`id_client`))) join `magasin` `m` on((`m`.`id_magasin` = `f`.`id_magasin`))) join `mode_paiement` `mp` on((`mp`.`id_mode_paiement` = `f`.`id_mode_paiement`))) left join `employe` `e` on((`e`.`id_employe` = `f`.`id_employe`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_garanties_actives`
--

/*!50001 DROP VIEW IF EXISTS `v_garanties_actives`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_garanties_actives` AS select `g`.`id_garantie` AS `id_garantie`,`g`.`numero_serie` AS `numero_serie`,`g`.`date_debut` AS `date_debut`,`g`.`date_fin` AS `date_fin`,`g`.`est_etendue` AS `est_etendue`,`p`.`nom` AS `produit`,`p`.`id_produit` AS `id_produit`,coalesce(`c`.`pseudo`,`f`.`nom_client`) AS `client`,coalesce(`c`.`email`,`f`.`email_client`) AS `client_email`,`f`.`id_facture` AS `id_facture`,`f`.`date_facture` AS `date_achat`,`f`.`contexte_vente` AS `contexte_vente` from (((`garantie` `g` join `produit` `p` on((`p`.`id_produit` = `g`.`id_produit`))) join `facture` `f` on((`f`.`id_facture` = `g`.`id_facture`))) left join `client` `c` on((`c`.`id_client` = `f`.`id_client`))) where (`g`.`date_fin` >= curdate()) order by `g`.`date_fin` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_garanties_expirees`
--

/*!50001 DROP VIEW IF EXISTS `v_garanties_expirees`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_garanties_expirees` AS select `g`.`id_garantie` AS `id_garantie`,`g`.`numero_serie` AS `numero_serie`,`g`.`date_debut` AS `date_debut`,`g`.`date_fin` AS `date_fin`,`p`.`nom` AS `produit`,`p`.`id_produit` AS `id_produit`,`f`.`id_facture` AS `id_facture`,`f`.`date_facture` AS `date_achat`,`f`.`contexte_vente` AS `contexte_vente` from ((`garantie` `g` join `produit` `p` on((`p`.`id_produit` = `g`.`id_produit`))) join `facture` `f` on((`f`.`id_facture` = `g`.`id_facture`))) where (`g`.`date_fin` < curdate()) order by `g`.`date_fin` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_note_moyenne_produit`
--

/*!50001 DROP VIEW IF EXISTS `v_note_moyenne_produit`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_note_moyenne_produit` AS select `p`.`id_produit` AS `id_produit`,`p`.`nom` AS `produit`,count(`a`.`id_avis`) AS `nb_avis`,round(avg(`a`.`note`),1) AS `note_moyenne`,sum((`a`.`note` = 5)) AS `nb_5_etoiles`,sum((`a`.`note` = 4)) AS `nb_4_etoiles`,sum((`a`.`note` = 3)) AS `nb_3_etoiles`,sum((`a`.`note` = 2)) AS `nb_2_etoiles`,sum((`a`.`note` = 1)) AS `nb_1_etoile` from (`produit` `p` left join `avis_produit` `a` on((`a`.`id_produit` = `p`.`id_produit`))) where (`p`.`deleted` = false) group by `p`.`id_produit`,`p`.`nom` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_points_client`
--

/*!50001 DROP VIEW IF EXISTS `v_points_client`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_points_client` AS select `c`.`id_client` AS `id_client`,`c`.`pseudo` AS `pseudo`,`c`.`nom` AS `nom`,`c`.`prenom` AS `prenom`,`tf`.`code` AS `niveau_fidelite`,`pf`.`solde_points` AS `solde_points`,`pf`.`total_achats_annuel` AS `total_achats_annuel`,`pf`.`date_debut_periode` AS `date_debut_periode` from ((`client` `c` join `type_fidelite` `tf` on((`tf`.`id_type_fidelite` = `c`.`id_type_fidelite`))) left join `points_fidelite` `pf` on((`pf`.`id_client` = `c`.`id_client`))) where (`c`.`deleted` = false) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_produit_actif`
--

/*!50001 DROP VIEW IF EXISTS `v_produit_actif`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_produit_actif` AS select `produit`.`id_produit` AS `id_produit`,`produit`.`nom` AS `nom`,`produit`.`description` AS `description`,`produit`.`id_categorie` AS `id_categorie`,`produit`.`date_sortie` AS `date_sortie`,`produit`.`editeur` AS `editeur`,`produit`.`constructeur` AS `constructeur`,`produit`.`pegi` AS `pegi`,`produit`.`plateforme` AS `plateforme`,`produit`.`actif` AS `actif`,`produit`.`deleted` AS `deleted`,`produit`.`niveau_acces_min` AS `niveau_acces_min`,`produit`.`langue` AS `langue`,`produit`.`necessite_numero_serie` AS `necessite_numero_serie`,`produit`.`date_creation` AS `date_creation`,`produit`.`date_modification` AS `date_modification` from `produit` where ((`produit`.`deleted` = false) and (`produit`.`actif` = true)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_produit_avec_acces`
--

/*!50001 DROP VIEW IF EXISTS `v_produit_avec_acces`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_produit_avec_acces` AS select `p`.`id_produit` AS `id_produit`,`p`.`nom` AS `nom`,`p`.`description` AS `description`,`p`.`plateforme` AS `plateforme`,`p`.`pegi` AS `pegi`,`p`.`actif` AS `actif`,`p`.`niveau_acces_min` AS `niveau_acces_min`,`p`.`langue` AS `langue_produit`,`p`.`necessite_numero_serie` AS `necessite_numero_serie`,`c`.`nom` AS `categorie`,`tc`.`code` AS `type_categorie`,`pp`.`prix` AS `prix_neuf`,`img`.`url` AS `image_principale_url`,`img`.`alt` AS `image_principale_alt`,`img`.`decorative` AS `image_decorative` from (((((`produit` `p` join `categorie` `c` on((`c`.`id_categorie` = `p`.`id_categorie`))) join `type_categorie` `tc` on((`tc`.`id_type_categorie` = `c`.`id_type_categorie`))) left join `produit_prix` `pp` on((`pp`.`id_produit` = `p`.`id_produit`))) left join `statut_produit` `sp` on(((`sp`.`id_statut_produit` = `pp`.`id_statut_produit`) and (`sp`.`code` = 'NEUF')))) left join `produit_image` `img` on(((`img`.`id_produit` = `p`.`id_produit`) and (`img`.`principale` = true)))) where ((`p`.`deleted` = false) and (`p`.`actif` = true)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_produit_media`
--

/*!50001 DROP VIEW IF EXISTS `v_produit_media`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_produit_media` AS select `p`.`id_produit` AS `id_produit`,`p`.`nom` AS `produit`,'IMAGE' AS `type_media`,`i`.`id_image` AS `id_media`,`i`.`url` AS `url`,`i`.`alt` AS `description_accessible`,`i`.`decorative` AS `decorative`,NULL AS `langue`,NULL AS `sous_titres_url`,NULL AS `audio_desc_url`,NULL AS `transcription`,`i`.`ordre` AS `ordre`,`i`.`principale` AS `principale` from (`produit` `p` join `produit_image` `i` on((`i`.`id_produit` = `p`.`id_produit`))) where (`p`.`deleted` = false) union all select `p`.`id_produit` AS `id_produit`,`p`.`nom` AS `produit`,'VIDEO' AS `type_media`,`v`.`id_video` AS `id_media`,`v`.`url` AS `url`,`v`.`titre` AS `description_accessible`,false AS `decorative`,`v`.`langue` AS `langue`,`v`.`sous_titres_url` AS `sous_titres_url`,`v`.`audio_desc_url` AS `audio_desc_url`,`v`.`transcription` AS `transcription`,`v`.`ordre` AS `ordre`,false AS `principale` from (`produit` `p` join `produit_video` `v` on((`v`.`id_produit` = `p`.`id_produit`))) where (`p`.`deleted` = false) order by `id_produit`,`type_media`,`ordre` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_ratios_points`
--

/*!50001 DROP VIEW IF EXISTS `v_ratios_points`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_ratios_points` AS select `tc`.`code` AS `type_categorie`,`tf`.`code` AS `niveau_fidelite`,`rp`.`ratio` AS `pts_par_euro` from ((`ratio_points` `rp` join `type_categorie` `tc` on((`tc`.`id_type_categorie` = `rp`.`id_type_categorie`))) join `type_fidelite` `tf` on((`tf`.`id_type_fidelite` = `rp`.`id_type_fidelite`))) order by `tc`.`code`,`tf`.`code` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_rgaa_audit_media`
--

/*!50001 DROP VIEW IF EXISTS `v_rgaa_audit_media`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_rgaa_audit_media` AS select `p`.`id_produit` AS `id_produit`,`p`.`nom` AS `produit`,count(distinct `i`.`id_image`) AS `nb_images`,sum((case when ((`i`.`decorative` = false) and ((`i`.`alt` is null) or (`i`.`alt` = ''))) then 1 else 0 end)) AS `images_sans_alt`,count(distinct `v`.`id_video`) AS `nb_videos`,sum((case when (`v`.`sous_titres_url` is null) then 1 else 0 end)) AS `videos_sans_sous_titres`,sum((case when (`v`.`transcription` is null) then 1 else 0 end)) AS `videos_sans_transcription`,sum((case when (`v`.`audio_desc_url` is null) then 1 else 0 end)) AS `videos_sans_audio_desc` from ((`produit` `p` left join `produit_image` `i` on((`i`.`id_produit` = `p`.`id_produit`))) left join `produit_video` `v` on((`v`.`id_produit` = `p`.`id_produit`))) where (`p`.`deleted` = false) group by `p`.`id_produit`,`p`.`nom` having ((`images_sans_alt` > 0) or (`videos_sans_sous_titres` > 0) or (`videos_sans_transcription` > 0) or (`videos_sans_audio_desc` > 0)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_stock_magasin`
--

/*!50001 DROP VIEW IF EXISTS `v_stock_magasin`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_stock_magasin` AS select `s`.`id_stockage` AS `id_stockage`,`m`.`nom` AS `magasin`,`p`.`nom` AS `produit`,`s`.`quantite` AS `quantite` from ((`stockage` `s` join `magasin` `m` on((`m`.`id_magasin` = `s`.`id_magasin`))) join `produit` `p` on((`p`.`id_produit` = `s`.`id_produit`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-05 16:54:27
