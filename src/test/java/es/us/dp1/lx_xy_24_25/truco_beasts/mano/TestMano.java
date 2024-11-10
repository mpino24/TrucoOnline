package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
        Carta carta6= setUpCarta(Palo.COPAS, 1);

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

    



    

    
    
}
