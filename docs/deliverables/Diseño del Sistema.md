# Documento de diseño del sistema

## Introducción

_En esta sección debes describir de manera general cual es la funcionalidad del proyecto a rasgos generales. ¿Qué valor puede aportar? ¿Qué objetivos pretendemos alcanzar con su implementación? ¿Cuántos jugadores pueden intervenir en una partida como máximo y como mínimo? ¿Cómo se desarrolla normalmente una partida?¿Cuánto suelen durar?¿Cuando termina la partida?¿Cuantos puntos gana cada jugador o cual es el criterio para elegir al vencedor?_

[Enlace al vídeo de explicación de las reglas del juego / partida jugada por el grupo](https://www.youtube.com/watch?v=IAKDghOqNaM&t=1s)

## Diagrama(s) UML:

### Diagrama de Dominio/Diseño

El diagramaes de una gran magnitud, pero al estar exportado en SVG se puede abrir en el navegador y ver en detalle sin problema. Se ha decidido utilizar esta disposición ya que era la que mejor mostraba las relaciones directas de cada Entidad/componente (separarlo por paquetes de clases lo dejaba casi ilegible)

![Diagrama de dominio final](/docs/diagrams/Diagrama%20de%20dominio%20final%20final.drawio.svg)

Además, en la carpeta "Diagrams" tambien se incluye el PNG por si se prefiriese ver en ese formato.

### Diagrama de Capas (incluyendo Controladores, Servicios y Repositorios)
Debido a la magnitud del diagrama completo no hemos podido incluir una imagen legible con el diagrama completo. Por ello, además de la imagen que contiene el diagrama completo, hemos incluido 3 imágenes que muestran por separado todo lo que contiene el diagrama. Una muestra las relaciones entre la capa de Presentación y la capa de Lógica de Negocio, otra las relaciones entre la capa de Lógica de Negocio y la capa de Recursos y otra los repositorios de la capa de Recursos y sus consultas personalizadas. Además, hemos dejado el código UML que construye el diagrama completo [aquí](https://github.com/gii-is-DP1/DP1-2024-2025--l6-5/blob/main/docs/diagrams/LayersUMLPackageDiagram.iuml).

#### Diagrama completo
![diagrama_completo](https://github.com/user-attachments/assets/7b215724-8c2b-46ee-a4b3-eed52ec35492)

#### Relaciones entre la capa de Presentación y la capa de Lógica de Negocio (incluidas relaciones internas en la capa de Lógica de Negocio)
![diagrama_de_capas_capas_P_BL](https://github.com/user-attachments/assets/cb8ea330-d52a-40e8-bcdc-d40065eca035)

#### Relaciones entre la capa de Lógica de Negocio y la capa de Recursos
![diagrama_de_capas_BL_R](https://github.com/user-attachments/assets/11fb2c2f-fa87-45a5-93ff-05fa39f70cd7)

#### Repositorios de la capa de Recursos y sus consultas personalizadas
![diagrama_de_capas_R](https://github.com/user-attachments/assets/9d548e75-6946-4600-a1e0-c00738c61f6b)

## Descomposición del mockups del tablero de juego en componentes

En esta sección procesaremos el mockup del tablero de juego (o los mockups si el tablero cambia en las distintas fases del juego). Etiquetaremos las zonas de cada una de las pantallas para identificar componentes a implementar. Para cada mockup se especificará el árbol de jerarquía de componentes, así como, para cada componente el estado que necesita mantener, las llamadas a la API que debe realizar y los parámetros de configuración global que consideramos que necesita usar cada componente concreto. 
Por ejemplo, para la pantalla de visualización de métricas del usuario en un hipotético módulo de juego social:

![Descomposición en componentes de la interfaz de estadísticas](https://github.com/gii-is-DP1/react-petclinic/assets/756431/12b36c37-39ed-422e-b8d9-56c94753cbdc)

  - App – Componente principal de la aplicación
    - $\color{orange}{\textsf{NavBar – Barra de navegación lateral}}$
      - $\color{darkred}{\textsf{[ NavButton ]. Muestra un botón de navegación con un icono asociado.}}$
    - $\color{darkblue}{\textsf{UserNotificationArea – Área de notificaciones e identificación del usuario actual}}$
    - $\color{blue}{\textsf{MetricsBar – En este componente se muestran las métricas principales del juego. Se mostrarán 4 métricas: partidas jugadas, puntos logrados, tiempo total, y cartas jugadas.}}$
      - $\color{darkgreen}{\textsf{[ MetricWell ] – Proporciona el valor y el incremento semanal de una métrica concreta. }}$
    - $\color{purple}{\textsf{GamesEvolutionChart – Muestra la tendencia de evolución en ellos últimos 4 meses en cuanto a partida jugadas, ganadas, perdidas y abandonadas.}}$
    - $\color{yellow}{\textsf{PopularCardsChart – Muestra la proporción de las N (parámetro de configuración) cartas más jugadas en el juego por el jugador.}}$
    - $\color{red}{\textsf{FrequentCoPlayersTable – Muestra los jugadores  con los que más se  ha jugado (de M en M donde M es un parámetro definido por la configuración del componente). Concretamente, se mostrarán la el nombre, la fecha de la última partida, la localización del jugador el porcentaje de partidas jugadas por ambos en las que el usuario ha ganado y si el jugador es amigo o no del usuario.}}$

![tablero_dividido_en_componentes](https://github.com/user-attachments/assets/6c02c8fd-d7d7-4631-8f22-a7288d415705)

  - PlayingModal – Componente principal del tablero
    - $$\textcolor[rgb]{0.1333,0.6941,0.2980}{\textsf{Logo – Botón que muestra el logotipo y permite salir de la partida.}}$$
    - $$\textcolor[rgb]{1,0,0}{\textsf{Perfil – Muestra la foto y el nombre de un jugador.}}$$
      - $$\textcolor[rgb]{0.7098,0.9019,0.1137}{\textsf{TextFoto – Muestra el nombre del jugador.}}$$
      - $$\textcolor[rgb]{0,0.6353,0.9098}{\textsf{Foto – Muestra la foto del jugador.}}$$
    - $$\textcolor[rgb]{0,0.6353,0.9098}{\textsf{Quedan – Muestra una imagen con las cartas restantes (boca abajo) de un jugador.}}$$
    - $$\textcolor[rgb]{0.6019,0.851,0.917}{\textsf{Mazo – Muestra el mazo de cartas para esclarecer quién es el jugador mano.}}$$
    - $$\textcolor[rgb]{1,0.949,0}{\textsf{DropArea – Proporciona el espacio donde los jugadores sueltan las cartas.}}$$
      - $$\textcolor[rgb]{0.1333,0.6941,0.2980}{\textsf{CardContainer – Contiene las cartas que se han tirado durante la ronda.}}$$
        - $$\textcolor[rgb]{1,0,0}{\textsf{CartaRonda – Muestra la carta tirada por uno de los jugadores.}}$$
    - $$\textcolor[rgb]{0.7098,0.9019,0.1137}{\textsf{CartasJugadorContainer – Muestra el conjunto de cartas que tiene el jugador.}}$$
      - $$\textcolor[rgb]{1,0,0}{\textsf{CartaJugador – Muestra una de las cartas que tiene el jugador.}}$$
    - $$\textcolor[rgb]{1,0.7882,0.0549}{\textsf{Truco – Botón que permite cantar truco.}}$$
    - $$\textcolor[rgb]{1,0.498,0.1529}{\textsf{EnvidoButtonContainer – Contiene las cartas que se han tirado durante la ronda.}}$$
      - $$\textcolor[rgb]{1,0.949,0}{\textsf{Envido – Botón que permite cantar envido.}}$$
      - $$\textcolor[rgb]{1,0,0}{\textsf{Real Envido – Botón que permite cantar real envido.}}$$
      - $$\textcolor[rgb]{0.7098,0.9019,0.1137}{\textsf{Falta Envido – Botón que permite cantar falta envido.}}$$
    - $$\textcolor[rgb]{1,0.6824,0.7882}{\textsf{NosEllos – Texto que permite saber si los puntos mostrados son de un equipo u otro.}}$$
    - $$\textcolor[rgb]{0.6392,0.2863,0.6431}{\textsf{Puntos – Permite conocer el número de puntos de un equipo.}}$$
    - $$\textcolor[rgb]{0.2471,0.2824,0.8}{\textsf{Jugador – Texto que indica la posición en la que se encuentra el jugador dentro de la mano actual.}}$$
    - $$\textcolor[rgb]{0.9725,0,1}{\textsf{TurnoHeading – Texto que aparece en la pantalla de un jugador cuando es su turno.}}$$
    - $$\textcolor[rgb]{0.9373,0.8941,0.6902}{\textsf{Musica – Botón que permite desplegar el reproductor de música de fondo.}}$$
    - $$\textcolor[rgb]{0.7255,0.4784,0.3412}{\textsf{Chat – Botón que permite desplegar el chat de la partida.}}$$
    - $$\textcolor[rgb]{0.4392,0.5725,0.7451}{\textsf{Gestos – Botón que permite desplegar el menú de gestos.}}$$
    

## Documentación de las APIs
Para visualizar de una manera intuitiva, dinamica e interactiva como se utiliza la API de Truco Beast: Bardo en la Jungla lo mejor es iniciar la app como se explica en el README y abrir [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html), ya que de esta forma se pueden hacer llamadas en tiempo real a la API y ver lo que responde.

Por otro lado, si no se desea tener que descargar e iniciar la app completa para poder ver el funcionamiento de la API, brindamos este archivo html que permite visualizar todas las opciones y que permite: ![Descargar pagina de la API](/docs/API/api_de_truco_beast_bardo_en_la_jungla.html)

## Patrones de diseño y arquitectónicos aplicados
En esta sección especificamos el conjunto de patrones de diseño y arquitectónicos aplicados durante el proyecto:

### Patrón: Strategy
*Tipo*: Arquitectónico | de Comportamiento

Se ha utilizado el patrón estrategia principalmente en la lógica del canto truco. Esta lógica se utiliza en el juego para aumentar la apuesta de modo que tenemos un cante y una respuesta que se irán sucediendo hasta la finalización de esta subtrama del juego. Las posibles respuestas las encontramos en la clase enumerado Cantos y pueden ser Retruco, Vale cuatro, Envido, Quiero y No quiero. En función de las respuestas, el jugador que responde tendrá unas posibilidades u otras dependiendo del momento en el que se encuentre la subtrama de modo que, según la respuesta, el sistema se comporta de una forma u otra ofreciendo las respuestas y la lógica correspondientes.

Para la realización del patrón se han debido crear el paquete patronEstrategiaTruco donde se han creado las siguientes clases:

        Clases para poder realizar la conversión de Respuestas y Cantos y poder aplicar la lógica correspondiente de cada Canto o Respuesta cuando sea utilizada.
                - CoverterRespuestaTruco
                - ConverterTruco
        Clases con la lógica de cada Respuesta
                - RespuestaTruco: interfaz común a todas las estrategias de respuesta.
                - RespuestaEnvido
                - RespuestaNoQuieroTruco
                - RespuestaQuieroTruco
                - RespuestaSubirTruco
        Clases con la lógica de cada Canto
                - Truco: interfaz común a todas las estrategias de canto.
                - TipoTruco
                - TipoRetruco
                - TipoValeCuatro
Además, en el paquete mano se encuentra el enum Cantos.
                
El uso de este patrón nos trae la gran ventaja de no tener un código excesivamente largo con muchos condicionales en la propia lógica del service y poder modularizarlo, aislando parte del código. Además, no es necesario establecer distintos métodos en función de la acción sino que con un mismo método en la interfaz lo definimos de distintas formas según el caso haciendo el software más eficiente. De igual manera, si en un futuro se quisieran añadir otras respuestas o cantos, con este patrón sería más sencillo agregarlos, lo cuál crea un software más flexible y mantenible. 

### Patrón: Single Page Application (SPA)
*Tipo*: Arquitectónico | de Diseño

Se ha utilizado el patrón SPA en múltiples puntos de la aplicación como lo son la partida en sí, la creación de una partida, la sección social, la creación de un perfil, el iniciado de sesión y la edición de perfil.

Este patrón es utilizado, concretamente, por la inmensa mayoría de las carpetas del frontend de la aplicación como lo son:
- auth
- components
- game
- home
- manos
- profile
- util

La aplicación de este patrón ha sido crucial para evitar una experiencia de usuario paupérrima. Sin el patrón SPA no se podría, dentro de una partida, tirar una carta y que la carta tirada se vea en la mesa sin la necesidad de tener que recargar la página. Tampoco sería posible iniciar sesión o editar datos de un perfil viendo en el formulario los cambios que se realizan en los campos.
Este patrón es uno de los más importantes.

### Patrón: Model View Controller (MVC)
*Tipo*: Arquitectónico | de Diseño

Este patrón es otro de los pilares de esta aplicación, pues define los aspectos generales de la arquitectura de esta. El Modelo está integrado por los servicios, los repositorios y las clases de dominio, la Vista está compuesta por la totalidad del frontend y el Controlador está representado por los controladores en el backend de la aplicación.

Ejemplos de clases clasificadas según su pertenencia al Modelo, la Vista o el Controlador:
#### Modelo
- User
- Mano
- PartidaRepository
- CartaRepository
#### Vista
- manos/PlayingModal.js
- home/index.js
- game/EquipoView.js
#### Controlador
- ManoController
- PartidaController
- CartaController

El patrón MVC nos ha proporcionado una separación clara de responsabilidades en la aplicación, facilitando mucho llevar a cabo refactorizaciones, añadir nuevas funcionalidades, hacer pruebas y corregir errores. La forma en la que este patrón separa la lógica de negocio, la gestión de interacciones y la interfaz de usuario nos ha facilitado mucho la labor de desarrollo, definiendo una estructura bien organizada.

### Patrón: Domain Model
*Tipo*: Arquitectónico

Se aplica en casi todas las entidades de la aplicación, permitiendo que estas, además de servir como modelo para los datos, sirven para describir el comportamiento de las diferentes entidades que hay en la base de datos, especialmente en cuanto a cómo se relacionan distintas entidades.

Como se ha mencionado antes, este patrón se aplica en casi todas las entidades. Aquí hay algunos ejemplos:
- Carta
- Jugador
- Partida
- PartidaJugador
- User

El uso de domain model nos ha permitido tener un control total sobre todos los objetos, sobre todo en cuanto a relaciones y herencias, algo que nos ha permitido implementar una lógica de negocio más compleja (y poder así aplicar el patrón Service Layer) que lo que nos permitiría otro patrón como lo es Table Module.

### Patrón: Service Layer
*Tipo*: Arquitectónico | de Diseño

Este patrón es fundamental en esta aplicación, pues la interacción entre la presentación y el dominio recae en la capa de servicios.

Todos los servicios creados pertenecen a esta capa y cada uno de ellos se encarga de gestionar la lógica de negocio que le corresponde. Aquí algunos ejemplos:
- UserService
- JugadorService
- PartidaService
- ManoService
- EstadisticasService

Gracias a este patrón hemos podido dividir la lógica de negocio en lógica de dominio y lógica de la aplicación, gestionando mucho mejor la totalidad de la lógica de negocio. Nos ha proporcionado eficiencia en interacciones bien coordinadas entre entidades de la base de datos, seguridad en las transacciones y en otras interacciones del usuario con la aplicación, entre otras ventajas.

### Patrón: (Meta) Data Mapper 
*Tipo*: Arquitectónico | de Diseño

El (Meta) Data Mapper es aplicado en la capa de persistencia de la aplicacion, la encargada de trasladar los datos entre los objetos del dominio y la base de datos. Este patron nos permite mantener el modelo de dominio independiente del modelo de datos, facilitando la separacion de responsabilidades.

Este patron lo aplica Spring utilizando JPA en todas nuestras clases del dominio, como:
- Carta
- Jugador
- Partida
- PartidaJugador
- User

Las ventajas más claras son la independencia del modelo de dominio y la persistencia de los datos, simplificando su lógica. La aplicación del patrón (Meta) Data Mapper fue esencial para lograr una arquitectura limpia y desacoplada, donde la lógica de negocio y la lógica de persistencia están claramente separadas

### Patrón: Identity Field
*Tipo*: Arquitectónico | de Diseño

Aplicamos el patrón Identity Field para garantizar que cada instancia de un objeto de dominio pueda ser identificada de manera única y persistida en la base de datos. Lo utilizamos en todas las entidades de la aplicacion que extiendan de BaseEntity, que se encuentra en el paquete: es.us.dp1.lx_xy_24_25.truco_beasts.model
Además siempre que se cree una nueva entidad se genera automaticamente una ID para esta.

Este patrón nos aporta diversos beneficios como:
- Identificar facilmente y de manera única las instancias de objetos con un solo registro en base de datos.
- Facilita la gestion de las relaciones entre entidades (se usa la id como Primary key o Foreign key).
- Se optimizan las consultas a la base de datos ya que se usan como indice.

La implementación de Identity Field asegura la unicidad de las entidades del dominio, facilita las operaciones CRUD y mantiene la integridad entre los objetos en memoria y sus representaciones en la base de datos.

### Patrón: Repository
*Tipo*: Arquitectónico | de Diseño

El patrón Repository se ha utilizado para gestionar el acceso a los datos y poder así encapsular las operaciones de persistencia, consulta y manipulación de las entidades del modelo de dominio.

Lo hemos aplicado para las entidades:
- Carta
- Jugador
- Partida
- PartidaJugador
- User
En sus respectivos paquetes: es.us.dp1.lx_xy_24_25.truco_beasts.{nombre de la entidad}Repository

Entre sus ventajas destaca la separacion de responsabilidades facilitando el mantenimiento y claridad del código y su integracion directa con el framework que estamos utilizando, pudiendo hacer de una manera simple consultas avanzadas. En resumen, el patrón Repository es clave para estructurar la capa de acceso a datos de forma modular y desacoplada. 


## Decisiones de diseño
En esta sección describimos las decisiones de diseño que hemos tomado a lo largo del desarrollo de la aplicación:

### Decisión 1: Creacion de la clase PartidaJugador
#### Descripción del problema:

Al haber creado la clase Jugador y la clase Partida que tienen una relación ManyToMany necesitamos que ambas esten conectadas. Además, cada jugador tiene una posicion asociada en cada partida.

#### Alternativas de solución evaluadas:
*Alternativa 1*: Poner el atributo posicion en partida y que se evalue cual es la posición de cada jugador en esa partida especifica.

*Ventajas:*
• Solucion inmediata y pudiendo aprovechar la creacion de tablas automaticas de JPA.
*Inconvenientes:*
• Implementación mucho más complicada para obtener las posiciones de cada jugador especifico en cada partida.
• Se necesitarian funciones auxiliares para que funcione como es debido.

*Alternativa 2*: Crear una clase llamada PartidaJugador intermedia que tenga un ManyToOne a Partida y a Jugador y el atributo de la posición, siendo así uno solo especifico por Jugador en cada Partida.

*Ventajas:*
• Obtenemos una clase más con los datos necesarios y faciles de acceder.
*Inconvenientes:*
• Se crea una entidad más en la base de datos.

#### Justificación de la solución adoptada
Elegimos la alternativa 2 ya que está nos facilitará la obtención de la posicion de un jugador en una partida especifica de una manera más sencilla, ya que su implementación en otra clase no nos permitiria utilizar este atributo como lo necesitamos.

### Decisión 2: Separar funciones canto y respuesta del truco.
#### Descripción del problema:

En el juego tenemos que poder evaluar los cambios de puntaje y cuando se puede cantar el truco y quien debe dar respuesta al mismo. Surgieron varias maneras de abordarlo.

#### Alternativas de solución evaluadas:
*Alternativa 1*: Crear una única funcion que se encargara del cante y la respuesta.

*Ventajas:*
• Simplicidad conceptual.
• Más cohesionada.
*Inconvenientes:*
• La implementacion de todos los posibles casos se acomplejaba más de lo que debería.
• Demasiadas funcionalidades agrupadas en una sola (bloater).

*Alternativa 2*: Crear dos funciones separadas, una encargada de la gestión del cante y otra de la respuesta.

*Ventajas:*
• Se tienen en cuenta todos los casos.
• Separacion de funcionalidades.
• Potencial de reutilizacion (en el envido por ejemplo).
*Inconvenientes:*
• Mayor abstracción, por ende complicada de entender.
• Cantidad de código elevada incluso para haber dividido las funcionalidades.

#### Justificación de la solución adoptada
Después de un acalorado debate y lluvia de ideas, nos decantamos por la alternativa 2 ya que la separación de responsabilidades permite una implementación más fiel y comprensible de las reglas específicas del canto y la respuesta en el truco. Además de adecuarse mejor a las reglas de negocio de nuestro juego.

### Decisión 3: Empleo del modal en lugar de pestañas en la creación y modal de unión a la partida
#### Descripción del problema:
Como grupo no teniamos claro como abordar los apartados de creación y unión de partidas.
#### Alternativas de solución evaluadas:
*Alternativa 1*: : Crear páginas nuevas para ambos apartados.

*Ventajas:*
• Separación mas clara de los apartados de página.
• Personalización individual de cada apartado.
*Inconvenientes:*
• Recargas constantes debido a las redirecciones.
• Aumento de carga en memoria y de tiempo de espera .

*Alternativa 2*: Crear dos funciones separadas, una encargada de la gestión del cante y otra de la respuesta.

*Ventajas:*
• Interfaz mas limpia y dinámica.
• Omisión de recargas.
• Menor carga en memoria y tiempo de espera.
*Inconvenientes:*
• Mayor complejidad en la implementacíon.
• Falta de personalización.


#### Justificación de la solución adoptada
Al emplear modales en lugar de páginas, la UX es más dinámica y limpia.

### Decisión 4: Creación de un Map<String, Mano> manosPorPartida en ManoService
#### Descripción del problema:
A la hora de gestionar las manos de una partida, teniamos que tener en cuenta que pueden estarse gestionando varias partidas simultaneamente teniendo en cuenta que las manos no son algo que queremos guardar en base de datos ya que tienen mucha información irrelevante, por lo que surgieron bastantes dudas.
#### Alternativas de solución evaluadas:
*Alternativa 1*: : Tener una Mano llamada manoActual que sea la mano con la que se construye el ManoService.

*Ventajas:*
• Solución más simple de ver (de hecho en un principio estaba así).
• Haciamos las funciones en base a esa manoActual.
*Inconvenientes:*
• A la hora de tener más de una partida simultamente no podriamos saber a qué partida pertenecia.


*Alternativa 2*: Crear un Map<String, Mano> manosPorPartida que tenga de clave el codigo de la partida y de valor su Mano asociada.

*Ventajas:*
• Cada partida va a poder tener su Mano actual en el ManoService.
• Es más fácil encontrar desde el ManoService cual es la Mano de esa partida.
*Inconvenientes:*
• Tenemos que agregar una función llamada getMano(String codigo) en el ManoService y debe ser llamada en todos los metodos del mismo para poder actuar sobre esa partida.

*Alternativa 3*: Poner Mano en la base de datos y buscarla a partir de su partida asociada.

*Ventajas:*
• Se gestiona el problema de las partidas simultaneas.
*Inconvenientes:*
• Igual que en la alternativa 2 tendriamos que agregar en cada función un getMano además de su repository asociado.
• Guardamos toda la información de todas las manos jugadas, y puede haber muchas por partida, siendoUna acumulación innecesaria de información irrelevante en la base de datos.
• Las llamadas a base de datos para cada una de las funciones podrían relentizar de manera significativa las partidas.

#### Justificación de la solución adoptada
Cómo se puede leer en la Refactorización 7, optamos por la alternativa 2, ya que a la hora de gestionar varias partidas simultaneas es mucho más útil y segura.

### Decisión 5: Usar un canto "SUBIR"
#### Descripción del problema:
Para las respuestas al "TRUCO", se puede decir "RETRUCO" y luego a este "VALE CUATRO", el problema que surgia es que lo único que diferencia estas dos acciones son el los puntos que ya hay en juego en la partida (1 sin truco, 2 con truco, 3 con retruco y 4 con vale cuatro). La duda nació en como implementarlo de la manera más fácil de gestionarlos.
#### Alternativas de solución evaluadas:
*Alternativa 1*: : Crear un canto llamado "SUBIR" que según el estado de la partida haga la llamada al retruco o valecuatro.

*Ventajas:*
• Todo simplificado en una sola clase llamada RespuestaSubirTruco.
• A la hora de hacer las llamadas en frontend es mucho más simple ya que no hay que crear dos respuestas distintas.
• En frontend siempre se llama a la respuesta subir, lo unico que cambiamos es el texto para que diga "RETRUCO" o "VALE CUATRO" correspondientemente 
*Inconvenientes:*
• Si se quisieran agregar más cantos en un futuro sería mejor que esten separadas para que no haya muchos condicionales en esta clase (considerando que cambiamos las reglas del juego).


*Alternativa 2*: Crear dos clases, una dedicada a la respuesta "RETRUCO" y otra al "VALE CUATRO" sin crear un canto "de mentira".

*Ventajas:*
• Las respuestas están completamente separadas.
• No hay necesidad de incluir condicionales en las clases.
*Inconvenientes:*
• Dado que solamente existen esas dos posibilidades, que esencialmente realizan lo mismo, separarla en dos clases resulta menos intuitivo.
• Necesitariamos crear dos botones diferentes que se activen o desactiven en base a los puntos de la mano, lo cual generaria código innecesario.

#### Justificación de la solución adoptada
Nos decantamos por la alternativa 1 ya que nos pareció lo más óptimo porque no se puede cantar "RETRUCO" o "VALECUATRO" simultaneamente. Además, dado que ambas tienen que llamar a cantosTruco, solo que según los puntos de la mano se hace con un Canto u otro, crear el canto "imaginario" nos es más útil, simplificando también la cantidad de botones que necesitabamos en frontend.

### Decisión 6: Añadir un atributo creador en PartidaJugador
#### Descripción del problema:
Para almacenar quien es el jugador que ha creado la partida y otorgarle así el derecho a expulsar gente o comenzarla cuando crea oportuno, necesitamos guardar para cada partida un atributo creador que haga referencia al jugador que creó la partida.
#### Alternativas de solución evaluadas:
*Alternativa 1*: Añadir un atributo creador a la clase Partida.

*Ventajas:*
• Te permite obtener el creador de la partida directamente con una llamada a la base de datos que obtenga los datos de la partida.
• Evitamos redundancias en la base de datos respecto a la otra alternativa, ya que solamente tendremos ese dato almacenado una vez en la db por cada partida.

*Inconvenientes:*
• La relación de la partida con cada jugador queda esparcida en distintas clases, con lo que el código es menos intuitivo.

*Alternativa 2*: Crear dos clases, una dedicada a la respuesta "RETRUCO" y otra al "VALE CUATRO" sin crear un canto "de mentira".

*Ventajas:*
• Unificas toda la información del jugador respecto a una partida en una misma clase.
• Dada la arquitectura del proyecto al momento de tomar la decisión y el código ya desarrollado, es la opción más optima de implementar.

*Inconvenientes:*
• En la base de datos, se guardan valores False para todos los jugadores conectados a una partida menos para uno, lo que resulta en información redundante y menos eficiente.

#### Justificación de la solución adoptada
Nos decantamos por la alternativa 2 ya que hemos primado la unificación de toda la información del jugador respecto a una partida en una misma entidad PartidaJugador sobre separar esa información en distintas clases. Además, dado el código ya implementado era la solución que generaba menos retrabajo.

### Decisión 7: Permitir invitar a tus amigos aunque no sean amigos de otros
#### Descripción del problema:
Antes de empezar una partida se tiene la opción de invitar a tus amigos para que se unan a ella.  
#### Alternativas de solución evaluadas:
*Alternativa 1*: Poder invitar a un amigo solo si es amigo de todos los demás.

*Ventajas:*
• Permite que la partida sea más privada y haya relación de amistad entre todos los jugadores.
• Reduce la posibilidad de conflictos o incomodidades entre los jugadores.

*Inconvenientes:*
• Limita considerablemente las opciones de jugadores disponibles para cada partida.
• Puede generar frustración en los usuarios que quieran jugar con amigos que no conocen al resto del grupo.

*Alternativa 2*: Poder invitar a un amigo aunque no sean amigos de todos los demás.

*Ventajas:*
• Fomenta la flexibilidad en la creación de partidas y facilita que más personas puedan participar.
• Aumenta las posibilidades de socialización y creación de nuevas amistades dentro del juego.
• Promueve la diversidad en las partidas, ya que no se requiere una red de amistades cerrada.
*Inconvenientes:*
• Existe la posibilidad de conflictos o incomodidades si algunos jugadores no se llevan bien.


#### Justificación de la solución adoptada
Nos decantamos por la alternativa 2 ya que preferimos que haya más rango posible de diferentes partidas sin tantas limitaciones.


## Refactorizaciones aplicadas

### Refactorización 1: Botón Ver 
En esta refactorización cambiamos nuestro código para que quedase mas legible, en lugar de tener dos veces la misma declaración del boton Ver (para poder ver partidas), ya solo aparece una vez.
#### Estado inicial del código
```JSX
            {
                <Link
                        to={`/partidas/${game.codigo}`}
                        style={{ textDecoration: "none" }}
                    ><button class="button" style={{ margin: 10, color: 'darkgreen' }} >
                            Ver
                        </button>
                    </Link>
                </div>
            }
            {
                connectedUsers>=game.numJugadores &&
                <>
                    <p>{game.numJugadores}/{game.numJugadores} jugadores</p>
                    <Link
                        to={`/partidas/${game.codigo}`}
                        style={{ textDecoration: "none" }}
                    ><button class="button" style={{ margin: 10, color: 'darkgreen' }} >
                            Ver
                        </button>
                    </Link>
                </>
            }

```
#### Estado del código refactorizado
```JSX
{
                connectedUsers < game.numJugadores &&
                    <Link
                        to={`/partidas/${game.codigo}`}
                        style={{ textDecoration: "none" }}
                    ><button class="button" style={{ margin: 10, color: 'brown' }} onClick={() => handleSubmit()} >
                            Unirse
                        </button>
                    </Link> 
            }
            </div>
            <Link
                to={`/partidas/${game.codigo}`}
                style={{ textDecoration: "none" }}
            ><button class="button" style={{ margin: 10, color: 'darkgreen' }} >
                    Ver
                </button>
            </Link>

        </div>
        
```
#### Problema que nos hizo realizar la refactorización
Esta duplicación en el código podía generar dudas entre nosotros (los desarrolladores) ya que la opción de ver partida siempre está disponible y ese problema que teníamos podría llegar a generar confusión.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Ahora la visualización de las partidas queda bastante más claro y no genera iopción de duda.

### Refactorización 2: Canto Truco 
En esta refactorización retocamos la funcion de cantarTruco para dividirla en su canto y su respuesta en casos separados
#### Estado inicial del código
```Java
public void cantar(Boolean respuesta)
{
  *lógica del truco y sus casos segun la respuesta*
}
``` 

#### Estado del código refactorizado

```Java
public void cantarTruco(CantosTruco canto){
    *lógica del truco*
}
public void responderTruco(Respuestas respuesta){
    *lógica de los casos de las respuestas del truco*
}
```
#### Problema que nos hizo realizar la refactorización
Era dificil evaluar los cambios en los turnos de los jugadores y gestion del puntaje en la mano.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Estando separado nos es sencillo dicha gestion y aparte nos sirve como plantilla para la gestión del envido (función que se hizo posteriormente)


### Refactorización del cálculo del ganador del envido: 
En esta refactorización trasladamos las funciones para determinar el ganador del envido en una mano. En concreto colocamos las funciones de obtener el ganador y determinar los puntos del mismo en lugar de en la clase manoService a la clase mano ahorrandonos la llamada a la clase mano para obtener las cartas disponibles. 

Además determinamos que solo con la posicion del jugador, podiamos saber que equipo era el ganador, por lo que tambien nos ahorramos el hacer el calculo de la puntuacion de ambos equipos y la comparacion entre los mismos.
#### Estado inicial del código
```Java 
class ManoService
public void envido(Boolean respuesta) throws EnvidoException{
                                                              ...
            
           List<List<Carta>> cartasEquipo1 = manoActual.getCartasDisp().stream().filter(t-> manoActual.getCartasDisp().indexOf(t)%2 == 0).toList();
           List<List<Carta>> cartasEquipo2 = manoActual.getCartasDisp().stream().filter(t-> manoActual.getCartasDisp().indexOf(t)%2 != 0).toList();
           
        if(respuesta){  
           Integer puntosEquipo1=comprobarValor(cartasEquipo1);
           Integer puntosEquipo2=comprobarValor(cartasEquipo2);
     
            if(puntosEquipo1>puntosEquipo2) partidaActual.setPuntosEquipo1(partidaActual.getPuntosEquipo1() + 2);
            else partidaActual.setPuntosEquipo2(partidaActual.getPuntosEquipo2() + 2);
else if (jugadorActual%2 == 1){
                
            }
           }
    }
    public Integer comprobarValor(List<List<Carta>> cartasEquipo){ 
        Integer puntos=0;
        for(int i=0; i<cartasEquipo.size(); i++){
            Map<Palo, List<Carta>> diccCartasPaloJugador = cartasEquipo.get(i).stream().collect(Collectors.groupingBy(Carta::getPalo));
            Integer sumaJugador= getMaxPuntuacion(diccCartasPaloJugador);
            if(sumaJugador> puntos){
                puntos = sumaJugador;
            }
        }
        return puntos;
    }
    public Integer getMaxPuntuacion (Map<Palo, List<Carta>> diccCartasPaloJugador) {
        List< Integer> listaSumas= new ArrayList<>();
        for(Map.Entry<Palo, List<Carta>> e : diccCartasPaloJugador.entrySet()){
            if(e.getValue().size()==1){
                listaSumas.add( comprobarValor(e.getValue().get(0).getValor()));
            }
            else if(e.getValue().size()==2){
                Integer valor1= e.getValue().get(0).getValor();
                Integer valor2= e.getValue().get(1).getValor();
                listaSumas.add(  20 + comprobarValor(valor1) + comprobarValor(valor2));
            }else if(e.getValue().size()==3){
                Integer valor= e.getValue().stream().map(x-> comprobarValor(x.getValor())).sorted(Comparator.reverseOrder()).limit(2).reduce(0, (a, b) -> a+b);
                listaSumas.add( valor+20);
            }
        }
        return listaSumas.stream().max(Comparator.naturalOrder()).get();
    }
    private Integer comprobarValor(Integer value) {
        return value>=10?0:value;
    }
    
``` 

#### Estado del código refactorizado

```Java
public class Mano {
                        ...
 public List<Integer> listaEnvidos(List<List<Carta>> cartasDisp){ 
        List<Integer> listaEnvidosCadaJugador = new ArrayList<>();
        for(int i=0; i<cartasDisp.size(); i++){
            Map<Palo, List<Carta>> diccCartasPaloJugador = agrupaCartasPalo(cartasDisp.get(i));
            Integer sumaJugador= getMaxPuntuacion(diccCartasPaloJugador);
            listaEnvidosCadaJugador.add(i, sumaJugador);
        }
        return listaEnvidosCadaJugador;
    }


     public Integer getMaxPuntuacion (Map<Palo, List<Carta>> diccCartasPaloJugador) {
        List< Integer> listaSumas= new ArrayList<>();
        for(Map.Entry<Palo, List<Carta>> e : diccCartasPaloJugador.entrySet()){
            if(e.getValue().size()==1){
                listaSumas.add( comprobarValor(e.getValue().get(0).getValor()));
            }
            else if(e.getValue().size()==2){
                Integer valor1= e.getValue().get(0).getValor();
                Integer valor2= e.getValue().get(1).getValor();
                listaSumas.add(  20 + comprobarValor(valor1) + comprobarValor(valor2));
            }else if(e.getValue().size()==3){
                Integer valor= e.getValue().stream().map(x-> comprobarValor(x.getValor())).sorted(Comparator.reverseOrder()).limit(2).reduce(0, (a, b) -> a+b);
                listaSumas.add( valor+20);
            }
        }
        return listaSumas.stream().max(Comparator.naturalOrder()).get();
    }

    public Integer comprobarValor(Integer value) {
        return value>=10?0:value;
    }

    public Map<Palo, List<Carta>> agrupaCartasPalo(List<Carta> listaDeCartas){
        Map<Palo, List<Carta>> diccCartasPaloJugador = listaDeCartas.stream().collect(Collectors.groupingBy(Carta::getPalo));
        return diccCartasPaloJugador;
    }
}
```
#### Problema que nos hizo realizar la refactorización
Había muchos calculos innecesarios, lo que dificultaba tanto la lectura como la eficiencia del codigo.

#### Ventajas que presenta la nueva versión del código respecto de la versión original
Al cambiar el enfoque y determinar la posicion del ganador en lugar de comparar los puntos entre equipos nos ahorramos una llamada a la funcion, aumentando dicha legibilidad y eficacia.


### Refactorización 3: Funciones utiles de getCreationModal 
En esta refactorización externalizamos las funciones de generación de código de partida y la de parseo de partida.
#### Estado inicial del código
```JSX
getCreationModal.js

const GetCreationModal=forwardRef(
                                    ...

    function generateRandomCode() {
    const length = 5;
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
    let code = '';

    for (let i = 0; i < length; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        code += characters.charAt(randomIndex);
    }
    return code;
})
...
   function handleSubmit(event) {
        if(partida.conFlor){
            partidaParseada.conFlor=true
        }
        if(partida.puntosMaximos){
            partidaParseada.puntosMaximos=30
        }
        if(partida.visibilidad){
            partidaParseada.visibilidad='PRIVADA'
        } 
        partidaParseada.codigo=generateRandomCode();                     
    console.log(partidaParseada)
    event.preventDefault();
    fetch("/api/v1/partida", {
      method:  "POST",
    ...
   })
}
export default GetCreationModal
```

#### Estado del código refactorizado

```JSX
generateRandomCode.js
export default function generateRandomCode() { 
    const length = 5;
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
    let code = '';

    for (let i = 0; i < length; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        code += characters.charAt(randomIndex);
    }
    return code;
}

parseSubmit.js

import generateCode from './generateCode.js'
export default function parseSubmit(partida, partidaParseada){

        
   if(partida.conFlor){
      partidaParseada.conFlor=true
  }
  if(partida.puntosMaximos){
      partidaParseada.puntosMaximos=30
  }
  if(partida.visibilidad){
      partidaParseada.visibilidad='PRIVADA'
  }
      partidaParseada.numJugadores=partida.numJugadores
      partidaParseada.codigo=generateCode()
    return partidaParseada;

}

```
#### Problema que nos hizo realizar la refactorización
Se delegaban demasiadas responsabilidades al componente gerCreationModal haciendo que su cohesion fuera baja, por lo que era dificil la lectura y el codigo quedaba demasiado grande.

#### Ventajas que presenta la nueva versión del código respecto de la versión original
Al tener las funciones generateRandomCode y parseSubmit separadas queda mas claro que el componente getCreationModal se encarga de crear la partida. Además si estas funciones se necesitan en un futuro se encuentran disponibles como utils. 


### Refactorización 4: Obtener el jugador que responde al truco 
En esta refactorización se separo la logica de obtener el jugador que responde del metodo que gestiona el truco:
#### Estado inicial del código
```Java 
public void cantosTruco(CantosTruco canto) throws Exception{
    ....
            case RETRUCO:
                if (manoActual.getPuntosTruco() <2) {
                    throw new Exception( "No se canto truco"); 
                }
                List<Integer> cantoEnTruco = secuenciaCantos.get(0);
                Integer rondaTruco = cantoEnTruco.get(0);
                Integer jugadorTruco = cantoEnTruco.get(1);
                manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));

                if(rondaActual==rondaTruco && jugadorAnterior == jugadorTruco){
                    manoActual.setJugadorTurno(jugadorAnterior);
                } else {
                    manoActual.setJugadorTurno(jugadorSiguiente);
                }
                secuenciaCantos.add(listaRondaJugador);
                manoActual.setSecuenciaCantoLista(secuenciaCantos);
                
                break;
            case VALECUATRO:
                if (manoActual.getPuntosTruco() <3) {
                    throw new Exception( "No se canto retruco"); 
                }
                List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                Integer rondaRetruco = cantoEnRetruco.get(0);
                Integer jugadorRetruco = cantoEnRetruco.get(1);
                manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
                if(rondaActual==rondaRetruco && (jugadorAnterior == jugadorRetruco )){ 
                    
                        manoActual.setJugadorTurno(jugadorAnterior);
                    
                    
                } else {
                    manoActual.setJugadorTurno(jugadorSiguiente);
                }
                secuenciaCantos.add(listaRondaJugador);
                manoActual.setSecuenciaCantoLista(secuenciaCantos);

                break;
    ....
}
``` 


#### Estado del código refactorizado

```Java
 public void cantosTruco(CantosTruco canto) throws Exception{
    ....
            case RETRUCO:
                if (manoActual.getPuntosTruco() <2) {
                    throw new Exception( "No se canto truco"); 
                }
                List<Integer> cantoEnTruco = secuenciaCantos.get(0);
                Integer elQueRespondeAlRetruco = quienResponde(cantoEnTruco, jugadorTurno);
                manoActual.setJugadorTurno(elQueRespondeAlRetruco);
                manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
                secuenciaCantos.add(listaRondaJugador);
                manoActual.setSecuenciaCantoLista(secuenciaCantos);
                
                break;
            case VALECUATRO:
                if (manoActual.getPuntosTruco() <3) {
                    throw new Exception( "No se canto retruco"); 
                }
                List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                Integer elQueResponde = quienResponde(cantoEnRetruco, jugadorTurno);
                manoActual.setJugadorTurno(elQueResponde);
                manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
                secuenciaCantos.add(listaRondaJugador);
                manoActual.setSecuenciaCantoLista(secuenciaCantos);

                break;
    ....
 }
    public Integer quienResponde(List<Integer> cantoHecho, Integer jugadorTurno){
        Integer res = null;
        Integer rondaActual = obtenerRondaActual();
        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);
        Integer jugadorSiguiente = siguienteJugador(jugadorTurno);
        Integer rondaCanto = cantoHecho.get(0);
        Integer jugadorCanto = cantoHecho.get(1); 
        if (rondaActual==rondaCanto && jugadorAnterior== jugadorCanto) {
            res = jugadorAnterior;
        }else{
            res = jugadorSiguiente;
        }
        return res;
    }
```
#### Problema que nos hizo realizar la refactorización
El código era demasiado largo y se podía ver claramente que la lógica se repetia.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Se puede visualizar facilmente lo que hace el codigo y al estar separado es más sencillo mantenerlo.

### Refactorización 5: Obtener el jugador al que le toca luego de responder un truco 
En esta refactorización se separo la obtención del que debe jugar ahora después de decir "Quiero" al Retruco y/o Valecuatro.
#### Estado inicial del código
```Java 
public void responderTruco(Respuestas respuesta) throws Exception{ 
        switch (respuesta) {
            case QUIERO:
                manoActual.setPuntosTruco(truco +1);
                if(queTrucoEs == 1){ //Es decir, Truco
                    manoActual.setJugadorTurno(jugadorAnterior);
                } else if( queTrucoEs == 2){
                    List<Integer> cantoEnTruco = secuenciaCantos.get(0);
                    Integer rondaTruco = cantoEnTruco.get(0);
                    Integer jugadorTruco = cantoEnTruco.get(1);

                    List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                    Integer rondaRetruco = cantoEnRetruco.get(0);
                    Integer jugadorRetruco = cantoEnRetruco.get(1);
                    if((rondaTruco == rondaActual && rondaRetruco == rondaActual) && (jugadorTruco==jugadorTurno && jugadorSiguiente==jugadorRetruco)){
                        manoActual.setJugadorTurno(jugadorTurno);
                    } else{
                        manoActual.setJugadorTurno(jugadorAnterior);
                    }
                    
                } else{         
                    List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                    Integer rondaRetruco = cantoEnRetruco.get(0);
                    Integer jugadorRetruco = cantoEnRetruco.get(1);
                    List<Integer> cantoEnValecuatro = secuenciaCantos.get(2);
                    Integer rondaValecuatro = cantoEnValecuatro.get(0);
                    Integer jugadorValecuatro = cantoEnValecuatro.get(1);

                    if((rondaRetruco == rondaActual && rondaValecuatro == rondaActual) && (jugadorRetruco==jugadorTurno && jugadorSiguiente==jugadorValecuatro)){
                        manoActual.setJugadorTurno(jugadorTurno);
                    } else{
                        manoActual.setJugadorTurno(jugadorAnterior);
                    }
                }
            break;
        ....
        }
    ....
 }

``` 


#### Estado del código refactorizado

```Java
public void responderTruco(Respuestas respuesta) throws Exception{ 
    switch (respuesta) {
            case QUIERO:
                manoActual.setPuntosTruco(truco +1);
                if(queTrucoEs == 1){ //Es decir, Truco
                    manoActual.setJugadorTurno(jugadorAnterior);
                } else if( queTrucoEs == 2){
                    List<Integer> cantoEnTruco = secuenciaCantos.get(0);
                    List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                    Integer aQuienLeTocaAhora = aQuienLeToca(cantoEnTruco, cantoEnRetruco, jugadorTurno);
                    manoActual.setJugadorTurno(aQuienLeTocaAhora);
                    
                } else {         
                    List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                    List<Integer> cantoEnValecuatro = secuenciaCantos.get(2);
                    Integer aQuienLeTocaAhora = aQuienLeToca(cantoEnRetruco, cantoEnValecuatro, jugadorTurno);
                    manoActual.setJugadorTurno(aQuienLeTocaAhora);
                }  
                break;
            ....
            }
    ....
}

public Integer aQuienLeToca(List<Integer> cantoAnterior, List<Integer> cantoAhora, Integer jugadorTurno) {
        Integer res = null;
        Integer rondaActual = obtenerRondaActual();
        Integer jugadorSiguiente = siguienteJugador(jugadorTurno);
        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);

        Integer rondaCantoAnterior = cantoAnterior.get(0);
        Integer jugadorCantoAnterior = cantoAnterior.get(1);

        Integer rondaCantoAhora = cantoAhora.get(0);
        Integer jugadorCantoAhora = cantoAhora.get(1);
        if ((rondaCantoAnterior == rondaActual && rondaCantoAhora == rondaActual) && (jugadorCantoAnterior==jugadorTurno && jugadorCantoAhora== jugadorSiguiente)){
            res = jugadorTurno;
        }else{
            res = jugadorAnterior;
        }
        return res;
    }
```
#### Problema que nos hizo realizar la refactorización
Como en el caso anterior, había código que se repetía y era difícil de comprender, un claro code smell.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Ahora separado es mucho más fácil de mantener y se entiende mejor su funcionalidad.

### Refactorización 6: Modal Abandonar Partida 
En esta refactorización unificamos en un único componente de React dos modales iguales que se encontraban en distintos puntos del código y que aparecen cuando quieres abandonar una partida
#### Estado inicial del código
Este mismo código se encontraba en WaitingModal.js y en AppNavbar.js
```JSX
            <Modal
                isOpen={modalIsOpen}
                onRequestClose={closeModal}
                style={customModalStyles}
                ariaHideApp={false}
            >
                <h2 style={{ marginBottom: '20px', color: '#333' }}>¿Quieres abandonar la partida?</h2>
                <img src="https://c.tenor.com/vkvU9Fi4uOsAAAAC/tenor.gif" alt="Mono GIF" style={{ width: '100%', height: 'auto', marginBottom: '20px' }} />
                <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                    <button
                        onClick={leaveGame}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#ff4d4d',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                            fontSize: '16px',
                            transition: 'background-color 0.3s ease',
                            marginRight: '10px'
                        }}
                        onMouseEnter={(e) => (e.target.style.backgroundColor = '#ff3333')}
                        onMouseLeave={(e) => (e.target.style.backgroundColor = '#ff4d4d')}
                    >
                        Sí, abandonar partida
                    </button>
                    <button
                        onClick={closeModal}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#4CAF50',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                            fontSize: '16px',
                            transition: 'background-color 0.3s ease'
                        }}
                        onMouseEnter={(e) => (e.target.style.backgroundColor = '#45a049')}
                        onMouseLeave={(e) => (e.target.style.backgroundColor = '#4CAF50')}
                    >
                        No, me quedo
                    </button>
                </div>
            </Modal>
``` 

#### Estado del código refactorizado

```JSX
import { forwardRef } from 'react';
import Modal from 'react-modal';
import tokenService from 'frontend/src/services/token.service.js';
import { useNavigate } from 'react-router-dom'



const LeavingGameModal = forwardRef((props, ref) => {
    const navigate = useNavigate();
    const jwt = tokenService.getLocalAccessToken();
    function closeModal() {
        props.setIsOpen(false);
    }

    function leaveGame() {
        fetch(
            "/api/v1/partidajugador",
            {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                  },
            }
        )
            .then((response) => response.text())
            .then((data) => {
                closeModal();
                navigate("/home");
            })
            .catch((message) => alert(message));
    }

    const customModalStyles = {
        content: {
            top: '50%',
            left: '50%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
            width: '400px',
            padding: '20px',
            borderRadius: '10px',
            boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
            textAlign: 'center',
            border: 'none',

        },
        overlay: {
            backgroundColor: 'rgba(0, 0, 0, 0.6)'
        }
    };



    return (
        <Modal
            isOpen={props.modalIsOpen}
            onRequestClose={closeModal}
            style={customModalStyles}
            ariaHideApp={false}
        >
            <h2 style={{ marginBottom: '20px', color: '#333' }}>¿Quieres abandonar la partida?</h2>
            <img src="https://c.tenor.com/vkvU9Fi4uOsAAAAC/tenor.gif" alt="Mono GIF" style={{ width: '100%', height: 'auto', marginBottom: '20px' }} />
            <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                <button
                    onClick={leaveGame}
                    style={{
                        padding: '10px 20px',
                        backgroundColor: '#ff4d4d',
                        color: '#fff',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer',
                        fontSize: '16px',
                        transition: 'background-color 0.3s ease',
                        marginRight: '10px'
                    }}
                    onMouseEnter={(e) => (e.target.style.backgroundColor = '#ff3333')}
                    onMouseLeave={(e) => (e.target.style.backgroundColor = '#ff4d4d')}
                >
                    Sí, abandonar partida
                </button>
                <button
                    onClick={closeModal}
                    style={{
                        padding: '10px 20px',
                        backgroundColor: '#4CAF50',
                        color: '#fff',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer',
                        fontSize: '16px',
                        transition: 'background-color 0.3s ease'
                    }}
                    onMouseEnter={(e) => (e.target.style.backgroundColor = '#45a049')}
                    onMouseLeave={(e) => (e.target.style.backgroundColor = '#4CAF50')}
                >
                    No, me quedo
                </button>
            </div>
        </Modal>
    );
});
export default LeavingGameModal;
```
#### Problema que nos hizo realizar la refactorización
Sin la refactorización no era viable reutilzar el modal, pues sería copiar y pegar el mismo código en distintas zonas del proyecto.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Permite reutilizar el modal en todas las zonas del código que la necesiten, pudiendo modificarlo desde un mismo sitio.

### Refactorización 7: Relacionar una mano con su correspondiente partida 
En esta refactorización hemos añadido en ManoService un Map que tiene como claves los códigos de las partidas y como valores las manos actuales de las partidas. Los métodos que no necesitaban ningún parámetro para hacer referencia a la partida a la que pertenece una mano ahora usan el código para encontrar en el Map la partida a la que pertenece cada mano que se juega en la aplicación.
#### Estado inicial del código
Ejemplos:
```Java
private final Mano manoActual;

public  void siguienteTurno() {
        Integer jugadorActual = manoActual.getJugadorTurno();
        Integer siguiente = (jugadorActual + 1) % manoActual.getPartida().getNumJugadores();
        manoActual.setJugadorTurno(siguiente);
}
public void tirarCarta(Integer indiceCarta) {
      if(!manoActual.getEsperandoRespuesta()){
            Integer jugadorActual = manoActual.getJugadorTurno();
            Carta carta = manoActual.getCartasDisp().get(jugadorActual).get(indiceCarta);
            manoActual.getCartasDisp().get(jugadorActual).remove(carta);
            manoActual.getCartasLanzadasRonda().set(jugadorActual, carta);
            siguienteTurno();
      }
}
```

#### Estado del código refactorizado
Map creado:
```Java
private final Map<String, Mano> manosPorPartida = new HashMap<>();
```
Método actualizarMano creado para actualizar la mano correcta:
```Java
public void actualizarMano(Mano mano, String codigo){
        Mano manoAnterior = manosPorPartida.get(codigo);
        if(manoAnterior != null){
            manosPorPartida.remove(codigo);
        }
        manosPorPartida.put(codigo, mano);
}
```
Ejemplos:
```Java
public  void siguienteTurno(String codigo) {
        Mano manoActual = getMano(codigo); 

        Integer jugadorActual = manoActual.getJugadorTurno();
        Integer siguiente = (jugadorActual + 1) % manoActual.getPartida().getNumJugadores();
        manoActual.setJugadorTurno(siguiente);
  
        actualizarMano(manoActual, codigo); 
}
public Carta tirarCarta(Integer indiceCarta, String codigo) {

        manoActual.getCartasDisp().get(jugadorActual).remove(carta);
        manoActual.getCartasLanzadasRonda().set(jugadorActual, carta);
        siguienteTurno();
        siguienteTurno(codigo);
        manosPorPartida.remove(codigo);
        manosPorPartida.put(codigo, manoActual);
}
```
#### Problema que nos hizo realizar la refactorización
Sin la refactorización no era posible relacionar las manos que se juegan al mismo tiempo (en distintas partidas) con sus partidas correspondientes, haciendo que el servicio actualizase una mano incorrecta en la mayoría de las ocasiones.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Ahora es posible que distintos jugadores juegen partidas simultáneamente sin que las acciones realizadas en la mano de una partida afecten en las de las otras partidas.

### Refactorización 8: Mover funciones de ManoService a Mano
En esta refactorización hemos movido la mayoría de las funciones de ManoService a Mano, pues hemos considerado que todas ellas son funciones que puede realizar un objeto Mano sobre sí mismo sin necesidad de un servicio que requiera además del código de la partida a la que pertenece la mano sobre la que está trabajando.
#### Estado inicial del código
Ejemplos de funciones que estaban en ManoService:
```Java
public  void siguienteTurno(String codigo) {
        Mano manoActual = getMano(codigo); 
            
        Integer jugadorActual = manoActual.getJugadorTurno();
        Integer siguiente = (jugadorActual + 1) % manoActual.getPartida().getNumJugadores();
        manoActual.setJugadorTurno(siguiente);
        actualizarMano(manoActual, codigo); 
}
public  Integer compararCartas(String codigo) {
        Mano manoActual = getMano(codigo);
        Integer poderMayor = 0;
        Integer empezador = null;
        List<Carta> cartasLanzadas = manoActual.getCartasLanzadasRonda();
        List<Integer> empate = new ArrayList<>();
        
        for (int i = 0; i < cartasLanzadas.size(); i++) {
            Integer poder = cartasLanzadas.get(i).getPoder();
            if (poderMayor < poder) {
                poderMayor = poder;
                empezador = i;
            } else if (poderMayor == poder) {
                empate.add(i);
                if (empate.size() == 1) {
                    empate.add(empezador);
                }
                empezador = null;
            }
        }
        
        gestionarGanadoresRonda(empate, empezador, codigo);
        
        empezador = empezador != null ? empezador : cercanoAMano(empate,codigo);
        manoActual.setCartasLanzadasRonda(new ArrayList<>());
        manoActual.setJugadorTurno(empezador);
        actualizarMano(manoActual, codigo);
        return empezador;
}
```
#### Estado del código refactorizado
Las funciones anteriores ahora en Mano:
```Java
public  void siguienteTurno() {      
        Integer jugadorActual = getJugadorTurno();
        Integer siguiente = (jugadorActual + 1) % getPartida().getNumJugadores();
        setJugadorTurno(siguiente);
    }
public  Integer compararCartas() {
        Integer poderMayor = 0;
        Integer empezador = null;
        List<Carta> cartasLanzadas = getCartasLanzadasRonda();
        List<Integer> empate = new ArrayList<>();
        
        for (int i = 0; i < cartasLanzadas.size(); i++) {
            Integer poder = cartasLanzadas.get(i).getPoder();
            if (poderMayor < poder) {
                poderMayor = poder;
                empezador = i;
            } else if (poderMayor == poder) {
                empate.add(i);
                if (empate.size() == 1) {
                    empate.add(empezador);
                }
                empezador = null;
            }
        }
        
        gestionarGanadoresRonda(empate, empezador);
        
        empezador = empezador != null ? empezador : cercanoAMano(empate);
        List<Carta> listaCartasLanzadasNuevo = new ArrayList<>();
        for (int i = 0; i < getPartida().getNumJugadores(); i++){
            listaCartasLanzadasNuevo.add(null);
        }
        setCartasLanzadasRonda(listaCartasLanzadasNuevo);
        setJugadorTurno(empezador);
        return empezador;
    }
```
#### Problema que nos hizo realizar la refactorización
Sobrecargábamos el servicio con funciones que no eran necesarias realizar ahí.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
El código de ManoService es más reducido y muchos de sus métodos, al estar ahora en la clase Mano, no requieren del código de la partida a la que pertenece la mano en cuestión.

### Refactorización 9: Eliminar obtenerRondaActual y quienResponde 
En esta refactorización hemos conseguido simplificar mucho el código eliminando obtenerRondaActual y quienResponde (funciones de la clase Mano). La segunda función (de nombre no tan intuitivo como la primera) era usada para saber a qué jugador le tocaba tirar carta tras una secuencia de cantos de truco, retruco y valecuatro.
#### Estado inicial del código
```Java
public  Integer obtenerRondaActual(){
        Integer ronda = 0;
        Integer ganadores = getGanadoresRondas().get(0) + getGanadoresRondas().get(1);
        if (ganadores == 0) ronda = 1;
        else if(ganadores == 1) ronda = 2;
        else ronda = 3;
        return ronda;
}
public  Integer aQuienLeToca() {
        Integer res = null;
        List<Integer> cantoAnterior = getSecuenciaCantoLista().get(secuenciaCantoLista.size()-2);
        List<Integer> cantoAhora = getSecuenciaCantoLista().get(secuenciaCantoLista.size()-1);
        Integer rondaActual = obtenerRondaActual();
        Integer jugadorSiguiente = siguienteJugador(jugadorTurno);
        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);
        Integer rondaCantoAnterior = cantoAnterior.get(0);
        Integer jugadorCantoAnterior = cantoAnterior.get(1);
        Integer rondaCantoAhora = cantoAhora.get(0);
        Integer jugadorCantoAhora = cantoAhora.get(1);
       
        if ((rondaCantoAnterior == rondaActual && rondaCantoAhora == rondaActual) && (jugadorCantoAnterior==jugadorTurno && jugadorCantoAhora== jugadorSiguiente)){
            res = jugadorTurno;
        }else{
            res = jugadorAnterior;
        }
        return res;
}
```
#### Estado del código refactorizado
Ahora esas funciones no existen y hemos creado dos nuevas propiedades para la clase mano que nos dan el resultado esperado:
```Java
private Integer rondaActual = 1;
private Integer jugadorIniciadorDelCanto;
```
#### Problema que nos hizo realizar la refactorización
La función obtenerRondaActual sólo calculaba la ronda actual para casos específicos en los que el jugador pie (el que está a la izquierda del jugador mano) era el último en tirar una carta, cosa que no siempre es así. La función quienResponde tampoco funcionaba para todos los casos porque era bastante complicado tratar de contemplar todas las posibilidades.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Ya no es necesario calcular ni la ronda actual ni el jugador al que le toca tirar carta tras una secuencia de cantos mediante funciones.


### Refactorización 10: Mover funciones de Mano a ManoService 
En esta refactorización movimos algunas funciones de Mano a ManoService, más especificamente aquellas en las que toman acción los jugadores como la de tirar cartas, cantar truco o responder al mismo. Esto debido a que son funciones de lógica que corresponden al Service, ya que en ellas, entre otras cosas, es donde pueden manejarse las exceptions y son las que son llamadas en el ManoController.
#### Estado inicial del código
Ejemplo de funcion que estaba en Mano:
```Java
 public  Carta tirarCarta(Integer cartaId){
        if(!getEsperandoRespuesta()){
            Integer jugadorActual = getJugadorTurno();
            
            List<Carta> cartasDisponibles = getCartasDisp().get(jugadorActual);
            Integer indice = null;
            for (int i=0; i < cartasDisponibles.size(); i++){
                if(cartasDisponibles.get(i)!= null){
                    if(cartasDisponibles.get(i).getId()==cartaId){
                        indice=i;
                        
                    }
                }
                
            }
            if(indice==null){
                throw new CartaTiradaException();
            }
            Carta cartaALanzar = cartasDisponibles.get(indice);
            getCartasDisp().get(jugadorActual).set(indice,null);
            getCartasLanzadasRonda().set(jugadorActual, cartaALanzar);
            List<Carta> listaCartasLanzadas = getCartasLanzadasRonda();
            if(listaCartasLanzadas.stream().allMatch(c -> c!=null)){
                compararCartas();
            } else{
                siguienteTurno();
            }
            
            

            return cartaALanzar;
        } else{
            throw new CartaTiradaException("Tenés que responder antes de poder tirar una carta");
        }
        
    }
```
#### Estado del código refactorizado
Las funciones anteriores ahora adaptadas en ManoService:
```Java
     public Carta tirarCarta(String codigo, Integer cartaId){
		Mano manoActual = getMano(codigo);
        if(!manoActual.getEsperandoRespuesta()){
            Integer jugadorActual = manoActual.getJugadorTurno();
            
            List<Carta> cartasDisponibles = manoActual.getCartasDisp().get(jugadorActual);
            Integer indice = null;
            for (int i=0; i < cartasDisponibles.size(); i++){
                if(cartasDisponibles.get(i)!= null){
                    if(cartasDisponibles.get(i).getId()==cartaId){
                        indice=i;
                        
                    }
                }
                
            }
            if(indice==null){
                throw new CartaTiradaException();
            }
            Carta cartaALanzar = cartasDisponibles.get(indice);
            manoActual.getCartasDisp().get(jugadorActual).set(indice,null);
            manoActual.getCartasLanzadasRonda().set(jugadorActual, cartaALanzar);
            List<Carta> listaCartasLanzadas = manoActual.getCartasLanzadasRonda();
            if(listaCartasLanzadas.stream().allMatch(c -> c!=null)){
                manoActual.compararCartas();
            } else{
                manoActual.siguienteTurno();
            }
            actualizarMano(manoActual, codigo);
            return cartaALanzar;
        } else{
            throw new CartaTiradaException("Tenés que responder antes de poder tirar una carta");
        }
        
    }
```
#### Problema que nos hizo realizar la refactorización
Las exceptions no funcionaban y además este tipo de funciones es más correcto que se encuentren ahí.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
El código de Mano no hace nada que tenga que ver con una acción del jugador en su turno, lo que permite una mejor separación de las propiedades de cada clase.

### Refactorización 11: Quien responde?
Cambiamos la función quien responde para poder hacerla más general utilizando la propiedad de la mano jugadorIniciadorCanto y así reutilizarla en otros cantos.
#### Estado inicial del código
```Java
public  Integer quienResponde(List<Integer> cantoHecho, Integer jugadorTurno){
        Integer res = null;
        Integer rondaActual = getRondaActual();
        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);
        Integer jugadorSiguiente = siguienteJugador(jugadorTurno);
        Integer rondaCanto = cantoHecho.get(0);
        Integer jugadorCanto = getJugadorIniciadorDelCanto(); 
        if(jugadorCanto==jugadorAnterior && rondaActual==rondaCanto){
            res = jugadorAnterior;
        }else{
            res = jugadorSiguiente;
        }
        return res;
        
        
    }

``` 

#### Estado del código refactorizado

```Java
public Integer quienResponde(){
        Integer jugadorQueResponde;
        Integer jugadorIniciador = getJugadorIniciadorDelCanto();
        Integer jugadorSiguiente = siguienteJugador(jugadorIniciador);
        Integer jugadorActual = getJugadorTurno();
        if(jugadorActual==jugadorIniciador){
            jugadorQueResponde = jugadorSiguiente;
        }else{
            jugadorQueResponde=jugadorIniciador;
        }
        return jugadorQueResponde;
    }
```
#### Problema que nos hizo realizar la refactorización
Anteriormente solo contemplaba los casos del truco y dependia de una lista llamada secuenciaCantos que no permitia que fuera reutilizable en el caso del envido y flor.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Ahora podemos usar esa quienResponde tanto para el truco como para el envido, sin necesidad de crear dos funciones que hagan esencialmente lo mismo pero con distintas aproximaciones.


### Refactorización 11.5: Secuencia Cantos
A consecuencia del cambio anterior, el atributo secuenciaCantosLista de la mano se convirtió en "legacy code", siendo obsoleto y borrado en todos los archivos que se utilizaba.
#### Estado inicial del código
```Java Ejemplo en tipoTruco y el setUpSecuencia cantos de los tests (hay más casos):
    public Mano accionAlTipoTruco(Mano manoActual,Integer jugadorTurno, Integer equipoCantor, List<List<Integer>> secuenciaCantos, List<Integer> listaRondaJugador, Integer rondaActual) {
        listaRondaJugador.add(rondaActual);
        listaRondaJugador.add(jugadorTurno);
        manoActual.setEquipoCantor(getEquipo(jugadorTurno));
                                                             
        manoActual.setJugadorTurno(manoActual.siguienteJugador(jugadorTurno));
        secuenciaCantos.add(listaRondaJugador);
        manoActual.setSecuenciaCantoLista(secuenciaCantos);
        return manoActual;
    }

    ....

    public void setupSecuenciaCantos(Integer jugadorCantorTruco, Integer rondaTruco, Integer jugadorCantorRetruco, Integer rondaRetruco, Integer jugadorCantorValecuatro, Integer rondaValecuatro){
        List<List<Integer>> secuenciaCantos = new ArrayList<>();
        List<Integer> listaRondaJugadorTruco = new ArrayList<>();
        listaRondaJugadorTruco.add(rondaTruco);
        listaRondaJugadorTruco.add(jugadorCantorTruco);
        secuenciaCantos.add(listaRondaJugadorTruco);
        if(rondaRetruco!=null && jugadorCantorRetruco!=null){
            List<Integer> listaRondaJugadorRetruco = new ArrayList<>();
            listaRondaJugadorRetruco.add(rondaRetruco);
            listaRondaJugadorRetruco.add(jugadorCantorRetruco);
            secuenciaCantos.add(listaRondaJugadorRetruco);
            if(rondaValecuatro!=null && jugadorCantorValecuatro!=null){
                List<Integer> listaRondaJugadorValecuatro = new ArrayList<>();
                listaRondaJugadorValecuatro.add(rondaValecuatro);
                listaRondaJugadorValecuatro.add(jugadorCantorValecuatro);
                secuenciaCantos.add(listaRondaJugadorValecuatro);
            }
        }
        mano.setSecuenciaCantoLista(secuenciaCantos);
    }

    // Ejemplo inicio de test
    public void testsTruco(){
        setup(0, 4);
        setupTruco(0, 3); 
        setupCartasDisponibles(1, 2);

        setupSecuenciaCantos(1, 1, 0, 1, null, null);
        
    } 

``` 

#### Estado del código refactorizado

```Java
    public Mano accionAlTipoTruco(Mano manoActual,Integer jugadorTurno, Integer equipoCantor, Integer rondaActual) {
        manoActual.setEquipoCantor(getEquipo(jugadorTurno));                                                     
        manoActual.setJugadorTurno(manoActual.siguienteJugador(jugadorTurno));
        return manoActual;
    }

    ....

    public void setupTruco(Integer equipoCantor, Integer truco, Integer jugadorIniciadorCanto){ // FUNCION YA EXISTENTE + lo de jugadorIniciadorCanto
        if(equipoCantor!=null) mano.setEquipoCantor(equipoCantor);
        if(truco!=null) mano.setPuntosTruco(truco);

        if(jugadorIniciadorCanto !=null){ // De ponerlo en otro valor que no sea null, quiere decir que estamos en un "ida y vuelta" de cantos
            mano.setJugadorIniciadorDelCanto(jugadorIniciadorCanto); 
        }
    }

    // Ejemplo inicio de test nuevo
    public void testsTruco(){
        setup(0, 4);
        setupTruco(0, 3, 0); // <- Atributo nuevo 
        setupCartasDisponibles(1, 2);

        
    } 
    
```
#### Problema que nos hizo realizar la refactorización
Como ya dijimos anteriormente, la necesidad de secuenciaCantos en la función de quien responde implicaba que solo contemplara los casos del truco. Además, el recargo de todas las posibilidades a través de ese setup tan enorme en el test era un claro code smell de que no era el camino más apropiado. 
#### Ventajas que presenta la nueva versión del código respecto de la versión original
No solo quienResponde está generalizado sino que también tenemos menos atributos en mano, funciones más compactas y hasta tests mucho más cortos. 

### Refactorización 12: Enum Cantos
Por el ansia de modularizar y separa las cosas creamos dos enums, uno para el truco y otro para sus respuestas. El problema es que llegados al envido surgia la necesidad de crear otros dos enums más. Esto claramente no tenía sentido ya que se utilizan practicamente los mismos cantos (además que en el truco también existe la posibilidad de decir "Envido" según el caso) así que fueron borrados y unidos en un solo enum.
#### Estado inicial del código
```Java 
    public enum CantosTruco {
        TRUCO, RETRUCO, VALECUATRO
    }
    public enum CantosEnvido {
        ENVIDO, REAL_ENVIDO, FALTA_ENVIDO, QUIERO, NO_QUIERO
    }

    public enum RespuestasTruco {
        QUIERO, NO_QUIERO, SUBIR, ENVIDO, REAL_ENVIDO,FALTA_ENVIDO
    }


``` 

#### Estado del código refactorizado

```Java
    public enum Cantos {
        TRUCO, RETRUCO, VALECUATRO, ENVIDO, REAL_ENVIDO, FALTA_ENVIDO, FLOR, CONTRAFLOR, CONTRAFLOR_AL_RESTO, QUIERO, NO_QUIERO, SUBIR
    } 
    
```
#### Problema que nos hizo realizar la refactorización
Tener enums para diferenciarlos era una forma simple de ordenarlos, pero en la práctica solo generaba que haya que volver a escribir las mismas cosas en varios enums y comprobaciones más complejas para que correspondan los nombres.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Todo está fácil de encontrar en un mismo lugar y además a la hora de hacer el caso de la flor va a ser una implementación más sencilla.

### Refactorización 13: Patron truco
Habiamos aplicado el patron de estrategia en los cantos del truco, pero para poder gestionar las exceptions todavia haciamos uso de un switch, cosa que hacía que no podamos aprovechar todos los beneficios del patron. Ahora cada excepción se maneja en su respectiva clase. 
#### Estado inicial del código
```Java Ejemplos
    public Mano cantosTruco(String codigo, Cantos canto){
		Mano manoActual = getMano(codigo);
        Integer jugadorTurno = manoActual.getJugadorTurno();
        Integer equipoCantor = manoActual.getEquipoCantor();

        Integer rondaActual = manoActual.getRondaActual();
        
        if(manoActual.getEsperandoRespuesta()==false){
            manoActual.setJugadorIniciadorDelCanto(jugadorTurno);
        }
        manoActual.setEsperandoRespuesta(true); 
        
        
        manoActual.setEsTrucoEnvidoFlor(0);
        Mano mano = new Mano();
        try {
            if (!manoActual.comprobarSiPuedeCantarTruco()) {
                throw new TrucoException(); 
            }
            Truco estadoTruco =  converterTruco.convertToEntityAttribute(canto);

            Integer puntosTruco = manoActual.getPuntosTruco();
            Integer puntosNoHayTruco = 1; Integer puntosHayTruco = 2; Integer puntosHayRetruco = 3;
            switch (canto) {
                case TRUCO: 
                    if(puntosTruco > puntosNoHayTruco){
                        throw new TrucoException("Ya se canto el truco");
                    }
                    mano = estadoTruco.accionAlTipoTruco(manoActual, jugadorTurno, equipoCantor);
                    manoActual.copiaParcialTruco(mano);
                    break;
                case RETRUCO:
                    if (puntosTruco < puntosHayTruco) {
                        throw new TrucoException( "No se cantó el truco");
                    } else if(puntosTruco>puntosHayTruco){
                        throw new TrucoException("Ya se canto el retruco");
                    }
                    mano = estadoTruco.accionAlTipoTruco(manoActual,jugadorTurno, equipoCantor);
                    manoActual.copiaParcialTruco(mano);
                    break;
                case VALECUATRO:
                    if (puntosTruco < puntosHayRetruco) {
                        throw new TrucoException( "No se cantó el retruco"); 
                    }else if(puntosTruco > puntosHayRetruco){
                        throw new TrucoException("Ya se canto el valecuatro");
                    }
                    mano = estadoTruco.accionAlTipoTruco(manoActual, jugadorTurno, equipoCantor);
                    manoActual.copiaParcialTruco(mano);
                
                    break;
                default:
                    throw new TrucoException( "Canto no valido"); 
            }
        } catch (Exception e) {
            manoActual.setEsperandoRespuesta(false);
            throw e;
        }
        manoActual.comprobarSiPuedeCantarTruco();
        manoActual.comprobarSiPuedeCantarEnvido(false);

		actualizarMano(manoActual, codigo);
        return manoActual;
    }

    ....
    // En retruco
    public Mano accionAlTipoTruco(Mano manoActual, Integer jugadorTurno, Integer equipoCantor) {
        Integer elQueRespondeAlRetruco = manoActual.quienResponde();
        manoActual.setJugadorTurno(elQueRespondeAlRetruco);
        manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
        
        
        return manoActual;
    }

``` 

#### Estado del código refactorizado

```Java Ejemplos
    public Mano cantosTruco(String codigo, Cantos canto){
		Mano manoActual = getMano(codigo);
        Integer jugadorTurno = manoActual.getJugadorTurno();
        Integer equipoCantor = manoActual.getEquipoCantor();
        
        if(manoActual.getEsperandoRespuesta()==false){
            manoActual.setJugadorIniciadorDelCanto(jugadorTurno);
        }
        manoActual.setEsperandoRespuesta(true);
        
        
        manoActual.setEsTrucoEnvidoFlor(0);
        Mano mano = new Mano();
        try {
            if (!manoActual.comprobarSiPuedeCantarTruco()) {
                throw new TrucoException(); 
            }
            Truco estadoTruco =  converterTruco.convertToEntityAttribute(canto);

           
            mano = estadoTruco.accionAlTipoTruco(manoActual, jugadorTurno, equipoCantor);
            manoActual.copiaParcialTruco(mano);
        } catch (Exception e) {
            manoActual.setEsperandoRespuesta(false);
            throw e;
        }
        manoActual.comprobarSiPuedeCantarTruco();
        manoActual.comprobarSiPuedeCantarEnvido(false);

		actualizarMano(manoActual, codigo);
        return manoActual;
    }
    
    ....
    //En retruco
    public Mano accionAlTipoTruco(Mano manoActual, Integer jugadorTurno, Integer equipoCantor) {
        Integer puntosTruco = manoActual.getPuntosTruco();
        Integer puntosHayTruco = 2;
        if (puntosTruco < puntosHayTruco) {
                throw new TrucoException( "No se cantó el truco");
            } else if(puntosTruco>puntosHayTruco){
                throw new TrucoException("Ya se canto el retruco");
            }
        Integer elQueRespondeAlRetruco = manoActual.quienResponde();
        manoActual.setJugadorTurno(elQueRespondeAlRetruco);
        manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
        
        
        return manoActual;
    }
```
#### Problema que nos hizo realizar la refactorización
No estabamos aplicando completamente el patrón y por lo tanto no aprovechabamos los beneficios que este podía brindarnos.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Ahora tanto cantosTruco como respuestasTruco quedó mucho más limpio y fácil de comprender.

### Refactorización 14: Creación de MessageList
En esta refactorización lo que hemos hecho ha sido sacar el estilo del chat de ShowChat.js y hemos creado un componente a parte que es MessageList.js que se encarga de rendirizar los mensajes. Además tambien hemos creado el InpotConteiner que es donde se escribe el mensaje y se envía.
#### Estado inicial del código
```Java 
   return (
    <>
    <div className="messages-container">
    {mensajes.map((msg, i) =>
    msg.remitente.id === user.id ? (
      <div key={i} className="own-message">
        {msg.contenido}
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
        alignItems: "stretch",
        height: "85vh",
      }}
    >
      <div
        className="messages-container"
        style={{
          flexGrow: 1,
          overflowY: "auto",
          padding: "10px",
        }}
      >
        {mensajes.map((msg, i) =>
          msg.remitente.id === user.id ? (
            <div key={i} className="own-message">
              {msg.contenido}
            </div>
          ) : (
            <>
              <div key={i}>{msg.remitente.username}</div>
              <div key={i} className="other-message">
                {msg.contenido}
              </div>
            </>
          )
        )}
        
        <div ref={messagesEndRef} />
      </div>
    ) : (
      <>
      <div key={i} >{msg.remitente.username}</div>
      <div key={i} className="other-message">
        {msg.contenido}
      <div className="input-container">
        <input
          type="text"
          value={mensaje}
          onChange={(e) => setMensaje(e.target.value)}
          placeholder="Escribe un mensaje..."
          className="input-text"
        />
        <button onClick={handleEnviar} className="btn-send">
          Enviar
        </button>
      </div>
      </>
    )
    )}
  </div>
    <div className="input-container">
      <input
        type="text"
        value={mensaje}
        onChange={(e) => setMensaje(e.target.value)} 
        placeholder="Escribe un mensaje..."
        className="input-text"
      />
      <button onClick={handleEnviar} className="btn-send" >Enviar</button>
    </div>
    </>
    
  );


``` 

#### Estado del código refactorizado

```Java
    return(
        <MessageList mensajes={mensajes} userId={user.id} />
        <InputContainer mensaje={mensaje} setMensaje={setMensaje} evtEnviarMensaje={evtEnviarMensaje} />
    )
    
```
#### Problema que nos hizo realizar la refactorización
Al tener el chat integrado tanto en la página principal como en la partida, se iba a tener una duplicación del código innecesaria.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Con este componente ahora cada vez que queremos mostrar mensajes solo tenemos que pasar los mensajes a MessageList para que los muestre con el estilo que creamos. Con esto el código queda más legible además de que es muy reutilizable.
 

