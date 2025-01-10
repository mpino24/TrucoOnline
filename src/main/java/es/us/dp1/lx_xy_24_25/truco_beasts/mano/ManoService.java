package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;
import es.us.dp1.lx_xy_24_25.truco_beasts.carta.CartaRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas.Metrica;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.CartaTiradaException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.EnvidoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.FlorException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco.ConverterRespuestaTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco.ConverterTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco.RespuestaTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco.Truco;


@Service
public class ManoService {

    private final Map<String, Mano> manosPorPartida = new HashMap<>();
    
    private final CartaRepository cartaRepository;

    private final PartidaService partidaService;

	private final ConverterTruco converterTruco = new ConverterTruco();
    private final ConverterRespuestaTruco converterRespuestaTruco = new ConverterRespuestaTruco(this);
    private final PartidaJugadorService partidaJugadorService;
	
    

    @Autowired
    public ManoService(CartaRepository cartaRepository, PartidaService partidaService, PartidaJugadorService partidaJugadorService) {
        this.cartaRepository = cartaRepository;
        this.partidaService = partidaService;
        this.partidaJugadorService= partidaJugadorService;
    }

    public void actualizarMano(Mano mano, String codigo){
		Mano manoAnterior = manosPorPartida.get(codigo);
        if(manoAnterior != null){
            manosPorPartida.remove(codigo);
        }
        manosPorPartida.put(codigo, mano);
    }


    public Mano getMano(String codigo) throws IllegalArgumentException{
        Mano mano=  manosPorPartida.get(codigo);
        if(mano == null){
            crearMano(partidaService.findPartidaByCodigo(codigo));
        } else if(mano.getTerminada()) mano = terminarMano(mano, codigo);
        return mano;
    }

    public List<List<Carta>> repartirCartas(Partida partida){
		Integer numJugadores = partida.getNumJugadores();
		List<List<Carta>> res = new ArrayList<>();
		Integer cartasEnLaBaraja = 40;
		Integer cartasPorJugador = 3;
		List<Integer> listaCartasId = IntStream.rangeClosed(1, cartasEnLaBaraja).boxed().collect(Collectors.toList());
		if (numJugadores * 3 > listaCartasId.size()) {
			throw new IllegalArgumentException("No hay suficientes cartas para todos los jugadores.");
		}
		Collections.shuffle(listaCartasId);
		int indiceCarta = 0;
    	for (int i = 0; i < numJugadores; i++) {
        	List<Carta> cartasJugador = new ArrayList<>();
        	for (int j = 0; j < cartasPorJugador; j++) {
            	Carta carta = findCarta(listaCartasId.get(indiceCarta++));
            	if (carta != null) {
            	    cartasJugador.add(carta);
            	}
        	}
        	res.add(cartasJugador);
    	}
		return res;
	}


	public Carta findCarta(Integer cartaId){
		Carta res = cartaRepository.findById(cartaId).orElse(null);
		return res;
	}

	
	public Mano crearMano(Partida partida){
		Mano nuevaMano = new Mano();
		
		nuevaMano.setPartida(partida);
		nuevaMano.setJugadorTurno(partida.getJugadorMano());
		nuevaMano.setCartasDisp(repartirCartas(partida)); 

        Integer tiposDeEnvido = 3;
        Integer envidosIniciales=0;
        Integer numCantosFlorIniciales=0;
        List<Integer> envidos = new ArrayList<>();
        for(int i = 0; i<tiposDeEnvido;i++){
            envidos.add(envidosIniciales);
        }
        nuevaMano.setEnvidosCantados(envidos);
        nuevaMano.crearListaTantosCadaJugador();
        nuevaMano.crearListaTantosCadaJugadorFlor();
        nuevaMano.setFloresCantadas(numCantosFlorIniciales);
        
		Integer ganadasIniciales = 0;
		List<Integer> ganadoresRonda = new ArrayList<>();
		ganadoresRonda.add(ganadasIniciales);
		ganadoresRonda.add(ganadasIniciales);
		nuevaMano.setGanadoresRondas(ganadoresRonda);

        
        Integer numJugadores = partida.getNumJugadores();
        List<Carta> listaCartasLanzadas = new ArrayList<>();
        for (int i = 0; i <numJugadores; i++){
            listaCartasLanzadas.add(null);      
        }
        nuevaMano.setCartasLanzadasTotales(inicializarCartasLanzadasTotales(numJugadores));
        nuevaMano.setCartasLanzadasRonda(listaCartasLanzadas);
        nuevaMano.comprobarSiPuedeCantarEnvido(true);
        nuevaMano.comprobarSiPuedeCantarFlor();

        actualizarMano(nuevaMano, partida.getCodigo());
		return nuevaMano;
	}
	

