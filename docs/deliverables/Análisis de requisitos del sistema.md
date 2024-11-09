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

El truco: se puede cantar en cualquier momento de la mano. Si es aceptado (diciendo "quiero") la mano se jugará a 2 puntos, aunque también puede ser rechazado (diciendo "no quiero") dando por finalizada la mano y llevándose el equipo del jugador que lo cantó 1 punto o los puntos acumulados por subidas de la apuesta. Estas subidas se pueden realizar cantando "retruco" (a modo de respuesta al equipo que cantó "truco" inicialmente) siguiéndose la misma dinámica del "quiero" y el "no quiero". A su vez, este puede aumentar la apuesta aún más diciendo "vale cuatro".
El envido: sólo puede cantarse en la primera ronda y lo harán los últimos jugadores de cada equipo (cualquier jugador en caso de que sean sólo 2). Éste puede ser aceptado o rechazado de igual manera que el truco (aunque no se finaliza la mano en caso de ser rechazado). También puede subirse la apuesta con cantos como "envido" (otra vez), "real envido" y "falta envido". En el vídeo se muestra la puntuación de este canto de manera más detallada.
La flor: es un regalo del mazo y antes de crear la partida se puede elegir si jugar con ella o no. Consiste en tener tres cartas del mismo palo. Sólo podrán cantarla si la tienen realmente. En caso de que otro jugador también tenga flor puede responder a esta diciendo "contraflor" o "contraflor al resto". Se comparan los puntos de la misma forma que en el envido. Nuevamente, en el vídeo hay más detalle sobre este canto.

Para que los jugadores del mismo equipo se comuniquen existen señas (puesto a que no pueden ver las cartas de sus compañeros) relacionadas con las distintas cartas que hay. Estas señas también se suelen utilizar muy a menudo para engañar al equipo contrario.

