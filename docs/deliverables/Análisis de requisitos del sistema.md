# Documento de análisis de requisitos del sistema

## Introducción

Truco Beasts: Bardo en la Jungla® está basado en el juego del truco y pretendemos implementar todo lo que explicaremos a continuación (a excepción de las partidas de a 3 y a 8 jugadores).

El truco es un juego de cartas que se juega usando la baraja española de 40 naipes y su objetivo es llegar a 15 o 30 puntos utilizando engaño, estrategia y astucia. Es un juego para 2, 3, 4, 6 e incluso 8 jugadores en el que hay 2 equipos. La duración de una partida puede variar bastante, pero lo normal oscila entra 15 y 30 minutos.
El juego se divide en varias manos y cada mano está dividida en tres rondas. Al comienzo de cada mano cada jugador tiene tres cartas y comenzará jugando el jugador que está a la derecha del que repartió las cartas (llamado "mano").

### Dinámica de juego

Cada jugador irá tirando una de sus cartas (la que crea más conveniente) y de entre las cartas que hay en la mesa se conoce el ganador teniendo en cuenta los valores de las cartas:

![valores_cartas](https://github.com/user-attachments/assets/0f9b73cc-efe1-4782-b37e-d4fd382dc4d3)


_No existe orden vertical, las cartas colocadas encima de otras tienen el mismo valor._

El equipo que gane dos de las tres rondas de la mano sumará 1 punto. En caso de empate en una ronda se guardará el ganador para la siguiente (es decir, el que gane la siguiente ganará también la ronda en la que hubo empate). Lo mismo ocurrirá si vuelve a haber un empate. En caso de empates en las tres rondas, ganará el equipo al que pertenezca el jugador mano.
Además, existen tres cantos con los que ganar puntos adicionales. Estos son el truco (el canto más importante), el envido y la flor.

**El truco**: se puede cantar en cualquier momento de la mano. Si es aceptado (diciendo "quiero") la mano se jugará a 2 puntos, aunque también puede ser rechazado (diciendo "no quiero") dando por finalizada la mano y llevándose el equipo del jugador que lo cantó 1 punto o los puntos acumulados por subidas de la apuesta. Estas subidas se pueden realizar cantando "retruco" (a modo de respuesta al equipo que cantó "truco" inicialmente) siguiéndose la misma dinámica del "quiero" y el "no quiero". A su vez, este puede aumentar la apuesta aún más diciendo "vale cuatro".
**El envido**: sólo puede cantarse en la primera ronda y lo harán los últimos jugadores de cada equipo (cualquier jugador en caso de que sean sólo 2). Éste puede ser aceptado o rechazado de igual manera que el truco (aunque no se finaliza la mano en caso de ser rechazado). También puede subirse la apuesta con cantos como "envido" (otra vez), "real envido" y "falta envido". En el vídeo se muestra la puntuación de este canto de manera más detallada.
**La flor**: es un regalo del mazo y antes de crear la partida se puede elegir si jugar con ella o no. Consiste en tener tres cartas del mismo palo. Sólo podrán cantarla si la tienen realmente. En caso de que otro jugador también tenga flor puede responder a esta diciendo "contraflor" o "contraflor al resto". Se comparan los puntos de la misma forma que en el envido. Nuevamente, en el vídeo hay más detalle sobre este canto.

Para que los jugadores del mismo equipo se comuniquen existen señas (puesto a que no pueden ver las cartas de sus compañeros) relacionadas con las distintas cartas que hay. Estas señas también se suelen utilizar muy a menudo para engañar al equipo contrario.

[Para una noción más detallada del juego, este vídeo explicativo es de gran ayuda](https://www.youtube.com/watch?v=IAKDghOqNaM)

## Tipos de Usuarios / Roles

Administrador: Usuario con potestad para gestionar a otros usuarios y las partidas que juegan. Puede eliminar a otros usuarios en caso de mal comportamiento.

Jugador: Usuario estándar del sistema, puede jugar partidas, agregar a amigos o consultar sus logros.

Espectador: Usuario registrado, o no, con capacidad para visualizar partidas, pero no puede jugarlas ni interactuar con los usuarios que juegan.


## Historias de Usuario

A continuación se definen  todas las historias de usuario a implementar:
 ## Historia de Usuario Aspectos especificos del usuario:
### HU-1(NOISSUE): Poder loggearme ()
Como jugador quiero poder iniciar sesion o registrarme si no lo estoy
![Mockup_login](https://github.com/user-attachments/assets/1574d025-ee65-4937-b503-1d9ed27fd810) 
 ### HU-2(NOISSUE): Poder editar y borrar tu cuenta ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/70]) ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/159]) 
Como jugador quiero poder editar y borrar mi cuenta.
 ### HU-3(NOISSUE): Poder cerrar sesion ([])
Como jugador quiero poder cerrar sesion para que la web no se quede con mis datos
![Mockup_menuuser](https://github.com/user-attachments/assets/0c4db4e8-672e-47cf-b314-d5f005d3cb8d)
 ### HU-26(NOISSUE): Poder ver listado de partidas en curso y terminadas como administrador ([])
Como administador quiero poder ver un listado de partidas en curso y de partidas jugadas para tener constancia de todas las partidas que se juegan.
 ### HU-27(ISSUE#183): Poder ver listado de usuarios como administrador ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/183])
Como administrador quiero poder ver un listado de usuarios para saber en todo momento qué usuarios hay registrados.
 ### HU-28(ISSUE#293): Poder crear, ver, editar y borrar usuarios como administrador ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/293])
Como administrador quiero poder crear un usuario, ver los datos de un usuario, editar los datos de un usuario y borrar un usuario para tener control total sobre todos los usuarios que hay registrados.
 ## Historias de Usuario Creación y configuración partidas:
 ### HU-4(ISSUE#59): Crear partidas ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/59])
Como jugador quiero que el sistema me permita crear las partidas para poder jugar  
![Mockup_home](https://github.com/user-attachments/assets/fb8b849a-f27e-4e48-92ec-5b402253ecdf)
 ### HU-5(ISSUE#60): Editar los puntos de la partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/60])
Como jugador quiero que el sistema me permita poder editar las partidas para decidir la puntuación (15 o 30)  
 ### HU-6(ISSUE#61): Editar la flor en la partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/61])
Como jugador quiero que el sistema me permita editar las partidas para decidir si se juega con flor 
 ### HU-7(ISSUE#62): Editar numero de jugadores de la partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/62])
Como jugador quiero que el sistema me permita editar las paridas para decidir de cuantos jugadores será 
![Mockup_creacion](https://github.com/user-attachments/assets/f3682b2c-34ac-427d-96c6-7d473526d422)
 ### HU-8(ISSUE#63): Poder unirse a las partidas ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/63])
Como jugador quiero que el sistema me permita poder unirme a partidas para jugar 
![Mockup_union](https://github.com/user-attachments/assets/c244beb7-ca79-4549-989e-d677dfefa39a)

 ## Historias de Usuario Jugar al Truco:
 ### HU-9(ISSUE#143): Poder ver cartas ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/143])
Como jugador quiero poder ver mis cartas para poder crear una estrategia de juego.
 ### HU-29 (ISSUE#376) : Poder ver perfiles de Usuario en partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/376])
Como jugador quiero poder ver la foto y nombre de usuario de las personas que participan en la partida para poder ver con quién juego.
 ### HU-10(ISSUE#145): Poder cantar Truco ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/145])
Como jugador quiero poder cantar Truco para aumentar la puntuación de la mano.
 ### HU-11(ISSUE#146): Poder cantar Envido ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/146])
Como jugador quiero poder cantar Envido para poder aumentar la puntuación de mi equipo.
 ### HU-12(ISSUE#153): Poder cantar Flor ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/153])
Como jugador quiero poder cantar Flor para poder aumentar la puntuación de mi equipo.
![Mockup VerPartida](https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/blob/main/docs/mockups/Mockup_VerPartida.jpg)
 ### HU-13(ISSUE#144): Poder tirar carta ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/144])
Como jugador quiero poder tirar una carta para poder jugar.
 ### HU-14(ISSUE#147): Poder ver la puntuación en la partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/147])
Como jugador quiero poder ver la puntuación en la propia partida para saber cómo va mi equipo.
 ### HU-15(ISSUE#148): Poder ver cómo cambia la puntuación ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/148])
Como jugador quiero poder ver cómo cambia la puntuación en la partida para tener una mejor experiencia de usuario en cuanto a la visualización.
![Mockup MasPunt](https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/blob/main/docs/mockups/Mockup_MasPunt.jpg)
 ### HU-16(ISSUE#149): Poder decir Quiero y No Quiero ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/149])
Como jugador quiero poder aceptar o rechazar una propuesta para poder tomar decisiones en el juego.
 ### HU-17(ISSUE#150): Poder responder Envido y sus variantes ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/150])
Como jugador quiero poder responder Envido y sus variantes para subir la apuesta del Envido.
![Mockup Envido](https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/blob/main/docs/mockups/Mockup_Envido.jpg)
 ### HU-18(ISSUE#151): Poder responder Truco y sus variantes ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/151])
Como jugador quiero poder responder Truco y sus variantes para subir la apuesta del Truco.
![Mockup Truco](https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/blob/main/docs/mockups/Mockup_TirarCarta.jpg)
 ### HU-19(ISSUE#152): Poder responder Flor y sus variantes ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/152])
Como jugador quiero poder responder Flor y sus variantes para subir la apuesta del Flor.
![Mockup Flor](https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/blob/main/docs/mockups/Mockup_Flor.jpg)
 ### HU-37(ISSUE#384): Poder cambiar de equipo antes de iniciar la partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/384])
 Como jugador quiero poder cambiarme de equipo antes de que empiece la partida para mejorar mi experiencia de juego.
 ### HU-38(ISSUE#385): Expulsar jugador de una partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/385])
 Como creador de una partida quiero poder expulsar a un jugador para mejorar la experiencia de juego.
 

 ## Historias de Usuario Módulo social:
 ### HU-20(ISSUE#130): Poder buscar usuarios([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/130])
Como jugador quiero que el sistema me permita buscar usuarios poder agregar amigos.
 ![Mockup_buscar_usuario](https://github.com/user-attachments/assets/215631c4-ae2b-4f37-a4e5-53d0482a232d)
 ### HU-21(ISSUE#131): Poder ver mis amigos([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/131])
 Como jugador quiero poder ver los amigos que tengo agregados para chatear con ellos e invitarles a partidas.
  ![Mockup_ver_amigos](https://github.com/user-attachments/assets/9930c482-7b12-4484-9bc3-a376d4b37bde)
 ### HU-30(ISSUE#377): Poder añadir y eliminar amigos ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/377])
 Como usuario quiero poder añadir y eliminar amigos para poder gestionar mis amistades.
 ### HU-31(ISSUE#378): Gestionar solicitudes de amistad ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/378])
 Como usuario quiero poder aceptar o rechazar las solicitudes de amistad para poder gestionar mis amistades.
 ### HU-32(ISSUE#379): Poder chatear con amigos durante la partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/379])
 Como usuario quiero poder chatear con mis amigos mientras juego una partida para poder conversar con ellos.
 ### HU-33(ISSUE#380): Poder chatear con amigos fuera de una partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/380])
 Como usuario quiero poder chatear con mis amigos fuera de una partida para poder entablar conversación.
 ### HU-34(ISSUE#381): Poder invitar amigos conectados a una partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/381])
 Como usuario quiero poder invitar a mis amigos conectados a una partida para poder jugar con ellos.
 ### HU-35(ISSUE#382): Poder ver amigos conectados ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/382])
 Como usuario quiero poder ver los amigos que estén conectados para poder ver quién está en línea.
 ### HU-36(ISSUE#383): Poder recibir notificación de nuevo mensaje ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/383])
 Como usuario quiero poder recibir notificaciones de mensajes para ver si tengo mensajes sin leer.

 ## Historias de Usuario Módulo Estadístico:
 ### HU-22(ISSUE#387): Ver mis estadísticas ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/387])
Como jugador, quiero poder ver mis estadísticas para poder seguir mi progreso en el juego y mejorar mi rendimiento.
### HU-23(ISSUE#388): Ver logros ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/388])
Como jugador, quiero poder ver mis logros conseguidos y aquellos que me faltan por alcanzar, para motivarme a seguir jugando y completar todos los logros.
### HU-24(ISSUE#389): Crear, editar y eliminar logros ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/389])
Como administrador, quiero poder crear, editar y eliminar logros para mantener la experiencia de juego dinámica y actualizada, asegurando que los jugadores tengan nuevos objetivos que cumplir.
### HU-25(ISSUE#342): Ver ranking global ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/342]):
Como jugador quiero poder ver el ranking global para incentivarme a algún día estar primero.





## Diagrama conceptual del sistema

![modelo_conceptual](https://github.com/user-attachments/assets/c6f18a6a-d067-453d-9b87-15f83203038a)


## Reglas de Negocio
### R1-Condicion para la creacion y edicion de partidas
Solo los jugadores estarán autorizados a crear o editar  partidas mediante la pulsación de un botón. 
### R2-Edicion de partidas
El sistema debe permitir al jugador modificar los campos: ‘flor’, ‘nº jugadores’ y ‘puntuación’ en la edición de la partida. 
### R3-Unión a partidas privadas
El jugador habrá de unirse a una partida privada mediante un código único. 
### R4-Visibilidad para de las partidas
En caso de no estar logado, solo serán visibles las partidas públicas (espectador) 
### R5-Espectación de la partida
Al meterte en una partida como espectador no podrás interactuar con la partida 
### R6-Partidas concurrentes
Si un jugador está jugando una partida, no podrá unirse a otra partida o crear una nueva.
### R7-Conexión a la partida
Para comenzar una partida, todos los jugadores deben estar conectados al juego.
### R8-Gestionar una partida
El usuario creador de la partida será el único con capacidad para comenzar la partida y eliminar a usuarios de esta.
### R9-Invitación a la partida
Para poder invitar a un jugador a ver o jugar una partida, dicho jugador debe ser tu amigo y estar conectado.
### R10-Relevo del creador
Si el creador abandona la partida, deberá ser otro usuario conectado a la partida quien asuma dicho rol.
### R11-Chat de la partida
Los mensajes enviados durante la partida sólo deben estar disponible durante la partida.
### R12-Abandonar una partida
Si un jugador abandona una partida a la que estaba jugando, será el equipo contrario quien instantáneamente gane la partida.
