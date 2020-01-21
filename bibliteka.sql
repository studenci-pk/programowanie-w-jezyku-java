-- MySQL dump 10.13  Distrib 5.7.26, for Linux (x86_64)
--
-- Host: localhost    Database: biblioteka
-- ------------------------------------------------------
-- Server version	5.7.26-0ubuntu0.18.04.1

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
-- Table structure for table `audiobook`
--

DROP TABLE IF EXISTS `audiobook`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audiobook` (
  `id_audiobooka` int(10) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `id_ksiazki` int(10) NOT NULL COMMENT 'FK',
  PRIMARY KEY (`id_audiobooka`),
  KEY `id_ksiazki` (`id_ksiazki`),
  CONSTRAINT `audiobook_ibfk_1` FOREIGN KEY (`id_ksiazki`) REFERENCES `ksiazka` (`id_ksiazki`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audiobook`
--

LOCK TABLES `audiobook` WRITE;
/*!40000 ALTER TABLE `audiobook` DISABLE KEYS */;
/*!40000 ALTER TABLE `audiobook` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `autor`
--

DROP TABLE IF EXISTS `autor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autor` (
  `id_autora` int(10) NOT NULL AUTO_INCREMENT,
  `imie` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  `nazwisko` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  `krajPochodzenia` varchar(50) COLLATE utf8mb4_polish_ci DEFAULT NULL,
  PRIMARY KEY (`id_autora`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autor`
--

LOCK TABLES `autor` WRITE;
/*!40000 ALTER TABLE `autor` DISABLE KEYS */;
INSERT INTO `autor` VALUES (1,'Adam','Mickiewicz','Polska'),(2,'Henryk','Sienkiewicz','Polska'),(3,'Bolesław','Prus','Polska'),(4,'Johan','Goethe','Niemcy'),(5,'Albert','Camus','Francja'),(6,'Olga','Tokarczuk',''),(7,'Tadeusz','Borowski','Polska'),(8,'Julian','Tuwim','Polska'),(9,'William','Shakespeare','Anglia'),(10,'Bolesław','Prus','Polska'),(11,'aaa','aaa','aaa'),(12,'Raz','Dwa','Trzy'),(13,'111','222','333');
/*!40000 ALTER TABLE `autor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dzial`
--

DROP TABLE IF EXISTS `dzial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dzial` (
  `id_dzialu` int(10) NOT NULL AUTO_INCREMENT,
  `nazwa` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  PRIMARY KEY (`id_dzialu`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dzial`
--

LOCK TABLES `dzial` WRITE;
/*!40000 ALTER TABLE `dzial` DISABLE KEYS */;
INSERT INTO `dzial` VALUES (1,'klasyka'),(2,'epika'),(3,'liryka'),(4,'dramat'),(5,'nauki matematyczno-p'),(6,'informatyka'),(7,'ekonomia'),(8,'języki obce'),(9,'psychologia i socjol'),(10,'medycyna'),(11,'aabbcc');
/*!40000 ALTER TABLE `dzial` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `egzemplarz`
--

DROP TABLE IF EXISTS `egzemplarz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `egzemplarz` (
  `id_egzemplarza` int(10) NOT NULL AUTO_INCREMENT,
  `id_ksiazki` int(10) NOT NULL,
  PRIMARY KEY (`id_egzemplarza`),
  KEY `id_ksiazki` (`id_ksiazki`),
  CONSTRAINT `egzemplarz_ibfk_1` FOREIGN KEY (`id_ksiazki`) REFERENCES `ksiazka` (`id_ksiazki`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `egzemplarz`
--

LOCK TABLES `egzemplarz` WRITE;
/*!40000 ALTER TABLE `egzemplarz` DISABLE KEYS */;
INSERT INTO `egzemplarz` VALUES (1,7),(2,7),(3,7),(4,7),(5,7),(66,7),(50,8),(51,8),(52,8),(85,9),(86,9),(87,9),(81,10),(76,15),(69,16),(71,16),(75,16);
/*!40000 ALTER TABLE `egzemplarz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historiawypozyczenzwrotow`
--

DROP TABLE IF EXISTS `historiawypozyczenzwrotow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historiawypozyczenzwrotow` (
  `id_konta` int(10) NOT NULL,
  `id_wypozyczenia_zwrotu` int(10) NOT NULL,
  KEY `id_konta` (`id_konta`),
  KEY `id_wypozyczenia_zwrotu` (`id_wypozyczenia_zwrotu`),
  CONSTRAINT `historiawypozyczenzwrotow_ibfk_1` FOREIGN KEY (`id_konta`) REFERENCES `konto` (`id_konta`),
  CONSTRAINT `historiawypozyczenzwrotow_ibfk_2` FOREIGN KEY (`id_wypozyczenia_zwrotu`) REFERENCES `wypozyczenie_zwrot` (`id_wypozyczenia_zwrotu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historiawypozyczenzwrotow`
--

LOCK TABLES `historiawypozyczenzwrotow` WRITE;
/*!40000 ALTER TABLE `historiawypozyczenzwrotow` DISABLE KEYS */;
/*!40000 ALTER TABLE `historiawypozyczenzwrotow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ibuk`
--

DROP TABLE IF EXISTS `ibuk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ibuk` (
  `id_ibuka` int(10) NOT NULL AUTO_INCREMENT,
  `id_ksiazki` int(10) NOT NULL,
  PRIMARY KEY (`id_ibuka`),
  KEY `id_ksiazki` (`id_ksiazki`),
  CONSTRAINT `ibuk_ibfk_1` FOREIGN KEY (`id_ksiazki`) REFERENCES `ksiazka` (`id_ksiazki`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ibuk`
--

LOCK TABLES `ibuk` WRITE;
/*!40000 ALTER TABLE `ibuk` DISABLE KEYS */;
/*!40000 ALTER TABLE `ibuk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `konto`
--

DROP TABLE IF EXISTS `konto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `konto` (
  `id_konta` int(10) NOT NULL AUTO_INCREMENT,
  `login` varchar(32) COLLATE utf8mb4_polish_ci NOT NULL,
  `haslo` varchar(32) COLLATE utf8mb4_polish_ci NOT NULL,
  `email` varchar(32) COLLATE utf8mb4_polish_ci DEFAULT NULL,
  `dataUtworzenia` date NOT NULL,
  `id_uprawnienia` int(10) NOT NULL COMMENT 'FK\n\n1-uprawnienia student\n2- uprawnienia pracownik\n3-uprawnienia kierownik',
  `data_utworzenia` date DEFAULT NULL,
  PRIMARY KEY (`id_konta`),
  KEY `id_uprawnienia` (`id_uprawnienia`),
  CONSTRAINT `konto_ibfk_3` FOREIGN KEY (`id_uprawnienia`) REFERENCES `uprawnienia` (`id_uprawnienia`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `konto`
--

LOCK TABLES `konto` WRITE;
/*!40000 ALTER TABLE `konto` DISABLE KEYS */;
INSERT INTO `konto` VALUES (25,'Porok','202cb962ac59075b964b07152d234b70','przemekpapla@gmail.com','2019-12-26',2,NULL),(26,'Aga','202cb962ac59075b964b07152d234b70','studentkaagata@gmail.com','2019-12-26',1,NULL);
/*!40000 ALTER TABLE `konto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `koszyk`
--

DROP TABLE IF EXISTS `koszyk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `koszyk` (
  `id_konta` int(10) NOT NULL COMMENT 'FK',
  `tytul` varchar(40) COLLATE utf8mb4_polish_ci NOT NULL,
  KEY `id_konta` (`id_konta`),
  CONSTRAINT `koszyk_ibfk_1` FOREIGN KEY (`id_konta`) REFERENCES `konto` (`id_konta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `koszyk`
--

LOCK TABLES `koszyk` WRITE;
/*!40000 ALTER TABLE `koszyk` DISABLE KEYS */;
/*!40000 ALTER TABLE `koszyk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ksiazka`
--

DROP TABLE IF EXISTS `ksiazka`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ksiazka` (
  `id_ksiazki` int(10) NOT NULL AUTO_INCREMENT,
  `sygnatura` varchar(15) COLLATE utf8mb4_polish_ci NOT NULL,
  `id_autora` int(10) NOT NULL COMMENT 'FK',
  `id_wydawnictwa` int(10) NOT NULL,
  `id_dzialu` int(10) NOT NULL COMMENT 'FK',
  `tytul` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  `kategoria` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  `slowoKlucz` varchar(50) COLLATE utf8mb4_polish_ci DEFAULT NULL,
  PRIMARY KEY (`id_ksiazki`),
  KEY `id_autora` (`id_autora`),
  KEY `id_wydawnictwa` (`id_wydawnictwa`),
  KEY `id_dzialu` (`id_dzialu`),
  CONSTRAINT `ksiazka_ibfk_1` FOREIGN KEY (`id_autora`) REFERENCES `autor` (`id_autora`),
  CONSTRAINT `ksiazka_ibfk_2` FOREIGN KEY (`id_wydawnictwa`) REFERENCES `wydawnictwo` (`id_wydawnictwa`),
  CONSTRAINT `ksiazka_ibfk_3` FOREIGN KEY (`id_dzialu`) REFERENCES `dzial` (`id_dzialu`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ksiazka`
--

LOCK TABLES `ksiazka` WRITE;
/*!40000 ALTER TABLE `ksiazka` DISABLE KEYS */;
INSERT INTO `ksiazka` VALUES (7,'1',1,1,1,'Pan Tadeusz','lektury','Mickiewicz'),(8,'2',2,2,2,'Krzyżacy','historyczne','Grunwald'),(9,'3',3,2,2,'Lalka','lektury','kapitalizm'),(10,'4',4,2,2,'Cierpienia młodego Wertera','powieści epistolarne','Sturm und Drang'),(15,'5',4,2,2,'Pan Cogito','najlepsze',NULL),(16,'8793',12,5,4,'Zbrodnia Ikara','Beletrystyka','Ikar');
/*!40000 ALTER TABLE `ksiazka` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oplata`
--

DROP TABLE IF EXISTS `oplata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oplata` (
  `id_oplaty` int(10) NOT NULL AUTO_INCREMENT,
  `rodzaj` varchar(10) COLLATE utf8mb4_polish_ci NOT NULL,
  `opis` varchar(255) COLLATE utf8mb4_polish_ci DEFAULT NULL,
  `kwota` double NOT NULL,
  PRIMARY KEY (`id_oplaty`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oplata`
--

LOCK TABLES `oplata` WRITE;
/*!40000 ALTER TABLE `oplata` DISABLE KEYS */;
INSERT INTO `oplata` VALUES (1,'kara','lekkie uszkodzenie',4.5);
/*!40000 ALTER TABLE `oplata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pracownik_bibliotekarz`
--

DROP TABLE IF EXISTS `pracownik_bibliotekarz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pracownik_bibliotekarz` (
  `id_pracownika` int(10) NOT NULL AUTO_INCREMENT,
  `pesel` varchar(32) COLLATE utf8mb4_polish_ci NOT NULL,
  `imie` varchar(32) COLLATE utf8mb4_polish_ci NOT NULL,
  `nazwisko` varchar(32) COLLATE utf8mb4_polish_ci NOT NULL,
  `nickname` varchar(32) COLLATE utf8mb4_polish_ci DEFAULT NULL,
  `id_konta` int(10) NOT NULL,
  PRIMARY KEY (`id_pracownika`),
  UNIQUE KEY `pesel` (`pesel`),
  KEY `fk_bibliotekarz_1_idx` (`id_konta`)
) ENGINE=InnoDB AUTO_INCREMENT=1234567895 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pracownik_bibliotekarz`
--

LOCK TABLES `pracownik_bibliotekarz` WRITE;
/*!40000 ALTER TABLE `pracownik_bibliotekarz` DISABLE KEYS */;
INSERT INTO `pracownik_bibliotekarz` VALUES (1234567894,'12345678902','Agatka','BestBBB','nickname',26);
/*!40000 ALTER TABLE `pracownik_bibliotekarz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statusrezerwacji`
--

DROP TABLE IF EXISTS `statusrezerwacji`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `statusrezerwacji` (
  `id_statusRezerwacji` int(10) NOT NULL AUTO_INCREMENT,
  `statusRezerwacji` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  PRIMARY KEY (`id_statusRezerwacji`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statusrezerwacji`
--

LOCK TABLES `statusrezerwacji` WRITE;
/*!40000 ALTER TABLE `statusrezerwacji` DISABLE KEYS */;
INSERT INTO `statusrezerwacji` VALUES (1,'wolna'),(2,'zarezerwowana'),(3,'wypożyczona');
/*!40000 ALTER TABLE `statusrezerwacji` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uprawnienia`
--

DROP TABLE IF EXISTS `uprawnienia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uprawnienia` (
  `id_uprawnienia` int(10) NOT NULL AUTO_INCREMENT,
  `uprawnienia` int(10) NOT NULL,
  PRIMARY KEY (`id_uprawnienia`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uprawnienia`
--

LOCK TABLES `uprawnienia` WRITE;
/*!40000 ALTER TABLE `uprawnienia` DISABLE KEYS */;
INSERT INTO `uprawnienia` VALUES (1,1),(2,0),(3,2);
/*!40000 ALTER TABLE `uprawnienia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uzytkownik_student_czytelnik`
--

DROP TABLE IF EXISTS `uzytkownik_student_czytelnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uzytkownik_student_czytelnik` (
  `id_czytelnika` int(10) NOT NULL AUTO_INCREMENT,
  `pesel` varchar(11) COLLATE utf8mb4_polish_ci NOT NULL,
  `imie` varchar(32) COLLATE utf8mb4_polish_ci NOT NULL,
  `nazwisko` varchar(32) COLLATE utf8mb4_polish_ci NOT NULL,
  `wydzial` varchar(32) COLLATE utf8mb4_polish_ci NOT NULL,
  `kierunek` varchar(32) COLLATE utf8mb4_polish_ci NOT NULL,
  `id_konta` int(10) NOT NULL,
  PRIMARY KEY (`id_czytelnika`),
  UNIQUE KEY `pesel` (`pesel`),
  KEY `fk_uzytkownik_student_czytelnik_1_idx` (`id_konta`)
) ENGINE=InnoDB AUTO_INCREMENT=123456794 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci COMMENT='pesel - PK,FK';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uzytkownik_student_czytelnik`
--

LOCK TABLES `uzytkownik_student_czytelnik` WRITE;
/*!40000 ALTER TABLE `uzytkownik_student_czytelnik` DISABLE KEYS */;
INSERT INTO `uzytkownik_student_czytelnik` VALUES (123456793,'12345678901','Przemys?aw','BornToBeWild','WIEIK','Infromatyka',25);
/*!40000 ALTER TABLE `uzytkownik_student_czytelnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wydawnictwo`
--

DROP TABLE IF EXISTS `wydawnictwo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wydawnictwo` (
  `id_wydawnictwa` int(10) NOT NULL AUTO_INCREMENT,
  `nazwa` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  `miejscowosc` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  `kraj` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  PRIMARY KEY (`id_wydawnictwa`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wydawnictwo`
--

LOCK TABLES `wydawnictwo` WRITE;
/*!40000 ALTER TABLE `wydawnictwo` DISABLE KEYS */;
INSERT INTO `wydawnictwo` VALUES (1,'PWN','Kraków','Polska'),(2,'Helion','Warszawa','Polska'),(3,'Politechnika Krakows','Kraków','Polska'),(4,'Politechnika Rzeszow','Rzeszów','Polska'),(5,'Pearson','Londyn','Anglia'),(6,'1','2','3');
/*!40000 ALTER TABLE `wydawnictwo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wypozyczenie_zwrot`
--

DROP TABLE IF EXISTS `wypozyczenie_zwrot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wypozyczenie_zwrot` (
  `id_wypozyczenia_zwrotu` int(10) NOT NULL AUTO_INCREMENT,
  `id_egzemplarza` int(10) NOT NULL COMMENT 'FK',
  `id_konta` int(10) NOT NULL COMMENT 'FK',
  `id_StatusRezerwacji` int(10) DEFAULT NULL COMMENT 'FK',
  `id_oplaty` int(10) DEFAULT NULL,
  `dataRezerwacji` date DEFAULT NULL,
  `dataWygasnieciaRezerwacji` date DEFAULT NULL,
  `miejsceOdbioru` varchar(25) COLLATE utf8mb4_polish_ci DEFAULT NULL,
  `dataWypozyczenia` date DEFAULT NULL,
  `dataZwrotu` date DEFAULT NULL,
  PRIMARY KEY (`id_wypozyczenia_zwrotu`),
  KEY `id_egzemplarza` (`id_egzemplarza`),
  KEY `id_StatusRezerwacji` (`id_StatusRezerwacji`),
  KEY `id_oplaty` (`id_oplaty`),
  KEY `id_konta` (`id_konta`),
  CONSTRAINT `wypozyczenie_zwrot_ibfk_1` FOREIGN KEY (`id_egzemplarza`) REFERENCES `egzemplarz` (`id_egzemplarza`),
  CONSTRAINT `wypozyczenie_zwrot_ibfk_2` FOREIGN KEY (`id_StatusRezerwacji`) REFERENCES `statusrezerwacji` (`id_statusRezerwacji`),
  CONSTRAINT `wypozyczenie_zwrot_ibfk_3` FOREIGN KEY (`id_oplaty`) REFERENCES `oplata` (`id_oplaty`),
  CONSTRAINT `wypozyczenie_zwrot_ibfk_4` FOREIGN KEY (`id_konta`) REFERENCES `konto` (`id_konta`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wypozyczenie_zwrot`
--

LOCK TABLES `wypozyczenie_zwrot` WRITE;
/*!40000 ALTER TABLE `wypozyczenie_zwrot` DISABLE KEYS */;
INSERT INTO `wypozyczenie_zwrot` VALUES (14,66,25,1,NULL,'2020-01-01','2020-02-01',NULL,NULL,NULL),(15,5,25,2,NULL,'2020-01-01','2020-02-01',NULL,NULL,NULL);
/*!40000 ALTER TABLE `wypozyczenie_zwrot` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-03 21:51:38
