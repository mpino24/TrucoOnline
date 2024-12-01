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
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.CartaTiradaException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.EnvidoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Estado;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.CantosTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.ConverterRespuestaTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.ConverterTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestaTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestasTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.Truco;


@Service
public class ManoService {

    private final Map<String, Mano> manosPorPartida = new HashMap<>();
    
    private final CartaRepository cartaRepository;

    private final PartidaService partidaService;

	private final ConverterTruco converterTruco = new ConverterTruco();
    private final ConverterRespuestaTruco converterRespuestaTruco = new ConverterRespuestaTruco(this);

	private final Integer maximoPuntajeTruco=4;
    

    @Autowired
    public ManoService(CartaRepository cartaRepository, PartidaService partidaService) {
        this.cartaRepository = cartaRepository;
        this.partidaService = partidaService;
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
        } else if(mano.getTerminada()) mano = terminarMano(mano);
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
        List<Integer> envidos = new ArrayList<>();
        for(int i = 0; i<tiposDeEnvido;i++){
            envidos.add(envidosIniciales);
        }
        nuevaMano.setEnvidosCantados(envidos);
		Integer ganadasIniciales = 0;
		List<Integer> ganadoresRonda = new ArrayList<>();
		ganadoresRonda.add(ganadasIniciales);
		ganadoresRonda.add(ganadasIniciales);
		nuevaMano.setGanadoresRondas(ganadoresRonda);

