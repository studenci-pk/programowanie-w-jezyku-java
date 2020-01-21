-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 27 Lis 2019, 21:52
-- Wersja serwera: 10.4.8-MariaDB
-- Wersja PHP: 7.3.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `biblioteka`
--

DELIMITER $$
--
-- Procedury
--
CREATE DEFINER=`agata`@`localhost` PROCEDURE `dodaj_audiobook` (IN `id_ksiazki` INT(10))  BEGIN
INSERT INTO audiobook(id_ksiazki) 
VALUES (id_ksiazki);
END$$

CREATE DEFINER=`agata`@`localhost` PROCEDURE `dodaj_autora` (IN `imie` VARCHAR(10) CHARSET utf8, IN `nazwisko` VARCHAR(10) CHARSET utf8, IN `krajPochodzenia` VARCHAR(15) CHARSET utf8)  BEGIN
INSERT INTO autor(imie,nazwisko,krajPochodzenia) 
VALUES (imie,nazwisko,krajPochodzenia);
END$$

CREATE DEFINER=`agata`@`localhost` PROCEDURE `dodaj_dzial` (IN `nazwa` VARCHAR(20) CHARSET utf8)  BEGIN
INSERT INTO dzial(nazwa) 
VALUES (nazwa);
END$$

CREATE DEFINER=`agata`@`localhost` PROCEDURE `dodaj_ibuk` (IN `id_ksiazki` INT(10))  BEGIN
INSERT INTO ibuk(id_ksiazki) 
VALUES (id_ksiazki);
END$$

CREATE DEFINER=`agata`@`localhost` PROCEDURE `dodaj_ksiazke` (IN `id_autora` INT(10), IN `id_wydawnictwa` INT(10), IN `id_dzialu` INT(10), IN `tytul` VARCHAR(40) CHARSET utf8, IN `kategoria` VARCHAR(40) CHARSET utf8, IN `slowoKlucz` VARCHAR(20) CHARSET utf8, IN `ilosc_egzemplarzy` INT(10))  BEGIN

DECLARE	a INT;
SET  @sygnatura = (SELECT MAX(sygnatura) FROM ksiazka);
IF @sygnatura IS NULL THEN SET @sygnatura = 1;
    ELSE SET @sygnatura = @sygnatura + 1;
    END IF;


INSERT INTO ksiazka(sygnatura, id_autora, id_wydawnictwa, id_dzialu, tytul, kategoria, slowoKlucz) 
VALUES (@sygnatura, id_autora, id_wydawnictwa, id_dzialu, tytul, kategoria, slowoKlucz);



END$$

CREATE DEFINER=`agata`@`localhost` PROCEDURE `dodaj_wydawnictwo` (IN `nazwa` VARCHAR(20) CHARSET utf8, IN `miejscowosc` VARCHAR(20) CHARSET utf8, IN `kraj` VARCHAR(20) CHARSET utf8)  BEGIN
INSERT INTO wydawnictwo(nazwa,miejscowosc,kraj) 
VALUES (nazwa,miejscowosc,kraj);
END$$

CREATE DEFINER=`agata`@`localhost` PROCEDURE `usun_ksiazke` (IN `id_ksiazki` INT(10))  BEGIN
DELETE FROM ksiazka WHERE id_ksiazki = id_ksiazki;
END$$

CREATE DEFINER=`agata`@`localhost` PROCEDURE `wyswietl_info_ksiazka` (IN `tytul` VARCHAR(50) CHARSET utf8, IN `sygnatura` VARCHAR(15) CHARSET utf8, IN `id_ksiazki` INT(10))  BEGIN
SELECT k.* from ksiazka k WHERE k.tytul=tytul OR
k.sygnatura=sygnatura OR k.id_ksiazki=id_ksiazki;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `audiobook`
--

