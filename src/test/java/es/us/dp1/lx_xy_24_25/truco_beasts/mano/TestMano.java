package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;

public class TestMano {
    Carta carta = new Carta();
    Mano mano = new Mano();

   
    @ParameterizedTest
    @ValueSource(ints= {10,11,12})
    public void asignarValorCartaNumero10(Integer value) {
        Integer valorResultante = mano.comprobarValor(value);
        assertEquals(0, valorResultante);
    }
    @ParameterizedTest
    @ValueSource(ints= {1,2,4,6,7})
    public void asignarValorCartaNumeroMayor10(Integer valor) {
        Integer valorResultante = mano.comprobarValor(valor);
        assertEquals(valor, valorResultante);
    }

    public Carta setUpCarta(Palo p, Integer valor){
        Carta carta= new Carta();
        carta.setPalo(p);
        carta.setValor(valor);
        return carta;
    }

    public List<List<Carta>> setUpListaCartasDispo(){
        Carta carta1= setUpCarta(Palo.ESPADAS, 10);
        Carta carta2= setUpCarta(Palo.ESPADAS, 5);
        Carta carta3= setUpCarta(Palo.OROS, 6);

        Carta carta4= setUpCarta(Palo.BASTOS, 3);
        Carta carta5= setUpCarta(Palo.ESPADAS, 2);
        Carta carta6= setUpCarta(Palo.COPAS, 3);

        Carta carta7= setUpCarta(Palo.OROS, 5);
        Carta carta8= setUpCarta(Palo.OROS, 7);
        Carta carta9= setUpCarta(Palo.OROS, 12);

        Carta carta10= setUpCarta(Palo.COPAS, 11);
        Carta carta11= setUpCarta(Palo.BASTOS, 6);
        Carta carta12= setUpCarta(Palo.ESPADAS, 4);
        
        List<Carta> jugador1= List.of(carta1, carta2, carta3);
        List<Carta> jugador2= List.of(carta4, carta5, carta6);
        List<Carta> jugador3= List.of(carta7, carta8, carta9);
        List<Carta> jugador4= List.of(carta10, carta11, carta12);

        return List.of(jugador1, jugador2, jugador3, jugador4);

    }


    public Map<Palo, List<Carta>> setUpCartasPaloValorTresIguales(){
        List<Carta> cartasJug3=setUpListaCartasDispo().get(2);
        Map<Palo, List<Carta>> listaPaloValoresJug=mano.agrupaCartasPalo(cartasJug3);
        return listaPaloValoresJug;
    }

    public Map<Palo, List<Carta>> setUpCartasPaloValorTresDiferentes(){
        List<Carta> cartasJug2=setUpListaCartasDispo().get(1);
        Map<Palo, List<Carta>> listaPaloValoresJug=mano.agrupaCartasPalo(cartasJug2);
        return listaPaloValoresJug;
    }
   

    public void setUpCartas(List<List<Carta>> cartasDisponibles){
        mano.setCartasDisp(cartasDisponibles);
    }

    @Test
    public List<Integer> envidosListaLlena(){
        
        List<List<Carta>> listaCartasDispo = setUpListaCartasDispo();
        setUpCartas(listaCartasDispo);
        List<Integer> listaResultante = List.of(25,3,32,6);
        List<Integer> listaEnvidosRegistrados = mano.listaEnvidos(mano.getCartasDisp());
        assertEquals(listaResultante, listaEnvidosRegistrados);
        return listaEnvidosRegistrados;
    }

    @Test
    public void envidosListaVacia(){
        setUpCartas(new ArrayList<>());
        List<Integer> listaEnvidosRegistrados = mano.listaEnvidos(mano.getCartasDisp());
        assertEquals(new ArrayList<>(), listaEnvidosRegistrados);
    }

    

    @Test
    public void agrupaCartasPorPalo(){

        List<Carta> cartasDeJugador1= setUpListaCartasDispo().get(0);

        List<Carta> cartasDeEspada= Arrays.asList(setUpListaCartasDispo().get(0).get(0), setUpListaCartasDispo().get(0).get(1));
        List<Carta> cartasDeOro=  Arrays.asList(setUpListaCartasDispo().get(0).get(2)); 

        Map<Palo, List<Carta>> paloCartas= new HashMap<>();
        paloCartas.put(Palo.ESPADAS, cartasDeEspada);
        paloCartas.put(Palo.OROS, cartasDeOro);
        
        assertEquals(paloCartas, mano.agrupaCartasPalo(cartasDeJugador1));

    }
   
    @Test
    public void puntuacionDeJugadorCorrecta3Iguales(){
        Map<Palo, List<Carta>> listaPaloValoresJug3= setUpCartasPaloValorTresIguales();
        assertEquals(32, mano.getMaxPuntuacion(listaPaloValoresJug3));
    }


    @Test
    public void puntuacionDeJugadorCorrecta3Diferentes(){
        Map<Palo, List<Carta>> listaPaloValoresJug2= setUpCartasPaloValorTresDiferentes();
        assertEquals(3, mano.getMaxPuntuacion(listaPaloValoresJug2));
    }
 
    public void setup(Integer jugMano, Integer numJugadores) {
        Partida partida = new Partida();
        partida.setNumJugadores(numJugadores);
        partida.setJugadorMano(jugMano);
        partida.setCodigo("TESTS");
        mano.setPartida(partida);
        List<Integer> ganadoresRonda = new ArrayList<>();
        ganadoresRonda.add(0);
        ganadoresRonda.add(0);
        mano.setGanadoresRondas(ganadoresRonda);
        
        
    }

  
    

    
    
}
