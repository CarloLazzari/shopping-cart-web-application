-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: fumetti
-- ------------------------------------------------------
-- Server version	8.0.20

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
-- Table structure for table `carta`
--

DROP TABLE IF EXISTS `carta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carta` (
  `NUMERO_CARTA` varchar(19) NOT NULL,
  `INTESTATARIO` varchar(45) NOT NULL,
  `DATA_SCADENZA` date NOT NULL,
  `DELETED` varchar(1) NOT NULL,
  PRIMARY KEY (`NUMERO_CARTA`),
  KEY `Intestatario_idx` (`INTESTATARIO`),
  CONSTRAINT `INTESTATARIO` FOREIGN KEY (`INTESTATARIO`) REFERENCES `user` (`USERNAME`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carta`
--

LOCK TABLES `carta` WRITE;
/*!40000 ALTER TABLE `carta` DISABLE KEYS */;
/*!40000 ALTER TABLE `carta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `centro_vendita`
--

DROP TABLE IF EXISTS `centro_vendita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `centro_vendita` (
  `NOME_CENTRO` varchar(45) NOT NULL,
  `INDIRIZZO` varchar(45) NOT NULL,
  PRIMARY KEY (`NOME_CENTRO`),
  KEY `INDIRIZZO_INDEX` (`INDIRIZZO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `centro_vendita`
--

LOCK TABLES `centro_vendita` WRITE;
/*!40000 ALTER TABLE `centro_vendita` DISABLE KEYS */;
/*!40000 ALTER TABLE `centro_vendita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contenuto_nel_magazzino`
--

DROP TABLE IF EXISTS `contenuto_nel_magazzino`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contenuto_nel_magazzino` (
  `ISBN_FUMETTO` varchar(45) NOT NULL,
  `NOME_MAGAZZINO_REF` varchar(45) NOT NULL,
  `QUANTITA` int NOT NULL,
  `DELETED` varchar(1) NOT NULL,
  PRIMARY KEY (`ISBN_FUMETTO`,`NOME_MAGAZZINO_REF`),
  KEY `NOME_MAGAZZINO_idx` (`NOME_MAGAZZINO_REF`),
  KEY `ISBN_FUMETTO_INDEX` (`ISBN_FUMETTO`),
  CONSTRAINT `ISBN_FUMETTO` FOREIGN KEY (`ISBN_FUMETTO`) REFERENCES `fumetto` (`ISBN`) ON UPDATE CASCADE,
  CONSTRAINT `NOME_MAGAZZINO_REF` FOREIGN KEY (`NOME_MAGAZZINO_REF`) REFERENCES `magazzino` (`NOME_MAGAZZINO`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contenuto_nel_magazzino`
--

LOCK TABLES `contenuto_nel_magazzino` WRITE;
/*!40000 ALTER TABLE `contenuto_nel_magazzino` DISABLE KEYS */;
/*!40000 ALTER TABLE `contenuto_nel_magazzino` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contenuto_nell_ordine`
--

DROP TABLE IF EXISTS `contenuto_nell_ordine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contenuto_nell_ordine` (
  `ORDINE_ID_REF` int NOT NULL,
  `ISBN_FUMETTO_REF` varchar(45) NOT NULL,
  `QUANTITA` int NOT NULL,
  `NOME_MAGAZZINO` varchar(45) NOT NULL,
  `DISPATCHER` varchar(45) NOT NULL,
  PRIMARY KEY (`ORDINE_ID_REF`,`ISBN_FUMETTO_REF`),
  KEY `ORDINE_ID_REF_idx` (`ORDINE_ID_REF`),
  KEY `ISBN_FUMETTO_REF_idx` (`ISBN_FUMETTO_REF`),
  KEY `MAGAZZINO_REF_idx` (`NOME_MAGAZZINO`),
  KEY `DISPATCHER_idx` (`DISPATCHER`),
  CONSTRAINT `DISPATCHER` FOREIGN KEY (`DISPATCHER`) REFERENCES `centro_vendita` (`NOME_CENTRO`) ON UPDATE CASCADE,
  CONSTRAINT `ISBN_FUMETTO_REF` FOREIGN KEY (`ISBN_FUMETTO_REF`) REFERENCES `fumetto` (`ISBN`) ON UPDATE CASCADE,
  CONSTRAINT `NOME_MAGAZZINO` FOREIGN KEY (`NOME_MAGAZZINO`) REFERENCES `magazzino` (`NOME_MAGAZZINO`) ON UPDATE CASCADE,
  CONSTRAINT `ORDINE_ID_REF` FOREIGN KEY (`ORDINE_ID_REF`) REFERENCES `ordine` (`ID_ORDINE`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contenuto_nell_ordine`
--

LOCK TABLES `contenuto_nell_ordine` WRITE;
/*!40000 ALTER TABLE `contenuto_nell_ordine` DISABLE KEYS */;
/*!40000 ALTER TABLE `contenuto_nell_ordine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fornito_da`
--

DROP TABLE IF EXISTS `fornito_da`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fornito_da` (
  `ISBN_FUMETTO_REFERENZIATO` varchar(45) NOT NULL,
  `CENTRO_VENDITA_REFERENZIATO` varchar(45) NOT NULL,
  PRIMARY KEY (`ISBN_FUMETTO_REFERENZIATO`,`CENTRO_VENDITA_REFERENZIATO`),
  KEY `CENTRO_VENDITA_REFERENZIATO_idx` (`CENTRO_VENDITA_REFERENZIATO`),
  CONSTRAINT `CENTRO_VENDITA_REFERENZIATO` FOREIGN KEY (`CENTRO_VENDITA_REFERENZIATO`) REFERENCES `centro_vendita` (`NOME_CENTRO`) ON UPDATE CASCADE,
  CONSTRAINT `ISBN_FUMETTO_REFERENZIATO` FOREIGN KEY (`ISBN_FUMETTO_REFERENZIATO`) REFERENCES `fumetto` (`ISBN`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fornito_da`
--

LOCK TABLES `fornito_da` WRITE;
/*!40000 ALTER TABLE `fornito_da` DISABLE KEYS */;
/*!40000 ALTER TABLE `fornito_da` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fumetto`
--

DROP TABLE IF EXISTS `fumetto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fumetto` (
  `ISBN` varchar(45) NOT NULL,
  `TITOLO` varchar(45) NOT NULL,
  `AUTORE` varchar(45) NOT NULL,
  `NUMERO` int NOT NULL,
  `FORMATO` varchar(45) NOT NULL,
  `RILEGATURA` varchar(45) NOT NULL,
  `PREZZO` float NOT NULL,
  `PESO` float NOT NULL,
  `BLOCCATO` varchar(1) NOT NULL,
  PRIMARY KEY (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fumetto`
--

LOCK TABLES `fumetto` WRITE;
/*!40000 ALTER TABLE `fumetto` DISABLE KEYS */;
/*!40000 ALTER TABLE `fumetto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ha_nel_carrello`
--

DROP TABLE IF EXISTS `ha_nel_carrello`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ha_nel_carrello` (
  `USERNAME_UTENTE` varchar(45) NOT NULL,
  `ISBN_FUM` varchar(45) NOT NULL,
  `QUANTITA` int NOT NULL,
  PRIMARY KEY (`USERNAME_UTENTE`,`ISBN_FUM`),
  KEY `ISBN_FUM_idx` (`ISBN_FUM`),
  KEY `USERNAME_UTENTE_idx` (`USERNAME_UTENTE`),
  CONSTRAINT `ISBN_FUM` FOREIGN KEY (`ISBN_FUM`) REFERENCES `fumetto` (`ISBN`) ON UPDATE CASCADE,
  CONSTRAINT `USERNAME_UTENTE` FOREIGN KEY (`USERNAME_UTENTE`) REFERENCES `user` (`USERNAME`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ha_nel_carrello`
--

LOCK TABLES `ha_nel_carrello` WRITE;
/*!40000 ALTER TABLE `ha_nel_carrello` DISABLE KEYS */;
/*!40000 ALTER TABLE `ha_nel_carrello` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `magazzino`
--

DROP TABLE IF EXISTS `magazzino`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magazzino` (
  `NOME_CENTRO_APPARTENENZA` varchar(45) NOT NULL,
  `INDIRIZZO_CENTRO_REF` varchar(45) NOT NULL,
  `NOME_MAGAZZINO` varchar(45) NOT NULL,
  PRIMARY KEY (`NOME_MAGAZZINO`),
  KEY `NOME_CENTRO_REF_idx` (`NOME_CENTRO_APPARTENENZA`),
  CONSTRAINT `NOME_CENTRO_REF` FOREIGN KEY (`NOME_CENTRO_APPARTENENZA`) REFERENCES `centro_vendita` (`NOME_CENTRO`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magazzino`
--

LOCK TABLES `magazzino` WRITE;
/*!40000 ALTER TABLE `magazzino` DISABLE KEYS */;
/*!40000 ALTER TABLE `magazzino` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordine`
--

DROP TABLE IF EXISTS `ordine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordine` (
  `ID_ORDINE` int NOT NULL AUTO_INCREMENT,
  `DATA` date NOT NULL,
  `INDIRIZZO_SPEDIZIONE` varchar(45) NOT NULL,
  `NOME_EFFETTUANTE` varchar(45) NOT NULL,
  `MODALITA_PAGAMENTO` varchar(45) NOT NULL,
  `STATO` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_ORDINE`),
  KEY `NOME_EFFETTUANTE_idx` (`NOME_EFFETTUANTE`),
  KEY `MODALITA_PAGAMENTO_idx` (`MODALITA_PAGAMENTO`),
  CONSTRAINT `MODALITA_PAGAMENTO` FOREIGN KEY (`MODALITA_PAGAMENTO`) REFERENCES `carta` (`NUMERO_CARTA`) ON UPDATE CASCADE,
  CONSTRAINT `NOME_EFFETTUANTE` FOREIGN KEY (`NOME_EFFETTUANTE`) REFERENCES `user` (`USERNAME`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordine`
--

LOCK TABLES `ordine` WRITE;
/*!40000 ALTER TABLE `ordine` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `USERNAME` varchar(45) NOT NULL,
  `PASSWORD` varchar(45) NOT NULL,
  `NOME` varchar(20) NOT NULL,
  `COGNOME` varchar(20) NOT NULL,
  `EMAIL` varchar(45) NOT NULL,
  `DATA_N` date NOT NULL,
  `INDIRIZZO` varchar(45) NOT NULL,
  `BLOCCATO` varchar(1) NOT NULL,
  `IS_ADMIN` varchar(1) NOT NULL,
  PRIMARY KEY (`USERNAME`),
  KEY `INDIRIZZO_INDEX_USER` (`INDIRIZZO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-26  3:05:23