CREATE TABLE `audiobook` (
  `id_audiobooka` int(10) NOT NULL COMMENT 'PK',
  `id_ksiazki` int(10) NOT NULL COMMENT 'FK'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `autor`
--

CREATE TABLE `autor` (
  `id_autora` int(10) NOT NULL,
  `imie` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  `nazwisko` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  `krajPochodzenia` varchar(50) COLLATE utf8mb4_polish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Zrzut danych tabeli `autor`
--

INSERT INTO `autor` (`id_autora`, `imie`, `nazwisko`, `krajPochodzenia`) VALUES
(1, 'Adam', 'Mickiewicz', 'Polska'),
(2, 'Henryk', 'Sienkiewicz', 'Polska'),
(3, 'Bolesław', 'Prus', 'Polska'),
(4, 'Johan', 'Goethe', 'Niemcy'),
(5, 'Albert', 'Camus', 'Francja'),
(6, 'Olga', 'Tokarczuk', ''),
(7, 'Tadeusz', 'Borowski', 'Polska'),
(8, 'Julian', 'Tuwim', 'Polska'),
(9, 'William', 'Shakespeare', 'Anglia'),
(10, 'Bolesław', 'Prus', 'Polska');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `dzial`
--

CREATE TABLE `dzial` (
  `id_dzialu` int(10) NOT NULL,
  `nazwa` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Zrzut danych tabeli `dzial`
--

INSERT INTO `dzial` (`id_dzialu`, `nazwa`) VALUES
(1, 'klasyka'),
(2, 'epika'),
(3, 'liryka'),
(4, 'dramat'),
(5, 'nauki matematyczno-p'),
(6, 'informatyka'),
(7, 'ekonomia'),
(8, 'języki obce'),
(9, 'psychologia i socjol'),
(10, 'medycyna');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `egzemplarz`
--

CREATE TABLE `egzemplarz` (
  `id_egzemplarza` int(10) NOT NULL,
  `id_ksiazki` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `historiawypozyczenzwrotow`
--

CREATE TABLE `historiawypozyczenzwrotow` (
  `id_konta` int(10) NOT NULL,
  `id_wypozyczenia_zwrotu` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ibuk`
--

CREATE TABLE `ibuk` (
  `id_ibuka` int(10) NOT NULL,
  `id_ksiazki` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `konto`
--

CREATE TABLE `konto` (
  `id_konta` int(10) NOT NULL,
  `pesel` int(11) NOT NULL COMMENT 'FK',
  `login` varchar(25) COLLATE utf8mb4_polish_ci NOT NULL,
  `haslo` varchar(25) COLLATE utf8mb4_polish_ci NOT NULL,
  `email` varchar(20) COLLATE utf8mb4_polish_ci DEFAULT NULL,
  `dataUtworzenia` date NOT NULL,
  `id_uprawnienia` int(10) NOT NULL COMMENT 'FK\n\n1-uprawnienia student\n2- uprawnienia pracownik\n3-uprawnienia kierownik'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `koszyk`
--

CREATE TABLE `koszyk` (
  `id_konta` int(10) NOT NULL COMMENT 'FK',
  `tytul` varchar(40) COLLATE utf8mb4_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ksiazka`
--

CREATE TABLE `ksiazka` (
  `id_ksiazki` int(10) NOT NULL,
  `sygnatura` varchar(15) COLLATE utf8mb4_polish_ci NOT NULL,
  `id_autora` int(10) NOT NULL COMMENT 'FK',
  `id_wydawnictwa` int(10) NOT NULL,
  `id_dzialu` int(10) NOT NULL COMMENT 'FK',
  `tytul` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  `kategoria` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  `slowoKlucz` varchar(50) COLLATE utf8mb4_polish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Zrzut danych tabeli `ksiazka`
--

INSERT INTO `ksiazka` (`id_ksiazki`, `sygnatura`, `id_autora`, `id_wydawnictwa`, `id_dzialu`, `tytul`, `kategoria`, `slowoKlucz`) VALUES
(7, '1', 1, 1, 1, 'Pan Tadeusz', 'lektury', 'Mickiewicz'),
(8, '2', 2, 2, 2, 'Krzyżacy', 'historyczne', 'Grunwald'),
(9, '3', 3, 2, 2, 'Lalka', 'lektury', 'kapitalizm'),
(10, '4', 4, 2, 2, 'Cierpienia młodego Wertera', 'powieści epistolarne', 'Sturm und Drang');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `oplata`
--

CREATE TABLE `oplata` (
  `id_oplaty` int(10) NOT NULL,
  `rodzaj` varchar(10) COLLATE utf8mb4_polish_ci NOT NULL,
  `opis` varchar(255) COLLATE utf8mb4_polish_ci DEFAULT NULL,
  `kwota` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `pracownik_bibliotekarz`
--

CREATE TABLE `pracownik_bibliotekarz` (
  `pesel` int(11) NOT NULL,
  `imie` varchar(10) COLLATE utf8mb4_polish_ci NOT NULL,
  `nazwisko` int(10) NOT NULL,
  `nickname` varchar(10) COLLATE utf8mb4_polish_ci DEFAULT NULL,
  `Column` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `statusrezerwacji`
--

CREATE TABLE `statusrezerwacji` (
  `id_statusRezerwacji` int(10) NOT NULL,
  `statusRezerwacji` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Zrzut danych tabeli `statusrezerwacji`
--

INSERT INTO `statusrezerwacji` (`id_statusRezerwacji`, `statusRezerwacji`) VALUES
(1, 'wolna'),
(2, 'zarezerwowana'),
(3, 'wypożyczona');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `uprawnienia`
--

CREATE TABLE `uprawnienia` (
  `id_uprawnienia` int(10) NOT NULL,
  `uprawnienia` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `uzytkownik_student_czytelnik`
--

CREATE TABLE `uzytkownik_student_czytelnik` (
  `pesel` int(11) NOT NULL,
  `imie` varchar(10) COLLATE utf8mb4_polish_ci NOT NULL,
  `nazwisko` varchar(10) COLLATE utf8mb4_polish_ci NOT NULL,
  `wydzial` varchar(10) COLLATE utf8mb4_polish_ci NOT NULL,
  `kierunek` varchar(20) COLLATE utf8mb4_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci COMMENT='pesel - PK,FK';

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `wydawnictwo`
--

CREATE TABLE `wydawnictwo` (
  `id_wydawnictwa` int(10) NOT NULL,
  `nazwa` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  `miejscowosc` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL,
  `kraj` varchar(50) COLLATE utf8mb4_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Zrzut danych tabeli `wydawnictwo`
--

INSERT INTO `wydawnictwo` (`id_wydawnictwa`, `nazwa`, `miejscowosc`, `kraj`) VALUES
(1, 'PWN', 'Kraków', 'Polska'),
(2, 'Helion', 'Warszawa', 'Polska'),
(3, 'Politechnika Krakows', 'Kraków', 'Polska'),
(4, 'Politechnika Rzeszow', 'Rzeszów', 'Polska'),
(5, 'Pearson', 'Londyn', 'Anglia');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `wypozyczenie_zwrot`
--

CREATE TABLE `wypozyczenie_zwrot` (
  `id_wypozyczenia_zwrotu` int(10) NOT NULL,
  `id_egzemplarza` int(10) NOT NULL COMMENT 'FK',
  `id_konta` int(10) NOT NULL COMMENT 'FK',
  `id_StatusRezerwacji` int(10) DEFAULT NULL COMMENT 'FK',
  `id_oplaty` int(10) DEFAULT NULL,
  `dataRezerwacji` date DEFAULT NULL,
  `dataWygasnieciaRezerwacji` date DEFAULT NULL,
  `miejsceOdbioru` varchar(25) COLLATE utf8mb4_polish_ci DEFAULT NULL,
  `dataWypozyczenia` date DEFAULT NULL,
  `dataZwrotu` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `audiobook`
--
ALTER TABLE `audiobook`
  ADD PRIMARY KEY (`id_audiobooka`),
  ADD KEY `id_ksiazki` (`id_ksiazki`);

--
-- Indeksy dla tabeli `autor`
--
ALTER TABLE `autor`
  ADD PRIMARY KEY (`id_autora`);

--
-- Indeksy dla tabeli `dzial`
--
ALTER TABLE `dzial`
  ADD PRIMARY KEY (`id_dzialu`);

--
-- Indeksy dla tabeli `egzemplarz`
--
ALTER TABLE `egzemplarz`
  ADD PRIMARY KEY (`id_egzemplarza`),
  ADD KEY `id_ksiazki` (`id_ksiazki`);

--
-- Indeksy dla tabeli `historiawypozyczenzwrotow`
--
ALTER TABLE `historiawypozyczenzwrotow`
  ADD KEY `id_konta` (`id_konta`),
  ADD KEY `id_wypozyczenia_zwrotu` (`id_wypozyczenia_zwrotu`);

--
-- Indeksy dla tabeli `ibuk`
--
ALTER TABLE `ibuk`
  ADD PRIMARY KEY (`id_ibuka`),
  ADD KEY `id_ksiazki` (`id_ksiazki`);

--
-- Indeksy dla tabeli `konto`
--
ALTER TABLE `konto`
  ADD PRIMARY KEY (`id_konta`),
  ADD KEY `pesel` (`pesel`),
  ADD KEY `id_uprawnienia` (`id_uprawnienia`);

--
-- Indeksy dla tabeli `koszyk`
--
ALTER TABLE `koszyk`
  ADD KEY `id_konta` (`id_konta`);

--
-- Indeksy dla tabeli `ksiazka`
--
ALTER TABLE `ksiazka`
  ADD PRIMARY KEY (`id_ksiazki`),
  ADD KEY `id_autora` (`id_autora`),
  ADD KEY `id_wydawnictwa` (`id_wydawnictwa`),
  ADD KEY `id_dzialu` (`id_dzialu`);

--
-- Indeksy dla tabeli `oplata`
--
ALTER TABLE `oplata`
  ADD PRIMARY KEY (`id_oplaty`);

--
-- Indeksy dla tabeli `pracownik_bibliotekarz`
--
ALTER TABLE `pracownik_bibliotekarz`
  ADD PRIMARY KEY (`pesel`);

--
-- Indeksy dla tabeli `statusrezerwacji`
--
ALTER TABLE `statusrezerwacji`
  ADD PRIMARY KEY (`id_statusRezerwacji`);

--
-- Indeksy dla tabeli `uprawnienia`
--
ALTER TABLE `uprawnienia`
  ADD PRIMARY KEY (`id_uprawnienia`);

--
-- Indeksy dla tabeli `uzytkownik_student_czytelnik`
--
ALTER TABLE `uzytkownik_student_czytelnik`
  ADD PRIMARY KEY (`pesel`);

--
-- Indeksy dla tabeli `wydawnictwo`
--
ALTER TABLE `wydawnictwo`
  ADD PRIMARY KEY (`id_wydawnictwa`);

--
-- Indeksy dla tabeli `wypozyczenie_zwrot`
--
ALTER TABLE `wypozyczenie_zwrot`
  ADD PRIMARY KEY (`id_wypozyczenia_zwrotu`),
  ADD KEY `id_egzemplarza` (`id_egzemplarza`),
  ADD KEY `id_StatusRezerwacji` (`id_StatusRezerwacji`),
  ADD KEY `id_oplaty` (`id_oplaty`),
  ADD KEY `id_konta` (`id_konta`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `audiobook`
--
ALTER TABLE `audiobook`
  MODIFY `id_audiobooka` int(10) NOT NULL AUTO_INCREMENT COMMENT 'PK';

--
-- AUTO_INCREMENT dla tabeli `autor`
--
ALTER TABLE `autor`
  MODIFY `id_autora` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT dla tabeli `dzial`
--
ALTER TABLE `dzial`
  MODIFY `id_dzialu` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT dla tabeli `egzemplarz`
--
ALTER TABLE `egzemplarz`
  MODIFY `id_egzemplarza` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT dla tabeli `ibuk`
--
ALTER TABLE `ibuk`
  MODIFY `id_ibuka` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT dla tabeli `konto`
--
ALTER TABLE `konto`
  MODIFY `id_konta` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT dla tabeli `ksiazka`
--
ALTER TABLE `ksiazka`
  MODIFY `id_ksiazki` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT dla tabeli `oplata`
--
ALTER TABLE `oplata`
  MODIFY `id_oplaty` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT dla tabeli `pracownik_bibliotekarz`
--
ALTER TABLE `pracownik_bibliotekarz`
  MODIFY `pesel` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT dla tabeli `statusrezerwacji`
--
ALTER TABLE `statusrezerwacji`
  MODIFY `id_statusRezerwacji` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT dla tabeli `uprawnienia`
--
ALTER TABLE `uprawnienia`
  MODIFY `id_uprawnienia` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT dla tabeli `uzytkownik_student_czytelnik`
--
ALTER TABLE `uzytkownik_student_czytelnik`
  MODIFY `pesel` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT dla tabeli `wydawnictwo`
--
ALTER TABLE `wydawnictwo`
  MODIFY `id_wydawnictwa` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT dla tabeli `wypozyczenie_zwrot`
--
ALTER TABLE `wypozyczenie_zwrot`
  MODIFY `id_wypozyczenia_zwrotu` int(10) NOT NULL AUTO_INCREMENT;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `audiobook`
--
ALTER TABLE `audiobook`
  ADD CONSTRAINT `audiobook_ibfk_1` FOREIGN KEY (`id_ksiazki`) REFERENCES `ksiazka` (`id_ksiazki`);

--
-- Ograniczenia dla tabeli `egzemplarz`
--
ALTER TABLE `egzemplarz`
  ADD CONSTRAINT `egzemplarz_ibfk_1` FOREIGN KEY (`id_ksiazki`) REFERENCES `ksiazka` (`id_ksiazki`);

--
-- Ograniczenia dla tabeli `historiawypozyczenzwrotow`
--
ALTER TABLE `historiawypozyczenzwrotow`
  ADD CONSTRAINT `historiawypozyczenzwrotow_ibfk_1` FOREIGN KEY (`id_konta`) REFERENCES `konto` (`id_konta`),
  ADD CONSTRAINT `historiawypozyczenzwrotow_ibfk_2` FOREIGN KEY (`id_wypozyczenia_zwrotu`) REFERENCES `wypozyczenie_zwrot` (`id_wypozyczenia_zwrotu`);

--
-- Ograniczenia dla tabeli `ibuk`
--
ALTER TABLE `ibuk`
  ADD CONSTRAINT `ibuk_ibfk_1` FOREIGN KEY (`id_ksiazki`) REFERENCES `ksiazka` (`id_ksiazki`);

--
-- Ograniczenia dla tabeli `konto`
--
ALTER TABLE `konto`
  ADD CONSTRAINT `konto_ibfk_1` FOREIGN KEY (`pesel`) REFERENCES `uzytkownik_student_czytelnik` (`pesel`),
  ADD CONSTRAINT `konto_ibfk_2` FOREIGN KEY (`pesel`) REFERENCES `pracownik_bibliotekarz` (`pesel`),
  ADD CONSTRAINT `konto_ibfk_3` FOREIGN KEY (`id_uprawnienia`) REFERENCES `uprawnienia` (`id_uprawnienia`);

--
-- Ograniczenia dla tabeli `koszyk`
--
ALTER TABLE `koszyk`
  ADD CONSTRAINT `koszyk_ibfk_1` FOREIGN KEY (`id_konta`) REFERENCES `konto` (`id_konta`);

--
-- Ograniczenia dla tabeli `ksiazka`
--
ALTER TABLE `ksiazka`
  ADD CONSTRAINT `ksiazka_ibfk_1` FOREIGN KEY (`id_autora`) REFERENCES `autor` (`id_autora`),
  ADD CONSTRAINT `ksiazka_ibfk_2` FOREIGN KEY (`id_wydawnictwa`) REFERENCES `wydawnictwo` (`id_wydawnictwa`),
  ADD CONSTRAINT `ksiazka_ibfk_3` FOREIGN KEY (`id_dzialu`) REFERENCES `dzial` (`id_dzialu`);

--
-- Ograniczenia dla tabeli `wypozyczenie_zwrot`
--
ALTER TABLE `wypozyczenie_zwrot`
  ADD CONSTRAINT `wypozyczenie_zwrot_ibfk_1` FOREIGN KEY (`id_egzemplarza`) REFERENCES `egzemplarz` (`id_egzemplarza`),
  ADD CONSTRAINT `wypozyczenie_zwrot_ibfk_2` FOREIGN KEY (`id_StatusRezerwacji`) REFERENCES `statusrezerwacji` (`id_statusRezerwacji`),
  ADD CONSTRAINT `wypozyczenie_zwrot_ibfk_3` FOREIGN KEY (`id_oplaty`) REFERENCES `oplata` (`id_oplaty`),
  ADD CONSTRAINT `wypozyczenie_zwrot_ibfk_4` FOREIGN KEY (`id_konta`) REFERENCES `konto` (`id_konta`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
