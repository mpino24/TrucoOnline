package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Component;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.CartaTiradaException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.CantosTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.ConverterRespuestaTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.ConverterTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestaTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestasTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.Truco;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class Mano {
    
    private List<List<Carta>> cartasDisp; // La lista de cartas de la posicion 0 seran las cartas que tiene el jugador en la posicion 0
    private Integer jugadorTurno; // Por defecto sera el jugador mano
    private List<Carta> cartasLanzadasRonda; // IMPORTANTE !!!! : se inicializa como lista de nulls y cada carta sustituye al null de la posicion de su jugador (se borran en cada ronda)
    private List<Integer> ganadoresRondas;
    private Integer puntosTruco=1;
    private Integer puntosEnvido =0;
    private List<List<Integer>> secuenciaCantoLista = new ArrayList<>(); // Tiene como primer atributo la ronda y de segundo el jugador en el que lo canto (siempre la primera es el truco, segunda retruco y tercera valecuatro)
    private Integer equipoCantor = null;
    private Boolean esperandoRespuesta = false;
    

    private final Integer constanteEnvido=20;

    private static ConverterTruco converterTruco = new ConverterTruco();
    private static ConverterRespuestaTruco converterRespuestaTruco = new ConverterRespuestaTruco();

    private List<Integer> envidos = new ArrayList<>();
    @ManyToOne
    private Partida partida;

    public void copiaParcialTruco(Mano mano) {
        this.equipoCantor = mano.getEquipoCantor();
        this.esperandoRespuesta = mano.getEsperandoRespuesta();
        this.jugadorTurno = mano.getJugadorTurno();
        this.secuenciaCantoLista = mano.getSecuenciaCantoLista();
        this.puntosTruco = mano.getPuntosTruco();
    }

    public  Boolean puedeCantarTruco() { //O SUS OTRAS POSIBILIDADES
        
        Integer equipoCantor = getEquipoCantor();
        Integer jugadorTurno = getJugadorTurno();
        return (equipoCantor == null || jugadorTurno % 2 != equipoCantor);
    }

    

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

    public  Integer obtenerJugadorPie(){
        Integer pie = obtenerJugadorAnterior(getPartida().getJugadorMano());
        return pie;
    }
                       
    public  Integer obtenerJugadorAnterior(Integer jugador) { 
        Integer numJugadores = getPartida().getNumJugadores();
        return (jugador + numJugadores - 1) % numJugadores;
    }
        
    public  Integer siguienteJugador(Integer jugadorActual) {
        Integer siguiente = (jugadorActual + 1) % getPartida().getNumJugadores();
        return siguiente;
    }
            
    public  void siguienteTurno() {      
        Integer jugadorActual = getJugadorTurno();
        Integer siguiente = (jugadorActual + 1) % getPartida().getNumJugadores();
        setJugadorTurno(siguiente);
    }
                            
    public  void anteriorTurno() {
        Integer jugadorActual = getJugadorTurno();
        Integer anterior = obtenerJugadorAnterior(jugadorActual);
        setJugadorTurno(anterior);
    }
                            
    public  Integer compararCartas() {

        Integer poderMayor = 0;
        Integer empezador = null;
        List<Carta> cartasLanzadas = getCartasLanzadasRonda();
        List<Integer> empate = new ArrayList<>();
        
        for (int i = 0; i < cartasLanzadas.size(); i++) {
            Integer poder = cartasLanzadas.get(i).getPoder();
            if (poderMayor < poder) {
                poderMayor = poder;
                empezador = i;
            } else if (poderMayor == poder) {
                empate.add(i);
                if (empate.size() == 1) {
                    empate.add(empezador);
                }
                empezador = null;
            }
        }

        
        gestionarGanadoresRonda(empate, empezador);
        

        empezador = empezador != null ? empezador : cercanoAMano(empate);

        List<Carta> listaCartasLanzadasNuevo = new ArrayList<>();
        for (int i = 0; i < getPartida().getNumJugadores(); i++){
            listaCartasLanzadasNuevo.add(null);
        }
        setCartasLanzadasRonda(listaCartasLanzadasNuevo);
        setJugadorTurno(empezador);

        return empezador;
    }

    public  void gestionarGanadoresRonda(List<Integer> empates, Integer ganador){

        List<Integer> ganadoresRonda = getGanadoresRondas();
        Integer ganarRonda = 1;
        Integer ganadasEquipo1 = ganadoresRonda.get(0);
        Integer ganadasEquipo2 = ganadoresRonda.get(1);
        Boolean hayEquipo1 = false;
        Boolean hayEquipo2 = false;

        if(ganador!=null){
            if(ganador%2==0){
                hayEquipo1 = true;
            }else {
                hayEquipo2 = true;
            }

        } else {
            

            for(Integer jugador : empates){
                if(jugador%2==0){
                    hayEquipo1 = true;

                } else {
                    hayEquipo2 = true;
                }
            }

            
        }

        if(hayEquipo1 && hayEquipo2){
            ganadoresRonda.set(0, ganadasEquipo1 + ganarRonda);
            ganadoresRonda.set(1, ganadasEquipo2 + ganarRonda);
        } else if (hayEquipo1) {
            ganadoresRonda.set(0, ganadasEquipo1 + ganarRonda);
        } else {
            ganadoresRonda.set(1, ganadasEquipo2 + ganarRonda);
        }
        setGanadoresRondas(ganadoresRonda);
    }

    

    public  Integer cercanoAMano(List<Integer> jugadores) {
        Integer jugadorMano = getPartida().getJugadorMano();
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

    public  Carta tirarCarta(Integer cartaId){
        if(!getEsperandoRespuesta()){
            Integer jugadorActual = getJugadorTurno();
            
            List<Carta> cartasDisponibles = getCartasDisp().get(jugadorActual);
            Integer indice = null;
            for (int i=0; i < cartasDisponibles.size(); i++){
                if(cartasDisponibles.get(i)!= null){
                    if(cartasDisponibles.get(i).getId()==cartaId){
                        indice=i;
                        
                    }
                }
                
            }
            if(indice==null){
                throw new CartaTiradaException();
            }
            Carta cartaALanzar = cartasDisponibles.get(indice);

            getCartasDisp().get(jugadorActual).set(indice,null);
            getCartasLanzadasRonda().set(jugadorActual, cartaALanzar);

            List<Carta> listaCartasLanzadas = getCartasLanzadasRonda();
            if(listaCartasLanzadas.stream().allMatch(c -> c!=null)){
                compararCartas();
            } else{
                siguienteTurno();
            }

            
            
    
            return cartaALanzar;
        } else{
            throw new CartaTiradaException("Tenés que responder antes de poder tirar una carta");
        }
        
    }

    public Boolean puedeCantarEnvido(){

        Boolean sePuede = false;
        Boolean esRondaUno = obtenerRondaActual() ==1;
        Boolean esPie = false;
        Boolean noHayTruco = getPuntosTruco() <= 1;
        Boolean noSeCanto = getPuntosEnvido() == 0; //TODO: Evaluar esta funcion ya que para el "primer canto" de envido, real envido o falta envido esta funcion serviria, pero hay que revisar como se hacen los demás

        Integer jugTurno = getJugadorTurno();
        Integer pie = obtenerJugadorPie();
        Integer otroPie = obtenerJugadorAnterior(pie);

        if(jugTurno == pie || jugTurno == otroPie) esPie = true;


        sePuede = esPie && noHayTruco && esPie && esRondaUno && noSeCanto; //DONE //FALTARIA COMPROBAR SI EL "otroPie" no lo canto, habría que añadir un puntaje de envido en mano y otro de truco

        return sePuede ;  //La idea de esto es que en el turno del jugador le aparezca, tambien es importante que si se canta truco en la primer ronda el siguiente le puede decir envido aunque no sea pie 

    }

    public  Integer obtenerRondaActual(){

        Integer ronda = 0;
        List<List<Carta>> cartas = getCartasDisp();
        Integer cartasPie = cartas.get(obtenerJugadorPie()).size();
        if (cartasPie ==3) ronda= 1;
        else if(cartasPie==2) ronda=2;
        else ronda =3;
        return ronda;
    }

    //TODO: FALTAN TEST NEGATIVOS
    public Mano cantosTruco(CantosTruco canto){
        Integer jugadorTurno = getJugadorTurno();
        Integer equipoCantor = getEquipoCantor();

        Integer rondaActual = obtenerRondaActual();
        List<List<Integer>> secuenciaCantos = getSecuenciaCantoLista();
        List<Integer> listaRondaJugador = new ArrayList<>(); //Valores en el orden del nombre
        setEsperandoRespuesta(true); // PARA PODER CONFIRMAR QUE EL QUE DICE QUIERO NO TIRA CARTA
        listaRondaJugador.add(rondaActual);
        listaRondaJugador.add(jugadorTurno);

        Mano mano = new Mano();
        if (!puedeCantarTruco()) {
            throw new TrucoException( ); 
        }
        Truco estadoTruco =  converterTruco.convertToEntityAttribute(canto);

        switch (canto) {
            case TRUCO: 
                if(getPuntosTruco()>1){
                    throw new TrucoException("Ya se canto el truco");
                }
                mano = estadoTruco.accionAlTipoTruco(this, jugadorTurno, equipoCantor, secuenciaCantos, listaRondaJugador, rondaActual);
                copiaParcialTruco(mano);
                break;
            case RETRUCO:
                if (getPuntosTruco() <2) {
                    throw new TrucoException( "No se cantó el truco");
                } else if(getPuntosTruco() >2){
                    throw new TrucoException("Ya se canto el retruco");
                }
                mano = estadoTruco.accionAlTipoTruco(this,jugadorTurno, equipoCantor, secuenciaCantos, listaRondaJugador, rondaActual);
                copiaParcialTruco(mano);
                break;
            case VALECUATRO:
                if (getPuntosTruco() <3) {
                    throw new TrucoException( "No se cantó el retruco"); 
                }else if(getPuntosTruco() >3){
                    throw new TrucoException("Ya se canto el valecuatro");
                }
                mano = estadoTruco.accionAlTipoTruco(this, jugadorTurno, equipoCantor, secuenciaCantos, listaRondaJugador, rondaActual);
                copiaParcialTruco(mano);
            
                break;
            default:
                throw new TrucoException( "Canto no valido"); 
        }
        return mano;
    }
                                
    public  Integer quienResponde(List<Integer> cantoHecho, Integer jugadorTurno){
        Integer res = null;
        Integer rondaActual = obtenerRondaActual();
        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);
        Integer jugadorSiguiente = siguienteJugador(jugadorTurno);
        Integer rondaCanto = cantoHecho.get(0);
        Integer jugadorCanto = cantoHecho.get(1); 
        if(partida.getNumJugadores() ==2){
            res = jugadorSiguiente;
        } else if (rondaActual==rondaCanto && jugadorAnterior== jugadorCanto) {
            res = jugadorAnterior;
        }else{
            res = jugadorSiguiente;
        }
        return res;
    }
      
    public void responderTruco(RespuestasTruco respuesta) { 
        Integer jugadorTurno = getJugadorTurno();
        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);
        Integer truco = getPuntosTruco();
        List<List<Integer>> secuenciaCantos = getSecuenciaCantoLista();
        Integer queTrucoEs = secuenciaCantos.size();

        Mano mano = new Mano();
        setEsperandoRespuesta(false);

        RespuestaTruco respuestaTruco =   converterRespuestaTruco.convertToEntityAttribute(respuesta);
        // Boolean puedeEnvido = puedeCantarEnvido(); // TODO: IMPORTANTE VER COMO AGREGAR ESTA POSIBILIDAD
        switch (respuesta) {
            case QUIERO:
                mano = respuestaTruco.accionRespuestaTruco(this,jugadorTurno, jugadorAnterior, truco, secuenciaCantos, queTrucoEs);
                copiaParcialTruco(mano);
                
                break;
            case NO_QUIERO:
                //iria un terminarMano()
                    //Osea, se queda con truco -1 
                mano = respuestaTruco.accionRespuestaTruco(this,jugadorTurno, jugadorAnterior, truco, secuenciaCantos, queTrucoEs);
                copiaParcialTruco(mano);
                
                break;

            case SUBIR:

                mano = respuestaTruco.accionRespuestaTruco(this,jugadorTurno, jugadorAnterior, truco, secuenciaCantos, queTrucoEs);
                copiaParcialTruco(mano);
                
                break;
            default:
                throw new TrucoException( "Respuesta al truco no valida"); 
        }
    }

    public  Integer aQuienLeToca() {
        Integer res = null;

        List<Integer> cantoAnterior = getSecuenciaCantoLista().get(secuenciaCantoLista.size()-2);
        List<Integer> cantoAhora = getSecuenciaCantoLista().get(secuenciaCantoLista.size()-1);

        Integer rondaActual = obtenerRondaActual();
        Integer jugadorSiguiente = siguienteJugador(jugadorTurno);
        Integer jugadorAnterior = obtenerJugadorAnterior(jugadorTurno);

        Integer rondaCantoAnterior = cantoAnterior.get(0);
        Integer jugadorCantoAnterior = cantoAnterior.get(1);

        Integer rondaCantoAhora = cantoAhora.get(0);
        Integer jugadorCantoAhora = cantoAhora.get(1);

        if(rondaActual!=rondaCantoAnterior){
            res = jugadorAnterior;
        }

        if ((rondaCantoAnterior == rondaActual && rondaCantoAhora == rondaActual) && (jugadorCantoAnterior==jugadorTurno && jugadorCantoAhora== jugadorSiguiente)){
            res = jugadorTurno;
        }else{
            res = jugadorAnterior;
        }
        return res;
    }
}
