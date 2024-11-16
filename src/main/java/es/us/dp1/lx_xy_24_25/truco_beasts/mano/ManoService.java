package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ManoService {

    private static Mano manoActual;

    public ManoService(Mano mano ) {
        ManoService.manoActual = mano;
    }
    
    public static Integer obtenerJugadorPie(){
        Integer pie = obtenerJugadorAnterior(manoActual.getPartida().getJugadorMano());
        return pie;
    }
                       
    public static Integer obtenerJugadorAnterior(Integer jugador) { 
        Integer numJugadores = manoActual.getPartida().getNumJugadores();
        return (jugador + numJugadores - 1) % numJugadores;
    }
        
    public static Integer siguienteJugador(Integer jugadorActual) {
        Integer siguiente = (jugadorActual + 1) % manoActual.getPartida().getNumJugadores();
        return siguiente;
    }
       
    //no hay controller
    public static void siguienteTurno() {
        Integer jugadorActual = manoActual.getJugadorTurno();
        Integer siguiente = (jugadorActual + 1) % manoActual.getPartida().getNumJugadores();
        manoActual.setJugadorTurno(siguiente);
    }
        
    
    //No hay controller
    public void anteriorTurno() { 
        Integer jugadorActual = manoActual.getJugadorTurno();
        Integer anterior = obtenerJugadorAnterior(jugadorActual);
        manoActual.setJugadorTurno(anterior);
    }
      
    
    public static Integer compararCartas() {
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

    //No hay controller
    public static Integer cercanoAMano(List<Integer> jugadores) {
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

    public static void tirarCarta(Integer indiceCarta) {
        if(!manoActual.getEsperandoRespuesta()){
            Integer jugadorActual = manoActual.getJugadorTurno();
            Carta carta = manoActual.getCartasDisp().get(jugadorActual).get(indiceCarta);
            manoActual.getCartasDisp().get(jugadorActual).remove(carta);
            manoActual.getCartasLanzadasRonda().set(jugadorActual, carta);
            siguienteTurno();
        }
        
    }
                            
    //no hay controller
    public Boolean puedeCantarEnvido(){
        Boolean sePuede = false;
        Boolean esRondaUno = obtenerRondaActual() ==1;
        Boolean esPie = false;
        Boolean noHayTruco = manoActual.getPuntosTruco() <= 1;
        Boolean noSeCanto = manoActual.getPuntosEnvido() == 0; //TODO: Evaluar esta funcion ya que para el "primer canto" de envido, real envido o falta envido esta funcion serviria, pero hay que revisar como se hacen los demás

        Integer jugTurno = manoActual.getJugadorTurno();
        Integer pie = obtenerJugadorPie();
        Integer otroPie = obtenerJugadorAnterior(pie);
    
        if(jugTurno == pie || jugTurno == otroPie) esPie = true;
    
    
        sePuede = esPie && noHayTruco && esPie && esRondaUno && noSeCanto; //DONE //FALTARIA COMPROBAR SI EL "otroPie" no lo canto, habría que añadir un puntaje de envido en mano y otro de truco
    
        return sePuede ;  //La idea de esto es que en el turno del jugador le aparezca, tambien es importante que si se canta truco en la primer ronda el siguiente le puede decir envido aunque no sea pie 
    }    

    //no hay controller
    public static Integer obtenerRondaActual(){
        Integer ronda = 0;
        List<List<Carta>> cartas = manoActual.getCartasDisp();
        Integer cartasPie = cartas.get(obtenerJugadorPie()).size();
        if (cartasPie ==3) ronda= 1;
        else if(cartasPie==2) ronda=2;
        else ronda =3;            
        return ronda;
    }

    //TODO: FALTAN TEST NEGATIVOS
    public static void cantosTruco(CantosTruco canto) throws Exception{
        Integer jugadorTurno = manoActual.getJugadorTurno();
        Integer equipoCantor = manoActual.getEquipoCantor();

        Integer rondaActual = obtenerRondaActual();
        List<List<Integer>> secuenciaCantos = manoActual.getSecuenciaCantoLista();
        List<Integer> listaRondaJugador = new ArrayList<>(); //Valores en el orden del nombre
        manoActual.setEsperandoRespuesta(true); // PARA PODER CONFIRMAR QUE EL QUE DICE QUIERO NO TIRA CARTA
        listaRondaJugador.add(rondaActual);
        listaRondaJugador.add(jugadorTurno);


        if (!puedeCantarTruco()) {
            throw new Exception( "No podés cantar truco ni sus variantes"); //GESTIONAR MEJOR
        }
                            
        switch (canto) {
            case TRUCO: 
                Truco truco = new TipoTruco();
                truco.accionAlTipoTruco(manoActual, jugadorTurno, equipoCantor, secuenciaCantos, listaRondaJugador, rondaActual);
                siguienteTurno();
                break;
            case RETRUCO:
                if (manoActual.getPuntosTruco() <2) {
                    throw new Exception( "No se canto truco"); //GESTIONAR MEJOR
                }
                Truco retruco = new TipoRetruco();
                retruco.accionAlTipoTruco(manoActual,jugadorTurno, equipoCantor, secuenciaCantos, listaRondaJugador, rondaActual);
                break;
            case VALECUATRO:
                if (manoActual.getPuntosTruco() <3) {
                    throw new Exception( "No se canto retruco"); //GESTIONAR MEJOR
                }
                Truco valeCuatro = new TipoValeCuatro();
                valeCuatro.accionAlTipoTruco(manoActual, jugadorTurno, equipoCantor, secuenciaCantos, listaRondaJugador, rondaActual);
                break;
            default:
                    throw new Exception( "hubo algun error"); //GESTIONAR MEJOR
        }      
    }
       
    //no hay controller
    public static Integer quienResponde(List<Integer> cantoHecho, Integer jugadorTurno){
        Integer res = null;
        Integer rondaActual = obtenerRondaActual();
        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);
        Integer jugadorSiguiente = siguienteJugador(jugadorTurno);
        Integer rondaCanto = cantoHecho.get(0);
        Integer jugadorCanto = cantoHecho.get(1); 
        if (rondaActual==rondaCanto && jugadorAnterior== jugadorCanto) {
            res = jugadorAnterior;
        }else{
            res = jugadorSiguiente;
        }
        return res;
    }
      
    public static void responderTruco(RespuestasTruco respuesta) throws Exception{ 
        Integer jugadorTurno = manoActual.getJugadorTurno();
        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);
        Integer truco = manoActual.getPuntosTruco();
        List<List<Integer>> secuenciaCantos = manoActual.getSecuenciaCantoLista();
        Integer queTrucoEs = secuenciaCantos.size();
        
        manoActual.setEsperandoRespuesta(false);
        // Boolean puedeEnvido = puedeCantarEnvido(); // TODO: IMPORTANTE VER COMO AGREGAR ESTA POSIBILIDAD
        switch (respuesta) {
            case QUIERO:
                RespuestaTruco quieroTruco = new RespuestaQuieroTruco();
                quieroTruco.accionRespuestaTruco(manoActual,jugadorTurno, jugadorAnterior, truco, secuenciaCantos, queTrucoEs);  
                break;
            case NO_QUIERO:
                //iria un terminarMano()
                    //Osea, se queda con truco -1 
                RespuestaTruco noQuieroTruco = new RespuestaNoQuieroTruco();
                noQuieroTruco.accionRespuestaTruco(manoActual,jugadorTurno, jugadorAnterior, truco, secuenciaCantos, queTrucoEs);  
                break;

            case SUBIR:
                RespuestaTruco subirTruco= new RespuestaSubirTruco();
                subirTruco.accionRespuestaTruco(manoActual,jugadorTurno, jugadorAnterior, truco, secuenciaCantos, queTrucoEs);
                break;
            default:
                throw new Exception( "hubo algun error"); //GESTIONAR MEJOR;
        }
    }
                        
                    
                    
     //no hay controller               
    public static Boolean puedeCantarTruco() { //O SUS OTRAS POSIBILIDADES
        Integer equipoCantor = manoActual.getEquipoCantor();
        Integer jugadorTurno = manoActual.getJugadorTurno();
        return (equipoCantor == null || jugadorTurno % 2 != equipoCantor);
    }
    
    //no hay controller
    public static Integer aQuienLeToca(List<Integer> cantoAnterior, List<Integer> cantoAhora, Integer jugadorTurno) {
        Integer res = null;
        Integer rondaActual = obtenerRondaActual();
        Integer jugadorSiguiente = siguienteJugador(jugadorTurno);
        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);

        Integer rondaCantoAnterior = cantoAnterior.get(0);
        Integer jugadorCantoAnterior = cantoAnterior.get(1);

        Integer rondaCantoAhora = cantoAhora.get(0);
        Integer jugadorCantoAhora = cantoAhora.get(1);
        if ((rondaCantoAnterior == rondaActual && rondaCantoAhora == rondaActual) && (jugadorCantoAnterior==jugadorTurno && jugadorCantoAhora== jugadorSiguiente)){
            res = jugadorTurno;
        }else{
            res = jugadorAnterior;
        }
        return res;
    }


}