    /*
     * Fundamental que sea así ya que si creas una lista vacia y la añadis varias veces para cada uno de los casos poniendo los fors separados
     * considera que es la misma y si haces cambios en una se hacen en la otra, cosa que no queremos.
     */
    public List<List<Carta>> inicializarCartasLanzadasTotales(Integer numJugadores){
        List<List<Carta>> res = new ArrayList<>();
        Integer rondas = 3;    
        for (int j = 0; j < numJugadores; j++) {
            List<Carta> listaVacia = new ArrayList<>();
            for (int i = 0; i < rondas; i++) {
                listaVacia.add(null);
            }
            res.add(listaVacia);  
        }
        return res;
    }

	public Carta tirarCarta(String codigo, Integer cartaId){
		Mano manoActual = getMano(codigo);
        if(!manoActual.getEsperandoRespuesta()){
            Integer jugadorActual = manoActual.getJugadorTurno();
            
            List<Carta> cartasDisponibles = manoActual.getCartasDisp().get(jugadorActual);
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
            
            manoActual.getCartasLanzadasTotales().get(jugadorActual).set(manoActual.getRondaActual()-1, cartaALanzar);
            
            manoActual.getCartasDisp().get(jugadorActual).set(indice,null);
            manoActual.getCartasLanzadasRonda().set(jugadorActual, cartaALanzar);

            List<Carta> listaCartasLanzadas = manoActual.getCartasLanzadasRonda();
            if(listaCartasLanzadas.stream().allMatch(c -> c!=null)){
                manoActual.compararCartas();
            } else{
                manoActual.siguienteTurno();
            }
            manoActual.comprobarSiPuedeCantarTruco();
            manoActual.comprobarSiPuedeCantarEnvido(true);
            manoActual.comprobarSiPuedeCantarFlor();
            actualizarMano(manoActual, codigo);
            return cartaALanzar;
        } else{
            throw new CartaTiradaException("Tenés que responder antes de poder tirar una carta");
        }
        
    }

    public Boolean envidoBajoMenorAlResto(Mano mano, Integer jugadorDelTurno){ //FALTA TEST TODO
        Boolean res = false;
        Integer envidoBajo =24; //segun los ojos del que juege puede pensarse que no es tan bajo, por eso una variable
        if(mano.getEquipoGanadorEnvido()%2 != jugadorDelTurno%2 && mano.getTantoDe1Jugador(jugadorDelTurno)<=envidoBajo){
            res = true;

        }
        return res;
    }

    public Mano comprobarEngaño(Mano mano, Integer jugadorDelTurno, Boolean dijoNoQuiero){ //TODO hacer tests

        if(!mano.getEstaMintiendo()){
            if(envidoBajoMenorAlResto(mano, jugadorDelTurno)){
                mano.setEstaMintiendo(true);
            }
        }else{ //Ya se sabe que el otro jugador estaba mintiendo  
            if(dijoNoQuiero!=null){
                if(dijoNoQuiero){ //Es decir, el otro, aunque ganaba, tuvo miedo y dijo no quiero
                    partidaJugadorService.sumar1Estadistica(mano.getPartida().getCodigo(),jugadorDelTurno, Metrica.NUMERO_ENGANOS); //le sumamos el engaño
                }else{ //no tuvo miedo :(
                    partidaJugadorService.sumar1Estadistica(mano.getPartida().getCodigo(),jugadorDelTurno, Metrica.ATRAPADO); //Fue atrapado
                }
            }
            
        }
        return mano;
    }

    public void sumarQuieroNoQuiero(Cantos canto, Integer jugador, String codigo){
        switch (canto) {
            case QUIERO:
                partidaJugadorService.sumar1Estadistica(codigo, jugador, Metrica.QUIEROS);
                break;
            case NO_QUIERO:
                partidaJugadorService.sumar1Estadistica(codigo, jugador, Metrica.NO_QUIEROS);
                break;
            default:
                //nada
                break;
        }
    }


