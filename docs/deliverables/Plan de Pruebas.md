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
![jacoco](https://github.com/user-attachments/assets/8ed4b339-4ff5-412e-92fb-65336fc93ed8)


### 5.2 Matriz de Trazabilidad

| Historia de Usuario | Prueba | Descripción | Estado |Tipo |
|---------------------|--------|-------------|--------|--------|
| HU-1(NOISSUE): Poder loggearme | [UTB-1:AuthControllerTests](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/auth/AuthControllerTests.java) | Verifica que un usuario puede iniciar sesión con credenciales válidas. | Implementada | Unitaria en backend |
| HU-2(NOISSUE): Poder borrar la cuenta |  | Verifica que un usuario puede borrar su cuenta. | No implementada | Unitaria en backend |
| HU-3(NOISSUE): Poder cerrar sesion |  | Verifica que un usuario puede cerrar su sesión | No implementada | Unitaria en frontend |
| HU-4(ISSUE#59): Crear partidas | [UTB-2:PartidaServiceTests](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/partida/PartidaServiceTests.java) | Asegura que un usuario puede crear una partida. | Implementada | Unitaria en backend |
| HU-5(ISSUE#60): Editar los puntos de la partida |  | Verifica que un usuario puede decidir los puntos de la partida antes de crearla. | No implementada | Unitaria en frontend |
| HU-6(ISSUE#61): Editar la flor en la partida |  | Verifica que un usuario puede decidir jugar o no con flor en la partida antes de crearla. | No implementada | Unitaria en frontend |
| HU-7(ISSUE#62): Editar numero de jugadores de la partida |  | Verifica que un usuario puede decidir el número de jugadores de la partida antes de crearla. | No implementada | Unitaria en frontend |
| HU-8(ISSUE#63): Poder unirse a las partidas |  | Verifica que un usuario puede unirse a una partida. | No implementada | Unitaria en backend |
| HU-9(ISSUE#143): Poder ver cartas |  | Verifica que un usuario puede ver las cartas que tiene durante la partida. | No implementada | Unitaria en frontend |
| HU-10(ISSUE#145): Poder cantar Truco | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un jugador puede cantar truco durante la partida. | Implementada | Unitaria en backend |
| HU-11(ISSUE#146): Poder cantar Envido |  | Verifica que un jugador puede cantar envido durante la partida. | No implementada | Unitaria en backend |
| HU-12(ISSUE#153): Poder cantar Flor |  | Verifica que un jugador puede cantar flor durante la partida. | No implementada | Unitaria en backend |
| HU-13(ISSUE#144): Poder tirar carta | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un jugador puede tirar una carta. | Implementada | Unitaria en backend |
| HU-14(ISSUE#147): Poder ver la puntuación en la partida |  | Verifica que un jugador puede ver la puntuación durante la partida. | No implementada | Unitaria en frontend |
| HU-15(ISSUE#148): Poder ver cómo cambia la puntuación |  | Verifica que un jugador puede ver los cambios en la puntuación durante la partida. | No implementada | Unitaria en frontend |
| HU-16(ISSUE#149): Poder decir Quiero y No Quiero | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un jugador puede aceptar o rechazar un canto. | Implementada (sólo para el truco) | Unitaria en backend |
| HU-17(ISSUE#150): Poder responder Envido y sus variantes |  | Verifica que un jugador puede responder a un envido con una de sus variantes. | No implementada | Unitaria en backend |
| HU-18(ISSUE#151): Poder responder Truco y sus variantes | [UTB-3:TestManoService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/mano/TestManoService.java) | Verifica que un jugador puede responder a otro que ha cantado truco durante la partida. | Implementada | Unitaria en backend |
| HU-19(ISSUE#152): Poder responder Flor y sus variantes |  | Verifica que un jugador puede responder a una flor con una de sus variantes. | No implementada | Unitaria en backend |
| HU-20(ISSUE#130): Poder buscar usuarios | [UTB-4:TestJugadorService](/src/test/java/es/us/dp1/lx_xy_24_25/truco_beasts/jugador/TestJugadorService.java) | Implementada | Unitaria en backend |
| HU-21(ISSUE#131): Poder ver mis amigos |  | Verifica que un usuario puede ver los amigos que tiene. | No implementada | Unitaria en frontend |

### 5.3 Matriz de Trazabilidad entre Pruebas e Historias de Usuario

| Historias de usuario| UTB-1 | UTB-2 | UTB-3 | UTB-4 |
|--------------------|-------|-------|-------|-------|
| HU-1                |   X   |       |       |       |
| HU-2                |       |      |       |       |
| HU-3                |       |       |      |       |
| HU-4                |       |   X   |       |      |
| HU-5                |      |       |       |       |
| HU-6                |       |      |       |       |
| HU-7                |       |       |      |       |
| HU-8                |       |       |       |      |
| HU-9                |      |       |       |       |
| HU-10                |       |      |   X    |       |
| HU-11                |       |       |      |       |
| HU-12                |       |       |       |      |
| HU-13                |      |       |   X   |       |
| HU-14                |       |      |       |       |
| HU-15                |       |       |      |       |
| HU-16                |       |       |   X   |      |
| HU-17                |      |       |       |       |
| HU-18                |       |      |   X   |       |
| HU-19                |       |       |      |       |
| HU-20                |       |       |       |   X   |
| HU-21                |       |       |       |      |

## 6. Criterios de Aceptación

- Todas las pruebas unitarias deben pasar con éxito antes de la entrega final del proyecto.
- La cobertura de código debe ser al menos del 70%.
- No debe haber fallos críticos en las pruebas de integración y en la funcionalidad.

## 7. Conclusión

Este plan de pruebas establece la estructura y los criterios para asegurar la calidad del software desarrollado. Es responsabilidad del equipo de desarrollo y pruebas seguir este plan para garantizar la entrega de un producto funcional y libre de errores.
