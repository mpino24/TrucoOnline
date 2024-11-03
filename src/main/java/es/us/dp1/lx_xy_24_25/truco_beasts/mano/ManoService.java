package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.EnvidoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.SameEquipoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Equipo;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorRepository;

@Service
public class ManoService {

    private final Mano manoActual;
    
    

    public ManoService(Mano mano ) {
        this.manoActual = mano;
       
    }

    public Integer obtenerJugadorPie(){
        Integer pie = obtenerJugadorAnterior(manoActual.getPartida().getJugadorMano());
        return pie;
    }
   
    public Integer obtenerJugadorAnterior(Integer jugador) { //FALTA TEST
        Integer numJugadores = manoActual.getPartida().getNumJugadores();
        return (jugador + numJugadores - 1) % numJugadores;
    }
    

    public Integer siguienteJugador(Integer jugadorActual) {
        Integer siguiente = (jugadorActual + 1) % manoActual.getPartida().getNumJugadores();
        return siguiente;
    }

    public void siguienteTurno() {
        Integer jugadorActual = manoActual.getJugadorTurno();
        Integer siguiente = (jugadorActual + 1) % manoActual.getPartida().getNumJugadores();
        manoActual.setJugadorTurno(siguiente);
    }

    public Integer compararCartas() {
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
                if (empate.size() == 1)
                    empate.add(empezador);
                empezador = null;
            }
        }
        return empezador != null ? empezador : cercanoAMano(empate);
    }

    public Integer cercanoAMano(List<Integer> jugadores) {
        Integer jugadorMano = manoActual.getPartida().getJugadorMano();
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

    public void tirarCarta(Integer indiceCarta) {
        Integer jugadorActual = manoActual.getJugadorTurno();
        Carta carta = manoActual.getCartasDisp().get(jugadorActual).get(indiceCarta);
        manoActual.getCartasDisp().get(jugadorActual).remove(carta);
        manoActual.getCartasLanzadasRonda().set(jugadorActual, carta);
        siguienteTurno();
    }


    public Boolean puedeCantarEnvido(){
        Boolean sePuede = false;
        Boolean esRondaUno = obtenerRondaActual() ==1;
        Boolean esPie = false;
        Boolean noHayTruco = manoActual.getPuntosTruco() <= 1;
        Boolean noSeCanto = manoActual.getPuntosEnvido() == 0; //No estoy seguro como lo gestionaremos con el resto de subidas, pero para el "primer canto" de envido, real envido o falta envido esta funcion serviria

        Integer jugTurno = manoActual.getJugadorTurno();
        Integer pie = obtenerJugadorPie();
        Integer otroPie = obtenerJugadorAnterior(pie);
        
        if(jugTurno == pie || jugTurno == otroPie) esPie = true;
        
        
        sePuede = esPie && noHayTruco && esPie && esRondaUno && noSeCanto; //FALTARIA COMPROBAR SI EL "otroPie" no lo canto, habría que añadir un puntaje de envido en mano y otro de truco
        
        return sePuede ;  //La idea de esto es que en el turno del jugador le aparezca, tambien es importante que si se canta truco en la primer ronda el siguiente le puede decir envido aunque no sea pie 
    }    

    public Integer obtenerRondaActual(){
        Integer ronda = 0;
        List<List<Carta>> cartas = manoActual.getCartasDisp();
        Integer cartasPie = cartas.get(obtenerJugadorPie()).size();
        if (cartasPie ==3) ronda= 1;
        else if(cartasPie==2) ronda=2;
        else ronda =3;            
        return ronda;
    }


    

    public void cantarTruco(Boolean respuesta) throws SameEquipoException { //FALTA TEST
        // En frontend si es truco (respuesta si o no ) --> cantar(respuesta)
        // si respuesta = retruco --> cantar(true) y nueva llamada cantar(respuesta)
        // si respuesta = vale4 --> cantar(true), cantar(true) y nueva llamada
        // cantar(respuesta)
        Integer jugadorActual = manoActual.getJugadorTurno();
        
        Partida partidaActual= manoActual.getPartida();
        
        if (respuesta) { // Quiero
            manoActual.setPuntosTruco(manoActual.getPuntosTruco() + 1); // Acá sumas 1 si es que si, y guardas 1 en puntuacion siempre, 
        } else { // No quiero
            Integer puntuacionSuma = manoActual.getPuntosTruco(); //entonces, el no quiero es mano.getPuntosTruco nomas

            if (jugadorActual % 2 == partidaActual.getJugadorMano() % 2) //Esto no estoy del todo seguro, pero el equipo se puede sacar con los que son modulo, lo que no se la diferencia de ambos
                partidaActual.setPuntosEquipo1(partidaActual.getPuntosEquipo1() + puntuacionSuma);

            else
                partidaActual.setPuntosEquipo2(partidaActual.getPuntosEquipo2() + puntuacionSuma);
        }
    }

    



    public void envido(Boolean respuesta) throws EnvidoException{ //FALTA TEST
        Integer jugadorActual = manoActual.getJugadorTurno();
        Integer jugadorResp = siguienteJugador(jugadorActual);

        Integer jugadorMano = manoActual.getPartida().getJugadorMano();
        
        Partida partidaActual= manoActual.getPartida();

        Integer ultimo = obtenerJugadorPie();
        //Excepcion ver que el jugador que canta es el ultimo de cada equipo
        if(jugadorActual != ultimo || jugadorActual!=obtenerJugadorAnterior(ultimo)){ //Lo que estaba puesto no, porque no siempre el ultimo es el ultimo en esa lista, son posiciones fijas de la "mesa"
            throw new EnvidoException("Solo se canta envido en la primera ronda");
        }
      
        if(respuesta){
            

           List<List<Carta>> cartasEquipo1 = manoActual.getCartasDisp().stream().filter(t-> manoActual.getCartasDisp().indexOf(t)%2 == 0).toList();
           List<List<Carta>> cartasEquipo2 = manoActual.getCartasDisp().stream().filter(t-> manoActual.getCartasDisp().indexOf(t)%2 != 0).toList();
           

           Integer puntosEquipo1=comprobarValor(cartasEquipo1);
           Integer puntosEquipo2=comprobarValor(cartasEquipo2);
     
            if(puntosEquipo1>puntosEquipo2) partidaActual.setPuntosEquipo1(partidaActual.getPuntosEquipo1() + 2);
            else partidaActual.setPuntosEquipo2(partidaActual.getPuntosEquipo2() + 2);

           }
           else{
            Integer puntuacionSuma = manoActual.getPuntosTruco() - 1 == 0 ? 1 : manoActual.getPuntosTruco() - 1; //Hay que o crear otra puntuacion para el envido o hacerlo de otra forma, porque se está cambiando la puntuacion del truco con la del envido
            if (jugadorActual%2 == 0) //lo mismo que antes, se podría hacer que los que estén en posiciones pares sean equipo1 y en impares equipo2
                partidaActual.setPuntosEquipo1(partidaActual.getPuntosEquipo1() + puntuacionSuma);

            else if (jugadorActual%2 == 1)
                partidaActual.setPuntosEquipo2(partidaActual.getPuntosEquipo2() + puntuacionSuma);
            else {
                
                    if (jugadorMano%2== 0)
                        partidaActual.setPuntosEquipo1(partidaActual.getPuntosEquipo1() + manoActual.getPuntosTruco());
        
                    else
                        partidaActual.setPuntosEquipo2(partidaActual.getPuntosEquipo2() + manoActual.getPuntosTruco());
                
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


// Esto no estoy seguro si va aca, ya que la comprobacion del que gana la mano deberia hacerse en el partidaService cuando se está por borrar
    // public void ganarMano() { 
    //     Integer jugadorMano = manoActual.getPartida().getJugadorMano();
    //     PartidaJugador jugadorManoR = partidaJugadorRepository.findPartidaJugadorbyId(jugadorMano); //No habrá que pasarle el id del jugador en lugar del jugador entero?
    //     Partida partidaActual= manoActual.getPartida();
    //     Integer rondasGanadasEquipo1 = manoActual.getGanadoresRondas().stream().filter(t-> partidaJugadorRepository.findPartidaJugadorbyId(t).getEquipo()==Equipo.EQUIPO1).toList().size();
    //     Integer rondasGanadasEquipo2 = manoActual.getGanadoresRondas().stream().filter(t-> partidaJugadorRepository.findPartidaJugadorbyId(t).getEquipo()==Equipo.EQUIPO2).toList().size();
    //     if(rondasGanadasEquipo1==2){
    //         partidaActual.setPuntosEquipo1(manoActual.getPuntosTruco() + partidaActual.getPuntosEquipo1());
    //     }
    //     else if(rondasGanadasEquipo2==2){
    //         partidaActual.setPuntosEquipo2(manoActual.getPuntosTruco() + partidaActual.getPuntosEquipo2());
    //     }else {
    //         if (jugadorManoR.getEquipo() == Equipo.EQUIPO1)
    //             partidaActual.setPuntosEquipo1(partidaActual.getPuntosEquipo1() + manoActual.getPuntosTruco());

    //         else
    //             partidaActual.setPuntosEquipo2(partidaActual.getPuntosEquipo2() + manoActual.getPuntosTruco());
    //     }

    // }

}
