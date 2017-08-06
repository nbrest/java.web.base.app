-- ********************************
-- *** Last updated: 2017/08/05 ***
-- ********************************

-- Create schema:
DROP SCHEMA IF EXISTS kameHouse;
CREATE SCHEMA kameHouse;

-- Use schema:
USE kameHouse;

-- Create user:
-- Use '%' instead of 'localhost' to allow to connect remotely with this user
DROP USER IF EXISTS 'kameHouseUser'@'localhost';
CREATE USER 'kameHouseUser'@'localhost' identified by 'kameHousePwd';

-- Grant privileges:
GRANT ALL PRIVILEGES ON kameHouse.* TO 'kameHouseUser'@'localhost';

-- Tables for mysql:
DROP TABLE IF EXISTS dragonballuser;
DROP TABLE IF EXISTS hibernate_sequence;

CREATE TABLE dragonballuser (
  id bigint(20) NOT NULL,
  age int(11) DEFAULT NULL,
  email varchar(255) NOT NULL,
  powerlevel int(11) DEFAULT NULL,
  stamina int(11) DEFAULT NULL,
  username varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_username (username),
  UNIQUE KEY UK_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE hibernate_sequence (
   next_val bigint(20) DEFAULT NULL
 ) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Hibernate expects hibernate_sequence to have an initial value or it will fail
INSERT INTO hibernate_sequence(next_val) VALUES (0);

-- Query application tables:
SELECT * FROM dragonballuser;
SELECT * FROM hibernate_sequence;

-- Query user permissions in mysql:
SELECT * FROM mysql.user;

-- Export table creation script in mysql:
show create table dragonballuser;
show create table hibernate_sequence;
