package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

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
    

    public ManoService(Mano mano, Partida partida ) {
        this.manoActual = mano;
        this.partidaActual= partida;
    }
    
    public Integer siguienteJugador(Integer jugadorActual) { 
        Integer siguiente = (jugadorActual+1)%manoActual.getPartida().getNumJugadores();
        return siguiente;
    }

    public void siguienteTurno() {
        Integer jugadorActual = manoActual.getJugadorTurno();
        Integer siguiente = (jugadorActual+1)%manoActual.getPartida().getNumJugadores();
        manoActual.setJugadorTurno(siguiente);
    }

    public Integer compararCartas() {
        Integer poderMayor = 0;
        Integer empezador = null;
        List<Carta> cartasLanzadas = manoActual.getCartasLanzadasRonda();
        List<Integer> empate = new ArrayList<>();
        for(int i = 0; i<cartasLanzadas.size(); i++) {
            Integer poder = cartasLanzadas.get(i).getPoder();
            if(poderMayor < poder) {
                poderMayor = poder;
                empezador = i;
            } else if(poderMayor == poder) {
                empate.add(i);
                if(empate.size()==1) empate.add(empezador);
                empezador = null;
            }
        }
        return empezador!=null ? empezador : cercanoAMano(empate);
    }

    public Integer cercanoAMano(List<Integer> jugadores) {
        Integer jugadorMano = manoActual.getPartida().getJugadorMano();
        Integer jugadorPreferencia = null;
        List<Integer> lista = new ArrayList<>();
        for(Integer j:jugadores) {
            if(j%2==jugadorMano%2) {
                lista.add(j);
            }
        }
        if(lista.isEmpty()) {
            lista = jugadores;
        }
        for(int i = jugadorMano; jugadorPreferencia==null; i = siguienteJugador(i)) {
            if(lista.contains(i)) {
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
    
    
    public void cantar(Boolean respuesta, Integer jugadorRespuesta )throws SameEquipoException{
        //En frontend si es truco (respuesta si o no ) --> cantar(respuesta)
        //si respuesta = retruco --> cantar(true) y nueva llamada cantar(respuesta)
         //si respuesta = vale4 --> cantar(true), cantar(true) y nueva llamada cantar(respuesta)
        Integer jugadorActual = manoActual.getJugadorTurno();
        PartidaJugador jugadorActu = partidaJugadorRepository.findPartidaJugardorbyId(jugadorActual);
        PartidaJugador jugadorResp = partidaJugadorRepository.findPartidaJugardorbyId(jugadorRespuesta);

        if(jugadorActu.getEquipo() == jugadorResp.getEquipo()){
            throw new SameEquipoException();
        }
        if(respuesta){ //Quiero
            manoActual.setPuntuacion(manoActual.getPuntuacion()+1); 
        }
        else{ //No quiero
            Integer puntuacionSuma = manoActual.getPuntuacion()-1==0? 1: manoActual.getPuntuacion()-1;
            if(jugadorActu.getEquipo() == Equipo.EQUIPO1) partidaActual.setPuntosEquipo1(partidaActual.getPuntosEquipo1()+ puntuacionSuma) ; 

            else partidaActual.setPuntosEquipo2(partidaActual.getPuntosEquipo2()+ puntuacionSuma);   
        }      
    }


    public void ganarMano(){
        
        
    }

    
}