        List<Carta> listaCartasLanzadas = new ArrayList<>();
        for (int i = 0; i <partida.getNumJugadores(); i++){
            listaCartasLanzadas.add(null);
        }
        nuevaMano.setCartasLanzadasRonda(listaCartasLanzadas);
        nuevaMano.comprobarSiPuedeCantarEnvido(true);
        manosPorPartida.put(partida.getCodigo(), nuevaMano);
		return nuevaMano;
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
            actualizarMano(manoActual, codigo);
            return cartaALanzar;
        } else{
            throw new CartaTiradaException("Tenés que responder antes de poder tirar una carta");
        }
        
    }

    public Mano cantosEnvido(String codigo, CantosEnvido canto){
        Mano manoActual = getMano(codigo);
        Integer jugadorTurno = manoActual.getJugadorTurno();
        List<Integer> envidosCantados = manoActual.getEnvidosCantados();
        
        if(manoActual.getEsperandoRespuesta()==false){
            manoActual.setJugadorIniciadorDelCanto(jugadorTurno);
        }
        manoActual.setEsperandoRespuesta(true);
        Integer quienResponde = manoActual.quienRespondeEnvido();
        try {
        Integer queEnvidoPuedoCantar = manoActual.getQueEnvidoPuedeCantar();
        
            switch (canto) {
                case ENVIDO:
                    if (queEnvidoPuedoCantar<3) {
                        throw new EnvidoException("No podés cantar más veces envido");
                    }
                    Integer envidos = envidosCantados.get(0);
                    envidosCantados.set(0, envidos+1);
                    manoActual.setJugadorTurno(quienResponde);
                    break;
                case REAL_ENVIDO:
                    if(queEnvidoPuedoCantar<2){
                        throw new EnvidoException("No podés cantar más veces real envido");
                    }
                    Integer realEnvidos = envidosCantados.get(1);
                    envidosCantados.set(1, realEnvidos+1);
                    manoActual.setJugadorTurno(quienResponde);
                    break;
                case FALTA_ENVIDO:
                    if(queEnvidoPuedoCantar<1){
                        throw new EnvidoException("No podés cantar más veces falta envido");
                    }
                    Integer faltaEnvidos = envidosCantados.get(2);
                    envidosCantados.set(2, faltaEnvidos+1);
                    manoActual.setJugadorTurno(quienResponde);
                    break;
                default:
                    throw new EnvidoException("Canto no valido");
            }
        } catch (Exception e) {
            manoActual.setEsperandoRespuesta(false);
            throw e;
        }
        manoActual.setPuedeCantarTruco(false);
        manoActual.comprobarSiPuedeCantarEnvido(false);
        actualizarMano(manoActual, codigo);
        return manoActual;
    }

    public void responderEnvido(String codigo, CantosEnvido respuesta){
        Mano manoActual = getMano(codigo);
        Integer jugadorIniciador = manoActual.getJugadorIniciadorDelCanto();
        
        switch (respuesta) {
            case QUIERO:
                
                manoActual.setPuedeCantarEnvido(false);
                manoActual.setQueEnvidoPuedeCantar(0);

                manoActual.gestionarPuntosEnvido(false);
                
                manoActual.setJugadorTurno(jugadorIniciador);
                manoActual.comprobarSiPuedeCantarTruco();
                break;
            case NO_QUIERO:
                manoActual.setPuedeCantarEnvido(false);
                manoActual.setQueEnvidoPuedeCantar(0);
                manoActual.gestionarPuntosEnvido(true);
                manoActual.setJugadorTurno(jugadorIniciador);
                manoActual.comprobarSiPuedeCantarTruco();
                break;

            default:
                cantosEnvido(codigo, respuesta);
                break;
        }
    
        actualizarMano(manoActual, codigo);
    }

	//TODO: FALTAN TEST NEGATIVOS
    public Mano cantosTruco(String codigo, CantosTruco canto){
		Mano manoActual = getMano(codigo);
        Integer jugadorTurno = manoActual.getJugadorTurno();
        Integer equipoCantor = manoActual.getEquipoCantor();

        Integer rondaActual = manoActual.getRondaActual();
        List<List<Integer>> secuenciaCantos = manoActual.getSecuenciaCantoLista();
        List<Integer> listaRondaJugador = new ArrayList<>(); //Valores en el orden del nombre
        if(manoActual.getEsperandoRespuesta()==false){
            manoActual.setJugadorIniciadorDelCanto(jugadorTurno);
        }
        manoActual.setEsperandoRespuesta(true); // PARA PODER CONFIRMAR QUE EL QUE DICE QUIERO NO TIRA CARTA
        
        listaRondaJugador.add(rondaActual);
        listaRondaJugador.add(jugadorTurno);

        Mano mano = new Mano();
        try {
            if (!manoActual.comprobarSiPuedeCantarTruco()) {
                throw new TrucoException(); 
            }
            Truco estadoTruco =  converterTruco.convertToEntityAttribute(canto);

            Integer puntosTruco = manoActual.getPuntosTruco();
            Integer puntosNoHayTruco = 1; Integer puntosHayTruco = 2; Integer puntosHayRetruco = 3;
            switch (canto) {
                case TRUCO: 
                    if(puntosTruco > puntosNoHayTruco){
                        throw new TrucoException("Ya se canto el truco");
                    }
                    mano = estadoTruco.accionAlTipoTruco(manoActual, jugadorTurno, equipoCantor, secuenciaCantos, listaRondaJugador, rondaActual);
                    manoActual.copiaParcialTruco(mano);
                    break;
                case RETRUCO:
                    if (puntosTruco < puntosHayTruco) {
                        throw new TrucoException( "No se cantó el truco");
                    } else if(puntosTruco>puntosHayTruco){
                        throw new TrucoException("Ya se canto el retruco");
                    }
                    mano = estadoTruco.accionAlTipoTruco(manoActual,jugadorTurno, equipoCantor, secuenciaCantos, listaRondaJugador, rondaActual);
                    manoActual.copiaParcialTruco(mano);
                    break;
                case VALECUATRO:
                    if (puntosTruco < puntosHayRetruco) {
                        throw new TrucoException( "No se cantó el retruco"); 
                    }else if(puntosTruco > puntosHayRetruco){
                        throw new TrucoException("Ya se canto el valecuatro");
                    }
                    mano = estadoTruco.accionAlTipoTruco(manoActual, jugadorTurno, equipoCantor, secuenciaCantos, listaRondaJugador, rondaActual);
                    manoActual.copiaParcialTruco(mano);
                
                    break;
                default:
                    throw new TrucoException( "Canto no valido"); 
            }
        } catch (Exception e) {
            manoActual.setEsperandoRespuesta(false);
            throw e;
        }
        manoActual.comprobarSiPuedeCantarTruco();
        manoActual.comprobarSiPuedeCantarEnvido(false);

		actualizarMano(manoActual, codigo);
        return manoActual;
    }

	public void responderTruco(String codigo, RespuestasTruco respuesta) {
		Mano manoActual = getMano(codigo);
        Integer jugadorTurno = manoActual.getJugadorTurno();
        Integer jugadorAnterior = manoActual.obtenerJugadorAnterior(jugadorTurno);
        Integer puntosTruco = manoActual.getPuntosTruco();
        List<List<Integer>> secuenciaCantos = manoActual.getSecuenciaCantoLista();
        Integer queTrucoEs = secuenciaCantos.size();

        Mano mano = new Mano();
        

        RespuestaTruco respuestaTruco =   converterRespuestaTruco.convertToEntityAttribute(respuesta);
        Boolean puedeEnvido = mano.getPuedeCantarEnvido(); 
        switch (respuesta) {
            case QUIERO:
                
                mano = respuestaTruco.accionRespuestaTruco(manoActual,jugadorTurno, jugadorAnterior, puntosTruco, secuenciaCantos, queTrucoEs);
                manoActual.copiaParcialTruco(mano);
                
                puntosTruco= manoActual.getPuntosTruco();
                if(puntosTruco > maximoPuntajeTruco){
                    manoActual.setPuntosTruco(maximoPuntajeTruco);
                    throw new TrucoException("El máximo puntaje obtenible en el truco son " + maximoPuntajeTruco +" puntos");
                }
                
                
                break;
            case NO_QUIERO: 
                mano = respuestaTruco.accionRespuestaTruco(manoActual,jugadorTurno, jugadorAnterior, puntosTruco, secuenciaCantos, queTrucoEs);
                manoActual.copiaParcialTruco(mano);
                manoActual.setTerminada(true);
                
                break;

            case SUBIR:

                mano = respuestaTruco.accionRespuestaTruco(manoActual,jugadorTurno, jugadorAnterior, puntosTruco, secuenciaCantos, queTrucoEs);
                manoActual.copiaParcialTruco(mano);
                
                break;
            default:
                throw new TrucoException( "Respuesta al truco no valida"); 
        }
        manoActual.comprobarSiPuedeCantarTruco();
		actualizarMano(manoActual, codigo);
    }

	public Mano terminarMano(Mano manoActual){
		Partida partida = manoActual.getPartida();
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

		if (partida.getEstado() == Estado.FINISHED) {
			return manoActual; // TODO CAMBIAR!!!!!!
		} else {
            manosPorPartida.remove(partida.getCodigo());
			partida.setJugadorMano((partida.getJugadorMano() + 1) % partida.getNumJugadores());
            partidaService.updatePartida(partida, partida.getId());
			return crearMano(partida);
		}
		
		
	}

}
