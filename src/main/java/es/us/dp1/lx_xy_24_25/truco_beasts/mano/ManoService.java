package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

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
   
    public Integer obtenerJugadorAnterior(Integer jugador) { 
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

    public void anteriorTurno() { 
        Integer jugadorActual = manoActual.getJugadorTurno();
        Integer anterior = obtenerJugadorAnterior(jugadorActual);
        manoActual.setJugadorTurno(anterior);
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
        if(!manoActual.getEsperandoRespuesta()){
            Integer jugadorActual = manoActual.getJugadorTurno();
            Carta carta = manoActual.getCartasDisp().get(jugadorActual).get(indiceCarta);
            manoActual.getCartasDisp().get(jugadorActual).remove(carta);
            manoActual.getCartasLanzadasRonda().set(jugadorActual, carta);
            siguienteTurno();
        }
        
    }


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

    public Integer obtenerRondaActual(){
        Integer ronda = 0;
        List<List<Carta>> cartas = manoActual.getCartasDisp();
        Integer cartasPie = cartas.get(obtenerJugadorPie()).size();
        if (cartasPie ==3) ronda= 1;
        else if(cartasPie==2) ronda=2;
        else ronda =3;            
        return ronda;
    }

    //TODO: FALTAN TEST NEGATIVOS

    public void cantosTruco(CantosTruco canto) throws Exception{
        Integer jugadorTurno = manoActual.getJugadorTurno();
        Integer equipoCantor = manoActual.getEquipoCantor();

        Integer rondaActual = obtenerRondaActual();
        List<List<Integer>> secuenciaCantos = manoActual.getSecuenciaCantoLista();
        List<Integer> listaRondaJugador = new ArrayList<>(); //Valores en el orden del nombre
        listaRondaJugador.add(rondaActual);
        listaRondaJugador.add(jugadorTurno);

        
        manoActual.setEsperandoRespuesta(true); // PARA PODER CONFIRMAR QUE EL QUE DICE QUIERO NO TIRA CARTA
        
        if (!puedeCantarTruco()) {
            throw new Exception( "No podés cantar truco ni sus variantes"); //GESTIONAR MEJOR
        }
        
        switch (canto) {
            case TRUCO: 
                
                manoActual.setEquipoCantor(getEquipo(jugadorTurno));//el 0 es el equipo 1 (los pares) y el 1 es el equipo 2 (impares) 
                                                             //se le podría sumar 1 al resultado del modulo y quedan con el mismo numero (yo creo que lo complica más) 
                
                secuenciaCantos.add(listaRondaJugador);
                manoActual.setSecuenciaCantoLista(secuenciaCantos);
                siguienteTurno();
                break;
            case RETRUCO:
                if (manoActual.getPuntosTruco() <2) {
                    throw new Exception( "No se canto truco"); //GESTIONAR MEJOR
                }
                List<Integer> cantoEnTruco = secuenciaCantos.get(0);
                Integer elQueRespondeAlRetruco = quienResponde(cantoEnTruco, jugadorTurno);
                manoActual.setJugadorTurno(elQueRespondeAlRetruco);
                manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
                secuenciaCantos.add(listaRondaJugador);
                manoActual.setSecuenciaCantoLista(secuenciaCantos);
                
                break;
            case VALECUATRO:
                if (manoActual.getPuntosTruco() <3) {
                    throw new Exception( "No se canto retruco"); //GESTIONAR MEJOR
                }
                List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                Integer elQueResponde = quienResponde(cantoEnRetruco, jugadorTurno);
                manoActual.setJugadorTurno(elQueResponde);
                manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
                secuenciaCantos.add(listaRondaJugador);
                manoActual.setSecuenciaCantoLista(secuenciaCantos);

                break;
            
            default:
                throw new Exception( "hubo algun error"); //GESTIONAR MEJOR
        }      
    }

    public Integer quienResponde(List<Integer> cantoHecho, Integer jugadorTurno){
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

    

    //TODO: FALTA TEST
    public void responderTruco(Respuestas respuesta) throws Exception{ 
        Integer jugadorTurno = manoActual.getJugadorTurno();

        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);

        Integer truco = manoActual.getPuntosTruco();
        List<List<Integer>> secuenciaCantos = manoActual.getSecuenciaCantoLista();
        Integer queTrucoEs = secuenciaCantos.size();
        
        manoActual.setEsperandoRespuesta(false);
        // Boolean puedeEnvido = puedeCantarEnvido(); // TODO: IMPORTANTE VER COMO AGREGAR ESTA POSIBILIDAD
        switch (respuesta) {
            case QUIERO:
                manoActual.setPuntosTruco(truco +1);
                if(queTrucoEs == 1){ //Es decir, Truco
                    manoActual.setJugadorTurno(jugadorAnterior);
                } else if( queTrucoEs == 2){
                    List<Integer> cantoEnTruco = secuenciaCantos.get(0);
                    List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                    Integer aQuienLeTocaAhora = aQuienLeToca(cantoEnTruco, cantoEnRetruco, jugadorTurno);
                    manoActual.setJugadorTurno(aQuienLeTocaAhora);
                    
                } else {         
                    List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                    List<Integer> cantoEnValecuatro = secuenciaCantos.get(2);
                    Integer aQuienLeTocaAhora = aQuienLeToca(cantoEnRetruco, cantoEnValecuatro, jugadorTurno);
                    manoActual.setJugadorTurno(aQuienLeTocaAhora);
                }  
                break;
            case NO_QUIERO:
                //iria un terminarMano()
                manoActual.setPuntosTruco(truco); //Osea, se queda con truco -1 
                break;
            case SUBIR:
                if(truco == 1){
                    manoActual.setPuntosTruco(truco+1); //Declaramos como un "quiero" el truco
                    cantosTruco(CantosTruco.RETRUCO);
                }else if(truco==2){
                    manoActual.setPuntosTruco(truco +1);
                    cantosTruco(CantosTruco.VALECUATRO);
                } else {
                    throw new Exception( "No se puede subir más, capo"); //GESTIONAR MEJOR
                }
                
                break;
            default:
                throw new Exception( "hubo algun error"); //GESTIONAR MEJOR;
        }
    }

    //TODO: FALTA TEST (NO SE NI SI ES NECESARIA ESTA FUNCIÓN)
    public Integer getEquipo(Integer jugador){ // Podria devolver un integer sino
        Integer equipo = null;
        if (jugador%2==0) equipo=0; //equipo 1
        else if(jugador%2==1) equipo =1; //equipo 2
        return equipo;
    }



    public Boolean puedeCantarTruco() { //O SUS OTRAS POSIBILIDADES
        Integer equipoCantor = manoActual.getEquipoCantor();
        Integer jugadorTurno = manoActual.getJugadorTurno();
        return (equipoCantor == null || jugadorTurno % 2 != equipoCantor);
    }
    

    public Integer aQuienLeToca(List<Integer> cantoAnterior, List<Integer> cantoAhora, Integer jugadorTurno) {
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