[Para una noción más detallada del juego, este vídeo explicativo es de gran ayuda](https://www.youtube.com/watch?v=IAKDghOqNaM)

## Tipos de Usuarios / Roles

< Nombre Rol >: < Breve descripción del rol >

Administrador: Usuario con potestad para gestionar a otros usuarios y las partidas que juegan. Puede eliminar a otros usuarios en caso de mal comportamiento._

Jugador: Usuario estándar del sistema, puede jugar partidas, agregar a amigos o consultar sus logros._

Espectador: Usuario registrado, o no, con capacidad para visualizar partidas, pero no puede jugarlas ni interactuar con los usuarios que juegan. _


## Historias de Usuario

A continuación se definen  todas las historias de usuario a implementar:
 ## Historia de Usuario Creación y configuración partidas:
 ### HU-(NOISSUE): Poder loggearme
Como jugador quiero poder iniciar sesion o registrarme si no lo estoy
![Mockup_login](https://github.com/user-attachments/assets/1574d025-ee65-4937-b503-1d9ed27fd810) 
 ### HU-(NOISSUE): Poder borrar la cuenta ([])
Como jugador quiero poder borrar la cuenta para dejar de jugar si me canso
 ### HU-(ISSUE#59): Crear partidas ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/59])
Como jugador quiero que el sistema me permita crear las partidas para poder jugar  
![Mockup_home](https://github.com/user-attachments/assets/fb8b849a-f27e-4e48-92ec-5b402253ecdf)
 ### HU-(ISSUE#60): Editar los puntos de la partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/60])
Como jugador quiero que el sistema me permita poder editar las partidas para decidir la puntuación (15 o 30)  
 ### HU-(ISSUE#61): Editar la flor en la partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/61])
Como jugador quiero que el sistema me permita editar las partidas para decidir si se juega con flor 
 ### HU-(ISSUE#62): Editar numero de jugadores de la partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/62])
Como jugador quiero que el sistema me permita editar las paridas para decidir de cuantos jugadores será 
![Mockup_creacion](https://github.com/user-attachments/assets/f3682b2c-34ac-427d-96c6-7d473526d422)
 ### HU-(ISSUE#63): Poder unirse a las partidas ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/63])
Como jugador quiero que el sistema me permita poder unirme a partidas para jugar 
![Mockup_union](https://github.com/user-attachments/assets/c244beb7-ca79-4549-989e-d677dfefa39a)



 ### HU-(ISSUE#143): Poder ver cartas ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/143])
Como jugador quiero poder ver mis cartas para poder crear una estrategia de juego.
 ### HU-(ISSUE#145): Poder cantar Truco ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/145])
Como jugador quiero poder cantar Truco para aumentar la puntuación de la mano.
 ### HU-(ISSUE#146): Poder cantar Envido ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/146])
Como jugador quiero poder cantar Envido para poder aumentar la puntuación de mi equipo.
 ### HU-(ISSUE#153): Poder cantar Flor ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/153])
Como jugador quiero poder cantar Flor para poder aumentar la puntuación de mi equipo.
![Mockup VerPartida](https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/blob/main/docs/mockups/Mockup_VerPartida.jpg)

 ### HU-(ISSUE#144): Poder tirar carta ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/144])
Como jugador quiero poder tirar una carta para poder jugar.
 ### HU-(ISSUE#147): Poder ver la puntuación en la partida ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/147])
Como jugador quiero poder ver la puntuación en la propia partida para saber cómo va mi equipo.
 ### HU-(ISSUE#148): Poder ver cómo cambia la puntuación ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/148])
Como jugador quiero poder ver cómo cambia la puntuación en la partida para tener una mejor experiencia de usuario en cuanto a la visualización.
![Mockup MasPunt](https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/blob/main/docs/mockups/Mockup_MasPunt.jpg)


 ### HU-(ISSUE#149): Poder decir Quiero y No Quiero ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/149])
Como jugador quiero poder aceptar o rechazar una propuesta para poder tomar decisiones en el juego.
 ### HU-(ISSUE#150): Poder responder Envido y sus variantes ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/150])
Como jugador quiero poder responder Envido y sus variantes para subir la apuesta del Envido.
![Mockup Envido](https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/blob/main/docs/mockups/Mockup_Envido.jpg)
 ### HU-(ISSUE#151): Poder responder Truco y sus variantes ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/151])
Como jugador quiero poder responder Truco y sus variantes para subir la apuesta del Truco.
![Mockup Truco](https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/blob/main/docs/mockups/Mockup_TirarCarta.jpg)
 ### HU-(ISSUE#152): Poder responder Flor y sus variantes ([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/152])
Como jugador quiero poder responder Flor y sus variantes para subir la apuesta del Flor.
![Mockup Flor](https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/blob/main/docs/mockups/Mockup_Flor.jpg)
 
 ### HU-(NOISSUE): Poder cerrar sesion ([])
Como jugador quiero poder cerrar sesion para que la web no se quede con mis datos
![Mockup_menuuser](https://github.com/user-attachments/assets/0c4db4e8-672e-47cf-b314-d5f005d3cb8d)
 ## HU-(ISSUE#130): Poder buscar usuarios([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/130]):
Como jugador quiero que el sistema me permita buscar usuarios poder agregar amigos.
![Mockup_chat - copia](https://github.com/user-attachments/assets/f5e8999d-a6f5-4689-8801-f3cc5362cf66)
 ## HU-(ISSUE#131): Poder ver mis amigos([https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/issues/131]):
Como jugador quiero poder ver los amigos que tengo agregados para chatear con ellos e invitarles a partidas.
 ## Historia de Usuario Aspecto social:
 ## Historia de Usuario Aspectos especificos del usuario:

|Descripción de la historia siguiendo el esquema:  "Como <rol> quiero que el sistema  <funcionalidad>  para poder <objetivo/beneficio>."| 
|-----|
|Mockups (prototipos en formato imagen de baja fidelidad) de la interfaz de usuario del sistema|
|Decripción de las interacciones concretas a realizar con la interfaz de usuario del sistema para lleva a cabo la historia. |



## Diagrama conceptual del sistema
_En esta sección debe proporcionar un diagrama UML de clases que describa el modelo de datos a implementar en la aplicación. Este diagrama estará anotado con las restricciones simples (de formato/patrón, unicidad, obligatoriedad, o valores máximos y mínimos) de los datos a gestionar por la aplicación. _

_Recuerde que este es un diagrama conceptual, y por tanto no se incluyen los tipos de los atributos, ni clases específicas de librerías o frameworks, solamente los conceptos del dominio/juego que pretendemos implementar_


![modelo_conceptual](https://github.com/user-attachments/assets/c6f18a6a-d067-453d-9b87-15f83203038a)


_Si vuestro diagrama se vuelve demasiado complejo, siempre podéis crear varios diagramas para ilustrar todos los conceptos del dominio. Por ejemplo podríais crear un diagrama para cada uno de los módulos que quereis abordar. La única limitación es que hay que ser coherente entre unos diagramas y otros si nos referimos a las mismas clases_

_Puede usar la herramienta de modelado que desee para generar sus diagramas de clases. Para crear el diagrama anterior nosotros hemos usado un lenguaje textual y librería para la generación de diagramas llamada Mermaid_

_Si deseais usar esta herramienta para generar vuestro(s) diagramas con esta herramienta os proporcionamos un [enlace a la documentación oficial de la sintaxis de diagramas de clases de _ermaid](https://mermaid.js.org/syntax/classDiagram.html)_

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
### R7-Cuenta atrás en un turno
Cuando es el turno de un jugador en la ronda, este tiene un tiempo límite para actuar, pasado este tiempo comienza una cuenta atrás en pantalla que termina dando la victoria al equipo contrario si el jugador no reacciona a tiempo.
