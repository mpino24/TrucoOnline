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
        Boolean noSeCanto = manoActual.getPuntosEnvido() == 0; //No estoy seguro como lo gestionaremos con el resto de subidas, pero para el "primer canto" de envido, real envido o falta envido esta funcion serviria

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

    public void cantosTruco(CantosTruco canto) throws Exception{ //0 -> Truco, 1 -> Retruco, 2 -> Vale cuatro (CON UN ENUM QUEDARIA MÁS LINDO)
        Integer jugadorTurno = manoActual.getJugadorTurno();
        Integer equipoCantor = manoActual.getEquipoCantor();
        Integer jugadorSiguiente =  siguienteJugador(jugadorTurno);
        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);
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
            case TRUCO: //Truco
                
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
                Integer rondaTruco = cantoEnTruco.get(0);
                Integer jugadorTruco = cantoEnTruco.get(1);
                manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));

                if(rondaActual==rondaTruco && jugadorAnterior == jugadorTruco){
                    manoActual.setJugadorTurno(jugadorAnterior);
                } else {
                    manoActual.setJugadorTurno(jugadorSiguiente);
                }
                secuenciaCantos.add(listaRondaJugador);
                manoActual.setSecuenciaCantoLista(secuenciaCantos);
                
                break;
            case VALECUATRO:
                if (manoActual.getPuntosTruco() <3) {
                    throw new Exception( "No se canto retruco"); //GESTIONAR MEJOR
                }
                List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                Integer rondaRetruco = cantoEnRetruco.get(0);
                Integer jugadorRetruco = cantoEnRetruco.get(1);
                manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
                if(rondaActual==rondaRetruco && (jugadorAnterior == jugadorRetruco || jugadorSiguiente==jugadorRetruco )){ // Tecnicamente la comprobaci
                    if(jugadorSiguiente==jugadorRetruco){
                        manoActual.setJugadorTurno(jugadorSiguiente);
                    }else{
                        manoActual.setJugadorTurno(jugadorAnterior);
                    }
                    
                } else {
                    manoActual.setJugadorTurno(jugadorSiguiente);
                }
                secuenciaCantos.add(listaRondaJugador);
                manoActual.setSecuenciaCantoLista(secuenciaCantos);

                break;
            
            default:
                throw new Exception( "hubo algun error"); //GESTIONAR MEJOR
        }      
    }


    

    //TODO: FALTA TEST
    public void responderTruco(Respuestas respuesta) throws Exception{ //0 -> Quiero, 1 -> No Quiero, 2 -> Retruco (subir apuesta) (CON UN ENUM QUEDARIA MÁS LINDO)
        Integer jugadorTurno = manoActual.getJugadorTurno();
        Integer jugadorSiguiente =  siguienteJugador(jugadorTurno);
        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);

        Integer truco = manoActual.getPuntosTruco();
        List<List<Integer>> secuenciaCantos = manoActual.getSecuenciaCantoLista();
        Integer queTrucoEs = secuenciaCantos.size();
        Integer rondaActual = obtenerRondaActual();
        manoActual.setEsperandoRespuesta(false);
        // Boolean puedeEnvido = puedeCantarEnvido(); // TODO: IMPORTANTE VER COMO AGREGAR ESTA POSIBILIDAD
        switch (respuesta) {
            case QUIERO:
                manoActual.setPuntosTruco(truco +1);
                if(queTrucoEs == 1){ //Es decir, Truco
                    manoActual.setJugadorTurno(jugadorAnterior);
                } else if( queTrucoEs == 2){
                    List<Integer> cantoEnTruco = secuenciaCantos.get(0);
                    Integer rondaTruco = cantoEnTruco.get(0);
                    Integer jugadorTruco = cantoEnTruco.get(1);

                    List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                    Integer rondaRetruco = cantoEnRetruco.get(0);
                    Integer jugadorRetruco = cantoEnRetruco.get(1);
                    if((rondaTruco == rondaActual && rondaRetruco == rondaActual) && (jugadorTruco==jugadorTurno && jugadorSiguiente==jugadorRetruco)){
                        manoActual.setJugadorTurno(jugadorTurno);
                    } else{
                        manoActual.setJugadorTurno(jugadorAnterior);
                    }
                    
                } else{         
                    List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                    Integer rondaRetruco = cantoEnRetruco.get(0);
                    Integer jugadorRetruco = cantoEnRetruco.get(1);
                    List<Integer> cantoEnValecuatro = secuenciaCantos.get(2);
                    Integer rondaValecuatro = cantoEnValecuatro.get(0);
                    Integer jugadorValecuatro = cantoEnValecuatro.get(1);

                    if((rondaRetruco == rondaActual && rondaValecuatro == rondaActual) && (jugadorRetruco==jugadorTurno && jugadorSiguiente==jugadorValecuatro)){
                        manoActual.setJugadorTurno(jugadorTurno);
                    } else{
                        manoActual.setJugadorTurno(jugadorAnterior);
                    }
                }
                
               // lo arregle (creo xd) // anteriorTurno(); //TODO: ESTO ESTÁ MAL, ya que si truco, retruco, quiero no hay que volver 1 atras, porque le toca a él. Habria que guardad el jugador cantor y a partir de él obtener el equipo cantor mejor (VA A AFECTAR TAMBIEN SI SE CANTA TRUCO EN UNA RONDA Y DESPUES RETRUCO OTRA PERSONA EN OTRA)
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
    



    /* public void envido(Boolean respuesta) throws EnvidoException{ //FALTA TEST
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
    }    */


}
