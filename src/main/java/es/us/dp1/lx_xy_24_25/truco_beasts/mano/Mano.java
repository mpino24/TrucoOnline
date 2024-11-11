package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

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
    private List<Carta> cartasLanzadasRonda = new ArrayList<>(); // IMPORTANTE !!!! : se inicializa como lista de nulls y cada carta sustituye al null de la posicion de su jugador (se borran en cada ronda)
    private List<Integer> ganadoresRondas = new ArrayList<>();
    private Integer puntosTruco=1;
    private Integer puntosEnvido =0;
    private List<List<Integer>> secuenciaCantoLista = new ArrayList<>(); // Tiene como primer atributo la ronda y de segundo el jugador en el que lo canto (siempre la primera es el truco, segunda retruco y tercera valecuatro)
    private Integer equipoCantor = null;
    private Boolean esperandoRespuesta = false;

    private List<Integer> envidos = new ArrayList<>();
    @ManyToOne
    private Partida partida;

     public Integer jugadorGanadorEnvido(){ 
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
    public Integer siguienteJugador(Integer jugadorActual) {
        Integer siguiente = (jugadorActual + 1) % partida.getNumJugadores();
        return siguiente;
    }

    public List<List<Integer>> puntosEnvido(){
        List<List<Integer>> res = new ArrayList<>();
        
        for (int i = partida.getJugadorMano(); res.size() < cartasDisp.size(); i = siguienteJugador(i)) {
            List<Integer> posicionEnvido = new ArrayList<>();
            posicionEnvido.add(i);
            Map<Palo, List<Carta>> diccCartasPaloJugador = cartasDisp.get(i).stream().collect(Collectors.groupingBy(Carta::getPalo));
            Integer sumaJugador= getMaxPuntuacion(diccCartasPaloJugador);
            posicionEnvido.add(sumaJugador);
            res.add(posicionEnvido);
        }
        
        return res;
        
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
