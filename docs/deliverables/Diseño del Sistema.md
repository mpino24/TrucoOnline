# Documento de diseño del sistema

_Esta es una plantilla que sirve como guía para realizar este entregable. Por favor, mantén las mismas secciones y los contenidos que se indican para poder hacer su revisión más ágil._ 

## Introducción

_En esta sección debes describir de manera general cual es la funcionalidad del proyecto a rasgos generales. ¿Qué valor puede aportar? ¿Qué objetivos pretendemos alcanzar con su implementación? ¿Cuántos jugadores pueden intervenir en una partida como máximo y como mínimo? ¿Cómo se desarrolla normalmente una partida?¿Cuánto suelen durar?¿Cuando termina la partida?¿Cuantos puntos gana cada jugador o cual es el criterio para elegir al vencedor?_

[Enlace al vídeo de explicación de las reglas del juego / partida jugada por el grupo](http://youtube.com)

## Diagrama(s) UML:

### Diagrama de Dominio/Diseño

_En esta sección debe proporcionar un diagrama UML de clases que describa el modelo de dominio, recuerda que debe estar basado en el diagrama conceptual del documento de análisis de requisitos del sistema pero que debe:_
•	_Especificar la direccionalidad de las relaciones (a no ser que sean bidireccionales)_
•	_Especificar la cardinalidad de las relaciones_
•	_Especificar el tipo de los atributos_
•	_Especificar las restricciones simples aplicadas a cada atributo de cada clase de domino_
•	_Incluir las clases específicas de la tecnología usada, como por ejemplo BaseEntity, NamedEntity, etc._
•	_Incluir los validadores específicos creados para las distintas clases de dominio (indicando en su caso una relación de uso con el estereotipo <<validates>>._
![Diagrama de dominio Truco Beasts](https://github.com/user-attachments/assets/234ba735-8506-4207-884c-ef8f578bce82)
### Diagrama de Capas (incluyendo Controladores, Servicios y Repositorios)
_En esta sección debe proporcionar un diagrama UML de clases que describa el conjunto de controladores, servicios, y repositorios implementados, incluya la división en capas del sistema como paquetes horizontales tal y como se muestra en el siguiente ejemplo:_


![hLPHKjmm3FttAJpzWXzEq64WoyzsA33G7i3hYA2fOsSiXsccmwau0XThDb6eDwi4FhfVIEyzMNvoR2uecPZQsYdrX2LeYz4wK4ernluo5UWlUcCQetkeYK3Wax7uzkhxl25znwc4mUi_IlU_eKeDzGuY3Gdh396-RDFJ9lWKWtCGC-0Adi65fcUByBUsCc](https://github.com/user-attachments/assets/db11d049-7089-45a4-bb0f-844d428dca68)

_El diagrama debe especificar además las relaciones de uso entre controladores y servicios, entre servicios y servicios, y entre servicios y repositorios._
_Tal y como se muestra en el diagrama de ejemplo, para el caso de los repositorios se deben especificar las consultas personalizadas creadas (usando la signatura de su método asociado)._

_En este caso, como mermaid no soporta la definición de paquetes, hemos usado una [herramienta muy similar llamada plantUML}(https://www.plantuml.com/). Esta otra herramienta tiene un formulario para visualizar los diagramas previamente disponible en [https://www.plantuml.com/plantuml/uml/}(https://www.plantuml.com/plantuml/uml/). Lo que hemos hecho es preparar el diagrama en ese formulario, y una vez teníamos el diagrama lista, grabarlo en un fichero aparte dentro del propio repositorio, y enlazarlo con el formulario para que éste nos genera la imagen del diagrama usando una funcionalizad que nos permite especificar el código del diagrama a partir de una url. Por ejemplo, si accedes a esta url verás el editor con el código cargado a partir del fichero del repositorio original: [http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/gii-is-DP1/group-project-seed/main/docs/diagrams/LayersUMLPackageDiagram.iuml](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/gii-is-DP1/group-project-seed/main/docs/diagrams/LayersUMLPackageDiagram.iuml)._

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

## Documentación de las APIs
Se considerará parte del documento de diseño del sistema la documentación generada para las APIs, que debe incluir como mínimo, una descripción general de las distintas APIs/tags  proporcionadas. Una descripción de los distintos endpoints y operaciones soportadas. Y la especificación de las políticas de seguridad especificadas para cada endpoint y operación. Por ejemplo: “la operación POST sobre el endpoint /api/v1/game, debe realizarse por parte de un usuario autenticado como Player”.

Si lo desea puede aplicar la aproximación descrita en https://vmaks.github.io/2020/02/09/how-to-export-swagger-specification-as-html-or-word-document/ para generar una versión en formato Word de la especificación de la API generada por OpenAPI, colgarla en el propio repositorio y enlazarla en esta sección del documento.  En caso contrario debe asegurarse de que la interfaz de la documentación open-api de su aplicación está accesible, funciona correctamente, y está especificada conforme a las directrices descritas arriba.

## Patrones de diseño y arquitectónicos aplicados
En esta sección de especificar el conjunto de patrones de diseño y arquitectónicos aplicados durante el proyecto. Para especificar la aplicación de cada patrón puede usar la siguiente plantilla:

### Patrón: < Nombre del patrón >
*Tipo*: Arquitectónico | de Diseño

*Contexto de Aplicación*

Describir las partes de la aplicación donde se ha aplicado el patrón. Si se considera oportuno especificar el paquete donde se han incluido los elementos asociados a la aplicación del patrón.

*Clases o paquetes creados*

Indicar las clases o paquetes creados como resultado de la aplicación del patrón.

*Ventajas alcanzadas al aplicar el patrón*

Describir porqué era interesante aplicar el patrón.

## Decisiones de diseño
_En esta sección describiremos las decisiones de diseño que se han tomado a lo largo del desarrollo de la aplicación que vayan más allá de la mera aplicación de patrones de diseño o arquitectónicos._

### Decisión X
#### Descripción del problema:*

Describir el problema de diseño que se detectó, o el porqué era necesario plantearse las posibilidades de diseño disponibles para implementar la funcionalidad asociada a esta decisión de diseño.

#### Alternativas de solución evaluadas:
Especificar las distintas alternativas que se evaluaron antes de seleccionar el diseño concreto implementado finalmente en el sistema. Si se considera oportuno se pude incluir las ventajas e inconvenientes de cada alternativa

#### Justificación de la solución adoptada

Describir porqué se escogió la solución adoptada. Si se considera oportuno puede hacerse en función de qué  ventajas/inconvenientes de cada una de las soluciones consideramos más importantes.
Os recordamos que la decisión sobre cómo implementar las distintas reglas de negocio, cómo informar de los errores en el frontend, y qué datos devolver u obtener a través de las APIs y cómo personalizar su representación en caso de que sea necesario son decisiones de diseño relevantes.

_Ejemplos de uso de la plantilla con otras decisiones de diseño:_

### Decisión ejemplo: Importación de datos reales para demostración
#### Descripción del problema:

Como grupo nos gustaría poder hacer pruebas con un conjunto de datos reales suficientes, porque resulta más motivador. El problema es al incluir todos esos datos como parte del script de inicialización de la base de datos, el arranque del sistema para desarrollo y pruebas resulta muy tedioso.

#### Alternativas de solución evaluadas:

*Alternativa 1.a*: Incluir los datos en el propio script de inicialización de la BD (data.sql).

*Ventajas:*
•	Simple, no requiere nada más que escribir el SQL que genere los datos.
*Inconvenientes:*
•	Ralentiza todo el trabajo con el sistema para el desarrollo. 
•	Tenemos que buscar nosotros los datos reales

*Alternativa 1.b*: Crear un script con los datos adicionales a incluir (extra-data.sql) y un controlador que se encargue de leerlo y lanzar las consultas a petición cuando queramos tener más datos para mostrar.
*Ventajas:*
•	Podemos reutilizar parte de los datos que ya tenemos especificados en (data.sql).
•	No afecta al trabajo diario de desarrollo y pruebas de la aplicación
*Inconvenientes:*
•	Puede suponer saltarnos hasta cierto punto la división en capas si no creamos un servicio de carga de datos. 
•	Tenemos que buscar nosotros los datos reales adicionales

*Alternativa 1.c*: Crear un controlador que llame a un servicio de importación de datos, que a su vez invoca a un cliente REST de la API de datos oficiales de XXXX para traerse los datos, procesarlos y poder grabarlos desde el servicio de importación.

*Ventajas:*
•	No necesitamos inventarnos ni buscar nosotros lo datos.
•	Cumple 100% con la división en capas de la aplicación.
•	No afecta al trabajo diario de desarrollo y pruebas de la aplicación
*Inconvenientes:*
•	Supone mucho más trabajo. 
•	Añade cierta complejidad al proyecto

*Justificación de la solución adoptada*
Como consideramos que la división en capas es fundamental y no queremos renunciar a un trabajo ágil durante el desarrollo de la aplicación, seleccionamos la alternativa de diseño 1.c.

### Decisión 2: Creacion de la clase PartidaJugador
#### Descripción del problema:

Al haber creado la clase Jugador y la clase Partida que tienen una relación ManyToMany necesitamos que ambas esten conectadas. Además, cada jugador tiene una posicion asociada en cada partida.

#### Alternativas de solución evaluadas:
*Alternativa 1*: Poner el atributo posicion en partida y que se evalue cual es la posición de cada jugador en esa partida especifica.

*Ventajas:*
•	Solucion inmediata y pudiendo aprovechar la creacion de tablas automaticas de JPA.
*Inconvenientes:*
•	Implementación mucho más complicada para obtener las posiciones de cada jugador especifico en cada partida.
• Se necesitarian funciones auxiliares para que funcione como es debido.

*Alternativa 2*: Crear una clase llamada PartidaJugador intermedia que tenga un ManyToOne a Partida y a Jugador y el atributo de la posición, siendo así uno solo especifico por Jugador en cada Partida.

*Ventajas:*
•	Obtenemos una clase más con los datos necesarios y faciles de acceder.
*Inconvenientes:*
•	Se crea una entidad más en la base de datos.

#### Justificación de la solución adoptada
Elegimos la alternativa 2 ya que está nos facilitará la obtención de la posicion de un jugador en una partida especifica de una manera más sencilla, ya que su implementación en otra clase no nos permitiria utilizar este atributo como lo necesitamos.

### Decisión 3: Separar funciones canto y respuesta del truco.
#### Descripción del problema:

En el juego tenemos que poder evaluar los cambios de puntaje y cuando se puede cantar el truco y quien debe dar respuesta al mismo. Surgieron varias maneras de abordarlo.

#### Alternativas de solución evaluadas:
*Alternativa 1*: Crear una única funcion que se encargara del cante y la respuest.

*Ventajas:*
•	Simplicidad conceptual
• Más cohesionada.
*Inconvenientes:*
•	La implementacion de todos los posibles casos se acomplejaba más de lo que debería.
• Demasiadas funcionalidades agrupadas en una sola (bloater).

*Alternativa 2*: Crear dos funciones separadas, una encargada de la gestión del cante y otra de la respuesta.

*Ventajas:*
•	Se tienen en cuenta todos los casos.
• Separacion de funcionalidades.
• Potencial de reutilizacion (en el envido por ejemplo)
*Inconvenientes:*
•	Mayor abstracción, por ende complicada de entender.
• Cantidad de código elevada incluso para haber dividido las funcionalidades.

#### Justificación de la solución adoptada
Después de un acalorado debate y lluvia de ideas, nos decantamos por la alternativa 2 ya que la separación de responsabilidades permite una implementación más fiel y comprensible de las reglas específicas del canto y la respuesta en el truco. Además de adecuarse mejor a las reglas de negocio de nuestro juego.


## Refactorizaciones aplicadas

Si ha hecho refactorizaciones en su código, puede documentarlas usando el siguiente formato:

### Refactorización X: 
En esta refactorización añadimos un mapa de parámtros a la partida para ayudar a personalizar la información precalculada de la que partimos en cada fase del juego.
#### Estado inicial del código
```Java 
class Animal
{
}
``` 
_Puedes añadir información sobre el lenguaje concreto en el que está escrito el código para habilitar el coloreado de sintaxis tal y como se especifica en [este tutorial](https://docs.github.com/es/get-started/writing-on-github/working-with-advanced-formatting/creating-and-highlighting-code-blocks)_

#### Estado del código refactorizado

```
código fuente en java, jsx o javascript
```
#### Problema que nos hizo realizar la refactorización
_Ej: Era difícil añadir información para implementar la lógica de negocio en cada una de las fases del juego (en nuestro caso varía bastante)_
#### Ventajas que presenta la nueva versión del código respecto de la versión original
_Ej: Ahora podemos añadir arbitrariamente los datos que nos hagan falta al contexto de la partida para que sea más sencillo llevar a cabo los turnos y jugadas_

### Refactorización Botón Ver: 
En esta refactorización cambiamos nuestro código para que quedase mas legible, en lugar de tener dos veces la misma declaración del boton Ver (para poder ver partidas), ya solo aparece una vez.
#### Estado inicial del código
```

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
```
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

### Refactorización Canto Truco: 
En esta refactorización retocamos la funcion de cantarTruco para dividirla en su canto y su respuesta en casos separados
#### Estado inicial del código
``` 
public void cantar(Boolean respuesta)
{
  *logica del truco y sus casos segun la respuesta*
}
``` 

#### Estado del código refactorizado

```
public void cantarTruco(CantosTruco canto){
*logica del truco*
}
public void responderTruco(Respuestas respuesta){
*logica de los casos de las respuestas del truco*
}
```
#### Problema que nos hizo realizar la refactorización
Era dificil evaluar los cambios en los turnos de los jugadores y gestion del puntaje en la mano.
#### Ventajas que presenta la nueva versión del código respecto de la versión original
Estando separado nos es sencillo dicha gestion y aparte nos sirve como plantilla para la gestión del envido (función que se hizo posteriormente)














### Refactorización del cálculo del ganador del evnido: 
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
else if (jugadorActual%2 == 1)
                
            }
           }
    }
    public Integer comprobarValor(List<List<Carta>> cartasEquipo){ // Lo veo bastante bien, lo que no es necesario pasarle esa lista, las cartas de cada equipo son las que sean j%2==jugMano%2
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
    }
``` 
_Puedes añadir información sobre el lenguaje concreto en el que está escrito el código para habilitar el coloreado de sintaxis tal y como se especifica en [este tutorial](https://docs.github.com/es/get-started/writing-on-github/working-with-advanced-formatting/creating-and-highlighting-code-blocks)_

#### Estado del código refactorizado

```
public class Mano {
                        ...

     public Integer jugadorGanadorEnvido(List<List<Carta>> cartasDisp){ 
        Integer puntos=0;
        Integer posicion=0;
        for(int i=0; i<cartasDisp.size(); i++){
            Map<Palo, List<Carta>> diccCartasPaloJugador = cartasDisp.get(i).stream().collect(Collectors.groupingBy(Carta::getPalo));
            Integer sumaJugador= getMaxPuntuacion(diccCartasPaloJugador);
            if(sumaJugador> puntos){
                puntos= sumaJugador;
                posicion= i;
            }
        }
        return posicion;
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
}
```
#### Problema que nos hizo realizar la refactorización
Había muchos calculos innecesarios, lo que dificultaba tanto la lectura como la eficiencia del codigo.

#### Ventajas que presenta la nueva versión del código respecto de la versión original
Al cambiar el enfoque y determinar la posicion del ganador en lugar de comparar los puntos entre equipos nos ahorramos una llamada a la funcion, aumentando dicha legibilidad y eficacia.


### Refactorización de las funciones utiles de getCreationModal: 
En esta refactorización externalizamos las funciones de generación de código de partida y la de parseo de partida.
#### Estado inicial del código
```Java
getCreationModal.js

const GetCreationModal=forwardRef(
                                    ...

    function generateRandomCode() { //Recordemos que esta funcion, tambien generada por IA crea un codigo que se auto-genera y se asigna al abrir el modal
    const length = 5;
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
    let code = '';

    for (let i = 0; i < length; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        code += characters.charAt(randomIndex);
    }
    return code;
}
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
    )
}
export default GetCreationModal
```

#### Estado del código refactorizado

```
generateRandomCode.js
export default function generateRandomCode() { //Recordemos que esta funcion, tambien generada por IA crea un codigo que se auto-genera y se asigna al abrir el modal
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


