-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO authorities(id,authority) VALUES (1,'ADMIN');
INSERT INTO appusers(id,username,password,authority) VALUES (1,'admin1','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',1);

-- Ten player users, named player1 with passwor 0wn3r
INSERT INTO authorities(id,authority) VALUES (2,'PLAYER');
INSERT INTO appusers(id,username,password,authority) VALUES (3,'MNB3620','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (4,'player1','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (5,'player2','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (6,'player3','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (7,'player4','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (8,'player5','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (9,'player6','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (10,'player7','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (11,'player8','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (12,'player9','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (13,'player10','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (14,'hmf2475','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (15,'SJJ8308','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (16,'PHB1628','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (17,'TBP9161','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,password,authority) VALUES (18,'MVS9857','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO Partida(id,Estado,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad) VALUES (1,'WAITING',2,'ABCDE',FALSE,15,1,2,'PUBLICA');
INSERT INTO Partida(id,Estado,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad) VALUES (2,'ACTIVE',4,'BCDEF',TRUE,30,1,2,'PUBLICA');
INSERT INTO Partida(id,Estado,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad) VALUES (3,'FINISHED',2,'QWERT',FALSE,15,1,2,'PUBLICA');
INSERT INTO Partida(id,Estado,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad) VALUES (4,'WAITING',6,'YHGDT',FALSE,15,1,2,'PRIVADA');
INSERT INTO Partida(id,Estado,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad) VALUES (5,'WAITING',2,'APCDE',FALSE,15,1,2,'PUBLICA');
INSERT INTO Partida(id,Estado,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad) VALUES (6,'ACTIVE',4,'PCDEF',TRUE,30,1,2,'PUBLICA');
INSERT INTO Partida(id,Estado,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad) VALUES (7,'ACTIVE',2,'PWERT',FALSE,15,1,2,'PUBLICA');



-- Inserts de players
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (1,'Ivo', 'Raimondi', 'ivorai@alum.us.es','http://localhost:8080/resources/images/jaguar.jpg',4);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (2,'Paula', 'Rosa', 'paula@alum.us.es','http://localhost:8080/resources/images/paula.jpg',5);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (3,'Maria', 'del', 'pino@alum.us.es','http://localhost:8080/resources/images/canguro.jpg',12);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (4,'Eloy', 'Eloy', 'eloy@alum.us.es','http://localhost:8080/resources/images/quebuenascartas.jpg',6);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (5,'Ivan', 'FerLinandez', 'ivan@alum.us.es','http://localhost:8080/resources/images/paula.jpg',7);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (6,'jorge', 'el admin', 'admin@alum.us.es','http://localhost:8080/resources/images/jorge.jpg',1);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (7,'David', 'Fotografias', 'david@alum.us.es','http://localhost:8080/resources/images/cervecita.jpg',9);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (8,'Mi nombre', 'Mi apellido', 'micorreo@correo.de.verdad','http://localhost:8080/resources/images/gorila.jpg',10);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (9,'otro nombre', 'otro apellido', 'otrocorreo@correo.de.verdad','http://localhost:8080/resources/images/ballena.jpg',11);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (10,'lampi', 'la lampara', 'luz@alum.us.es','http://localhost:8080/resources/images/lampi.jpg',3);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (11,'mono', 'bananas', 'monin@alum.us.es','http://localhost:8080/resources/images/macaco.jpg',13);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (12,'bip', 'bop', '10101@alum.us.es','http://localhost:8080/resources/images/robot.jpg',14);

-- faltan algunos que no tienen jugador asociado
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (1, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (2, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (3, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (4, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (5, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (6, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (7, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (8, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (9, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (10, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (11, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (12, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (13, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (14, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (15, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (16, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (17, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (18, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');

INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (19, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (20, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (21, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (22, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (23, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (24, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (25, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (26, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (27, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (28, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (29, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (30, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (31, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (32, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (33, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (34, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (35, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (36, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (37, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (38, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (39, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (40, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');