    public Mano cantosEnvido(String codigo, Cantos canto){
        Mano manoActual = getMano(codigo);
        Integer jugadorTurno = manoActual.getJugadorTurno();
        List<Integer> envidosCantados = manoActual.getEnvidosCantados();
        
        if(manoActual.getEsperandoRespuesta()==false){
            manoActual.setJugadorIniciadorDelCanto(jugadorTurno);
        }
        manoActual.setEsperandoRespuesta(true);
        Integer quienResponde = manoActual.quienResponde();
        try {
        manoActual.comprobarSiPuedeCantarEnvido(false);
        Integer queEnvidoPuedoCantar = manoActual.getQueEnvidoPuedeCantar();
        manoActual.setEsTrucoEnvidoFlor(1);
        manoActual = comprobarEngaño(manoActual, jugadorTurno, null);
            switch (canto) {
                case ENVIDO:
                    if (queEnvidoPuedoCantar<3) {
                        throw new EnvidoException("No podés cantar más veces envido");
                    }
                    Integer envidos = envidosCantados.get(0);
                    envidosCantados.set(0, envidos+1);
                    manoActual.setJugadorTurno(quienResponde);
                    manoActual.comprobarSiPuedeCantarFlor();

                    break;
                case REAL_ENVIDO:
                    if(queEnvidoPuedoCantar<2){
                        throw new EnvidoException("No podés cantar más veces real envido");
                    }
                    Integer realEnvidos = envidosCantados.get(1);
                    envidosCantados.set(1, realEnvidos+1);
                    manoActual.setJugadorTurno(quienResponde);
                    manoActual.comprobarSiPuedeCantarFlor();

                    break;
                case FALTA_ENVIDO:
                    if(queEnvidoPuedoCantar<1){
                        throw new EnvidoException("No podés cantar más veces falta envido");
                    }
                    Integer faltaEnvidos = envidosCantados.get(2);
                    envidosCantados.set(2, faltaEnvidos+1);
                    manoActual.setJugadorTurno(quienResponde);
                    manoActual.comprobarSiPuedeCantarFlor();

                    break;
                default:
                    throw new EnvidoException("Canto no valido");
            }
        } catch (Exception e) {
            manoActual.setEsperandoRespuesta(false);
            throw e;
        }
        if(canto == Cantos.ENVIDO && envidosCantados.get(0) > 1){
            canto = Cantos.ENVIDO2;
        }
        manoActual.setUltimoMensaje(canto);
        manoActual.setPuedeCantarTruco(false);
        manoActual.comprobarSiPuedeCantarEnvido(false);
        actualizarMano(manoActual, codigo);
        return manoActual;
    }

    


    public Mano cantosFlor(String codigo, Cantos canto){
        Mano manoActual = getMano(codigo);
        Integer jugadorTurno = manoActual.getJugadorTurno();
        Integer numCantosFlores = manoActual.getFloresCantadas();
        
        if(manoActual.getEsperandoRespuesta()==false){
            manoActual.setJugadorIniciadorDelCanto(jugadorTurno);
        }
        manoActual.setEsperandoRespuesta(true);
        try {
        manoActual.comprobarSiPuedeCantarFlor();
        Integer queFlorPuedeCantar = manoActual.getQueFlorPuedeCantar();
        manoActual.setEsTrucoEnvidoFlor(2);

            if(canto.equals(Cantos.FLOR)){ 
                    if (queFlorPuedeCantar!=1) {
                        throw new FlorException("No podés cantar más veces flor/No tenés flor");
                    }
                    partidaJugadorService.sumar1Estadistica(codigo, jugadorTurno,  Metrica.NUMERO_FLORES); // ESTADISTICA (IMPORTANTE QUE ESTE ANTES DE QUE SE CAMBIE DE TURNO)
                    numCantosFlores=numCantosFlores+1;
                    manoActual.setEquipoCantor(null); //PARA RESETEAR EL TRUCO
                    Integer envidosIniciales=0;
                    Integer tiposDeEnvido=3;
                    List<Integer> envidos = new ArrayList<>();
                    for(int i = 0; i<tiposDeEnvido;i++){
                        envidos.add(envidosIniciales);
                    }
                    manoActual.setEnvidosCantados(envidos); //PARA RESETEAR EL ENVIDO
                    sumar3PuntosSiSoloUnJugadorTieneFlor(manoActual);
                    manoActual=getMano(codigo);
                    manoActual.setFloresCantadas(numCantosFlores);
            }

            else throw new FlorException("Canto no valido");
        } catch (Exception e) {
            manoActual.setEsperandoRespuesta(false);
            throw e;
        }
        manoActual.setUltimoMensaje(canto);
        manoActual.comprobarSiPuedeCantarFlor();
        manoActual.setPuedeCantarEnvido(false);
        actualizarMano(manoActual, codigo);
        return manoActual;
    }

