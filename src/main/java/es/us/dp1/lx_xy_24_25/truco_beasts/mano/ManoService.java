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
    private final Partida partidaActual;
    PartidaJugadorRepository partidaJugadorRepository;

    public ManoService(Mano mano, Partida partida) {
        this.manoActual = mano;
        this.partidaActual = partida;
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

    public void cantar(Boolean respuesta, Integer jugadorRespuesta) throws SameEquipoException {
        // En frontend si es truco (respuesta si o no ) --> cantar(respuesta)
        // si respuesta = retruco --> cantar(true) y nueva llamada cantar(respuesta)
        // si respuesta = vale4 --> cantar(true), cantar(true) y nueva llamada
        // cantar(respuesta)
        Integer jugadorActual = manoActual.getJugadorTurno();
        PartidaJugador jugadorActu = partidaJugadorRepository.findPartidaJugadorbyId(jugadorActual);
        PartidaJugador jugadorResp = partidaJugadorRepository.findPartidaJugadorbyId(jugadorRespuesta);

        if (jugadorActu.getEquipo() == jugadorResp.getEquipo()) {
            throw new SameEquipoException();
        }
        if (respuesta) { // Quiero
            manoActual.setPuntuacion(manoActual.getPuntuacion() + 1);
        } else { // No quiero
            Integer puntuacionSuma = manoActual.getPuntuacion() - 1 == 0 ? 1 : manoActual.getPuntuacion() - 1;
            if (jugadorActu.getEquipo() == Equipo.EQUIPO1)
                partidaActual.setPuntosEquipo1(partidaActual.getPuntosEquipo1() + puntuacionSuma);

            else
                partidaActual.setPuntosEquipo2(partidaActual.getPuntosEquipo2() + puntuacionSuma);
        }
    }



    public void envido(Boolean respuesta, Integer jugadorRespuesta ) throws EnvidoException{
        Integer jugadorActual = manoActual.getJugadorTurno();
        PartidaJugador jugadorActu = partidaJugadorRepository.findPartidaJugadorbyId(jugadorActual);
        PartidaJugador jugadorResp = partidaJugadorRepository.findPartidaJugadorbyId(jugadorRespuesta);
        Integer jugadorMano = manoActual.getPartida().getJugadorMano();
        PartidaJugador jugadorManoR = partidaJugadorRepository.findPartidaJugadorbyId(jugadorMano);

        //Excepcion ver que el jugador que canta es el ultimo de cada equipo
        if(manoActual.getCartasDisp().get(-1).size()!=3){
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
            Integer puntuacionSuma = manoActual.getPuntuacion() - 1 == 0 ? 1 : manoActual.getPuntuacion() - 1;
            if (jugadorActu.getEquipo() == Equipo.EQUIPO1)
                partidaActual.setPuntosEquipo1(partidaActual.getPuntosEquipo1() + puntuacionSuma);

            else if (jugadorActu.getEquipo() == Equipo.EQUIPO2)
                partidaActual.setPuntosEquipo2(partidaActual.getPuntosEquipo2() + puntuacionSuma);
            else {
                
                    if (jugadorManoR.getEquipo() == Equipo.EQUIPO1)
                        partidaActual.setPuntosEquipo1(partidaActual.getPuntosEquipo1() + manoActual.getPuntuacion());
        
                    else
                        partidaActual.setPuntosEquipo2(partidaActual.getPuntosEquipo2() + manoActual.getPuntuacion());
                
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



    public void ganarMano() {
        Integer jugadorMano = manoActual.getPartida().getJugadorMano();
        PartidaJugador jugadorManoR = partidaJugadorRepository.findPartidaJugadorbyId(jugadorMano); //No habrá que pasarle el id del jugador en lugar del jugador entero?
        
        Integer rondasGanadasEquipo1 = manoActual.getGanadoresRondas().stream().filter(t-> partidaJugadorRepository.findPartidaJugadorbyId(t).getEquipo()==Equipo.EQUIPO1).toList().size();
        Integer rondasGanadasEquipo2 = manoActual.getGanadoresRondas().stream().filter(t-> partidaJugadorRepository.findPartidaJugadorbyId(t).getEquipo()==Equipo.EQUIPO2).toList().size();
        if(rondasGanadasEquipo1==2){
            partidaActual.setPuntosEquipo1(manoActual.getPuntuacion() + partidaActual.getPuntosEquipo1());
        }
        else if(rondasGanadasEquipo2==2){
            partidaActual.setPuntosEquipo2(manoActual.getPuntuacion() + partidaActual.getPuntosEquipo2());
        }else {
            if (jugadorManoR.getEquipo() == Equipo.EQUIPO1)
                partidaActual.setPuntosEquipo1(partidaActual.getPuntosEquipo1() + manoActual.getPuntuacion());

            else
                partidaActual.setPuntosEquipo2(partidaActual.getPuntosEquipo2() + manoActual.getPuntuacion());
        }

    }

}
