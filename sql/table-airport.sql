DROP TABLE IF EXISTS names;

CREATE TABLE names (
  id int unsigned AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  PRIMARY KEY(id)
);

INSERT INTO names (id, name) VALUES (1, "koyama");
INSERT INTO names (id, name) VALUES (2, "tanaka");
INSERT INTO names (id, name) VALUES (3, "suzuki");



DROP TABLE IF EXISTS airports;

CREATE TABLE airports(
  airportCode VARCHAR(3) PRIMARY KEY,
  airportName VARCHAR(16) NOT NULL,
  country VARCHAR(16) NOT NULL
);

INSERT INTO airports (airportCode, airportName, country) VALUES('NRT' , ' NARITA' , 'JAPAN');
INSERT INTO airports (airportCode, airportName, country) VALUES('HND' , ' HANEDA' , 'JAPAN');
INSERT INTO airports (airportCode, airportName, country) VALUES('KIX' , ' KANSAI' , 'JAPAN');
INSERT INTO airports (airportCode, airportName, country) VALUES('NGO' , ' CHUBU' , 'JAPAN');
INSERT INTO airports (airportCode, airportName, country) VALUES('JFK' , ' NEW YORK' , 'AMERICA');
INSERT INTO airports (airportCode, airportName, country) VALUES('BOS' , ' BOSTON' , 'AMERICA');
INSERT INTO airports (airportCode, airportName, country) VALUES('ORD' , ' CHICAGO' , 'AMERICA');
INSERT INTO airports (airportCode, airportName, country) VALUES('DFW' , ' DALLAS' , 'AMERICA');
INSERT INTO airports (airportCode, airportName, country) VALUES('SFO' , ' SAN FRANCISCO' , 'AMERICA');
INSERT INTO airports (airportCode, airportName, country) VALUES('LAX' , ' LOS ANGELES' , 'AMERICA');
INSERT INTO airports (airportCode, airportName, country) VALUES('SAN' , ' SAN DIEGO' , 'AMERICA');
INSERT INTO airports (airportCode, airportName, country) VALUES('YVR' , ' VANCOUVER' , 'AMERICA');
INSERT INTO airports (airportCode, airportName, country) VALUES('HNL' , ' HONOLULU' , 'AMERICA');
INSERT INTO airports (airportCode, airportName, country) VALUES('LHR' , ' LONDON' , 'ENGLAND');
INSERT INTO airports (airportCode, airportName, country) VALUES('CDG' , ' PARIS' , 'FRANCE');
INSERT INTO airports (airportCode, airportName, country) VALUES('FRA' , ' FRANKFURT' , 'DEUTSCHLAND');
INSERT INTO airports (airportCode, airportName, country) VALUES('DME' , ' MOSCOW' , 'RUSSIA');
INSERT INTO airports (airportCode, airportName, country) VALUES('PVG' , ' PUDONG' , 'CHINA');
INSERT INTO airports (airportCode, airportName, country) VALUES('SHA' , ' HONGQIAO' , 'CHINA');
INSERT INTO airports (airportCode, airportName, country) VALUES('HKG' , ' HONG KONG' , 'CHINA');
INSERT INTO airports (airportCode, airportName, country) VALUES('BJS' , ' BEIJING' , 'CHINA');
INSERT INTO airports (airportCode, airportName, country) VALUES('DLC' , ' DALIAN' , 'CHINA');
INSERT INTO airports (airportCode, airportName, country) VALUES('TSN' , ' TIANJIN' , 'CHINA');
INSERT INTO airports (airportCode, airportName, country) VALUES('CAN' , ' GUANGZHOU' , 'CHINA');
INSERT INTO airports (airportCode, airportName, country) VALUES('ICN' , ' INCHOEN' , 'KOREA');
INSERT INTO airports (airportCode, airportName, country) VALUES('GMP' , ' GIMPO' , 'KOREA');
INSERT INTO airports (airportCode, airportName, country) VALUES('PUS' , ' BUSAN' , 'KOREA');
INSERT INTO airports (airportCode, airportName, country) VALUES('TPE' , ' TAOYUAN' , 'TAIWAN');
INSERT INTO airports (airportCode, airportName, country) VALUES('TSA' , ' SONGSHAN' , 'TAIWAN');
INSERT INTO airports (airportCode, airportName, country) VALUES('KHH' , ' KAOHSIUNG' , 'TAIWAN');
INSERT INTO airports (airportCode, airportName, country) VALUES('SGN' , ' HO CHI MINH CITY' , 'VIETNAM');
INSERT INTO airports (airportCode, airportName, country) VALUES('HAN' , ' HANOI' , 'VIETNAM');
INSERT INTO airports (airportCode, airportName, country) VALUES('BKK' , ' BANGKOK' , 'THAILAND');
INSERT INTO airports (airportCode, airportName, country) VALUES('KUL' , ' KUALA LUMPUR' , 'MALAYSIA');
INSERT INTO airports (airportCode, airportName, country) VALUES('SIN' , ' SINGAPORE' , 'SINGAPORE');
INSERT INTO airports (airportCode, airportName, country) VALUES('CGK' , ' JAKARTA' , 'INDONESIA');
INSERT INTO airports (airportCode, airportName, country) VALUES('MNL' , ' MANILA' , 'PHILIPPINES');
INSERT INTO airports (airportCode, airportName, country) VALUES('DEL' , ' DELHI' , 'INDIA');
INSERT INTO airports (airportCode, airportName, country) VALUES('GUM' , ' GUAM' , 'AMERICA');
INSERT INTO airports (airportCode, airportName, country) VALUES('SYD' , ' SYDNEY' , 'NEWZEALAND');