    public void responderEnvido(String codigo, Cantos respuesta){
        Mano manoActual = getMano(codigo);
        Integer jugadorIniciador = manoActual.getJugadorIniciadorDelCanto();
        manoActual.setEquipoCantor(null);
        Integer jugadorTurno = manoActual.getJugadorTurno();
        
        sumarQuieroNoQuiero(respuesta, jugadorTurno, codigo); // ESTADISTICA
        switch (respuesta) {
            case QUIERO:
                manoActual = comprobarEngaño(manoActual, jugadorIniciador, false); //ESTADISTICA
                gestionarPuntosEnvido(false, codigo);
                manoActual = getMano(codigo);
                manoActual.setUltimoMensaje(Cantos.LISTA_ENVIDOS);
                manoActual.setJugadorTurno(jugadorIniciador);
                manoActual.setJugadorIniciadorDelCanto(null);
                manoActual.comprobarSiPuedeCantarTruco();
                
                break;
            case NO_QUIERO:
                
                manoActual = comprobarEngaño(manoActual, jugadorIniciador, true); //ESTADISTICA
                gestionarPuntosEnvido(true, codigo);
                
                manoActual = getMano(codigo);
                manoActual.setUltimoMensaje(Cantos.NO_QUIERO);
                manoActual.setJugadorTurno(jugadorIniciador);
                manoActual.setJugadorIniciadorDelCanto(null);
                manoActual.comprobarSiPuedeCantarTruco();
                
                break;

            default:
                manoActual =cantosEnvido(codigo, respuesta);
                break;
        }
    
        actualizarMano(manoActual, codigo);
    }

