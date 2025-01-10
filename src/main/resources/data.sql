-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO authorities(id,authority) VALUES (1,'ADMIN');
INSERT INTO appusers(id,username,password,authority,last_connection) VALUES (1,'admin1','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',1,'2004-03-06 04:05');

-- Ten player users, named player1 with passwor 0wn3r
INSERT INTO authorities(id,authority) VALUES (2,'PLAYER');
INSERT INTO appusers(id,username,password,authority,last_connection) VALUES (2,'player1','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,'2004-03-06 04:05');
INSERT INTO appusers(id,username,password,authority,last_connection) VALUES (3,'player2','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,'2004-03-06 04:05');
INSERT INTO appusers(id,username,password,authority,last_connection) VALUES (4,'player3','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,'2004-03-06 04:05');
INSERT INTO appusers(id,username,password,authority,last_connection) VALUES (5,'player4','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,'2004-03-06 04:05');
INSERT INTO appusers(id,username,password,authority,last_connection) VALUES (6,'player5','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,'2004-03-06 04:05');
INSERT INTO appusers(id,username,password,authority,last_connection) VALUES (7,'player6','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,'2004-03-06 04:05');
INSERT INTO appusers(id,username,password,authority,last_connection) VALUES (8,'player7','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,'2004-03-06 04:05');
INSERT INTO appusers(id,username,password,authority,last_connection) VALUES (9,'player8','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,'2004-03-06 04:05');
INSERT INTO appusers(id,username,password,authority,last_connection) VALUES (10,'player9','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,'2004-03-06 04:05');
INSERT INTO appusers(id,username,password,authority,last_connection) VALUES (11,'player10','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,'2004-03-06 04:05');
INSERT INTO appusers(id,username,password,authority,last_connection) VALUES (12,'player11','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,'2004-03-06 04:05');

-- Insertar partidas
INSERT INTO Partida(id,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad,inicio,fin) VALUES (0,2,'WWWWW',FALSE,15,0,0,'PUBLICA','2024-11-08 20:28','2024-11-08 20:37');
INSERT INTO Partida(id,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad,inicio,fin) VALUES (1,2,'ABCDE',FALSE,15,1,2,'PUBLICA','2024-11-08 21:28', '2025-01-07 23:45');
INSERT INTO Partida(id,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad,inicio,fin) VALUES (2,4,'BCDEF',TRUE,30,1,2,'PUBLICA','2020-11-08 20:28:02','2024-11-08 20:37');
INSERT INTO Partida(id,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad,inicio,fin) VALUES (3,4,'BCDEG',TRUE,30,1,2,'PUBLICA','2024-11-08 20:28','2024-11-08 20:37');
INSERT INTO Partida(id,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad,inicio,fin) VALUES (4,2,'QWERT',TRUE,15,1,2,'PUBLICA','2023-11-08 10:12','2023-12-01 05:37');
INSERT INTO Partida(id,NumJugadores,Codigo,ConFlor,PuntosMaximos,PuntosEquipo1,PuntosEquipo2,Visibilidad,inicio,fin) VALUES (5,6,'YHGDT',TRUE,15,1,2,'PRIVADA','2025-01-01 17:06','2025-01-01 17:45:02');


-- Inserts de players
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (1,'jorge', 'el admin', 'admin@alum.us.es','http://localhost:8080/resources/images/jorge.jpg',1);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (2,'Paula', 'Rosa', 'paula@alum.us.es','http://localhost:8080/resources/images/paula.jpg',2);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (3,'Maria', 'del', 'pino@alum.us.es','http://localhost:8080/resources/images/canguro.jpg',3);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (4,'Eloy', 'Eloy', 'eloy@alum.us.es','http://localhost:8080/resources/images/quebuenascartas.jpg',4);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (5,'Ivan', 'FerLinandez', 'ivan@alum.us.es','http://localhost:8080/resources/images/mosca.jpg',5);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (6,'David', 'Fotografias', 'david@alum.us.es','http://localhost:8080/resources/images/cervecita.jpg',6);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (7,'Mi nombre', 'Mi apellido', 'micorreo@correo.de.verdad','http://localhost:8080/resources/images/gorila.jpg',7);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (8,'otro nombre', 'otro apellido', 'otrocorreo@correo.de.verdad','http://localhost:8080/resources/images/ballena.jpg',8);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (9,'lampi', 'la lampara', 'luz@alum.us.es','http://localhost:8080/resources/images/lampi.jpg',9);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (10,'mono', 'bananas', 'monin@alum.us.es','https://media1.tenor.com/m/xldK7lpjZyMAAAAd/goidrain-monkey.gif',10);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (11,'Ivo', 'Raimondi', 'ivorai@alum.us.es','http://localhost:8080/resources/images/jaguar.jpg',11);
INSERT INTO Jugadores(id, first_name,last_name, email, photo ,user_id) VALUES (12,'bip', 'bop', '10101@alum.us.es','http://localhost:8080/resources/images/robot.jpg',12);




-- INSERT INTO amigo(jugador_id,amigo_id) VALUES (3,1),(1,3),(7,1),(1,7),(2,7),(7,2);
-- INSERT INTO amigo(jugador_id,amigo_id) VALUES (10,8),(8,10);
-- INSERT INTO solicitud(solicitado_id,solicitante_id) VALUES (4,3);

-- Inserts de partida jugador

-- Partida 0 (2 jugadores) SUENA A JODA PERO IMPORTANTISIMO QUE ESTA PARTIDA TENGA UN SOLO PARTIDA JUGADOR, PORQUE SE USA EN LOS TESTS *calavera*
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (0, 0, 9, 0, TRUE, 0, 3, 1, 0, 1);

-- Partida 1 (2 jugadores)
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (1, 1, 1,                0, TRUE, 0, 2, 0, 1, 0);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (2, 1, 2,           1, FALSE, 0, 1, 2, 0, 1);

-- Partida 2 (4 jugadores)
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (3, 2, 1,      1, TRUE, 2, 3, 1, 1, 0);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (4, 2, 3,       2, FALSE, 2, 1, 3, 1, 0);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (5, 2, 4,           3, FALSE, 1, 3, 2, 0, 1);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (6, 2, 5,           4, FALSE, 1, 3, 2, 0, 1);

-- Partida 3 (4 jugadores)
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (7, 3, 2, 0, FALSE, 3, 2, 1, 1, 0);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (8, 3, 3, 2, FALSE, 2, 1, 3, 0, 1);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (9, 3, 4, 3, FALSE, 1, 3, 2, 1, 0);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (10, 3, 1, 5, TRUE, 2, 3, 12, 2, 1);

-- Partida 4 (2 jugadores)
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (11, 4, 2, 0, TRUE, 2, 1, 3, 0, 1);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (12, 4, 1, 4, FALSE, 1, 1, 5, 1, 1);

-- Partida 5 (6 jugadores)
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (13, 5, 2, 0, TRUE, 1, 2, 1, 1, 0);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (14, 5, 3, 1, FALSE, 2, 1, 3, 0, 1);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (15, 5, 4, 2, FALSE, 3, 2, 1, 1, 0);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (16, 5, 5, 3, FALSE, 1, 3, 2, 0, 1);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (17, 5, 6, 4, FALSE, 2, 1, 3, 1, 0);
INSERT INTO Partida_Jugador(id,game_id,player_id,posicion,is_creator, flores_cantadas, quieros_cantados, no_quieros_cantados, enganos, atrapado) 
VALUES (18, 5, 7, 5, FALSE, 2, 6, 3, 1, 0);

-- Inserts de cartas
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (1, 'ESPADAS', 1, 14, 'http://localhost:8080/resources/images/cartas/1espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (2, 'BASTOS', 1, 13, 'http://localhost:8080/resources/images/cartas/1bastos.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (3, 'ESPADAS', 7, 12, 'http://localhost:8080/resources/images/cartas/7espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (4, 'OROS', 7, 11, 'http://localhost:8080/resources/images/cartas/7oros.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (5, 'ESPADAS', 3, 10, 'http://localhost:8080/resources/images/cartas/3espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (6, 'BASTOS', 3, 10, 'http://localhost:8080/resources/images/cartas/3bastos.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (7, 'COPAS', 3, 10, 'http://localhost:8080/resources/images/cartas/3copas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (8, 'OROS', 3, 10, 'http://localhost:8080/resources/images/cartas/3oros.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (9, 'ESPADAS', 2, 9, 'http://localhost:8080/resources/images/cartas/2espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (10, 'BASTOS', 2, 9, 'http://localhost:8080/resources/images/cartas/2bastos.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (11, 'COPAS', 2, 9, 'http://localhost:8080/resources/images/cartas/2copas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (12, 'OROS', 2, 9, 'http://localhost:8080/resources/images/cartas/2oros.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (13, 'COPAS', 1, 8, 'http://localhost:8080/resources/images/cartas/1copas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (14, 'OROS', 1, 8, 'http://localhost:8080/resources/images/cartas/1oros.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (15, 'ESPADAS', 12, 7, 'http://localhost:8080/resources/images/cartas/12espadas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (16, 'BASTOS', 12, 7, 'http://localhost:8080/resources/images/cartas/12bastos.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (17, 'COPAS', 12, 7, 'http://localhost:8080/resources/images/cartas/12copas.jpg');
INSERT INTO Cartas(id, palo, valor, poder, foto) VALUES (18, 'OROS', 12, 7, 'http://localhost:8080/resources/images/cartas/12oros.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (19, 'ESPADAS', 11, 6, 'http://localhost:8080/resources/images/cartas/11espadas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (20, 'BASTOS', 11, 6, 'http://localhost:8080/resources/images/cartas/11bastos.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (21, 'COPAS', 11, 6, 'http://localhost:8080/resources/images/cartas/11copas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (22, 'OROS', 11, 6, 'http://localhost:8080/resources/images/cartas/11oros.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (23, 'ESPADAS', 10, 5, 'http://localhost:8080/resources/images/cartas/10espadas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (24, 'BASTOS', 10, 5, 'http://localhost:8080/resources/images/cartas/10bastos.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (25, 'COPAS', 10, 5, 'http://localhost:8080/resources/images/cartas/10copas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (26, 'OROS', 10, 5, 'http://localhost:8080/resources/images/cartas/10oros.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (27, 'ESPADAS', 6, 3, 'http://localhost:8080/resources/images/cartas/6espadas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (28, 'BASTOS', 6, 3, 'http://localhost:8080/resources/images/cartas/6bastos.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (29, 'COPAS', 6, 3, 'http://localhost:8080/resources/images/cartas/6copas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (30, 'OROS', 6, 3, 'http://localhost:8080/resources/images/cartas/6oros.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (31, 'ESPADAS', 5, 2, 'http://localhost:8080/resources/images/cartas/5espadas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (32, 'BASTOS', 5, 2, 'http://localhost:8080/resources/images/cartas/5bastos.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (33, 'COPAS', 5, 2, 'http://localhost:8080/resources/images/cartas/5copas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (34, 'OROS', 5, 2, 'http://localhost:8080/resources/images/cartas/5oros.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (35, 'ESPADAS', 4, 1, 'http://localhost:8080/resources/images/cartas/4espadas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (36, 'BASTOS', 4, 1, 'http://localhost:8080/resources/images/cartas/4bastos.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (37, 'COPAS', 4, 1, 'http://localhost:8080/resources/images/cartas/4copas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (38, 'OROS', 4, 1, 'http://localhost:8080/resources/images/cartas/4oros.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (39, 'BASTOS', 7, 4, 'http://localhost:8080/resources/images/cartas/7bastos.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (40, 'COPAS', 7, 4, 'http://localhost:8080/resources/images/cartas/7copas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (41, 'NADA', 0, 0, 'http://localhost:8080/resources/images/cartas/1carta.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (42, 'NADA', 0, 0, 'http://localhost:8080/resources/images/cartas/2cartas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (43, 'NADA', 0, 0, 'http://localhost:8080/resources/images/cartas/3cartas.jpg');
INSERT INTO Cartas(id,palo,valor,poder,foto) VALUES (44, 'NADA', 0, 0, 'http://localhost:8080/resources/images/cartas/mazo.png');


-- Inserts de Logros

INSERT INTO Logros(id,name,descripcion,valor,metrica,imagencita, oculto) VALUES (0,'Sos la bestia del truco', 'Obtene mas de 5 victorias en el Truco', 5, 'VICTORIAS','http://localhost:8080/resources/images/trofeos/trofeo1.jpg', TRUE);
INSERT INTO Logros(id,name,descripcion,valor,metrica,imagencita, oculto) VALUES (1,'Empezando', 'Juga 1 partida', 1, 'PARTIDAS_JUGADAS','http://localhost:8080/resources/images/trofeos/trofeo2.jpg', FALSE);
INSERT INTO Logros(id,name,descripcion,valor,metrica,imagencita, oculto) VALUES (2,'Un pequeño gran paso', 'Gana tu primera partida', 1, 'VICTORIAS','http://localhost:8080/resources/images/trofeos/trofeo3.jpg', FALSE);
INSERT INTO Logros(id,name,descripcion,valor,metrica,imagencita, oculto) VALUES (3,'Malardo', 'Perde tu primera partida', 1, 'DERROTAS','http://localhost:8080/resources/images/trofeos/trofeo4.jpg', TRUE);
INSERT INTO Logros(id,name,descripcion,valor,metrica,imagencita, oculto) VALUES (4,'Comenzo el vicio', 'Juga durante media hora', 1800, 'TIEMPO_JUGADO','http://localhost:8080/resources/images/trofeos/trofeo5.jpg', FALSE);
INSERT INTO Logros(id,name,descripcion,valor,metrica,imagencita, oculto) VALUES (5,'El arte del engaño', 'Engaña con tu envido 1 vez', 1, 'NUMERO_ENGANOS','http://localhost:8080/resources/images/trofeos/trofeo6.jpg', TRUE);
INSERT INTO Logros(id,name,descripcion,valor,metrica,imagencita, oculto) VALUES (6,'Tendrias que abrir una jardineria', 'Canta al menos 1 flor', 1, 'NUMERO_FLORES','http://localhost:8080/resources/images/trofeos/trofeo7.jpg', FALSE);
INSERT INTO Logros(id,name,descripcion,valor,metrica,imagencita, oculto) VALUES (7,'Vamos con todooo', 'Acepta al menos 1 canto', 1, 'QUIEROS','http://localhost:8080/resources/images/trofeos/trofeo8.jpg', FALSE);
INSERT INTO Logros(id,name,descripcion,valor,metrica,imagencita, oculto) VALUES (8,'A veces la calma lo es todo', 'Deci al menos 1 no quiero a algun canto', 1, 'NO_QUIEROS','http://localhost:8080/resources/images/trofeos/trofeo9.jpg', FALSE);
INSERT INTO Logros(id,name,descripcion,valor,metrica,imagencita, oculto) VALUES (9,'Alguna vez te iban a agarrar', 'Se atrapado con un envido bajo 1 vez', 1, 'ATRAPADO','http://localhost:8080/resources/images/trofeos/trofeo10.jpg', TRUE);
