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
    private List<List<Integer>> secuenciaCantoLista = new ArrayList<>(); // Tiene como primer atributo la ronda y de segundo el jugador en el que lo canto (siempre la primera es el truco, segunda retruco y tercera valecuatro)
    private Integer esTrucoEnvidoFlor = 0; // 0 -> Truco, 1 -> envido, 2 -> flor
    private Integer equipoCantor = null;
    private Boolean esperandoRespuesta = false;
    private Integer jugadorIniciadorDelCanto;
    private Boolean terminada = false;
    private Boolean puedeCantarTruco = true;
    private Boolean puedeCantarEnvido = false;
    private Integer queEnvidoPuedeCantar = 2; //1 -> solo falta envido, 2 -> falta y real, 3 -> falta, real y envido, otro -> nada
    private Integer equipoGanadorEnvido;
    private final Integer constanteEnvido=20;
    private final Integer puntosMaximosDelTruco = 4;
    private final Integer rondasMaximasGanables = 2;
    
    private List<Integer> envidosCantados;
    @ManyToOne
    private Partida partida;

    public void copiaParcialTruco(Mano mano) {
        this.equipoCantor = mano.getEquipoCantor();
        this.esperandoRespuesta = mano.getEsperandoRespuesta();
        this.jugadorTurno = mano.getJugadorTurno();
        this.secuenciaCantoLista = mano.getSecuenciaCantoLista();
        this.puntosTruco = mano.getPuntosTruco();
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
        if(getRondaActual() != 1 || getPuntosEnvido() !=0){
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



   

     public List<Integer> listaEnvidos(){ 
        List<Integer> listaEnvidosCadaJugador = new ArrayList<>();
        for(int i=0; i<getCartasDisp().size(); i++){
            Map<Palo, List<Carta>> diccCartasPaloJugador = agrupaCartasPalo(cartasDisp.get(i));
            Integer sumaJugador= getMaxPuntuacion(diccCartasPaloJugador);
            listaEnvidosCadaJugador.add(i, sumaJugador);
        }

        Integer jugadorMano = getJugadorTurno(); //Como esta funcion se llama al principio, será el mano
        List<Integer> nuevaLista = listaEnvidosCadaJugador;
    
        Integer equipoQueVaGanando = jugadorMano % 2;
        Integer puntajeGanador = listaEnvidosCadaJugador.get(jugadorMano);
        for(int i = siguienteJugador(jugadorMano); i<obtenerJugadorAnterior(jugadorMano);i= siguienteJugador(i)){ //TIENE QUE SER MÁS FÁCIL SEGURO
            if(i%2 == equipoQueVaGanando){
                nuevaLista.set(i, null);
            } else{
                if(listaEnvidosCadaJugador.get(i) >= puntajeGanador){ //TODO: NO SE CONTEMPLA SI HAY QUE HACER MARCHA ATRAS NI SI HAY EMPATE (LLAMARIA A CERCANO A MANO)
                    equipoQueVaGanando = i%2;
                    puntajeGanador = listaEnvidosCadaJugador.get(i);
                }else{
                    nuevaLista.set(i, null);
                }
            }
        }
        setEquipoGanadorEnvido(equipoQueVaGanando);
        return listaEnvidosCadaJugador;
    }

    //TODO: PROBABLEMENTE SEA MÁS CONVENIENTE EN MANOSERVICE
    public Integer gestionarPuntosEnvido(Boolean noQuiero){
        Integer res = getPuntosEnvido(); //Siempre sera cero en un principio
        Partida partida = getPartida();
        Integer puntosEquipo1 = partida.getPuntosEquipo1();
        Integer puntosEquipo2 = partida.getPuntosEquipo2();
        Integer puntosMaximos = partida.getPuntosMaximos();
        

        Integer multiplicadorEnvido = 2;
        Integer multiplicadorRealEnvido = 3;

        List<Integer> envidoCantados = getEnvidosCantados();
        Integer equipoGanadorEnvido = getEquipoGanadorEnvido();

        Integer cantidadFaltaEnvidos = envidoCantados.get(2);
        Integer cantidadEnvidos = envidoCantados.get(0);
        Integer cantidadRealEnvidos = envidoCantados.get(1);

        Integer maximoPuntaje = cantidadEnvidos*multiplicadorEnvido + cantidadRealEnvidos*multiplicadorRealEnvido;
        
        if(noQuiero){
            if(cantidadFaltaEnvidos==maximosFaltaEnvido){
                res =maximoPuntaje;
            } else if(cantidadRealEnvidos==maximosRealEnvido){
                res = cantidadEnvidos*multiplicadorEnvido;
            }else if(cantidadEnvidos == maximosEnvido) {
                res = (cantidadEnvidos -1) *multiplicadorEnvido;
            } else{
                res = 1;
            }
            
        }else{
            if(cantidadFaltaEnvidos == maximosFaltaEnvido){
                res = equipoGanadorEnvido ==0 ? puntosMaximos-puntosEquipo1 : puntosMaximos-puntosEquipo2;
            }
            else{
                res = maximoPuntaje;
            }
            
        }
        setPuntosEnvido(res);
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
            
    public  void siguienteTurno() {      
        Integer jugadorActual = getJugadorTurno();
        Integer siguiente = (jugadorActual + 1) % getPartida().getNumJugadores();
        setJugadorTurno(siguiente);
    }
                            
    public  void anteriorTurno() {
        Integer jugadorActual = getJugadorTurno();
        Integer anterior = obtenerJugadorAnterior(jugadorActual);
        setJugadorTurno(anterior);
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

    public Integer quienRespondeEnvido(){
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
}