    public void responderFlor(String codigo, Cantos respuesta){
        Mano manoActual = getMano(codigo);
        Integer jugadorIniciador = manoActual.getJugadorIniciadorDelCanto();
        manoActual.setEquipoCantor(null);
        Integer puntosRechazoNoQuiero=5;
        Integer puntosRechazoAchicarse=4;
        Integer numCantosFlores = manoActual.getFloresCantadas();
        Integer queFlorPuedeCantar = manoActual.getQueFlorPuedeCantar();
        Integer quienRespondeFlor = manoActual.quienRespondeFlor();
        Integer jugadorTurno = manoActual.getJugadorTurno();

        sumarQuieroNoQuiero(respuesta, jugadorTurno, codigo);
        
        switch (respuesta) {
            case QUIERO:
                gestionarPuntosFlor(false, codigo,null);
                manoActual.setUltimoMensaje(Cantos.LISTA_ENVIDOS_FLOR);
                manoActual.setJugadorTurno(jugadorIniciador);
                manoActual.setJugadorIniciadorDelCanto(null);
                manoActual.comprobarSiPuedeCantarTruco();
                manoActual.setQueFlorPuedeCantar(0);
                break;

            case CON_FLOR_ME_ACHICO:
                partidaJugadorService.sumar1Estadistica(codigo, jugadorTurno,  Metrica.NUMERO_FLORES); // ESTADISTICA (IMPORTANTE QUE ESTE ANTES DE QUE SE CAMBIE DE TURNO)
                gestionarPuntosFlor(true, codigo,puntosRechazoAchicarse);
                manoActual = getMano(codigo);
                manoActual.setUltimoMensaje(Cantos.CON_FLOR_ME_ACHICO);
                manoActual.setJugadorTurno(jugadorIniciador);
                manoActual.setJugadorIniciadorDelCanto(null);
                manoActual.comprobarSiPuedeCantarTruco();
                manoActual.setQueFlorPuedeCantar(0);


                break;

            case NO_QUIERO:

                gestionarPuntosFlor(true, codigo,puntosRechazoNoQuiero);
                manoActual = getMano(codigo);
                manoActual.setUltimoMensaje(Cantos.NO_QUIERO);
                manoActual.setJugadorTurno(jugadorIniciador);
                manoActual.setJugadorIniciadorDelCanto(null);
                manoActual.comprobarSiPuedeCantarTruco();
                manoActual.setQueFlorPuedeCantar(0);
                
                break;

             case CONTRAFLOR:
                    if(queFlorPuedeCantar!=2){
                        throw new EnvidoException("No podés cantar contraflor capo");
                    }
                    partidaJugadorService.sumar1Estadistica(codigo, jugadorTurno,  Metrica.NUMERO_FLORES); // ESTADISTICA (IMPORTANTE QUE ESTE ANTES DE QUE SE CAMBIE DE TURNO)
                    numCantosFlores=numCantosFlores+1;
                    manoActual.setFloresCantadas(numCantosFlores);
                    manoActual.setJugadorTurno(quienRespondeFlor);
                    manoActual.setUltimoMensaje(Cantos.CONTRAFLOR);
                    manoActual.comprobarSiPuedeCantarFlor();
                    break;
          
            default:
                 throw new FlorException("Canto no valido");
        }
        manoActual.comprobarSiPuedeCantarTruco();
        actualizarMano(manoActual, codigo);
    }

   
    public Integer gestionarPuntosEnvido(Boolean noQuiero, String codigo){
        Mano manoActual = getMano(codigo);

        
        Integer res = manoActual.getPuntosEnvido(); //Siempre sera cero en un principio
        Partida partida = partidaService.findPartidaByCodigo(codigo);

        Integer equipoRespondedor = manoActual.getJugadorTurno() %2;

        Integer puntosEquipo1 = partida.getPuntosEquipo1();
        Integer puntosEquipo2 = partida.getPuntosEquipo2();
        Integer puntosMaximos = partida.getPuntosMaximos();
        final Integer maximosEnvido = 2;
        final Integer maximosRealEnvido =1;
        final Integer maximosFaltaEnvido = 1; 

        Integer multiplicadorEnvido = 2;
        Integer multiplicadorRealEnvido = 3;

        List<Integer> envidoCantados = manoActual.getEnvidosCantados();
        Integer equipoGanadorEnvido = manoActual.getEquipoGanadorEnvido();

        Integer cantidadFaltaEnvidos = envidoCantados.get(2);
        Integer cantidadEnvidos = envidoCantados.get(0);
        Integer cantidadRealEnvidos = envidoCantados.get(1);

        Integer maximoPuntaje = cantidadEnvidos*multiplicadorEnvido + cantidadRealEnvidos*multiplicadorRealEnvido;
        
        if(noQuiero){
            if(cantidadFaltaEnvidos==maximosFaltaEnvido){
                res =maximoPuntaje;
            } else if(cantidadRealEnvidos==maximosRealEnvido){
                res = cantidadEnvidos*multiplicadorEnvido;
            }else if(cantidadEnvidos == maximosEnvido) {
                res = (cantidadEnvidos -1) *multiplicadorEnvido;
            } else{
                res = 1;
            }
            if(res ==0){
                res = 1;
            }

            if(equipoRespondedor==0){
                partida.setPuntosEquipo2(puntosEquipo2 + res);
            }else{
                partida.setPuntosEquipo1(puntosEquipo1 + res);
            }
            
        }else{
            if(cantidadFaltaEnvidos == maximosFaltaEnvido){
                res = equipoGanadorEnvido ==0 ? puntosMaximos-puntosEquipo1 : puntosMaximos-puntosEquipo2;
            }
            else{
                res = maximoPuntaje;
            }

            if(equipoGanadorEnvido==0){
                partida.setPuntosEquipo1(puntosEquipo1 + res);
            }else{
                partida.setPuntosEquipo2(puntosEquipo2 + res);
            }
            
        }
        manoActual.setPuntosEnvido(res);
        manoActual.setPuedeCantarEnvido(false);
        manoActual.setQueEnvidoPuedeCantar(-1);
        manoActual.setEsperandoRespuesta(false);
        partidaService.updatePartida(partida, partida.getId());
        actualizarMano(manoActual, codigo);
        return res;
        
    }
    public Integer gestionarPuntosFlor(Boolean rechazoFlor, String codigo, Integer puntosRechazo){
        Mano manoActual = getMano(codigo);
        Integer res = manoActual.getPuntosFlor(); //Siempre sera cero en un principio
        Partida partida = partidaService.findPartidaByCodigo(codigo);
        Integer equipoRespondedor = manoActual.getJugadorTurno()%2;
        Integer puntosEquipo1 = partida.getPuntosEquipo1();
        Integer puntosEquipo2 = partida.getPuntosEquipo2();
        Integer equipoGanadorContraflor = manoActual.getEquipoGanadorFlor();
       
        Integer puntosContraflorQuiero = 6;
        
        if(rechazoFlor){
            res=puntosRechazo;

            if(equipoRespondedor==0){
                partida.setPuntosEquipo2(puntosEquipo2 + res);
            }else{
                partida.setPuntosEquipo1(puntosEquipo1 + res);
            }
            
        }else{
            res=puntosContraflorQuiero;
            if(equipoGanadorContraflor==0){
                partida.setPuntosEquipo1(puntosEquipo1 + res);
            }else{
                partida.setPuntosEquipo2(puntosEquipo2 + res);
            }
            
        }
        manoActual.setPuntosFlor(res);
        manoActual.setPuedeCantarFlor(false);
        manoActual.setFloresCantadas(9);
        manoActual.setEsperandoRespuesta(false);
        partidaService.updatePartida(partida, partida.getId());
        actualizarMano(manoActual, codigo);
        return res;
        
    }


	
    public Mano cantosTruco(String codigo, Cantos canto){
		Mano manoActual = getMano(codigo);
        Integer jugadorTurno = manoActual.getJugadorTurno();
        Integer equipoCantor = manoActual.getEquipoCantor();
        
        if(manoActual.getEsperandoRespuesta()==false){
            manoActual.setJugadorIniciadorDelCanto(jugadorTurno);
        }
        manoActual.setEsperandoRespuesta(true); // PARA PODER CONFIRMAR QUE EL QUE DICE QUIERO NO TIRA CARTA
        
        
        manoActual.setEsTrucoEnvidoFlor(0);
        Mano mano = new Mano();
        try {
            if (!manoActual.comprobarSiPuedeCantarTruco()) {
                throw new TrucoException(); 
            }
            Truco estadoTruco =  converterTruco.convertToEntityAttribute(canto);

           
            mano = estadoTruco.accionAlTipoTruco(manoActual, jugadorTurno, equipoCantor);
            manoActual.copiaParcialTruco(mano);
        } catch (Exception e) {
            manoActual.setEsperandoRespuesta(false);
            throw e;
        }
        manoActual.comprobarSiPuedeCantarTruco();
        manoActual.comprobarSiPuedeCantarEnvido(false);
        manoActual.setUltimoMensaje(canto);
		actualizarMano(manoActual, codigo);
        return manoActual;
    }

