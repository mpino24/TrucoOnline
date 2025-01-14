# Plan de Pruebas

## 1. Introducción

Este documento describe el plan de pruebas para el proyecto **Truco Beasts: Bardo en la Jungla** desarrollado en el marco de la asignatura **Diseño y Pruebas 1** por el grupo **L6-5**. El objetivo del plan de pruebas es garantizar que el software desarrollado cumple con los requisitos especificados en las historias de usuario y que se han realizado las pruebas necesarias para validar su funcionamiento.

## 2. Alcance

El alcance de este plan de pruebas incluye:

- Pruebas unitarias.
  - Pruebas unitarias de backend incluyendo pruebas servicios o repositorios
  - Pruebas unitarias de frontend: pruebas de las funciones javascript creadas en frontend.
  - Pruebas unitarias de interfaz de usuario. Usan la interfaz de  usuario de nuestros componentes frontend.
- Pruebas de integración.  En nuestro caso principalmente son pruebas de controladores.

## 3. Estrategia de Pruebas

### 3.1 Tipos de Pruebas

#### 3.1.1 Pruebas Unitarias
Las pruebas unitarias se realizarán para verificar el correcto funcionamiento de los componentes individuales del software. Se utilizarán herramientas de automatización de pruebas como **JUnit** en background y .

#### 3.1.2 Pruebas de Integración
Las pruebas de integración se enfocarán en evaluar la interacción entre los distintos módulos o componentes del sistema, nosotros las realizaremos a nivel de API, probando nuestros controladores Spring.

## 4. Herramientas y Entorno de Pruebas

### 4.1 Herramientas
- **Maven**: Gestión de dependencias y ejecución de las pruebas.
- **JUnit**: Framework de pruebas unitarias.
- **Jacoco**: Generación de informes de cobertura de código.
- **Jest**: Framework para pruebas unitarias en javascript.
- **React-test**: Liberaría para la creación de pruebas unitarias de componentes React.

### 4.2 Entorno de Pruebas
Las pruebas se ejecutarán en el entorno de desarrollo y, eventualmente, en el entorno de pruebas del servidor de integración continua.

## 5. Planificación de Pruebas

### 5.1 Cobertura de Pruebas

