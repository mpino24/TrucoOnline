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

    private final Integer constanteEnvido=20;

    private List<Integer> envidos = new ArrayList<>();
    @ManyToOne
    private Partida partida;


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
}
