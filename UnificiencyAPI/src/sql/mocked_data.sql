-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Nov 28, 2016 at 10:21 PM
-- Server version: 10.1.16-MariaDB
-- PHP Version: 5.6.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `unificiency`
--

--
-- Dumping data for table `building`
--

INSERT INTO `building` (`id`, `city`, `country`, `street`, `street_num`, `zipcode`) VALUES
(1, 'München', 'Deutschland', 'Oettingenstr.', '67', '80538 '),
(2, 'München', 'Deutschland', 'Amalienstr.', '47', '80799');

--
-- Dumping data for table `building_rooms`
--

INSERT INTO `building_rooms` (`building_id`, `name`, `num_seats`) VALUES
(1, 'U139', 24),
(1, 'U133', 30);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