El informe de cobertura de pruebas es el siguiente:
![jacoco](https://github.com/user-attachments/assets/b8123fd6-8ac5-4203-8e7b-bc093a2832b2)



### 5.2 Matriz de Trazabilidad

| Historia de Usuario | Prueba | Descripción | Estado |Tipo |
|---------------------|--------|-------------|--------|--------|
| HU-1(NOISSUE): Poder loggearme | [UTB-1:AuthControllerTests](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/auth/AuthControllerTests.java) | Verifica que un usuario puede iniciar sesión con credenciales válidas. | Implementada | Unitaria en backend |
| HU-2(NOISSUE): Poder borrar la cuenta | [UTB-4:TestJugadorService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/jugador/TestJugadorService.java) | Verifica que un usuario puede borrar su cuenta. | Implementada | Unitaria en backend |
| HU-3(NOISSUE): Poder cerrar sesion | [UTF-1:logout.test.js](/frontend/src/auth/logout/logout.test.js) | Verifica que un usuario puede cerrar su sesión y se le quita el JWT actual | Implementada | Unitaria en frontend |
| HU-4(ISSUE#59): Crear partidas | [UTB-2:PartidaControllerTests](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\partida\PartidaControllerTests.java) | Asegura que un usuario puede crear una partida. | Implementada | Unitaria en backend |
| HU-5(ISSUE#60): Editar los puntos de la partida | [UTB-5:PartidaServiceTests](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\partida\PartidaServiceTests.java) | Verifica que un usuario puede decidir los puntos de la partida antes de crearla. | Implementada | Unitaria en backend |
| HU-6(ISSUE#61): Editar la flor en la partida | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un usuario puede decidir jugar o no con flor en la partida antes de crearla. | Implementada | Unitaria en backend |
| HU-7(ISSUE#62): Editar numero de jugadores de la partida | [UTB-5:PartidaServiceTests](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\partida\PartidaServiceTests.java) | Verifica que un usuario puede decidir el número de jugadores de la partida antes de crearla. | Implementada | Unitaria en backend |
| HU-8(ISSUE#63): Poder unirse a las partidas | [UTB-9:PartidaJugadorServiceTest](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\partidaJugador\PartidaJugadorServiceTest.java) | Verifica que un usuario puede unirse a una partida. | Implementada | Unitaria en backend |
| HU-9(ISSUE#143): Poder ver cartas | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un usuario puede ver las cartas que tiene durante la partida. | Implementada | Unitaria en backend |
| HU-10(ISSUE#145): Poder cantar Truco | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un jugador puede cantar truco durante la partida. | Implementada | Unitaria en backend |
| HU-11(ISSUE#146): Poder cantar Envido | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un jugador puede cantar envido durante la partida. | Implementada | Unitaria en backend |
| HU-12(ISSUE#153): Poder cantar Flor | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un jugador puede cantar flor durante la partida. | Implementada | Unitaria en backend |
| HU-13(ISSUE#144): Poder tirar carta | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un jugador puede tirar una carta. | Implementada | Unitaria en backend |
| HU-14(ISSUE#147): Poder ver la puntuación en la partida | [UTB-6:TestManoController](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\mano\TestManoController.java)  | Verifica que un jugador puede ver la puntuación durante la partida. | Implementada | Unitaria en backend |
| HU-15(ISSUE#148): Poder ver cómo cambia la puntuación | [UTB-6:TestManoController](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\mano\TestManoController.java)  | Verifica que un jugador puede ver los cambios en la puntuación durante la partida. | Implementada | Unitaria en backend |
| HU-16(ISSUE#149): Poder decir Quiero y No Quiero | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un jugador puede aceptar o rechazar un canto. | Implementada | Unitaria en backend |
| HU-17(ISSUE#150): Poder responder Envido y sus variantes | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un jugador puede responder a un envido con una de sus variantes. | Implementada | Unitaria en backend |
| HU-18(ISSUE#151): Poder responder Truco y sus variantes | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un jugador puede responder a otro que ha cantado truco durante la partida. | Implementada | Unitaria en backend |
| HU-19(ISSUE#152): Poder responder Flor y sus variantes | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un jugador puede responder a una flor con una de sus variantes. | Implementada | Unitaria en backend |
| HU-20(ISSUE#130): Poder buscar usuarios | [UTB-4:TestJugadorService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/jugador/TestJugadorService.java) | Verifica que un usuario puede buscar a otros usuarios |Implementada | Unitaria en backend |
| HU-21(ISSUE#131): Poder ver mis amigos | [UTB-4:TestJugadorService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/jugador/TestJugadorService.java) | Verifica que un usuario puede ver los amigos que tiene. | Implementada | Unitaria en backend |
| HU-22(ISSUE#387): Ver mis estadísticas | [UTB-7:EstadisticasServiceTest](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\estadisticas\EstadisticasServiceTest.java)  | Verifica que un usuario puede ver sus estadísticas. | Implementada | Unitaria en backend |
| HU-23(ISSUE#388): Ver logros | [UTB-8:LogrosServiceTest](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\estadisticas\LogrosServiceTest.java) | Verifica que un usuario puede ver los logros que tiene. | Implementada | Unitaria en backend |
| HU-24(ISSUE#389): Crear, editar y eliminar logros | [UTB-8:LogrosServiceTest](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\estadisticas\LogrosServiceTest.java) | Verifica que un usuario administrador puede crear, editar y eliminar logros. | Implementada | Unitaria en backend |
| HU-25(ISSUE#342): Ver ranking global | [UTB-7:EstadisticasServiceTest](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\estadisticas\EstadisticasServiceTest.java) | Verifica que un usuario puede ver el ranking global. | Implementada | Unitaria en backend |
| HU-26(NOISSUE): Poder ver listado de partidas en curso y terminadas como administrador | [UTB-5:PartidaServiceTests](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\partida\PartidaServiceTests.java) | Verifica que un usuario administrador puede ver la lista de partidas que se juegan. | Implementada | Unitaria en backend |
| HU-27(ISSUE#183): Poder ver listado de usuarios como administrador | [UTB-11:UserServiceTest](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\user\UserServiceTest.java) | Verifica que un usuario administrador puede ver los usuarios registrados. | Implementada | Unitaria en backend |
| HU-28(ISSUE#293): Poder crear, ver, editar y borrar usuarios como administrador | [UTB-11:UserServiceTest](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\user\UserServiceTest.java) | Verifica que un usuario administrador puede crear, ver, editar y eliminar usuarios. | Implementada | Unitaria en backend |
| HU-29 (ISSUE#376) : Poder ver perfiles de Usuario en partida | [UTB-9:PartidaJugadorServiceTest](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\partidaJugador\PartidaJugadorServiceTest.java) | Verifica que un usuario puede ver los participantes de las partidas que va a jugar. | Implementada | Unitaria en backend |
| HU-30(ISSUE#377): Poder añadir y eliminar amigos | [UTB-4:TestJugadorService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/jugador/TestJugadorService.java) | Verifica que un usuario puede añadir o eliminar a sus amigos. | Implementada | Unitaria en backend |
| HU-31(ISSUE#378): Gestionar solicitudes de amistad | [UTB-4:TestJugadorService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/jugador/TestJugadorService.java) | Verifica que un usuario puede aceptar o rechazar las solicitudes de amistad. | Implementada | Unitaria en backend |
| HU-32(ISSUE#379): Poder chatear con amigos durante la partida | [UTB-10:ChatServiceTest](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/chat/ChatServiceTest.java) | Verifica que un usuario puede chatear con sus amigos durante una partida. | Implementada | Unitaria en backend |
| HU-33(ISSUE#380): Poder chatear con amigos fuera de una partida | [UTB-10:ChatServiceTest](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/chat/ChatServiceTest.java) | Verifica que un usuario puede chatear con sus amigos fuera de una partida. | Implementada | Unitaria en backend |
| HU-34(ISSUE#381): Poder invitar amigos conectados a una partida | [UTB-10:ChatServiceTest](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/chat/ChatServiceTest.java) | Verifica que un usuario puede invitar a sus amigos conectados a una partida. | Implementada | Unitaria en backend |
| HU-35(ISSUE#382): Poder ver amigos conectados | [UTB-4:TestJugadorService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/jugador/TestJugadorService.java) | Verifica que un usuario puede ver a sus amigos que estén conectados. | Implementada | Unitaria en backend |
| HU-36(ISSUE#383): Poder recibir notificación de nuevo mensaje | [UTB-10:ChatServiceTest](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/chat/ChatServiceTest.java) | Verifica que un usuario puede recibir notificaciones de mensajes. | Implementada | Unitaria en backend |
| HU-37(ISSUE#384): Poder cambiar de equipo antes de iniciar la partida | [UTB-9:PartidaJugadorServiceTest](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\partidaJugador\PartidaJugadorServiceTest.java) | Verifica que un usuario puede cambiarse de equipo antes de que inicie la partida. | Implementada | Unitaria en backend |
| HU-38(ISSUE#385): Expulsar jugador de una partida | [UTB-9:PartidaJugadorServiceTest](src\test\java\es\us\dp1\lx_xy_24_25\truco_beasts\partidaJugador\PartidaJugadorServiceTest.java) | Verifica que un creador puede eliminar a un jugador. | Implementada | Unitaria en backend |

### 5.3 Matriz de Trazabilidad entre Pruebas e Historias de Usuario

| Historias de usuario| UTB-1 | UTB-2 | UTB-3 | UTB-4 | UTB-5 | UTB-6 | UTB-7 | UTB-8 |UTB-9 |UTB-10 |UTB-11 | UTF-1 |
|--------------------|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|
| HU-1                |   X   |       |       |       |       |       |       |      |        |        |        ||
| HU-2                |       |      |       |    X   |        |       |       |      |        |        |        ||
| HU-3                |       |       |      |       |        |       |       |      |        |        |        | X |
| HU-4                |       |   X   |       |      |        |       |       |      |        |        |        ||
| HU-5                |      |       |       |       |     X   |       |       |      |        |        |        ||
| HU-6                |       |      |    X   |       |        |       |       |      |        |        |        ||
| HU-7                |       |       |      |       |      X  |       |       |      |        |        |        ||
| HU-8                |       |       |       |      |        |       |       |      |     X   |        |        ||
| HU-9                |      |       |    X   |       |        |       |       |      |        |        |        | |   
| HU-10                |       |      |   X    |       |       |       |       |      |        |        |        | |
| HU-11                |       |       |   X  |       |        |       |       |      |        |        |        ||
| HU-12                |       |       |    X   |      |        |       |       |      |        |        |        ||
| HU-13                |      |       |   X   |       |        |       |       |      |        |        |        ||
| HU-14                |       |      |       |       |        |   X    |       |      |        |        |        ||
| HU-15                |       |       |      |       |        |   X    |       |      |        |        |        ||
| HU-16                |       |       |   X   |      |        |       |       |      |        |        |        ||
| HU-17                |      |       |    X   |       |        |       |       |      |        |        |        ||
| HU-18                |       |      |   X   |       |        |       |       |      |        |        |        ||
| HU-19                |       |       |  X    |       |        |       |       |      |        |        |        ||
| HU-20                |       |       |       |   X   |        |       |       |      |        |        |        ||
| HU-21                |       |       |       |   X   |        |       |       |      |        |        |        ||
| HU-22                |       |       |       |      |        |       |   X    |      |        |        |        ||
| HU-23                |       |       |       |      |        |       |       |   X   |        |        |        ||
| HU-24                |       |       |       |      |        |       |       |   X   |        |        |        ||
| HU-25                |       |       |       |      |        |       |   X    |      |        |        |        ||
| HU-26                |       |       |       |      |     X   |       |       |      |        |        |        ||
| HU-27                |       |       |       |      |        |       |       |      |        |        |   X     ||
| HU-28                |       |       |       |      |        |       |       |      |        |        |    X    ||
| HU-29                |       |       |       |      |        |       |       |      |    X    |        |        ||
| HU-30                |       |       |       | X     |        |       |       |      |        |        |        ||
| HU-31                |       |       |       |  X    |        |       |       |      |        |        |        ||
| HU-32                |       |       |       |      |        |       |       |      |        |    X    |        ||
| HU-33                |       |       |       |      |        |       |       |      |        |    X    |        ||
| HU-34                |       |       |       |      |        |       |       |      |        |    X    |        ||
| HU-35                |       |       |       |  X    |        |       |       |      |        |        |        ||
| HU-36                |       |       |       |      |        |       |       |      |        |    X    |        ||
| HU-37                |       |       |       |      |         |       |       |      |   X     |        |        ||
| HU-38                |       |       |       |      |        |       |       |      |    X    |        |        ||

## 6. Criterios de Aceptación

- Todas las pruebas unitarias deben pasar con éxito antes de la entrega final del proyecto.
- La cobertura de código debe ser al menos del 70%.
- No debe haber fallos críticos en las pruebas de integración y en la funcionalidad.

## 7. Conclusión

Este plan de pruebas establece la estructura y los criterios para asegurar la calidad del software desarrollado. Es responsabilidad del equipo de desarrollo y pruebas seguir este plan para garantizar la entrega de un producto funcional y libre de errores.