	public void responderTruco(String codigo, Cantos respuesta) {
		Mano manoActual = getMano(codigo);
 
        Integer puntosTruco = manoActual.getPuntosTruco();
        
        

        Mano mano = new Mano();
        sumarQuieroNoQuiero(respuesta, manoActual.getJugadorTurno(), codigo);

        RespuestaTruco respuestaTruco =   converterRespuestaTruco.convertToEntityAttribute(respuesta);
        
        mano = respuestaTruco.accionRespuestaTruco(manoActual,puntosTruco);
        manoActual.copiaParcialTruco(mano);
        
        manoActual.comprobarSiPuedeCantarTruco();
		actualizarMano(manoActual, codigo);
    }

	public Mano terminarMano(Mano manoActual, String codigo){
		Partida partida = partidaService.findPartidaByCodigo(codigo);
		List<Integer> ganadoresRondaActual = manoActual.getGanadoresRondas();
		

		Integer equipoMano =partida.getJugadorMano() % 2; // equipo 1 = 0, equipo 2 = 1 

        Integer puntosEquipo1 = partida.getPuntosEquipo1();
        Integer puntosEquipo2 = partida.getPuntosEquipo2();

		if(ganadoresRondaActual.get(0) == ganadoresRondaActual.get(1)){ // si hay empate, gana el mano

			if (equipoMano ==0)  partida.setPuntosEquipo1(puntosEquipo1+manoActual.getPuntosTruco());

			else partida.setPuntosEquipo2(puntosEquipo2+manoActual.getPuntosTruco());
			
		} else if (ganadoresRondaActual.get(0) > ganadoresRondaActual.get(1)){
			partida.setPuntosEquipo1(puntosEquipo1+manoActual.getPuntosTruco());

		} else {
			partida.setPuntosEquipo2(puntosEquipo2+manoActual.getPuntosTruco());
		}
       
		if (partida.haTerminadoLaPartida()) {
            manosPorPartida.remove(partida.getCodigo());
            partidaService.updatePartida(partida, partida.getId());
            return null;
			
		} else {
            manosPorPartida.remove(partida.getCodigo());
			partida.setJugadorMano((partida.getJugadorMano() + 1) % partida.getNumJugadores());
            partidaService.updatePartida(partida, partida.getId());
			return crearMano(partida);
		}
		
		
	}
    private void sumar3PuntosSiSoloUnJugadorTieneFlor(Mano manoActual) {
        // 1) Obtenemos la lista de tantos de Flor de cada jugador
        Integer jugadorIniciador=manoActual.getJugadorIniciadorDelCanto();
        Integer jugadorActual=manoActual.getJugadorTurno();
        String codigo= manoActual.getPartida().getCodigo();
        Boolean jugadorDeOtroEquipoConFlor=false;
        List<Boolean> listaFloreh=manoActual.getListaTienenFlores();
   
        for(int i = manoActual.siguienteJugador(jugadorActual); i!=jugadorActual;i= manoActual.siguienteJugador(i)){
            Boolean tieneFlor=listaFloreh.get(i);
            if(tieneFlor && jugadorActual%2!=i%2){
                jugadorDeOtroEquipoConFlor=true;
            }
        }
        // 2) Si sólo 1 jugador tiene Flor, sumamos 3 puntos a quien la cantó
        if (!jugadorDeOtroEquipoConFlor) {
            // El equipo del que cantó la Flor
            Integer equipoCantorFlor = manoActual.getJugadorIniciadorDelCanto() % 2;
            Partida partida = manoActual.getPartida();
            Integer puntosEquipo1 = partida.getPuntosEquipo1();
            Integer puntosEquipo2 = partida.getPuntosEquipo2();
            manoActual.setEsperandoRespuesta(false);
            manoActual.setJugadorTurno(jugadorIniciador);
            manoActual.comprobarSiPuedeCantarTruco();

            if (equipoCantorFlor == 0) {
                partida.setPuntosEquipo1(puntosEquipo1 + 3);
            } else {
                partida.setPuntosEquipo2(puntosEquipo2 + 3);
            }
    
            // 4) Actualizamos la partida
            partidaService.updatePartida(partida, partida.getId());
            
        }
        else{ manoActual.setJugadorTurno(manoActual.quienRespondeFlor());
            manoActual.setPuedeCantarTruco(false);
            manoActual.comprobarSiPuedeCantarFlor();

    }
    
    actualizarMano(manoActual, codigo);}





// TODA LA FUNCIONALIDAD DEL MANO SERVICE TERMINA ACÁ -----------------------------------------------------------
// FUNCIONES AUXILIARES PARA SOLAMENTE REPARTIR FLORES Y PODER TESTEARLA SIN DEPENDER DE LA SUERTE: 
public List<List<Carta>> repartirCartasSoloFlor(Partida partida) {
    Integer numJugadores = partida.getNumJugadores();
    Integer cartasEnLaBaraja = 40;
    Integer cartasPorJugador = 3;

    if (numJugadores * cartasPorJugador > cartasEnLaBaraja) {
        throw new IllegalArgumentException("No hay suficientes cartas para todos los jugadores.");
    }

    List<Integer> listaCartasId = IntStream.rangeClosed(1, cartasEnLaBaraja)
                                          .boxed()
                                          .collect(Collectors.toList());

    Collections.shuffle(listaCartasId);

    Map<Palo, List<Carta>> cartasPorPalo = new HashMap<>();
    for (Palo p : Palo.values()) {
        cartasPorPalo.put(p, new ArrayList<>()); 
    }

    for (Integer id : listaCartasId) {
        Carta carta = findCarta(id);
        if (carta != null) {
            Palo palo = carta.getPalo();
            cartasPorPalo.get(palo).add(carta);
        }
    }

    for (Palo palo : cartasPorPalo.keySet()) {
        Collections.shuffle(cartasPorPalo.get(palo));
    }

    List<List<Carta>> res = new ArrayList<>();
    for (int i = 0; i < numJugadores; i++) {
        List<Carta> cartasJugador = new ArrayList<>();


        Palo paloSeleccionado = null;
        for (Palo p : Palo.values()) {
            if (cartasPorPalo.get(p).size() >= cartasPorJugador) {
                paloSeleccionado = p;
                break;
            }
        }

        if (paloSeleccionado == null) {
            throw new IllegalArgumentException("No hay palos suficientes para repartir flor a todos los jugadores");
        }

        List<Carta> listaDelPalo = cartasPorPalo.get(paloSeleccionado);
        for (int j = 0; j < cartasPorJugador; j++) {
            cartasJugador.add(listaDelPalo.remove(0)); 
        }

        res.add(cartasJugador);
    }

    return res;
}

public List<List<Carta>> repartirCartasSoloUnaFlor(Partida partida) {
    Integer numJugadores = partida.getNumJugadores();
    Integer cartasEnLaBaraja = 40;       
    Integer cartasPorJugador = 3;

    if (numJugadores * cartasPorJugador > cartasEnLaBaraja) {
        throw new IllegalArgumentException("No hay suficientes cartas para todos los jugadores.");
    }

    List<Integer> listaCartasId = IntStream.rangeClosed(1, cartasEnLaBaraja)
                                          .boxed()
                                          .collect(Collectors.toList());
    Collections.shuffle(listaCartasId);

    Map<Palo, List<Carta>> cartasPorPalo = new HashMap<>();
    for (Palo p : Palo.values()) {
        cartasPorPalo.put(p, new ArrayList<>());
    }

    for (Integer id : listaCartasId) {
        Carta c = findCarta(id);
        if (c != null) {
            cartasPorPalo.get(c.getPalo()).add(c);
        }
    }


    int jugadorFlor = 0;


    Palo paloFlor = null;
    for (Palo p : Palo.values()) {
        if (cartasPorPalo.get(p).size() >= 3) {
            paloFlor = p;
            break;
        }
    }
    if (paloFlor == null) {
        throw new IllegalArgumentException("No hay ningún palo con >=3 cartas disponibles.");
    }

    List<Carta> listaFlor = cartasPorPalo.get(paloFlor);

    Collections.shuffle(listaFlor);
    List<Carta> manoFlor = new ArrayList<>();
    for (int i = 0; i < cartasPorJugador; i++) {
        manoFlor.add(listaFlor.remove(0));
    }


    List<Carta> barajaRestante = new ArrayList<>();
    for (Palo p : Palo.values()) {
        barajaRestante.addAll(cartasPorPalo.get(p));
    }
    Collections.shuffle(barajaRestante);

    List<List<Carta>> resultado = new ArrayList<>();
    resultado.add(jugadorFlor, manoFlor);



    for (int i = 0; i < numJugadores; i++) {
        if (i == jugadorFlor) {
            continue; // ya repartimos las 3 cartas del palo
        }
        List<Carta> manoJugador = new ArrayList<>();

        // Sacamos cartas una a una, garantizando que no salgan 3 de mismo palo
        while (manoJugador.size() < 3) {
            Carta siguienteCarta = barajaRestante.remove(0);
            manoJugador.add(siguienteCarta);

            // Si ya tiene 3, chequeamos si es flor (mismo palo)
            if (manoJugador.size() == 3) {
                Palo p1 = manoJugador.get(0).getPalo();
                Palo p2 = manoJugador.get(1).getPalo();
                Palo p3 = manoJugador.get(2).getPalo();
                if (p1.equals(p2) && p2.equals(p3)) {
                    // => Se formó flor "por accidente". 
                    //    REEMPLAZAMOS la 3ra carta por otra para forzar que no sea flor
                    //    (o repetimos la selección).
                    boolean encontreDistinta = false;
                    for (int idx = 0; idx < barajaRestante.size(); idx++) {
                        Carta cartaAlternativa = barajaRestante.get(idx);
                        if (!cartaAlternativa.getPalo().equals(p1)) {
                            manoJugador.set(2, cartaAlternativa);
                            barajaRestante.remove(idx);
                            barajaRestante.add(siguienteCarta);
                            encontreDistinta = true;
                            break;
                        }
                    }
                    if (!encontreDistinta) {
                        
                        throw new IllegalArgumentException(
                            "No hay suficientes cartas para evitar flor al resto de jugadores."
                        );
                    }
                }
            }
        }
        if (resultado.size() <= i) {
            while (resultado.size() <= i) {
                resultado.add(null);
            }
        }
        resultado.set(i, manoJugador);
    }

    return resultado;
}

}