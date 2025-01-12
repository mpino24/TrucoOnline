package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class Mano {
    
    private List<List<Carta>> cartasDisp; // La lista de cartas de la posicion 0 seran las cartas que tiene el jugador en la posicion 0
    private Integer jugadorTurno; // Por defecto sera el jugador mano
    private List<Carta> cartasLanzadasRonda; // IMPORTANTE !!!! : se inicializa como lista de nulls y cada carta sustituye al null de la posicion de su jugador (se borran en cada ronda)
    private List<Integer> ganadoresRondas;
    private Integer rondaActual = 1;
    private Integer puntosTruco=1;
    private Integer puntosEnvido =0;
    private Integer puntosFlor= 0;
    private List<List<Carta>> cartasLanzadasTotales; //Esta lista contiene las cartas que lanzo cada jugador siendo el indice de la lista grande, el jugador, y el de la sublista, la ronda en la que la lanzo
    private Integer esTrucoEnvidoFlor = 0; // 0 -> Truco, 1 -> envido, 2 -> flor
    private Integer equipoCantor = null;
    private Boolean esperandoRespuesta = false;
    private Integer jugadorIniciadorDelCanto;
    private Boolean terminada = false;
    private Boolean puedeCantarTruco = true;
    private Boolean puedeCantarEnvido = false;
    private Boolean puedeCantarFlor= false;
    private List<Boolean> listaTienenFlores;
    private Integer queEnvidoPuedeCantar = 2; //1 -> solo falta envido, 2 -> falta y real, 3 -> falta, real y envido, otro -> nada
    private Integer queFlorPuedeCantar = 0; //2-> Contraflor y con flor me achico, 1->Flor, 0->Nada
    private Integer equipoGanadorEnvido;
    private Integer equipoGanadorFlor;
    private Boolean tresMismoPalo=false; 
    private Cantos ultimoMensaje;

    private Boolean estaMintiendo= false; //PARA LAS ESTADISTICAS

    List<List<Carta>> cartasNoBorradas;

    private final Integer constanteEnvido=20;
    private final Integer puntosMaximosDelTruco = 4;
    private final Integer rondasMaximasGanables = 2;
    
    List<Integer> envidosCadaJugador;
    List<Integer> envidosFlorCadaJugador;

    private List<Integer> envidosCantados;
    private Integer floresCantadas; //Este integer se encargará de saber las veces que se cantó algo relacionado con la flor 

    @ManyToOne
    private Partida partida;

    public void copiaParcialTruco(Mano mano) {
        this.equipoCantor = mano.getEquipoCantor();
        this.esperandoRespuesta = mano.getEsperandoRespuesta();
        this.jugadorTurno = mano.getJugadorTurno();
        this.puntosTruco = mano.getPuntosTruco();
        this.queEnvidoPuedeCantar = mano.getQueEnvidoPuedeCantar();
        this.puedeCantarEnvido = mano.getPuedeCantarEnvido();
        this.terminada = mano.getTerminada();
        this.ultimoMensaje = mano.getUltimoMensaje();
    }

    public  Boolean comprobarSiPuedeCantarTruco() { //O SUS OTRAS POSIBILIDADES
        Boolean res;
        if(getPuntosMaximosDelTruco() == getPuntosTruco()){
            res = false;
        } else{
            Integer equipoCantor = getEquipoCantor();
            Integer jugadorTurno = getJugadorTurno();
            res =(equipoCantor == null || jugadorTurno % 2 != equipoCantor);
            
        }
        setPuedeCantarTruco(res);
        
        return res;
    }

    final Integer maximosEnvido = 2;
    final Integer maximosRealEnvido =1;
    final Integer maximosFaltaEnvido = 1; 

    public Boolean comprobarSiPuedeCantarEnvido(Boolean fueraDeCantos) { //O SUS OTRAS POSIBILIDADES
        Boolean res;
        if(getRondaActual() != 1 || getPuntosEnvido() !=0 || getPuntosTruco() > 1 || getFloresCantadas()!=0){
            res = false;
        } else{
            List<Integer> listaEnvidos = getEnvidosCantados();
            Integer envidos = listaEnvidos.get(0);
            Integer realEnvido = listaEnvidos.get(1);
            Integer faltaEnvido = listaEnvidos.get(2);
            if(faltaEnvido ==maximosFaltaEnvido){
                res=false;
                setQueEnvidoPuedeCantar(0);
            } else if(realEnvido ==maximosRealEnvido){
                res =true;
                setQueEnvidoPuedeCantar(1);
            } else if (envidos == maximosEnvido) {
                res = true;
                setQueEnvidoPuedeCantar(2);
            } else{
                res =true;
                setQueEnvidoPuedeCantar(3);
            }
            Integer jugador = getJugadorTurno();
            Integer jugadorPie = obtenerJugadorPie();
            Boolean esPie = jugador==jugadorPie || siguienteJugador(jugador) == jugadorPie;
            if(fueraDeCantos){
                res = res &&  esPie;
            }
             
        }
        setPuedeCantarEnvido(res);
        return res;
    }


    public Boolean comprobarSiPuedeCantarFlor() {
        Boolean res = false;
        Integer numeroCantosDeFlor = getFloresCantadas();
        Integer numFloresParaPoderResponder = 1;
        Integer numFloresParaNoPoderDecirNada = 2;
        
    
        // 1) Verifica las condiciones:
        //    - puntosTruco == 1 no se cantó truco  
        //    - puntosEnvido == 0 no se ha ido mas allá del primer envido
       
        if (getPuntosTruco() == 1 && (getPuntosEnvido() == 0 || getQueEnvidoPuedeCantar()<0) && getRondaActual()==1 && partida.getConFlor()) {
            List<Carta> cartasJugadorActual = getCartasNoBorradas().get(getJugadorTurno()); //TIENE QUE SER OTRA VARIABLE SI SE VAN A COMPROBAR SIEMPRE, YA QUE CARTAS DISPONIBLES SE VA BORRANDO
            
            if(tiene3CartasMismoPalo(cartasJugadorActual)){
                setTresMismoPalo(true);

                // 2) Revisa si el jugador tiene 3 cartas del mismo palo y si no se ha cantado flor antes
                if (numeroCantosDeFlor==0) {
                    res = true;
                    // Por defecto, configuramos "queFlorPuedeCantar" en 1 (puede cantar flor)
                    setQueFlorPuedeCantar(1);
                }
                // 3) Si hubo un solo canto previo de Flor,
                //    entonces habilitamos "Contraflor" / "Con flor me achico"
                    
                else if (numeroCantosDeFlor == numFloresParaPoderResponder) {
                        // Segunda vez que se canta algo de Flor (=> Contraflor).
                        res = true;
                        setQueFlorPuedeCantar(2);
                    }
                else if (numeroCantosDeFlor >= numFloresParaNoPoderDecirNada) {
                    // Tercera vez en adelante => no se puede seguir subiendo
                    res = false;
                    setQueFlorPuedeCantar(0);
                    }
                }
            }
        
        // 4) Se registra si, finalmente, este jugador puede (o no) cantar algo en relacion a la Flor.
        setPuedeCantarFlor(res);
        return res;
    }
    
    public Integer getTantoDe1Jugador(Integer posicion){ //PARA LAS ESTADISTICAS
        Integer res = 0;
        Map<Palo, List<Carta>> diccCartasPaloJugador = agrupaCartasPalo(cartasNoBorradas.get(posicion));
        res = getMaxPuntuacion(diccCartasPaloJugador);
        return res;
    }



     public List<Integer> crearListaTantosCadaJugador(){ 
        List<Integer> listaEnvidosCadaJugador = new ArrayList<>();

        for(int i=0; i<getCartasDisp().size(); i++){
            Map<Palo, List<Carta>> diccCartasPaloJugador = agrupaCartasPalo(cartasDisp.get(i));
            Integer sumaJugador= getMaxPuntuacion(diccCartasPaloJugador);
            listaEnvidosCadaJugador.add(i, sumaJugador);

        }

        Integer jugadorMano = getJugadorTurno(); //Como esta funcion se llama al principio, será el mano
        List<Integer> nuevaLista = new ArrayList<>(listaEnvidosCadaJugador);
    
        Integer equipoMano = jugadorMano % 2;
        Integer equipoQueVaGanando = equipoMano;
        
        Integer puntajeGanador = listaEnvidosCadaJugador.get(jugadorMano);
        for(int i = siguienteJugador(jugadorMano); i!=jugadorMano;i= siguienteJugador(i)){ 
            Integer puntajeNuevoJugador = listaEnvidosCadaJugador.get(i);
            if(puntajeNuevoJugador == puntajeGanador){
                if(equipoQueVaGanando == equipoMano){
                    nuevaLista.set(i, null);
                } else {
                    equipoQueVaGanando = equipoMano;
                }
            } else if(puntajeNuevoJugador > puntajeGanador){
                equipoQueVaGanando = i%2;
                puntajeGanador = puntajeNuevoJugador;
            }else{
                nuevaLista.set(i, null);
            }
            
        }
        setEquipoGanadorEnvido(equipoQueVaGanando);
        setEnvidosCadaJugador(nuevaLista);
        return nuevaLista;
    }

    public List<Integer> crearListaTantosCadaJugadorFlor() {
        // 1) Calculamos la puntuación "de Flor" para cada jugador.
        List<Integer> listaFlorCadaJugador = new ArrayList<>();
        List<Boolean> listaTieneFlor=new ArrayList<>();
        if(getCartasNoBorradas()==null){
            setCartasNoBorradas(getCartasDisp());
        }
        for (int i = 0; i < getCartasDisp().size(); i++) {
            List<Carta> cartasJugador = getCartasDisp().get(i);
            
            if (tiene3CartasMismoPalo(cartasJugador)) {
                // Si el jugador tiene 3 cartas del mismo palo,
                // calculamos su puntuación de la forma habitual.
                Map<Palo, List<Carta>> diccCartasPaloJugador = agrupaCartasPalo(cartasJugador);
                Integer sumaJugador = getMaxPuntuacion(diccCartasPaloJugador);
                listaFlorCadaJugador.add(sumaJugador);
                listaTieneFlor.add(true);
            } else {
                // Si NO tiene Flor, forzamos el valor a 0 para que no
                // compita con quienes sí tienen Flor.
                listaTieneFlor.add(false);
                listaFlorCadaJugador.add(0);
            }
        }
    
        setListaTienenFlores(listaTieneFlor);
        // 2) Copiamos la lista original para manipular
        //    qué jugadores quedan con su puntaje o en null,
        //    según la lógica de "quién gana".
        Integer jugadorMano = getJugadorTurno();
        List<Integer> nuevaLista = new ArrayList<>(listaFlorCadaJugador);
    
        Integer equipoMano = jugadorMano % 2;
        Integer equipoQueVaGanando = equipoMano;
    
        // 3) Empezamos asumiendo que el que "va ganando" es el jugador Mano.
        Integer puntajeGanador = listaFlorCadaJugador.get(jugadorMano);
    
        // 4) Recorremos todos los jugadores y comparamos puntajes
        for (int i = siguienteJugador(jugadorMano); i != jugadorMano; i = siguienteJugador(i)) {
            Integer puntajeNuevoJugador = listaFlorCadaJugador.get(i);
    
            // Comparación habitual
            if (puntajeNuevoJugador.equals(puntajeGanador)) {
                if (equipoQueVaGanando == equipoMano) {
                    nuevaLista.set(i, null);
                } else {
                    equipoQueVaGanando = equipoMano;
                }
            } else if (puntajeNuevoJugador > puntajeGanador) {
                equipoQueVaGanando = i % 2;
                puntajeGanador = puntajeNuevoJugador;
            } else {
                nuevaLista.set(i, null);
            }
        }
    
        // 5) Guardamos el resultado final (qué equipo gana) y
        //    asignamos en la Mano los puntos de cada jugador (o null).
        setEquipoGanadorFlor(equipoQueVaGanando);
        setEnvidosFlorCadaJugador(nuevaLista);
    
        return nuevaLista;
    }
    
    public boolean tiene3CartasMismoPalo(List<Carta> cartasJugador) {
        boolean res = false;
        if(cartasJugador.size()==3){

            Palo paloInicial = cartasJugador.get(0).getPalo();

        res =cartasJugador.stream()
                            .allMatch(c -> c.getPalo().equals(paloInicial));
        }

        return res;
    }
    

    
     public Integer getMaxPuntuacion (Map<Palo, List<Carta>> diccCartasPaloJugador) {
        List< Integer> listaSumasPalo= new ArrayList<>();
        for(Map.Entry<Palo, List<Carta>> cartasPaloJugador : diccCartasPaloJugador.entrySet()){
            if(cartasPaloJugador.getValue().size()==1){
                listaSumasPalo.add( comprobarValor(cartasPaloJugador.getValue().get(0).getValor()));
            }
            else if(cartasPaloJugador.getValue().size()==2){
                Integer valor1= cartasPaloJugador.getValue().get(0).getValor();
                Integer valor2= cartasPaloJugador.getValue().get(1).getValor();
                listaSumasPalo.add(  20 + comprobarValor(valor1) + comprobarValor(valor2));
            }else if(cartasPaloJugador.getValue().size()==3){
                Integer valor= cartasPaloJugador.getValue().stream().map(x-> comprobarValor(x.getValor())).sorted(Comparator.reverseOrder()).limit(2).reduce(0, (a, b) -> a+b);
                listaSumasPalo.add( valor+constanteEnvido);
            }
        }
        Integer puntosEnvidoJugador = listaSumasPalo.stream().max(Comparator.naturalOrder()).get();
        return puntosEnvidoJugador;
    }

    public Integer comprobarValor(Integer value) {
        return value>=10?0:value;
    }

    public Map<Palo, List<Carta>> agrupaCartasPalo(List<Carta> listaDeCartas){
        Map<Palo, List<Carta>> diccCartasPaloJugador = listaDeCartas.stream().collect(Collectors.groupingBy(Carta::getPalo));
        return diccCartasPaloJugador;
    }

    public  Integer obtenerJugadorPie(){
        Integer pie = obtenerJugadorAnterior(getPartida().getJugadorMano());
        return pie;
    }
                       
    public  Integer obtenerJugadorAnterior(Integer jugador) { 
        Integer numJugadores = getPartida().getNumJugadores();
        return (jugador + numJugadores - 1) % numJugadores;
    }
        
    public  Integer siguienteJugador(Integer jugadorActual) {
        Integer siguiente = (jugadorActual + 1) % getPartida().getNumJugadores();
        return siguiente;
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

        setRondaActual(getRondaActual()+1);

        haTerminadoLaMano();
        List<Carta> listaCartasLanzadasNuevo = new ArrayList<>();
        for (int i = 0; i < getPartida().getNumJugadores(); i++){
            listaCartasLanzadasNuevo.add(null);
        }
        setCartasLanzadasRonda(listaCartasLanzadasNuevo);
        setJugadorTurno(empezador);
        return empezador;
    }

    public void haTerminadoLaMano() {
        Boolean res = false;
        Integer rondasEquipo1 = getGanadoresRondas().get(0);
        Integer rondasEquipo2 = getGanadoresRondas().get(1);
        if(getRondaActual()==4) {
            res = true;
        } else if(getGanadoresRondas().contains(rondasMaximasGanables) && rondasEquipo1+rondasEquipo2!=4) {
            res = true;
        }
        setTerminada(res);
    }

    public void gestionarGanadoresRonda(List<Integer> empates, Integer ganador){

        List<Integer> ganadoresRonda = getGanadoresRondas();
        Integer ganarRonda = 1;
        Integer ganadasEquipo1 = ganadoresRonda.get(0);
        Integer ganadasEquipo2 = ganadoresRonda.get(1);
        Boolean hayEquipo1 = false;
        Boolean hayEquipo2 = false;

        if(ganador!=null){
            if(ganador%2==0){
                hayEquipo1 = true;
            }else {
                hayEquipo2 = true;
            }

        } else {
            

            for(Integer jugador : empates){
                if(jugador%2==0){
                    hayEquipo1 = true;

                } else {
                    hayEquipo2 = true;
                }
            }

        }

        if(hayEquipo1 && hayEquipo2){
            ganadoresRonda.set(0, ganadasEquipo1 + ganarRonda);
            ganadoresRonda.set(1, ganadasEquipo2 + ganarRonda);
        } else if (hayEquipo1) {
            ganadoresRonda.set(0, ganadasEquipo1 + ganarRonda);
        } else {
            ganadoresRonda.set(1, ganadasEquipo2 + ganarRonda);
        }
        setGanadoresRondas(ganadoresRonda);
    }

    

    public  Integer cercanoAMano(List<Integer> jugadores) {
        Integer jugadorMano = getPartida().getJugadorMano();
        Integer jugadorPreferencia = null;
        List<Integer> lista = new ArrayList<>();
        for (Integer j : jugadores) {
            if (j % 2 == jugadorMano % 2) {
                lista.add(j);
            }
        }
        if (lista.isEmpty()) {
            lista = jugadores;
        }
        for (int i = jugadorMano; jugadorPreferencia == null; i = siguienteJugador(i)) {
            if (lista.contains(i)) {
                jugadorPreferencia = i;
            }
        }
        return jugadorPreferencia;
    }

    


                                
    

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

    public Integer quienRespondeFlor(){
        Integer jugadorQueResponde=0;
        Integer jugadorActual = getJugadorTurno();
        Integer jugadorIniciador=getJugadorIniciadorDelCanto();
        List<Boolean> listaFloreh=getListaTienenFlores();

        if(jugadorActual!=jugadorIniciador){
            jugadorQueResponde=jugadorIniciador;
        }
      
        for(int i = siguienteJugador(jugadorActual); i!=jugadorActual;i= siguienteJugador(i)){
            Boolean tieneFlor=listaFloreh.get(i);
            if(tieneFlor && jugadorActual%2!=i%2){
                jugadorQueResponde=i;
                break;
            }
        }

        return jugadorQueResponde;
    }
}


