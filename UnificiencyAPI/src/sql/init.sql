CREATE DATABASE IF NOT EXISTS `Unificiency` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'msp'@'localhost' IDENTIFIED BY 'msp';
GRANT ALL PRIVILEGES ON `Unificiency`.* TO 'msp'@'localhost' IDENTIFIED BY 'msp';

CREATE USER IF NOT EXISTS 'msp'@'%' IDENTIFIED BY 'msp';
GRANT ALL PRIVILEGES ON `Unificiency`.* TO 'msp'@'%' IDENTIFIED BY 'msp';

FLUSH PRIVILEGES;