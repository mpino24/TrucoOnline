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
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.FlorException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.ConverterRespuestaTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.ConverterTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestaTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.Truco;


@Service
public class ManoService {

    private final Map<String, Mano> manosPorPartida = new HashMap<>();
    
    private final CartaRepository cartaRepository;

    private final PartidaService partidaService;

	private final ConverterTruco converterTruco = new ConverterTruco();
    private final ConverterRespuestaTruco converterRespuestaTruco = new ConverterRespuestaTruco(this);

	
    

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

public List<List<Carta>> repartirCartasSoloFlor(Partida partida) {
    Integer numJugadores = partida.getNumJugadores();
    Integer cartasEnLaBaraja = 40;
    Integer cartasPorJugador = 3;

    // 1) Verificamos que haya suficientes cartas
    if (numJugadores * cartasPorJugador > cartasEnLaBaraja) {
        throw new IllegalArgumentException("No hay suficientes cartas para todos los jugadores.");
    }

    // 2) Obtenemos los IDs del 1 al 40
    List<Integer> listaCartasId = IntStream.rangeClosed(1, cartasEnLaBaraja)
                                          .boxed()
                                          .collect(Collectors.toList());

    // 3) "Barajamos" los IDs para luego repartir
    Collections.shuffle(listaCartasId);

    // 4) Agrupamos las cartas por Palo en un Map<Palo, List<Carta>>
    Map<Palo, List<Carta>> cartasPorPalo = new HashMap<>();
    for (Palo p : Palo.values()) {
        cartasPorPalo.put(p, new ArrayList<>()); 
    }

    // 5) Cargamos las 40 cartas en el Map (según su palo)
    for (Integer id : listaCartasId) {
        Carta carta = findCarta(id);
        if (carta != null) {
            Palo palo = carta.getPalo();
            cartasPorPalo.get(palo).add(carta);
        }
    }

    // 6) Asegurarnos de "barajar" internamente la lista de cada palo
    for (Palo palo : cartasPorPalo.keySet()) {
        Collections.shuffle(cartasPorPalo.get(palo));
    }

    // 7) Ahora repartimos 3 cartas del mismo palo a cada jugador
    List<List<Carta>> res = new ArrayList<>();
    for (int i = 0; i < numJugadores; i++) {
        List<Carta> cartasJugador = new ArrayList<>();

        // 7a) Buscamos un palo que aún tenga >= 3 cartas disponibles
        //     Podrías escoger un "palo" al azar, o simplemente iterar los palos en orden
        Palo paloSeleccionado = null;
        for (Palo p : Palo.values()) {
            if (cartasPorPalo.get(p).size() >= cartasPorJugador) {
                paloSeleccionado = p;
                break;
            }
        }

        // Si no encontraste ningún palo con 3 cartas disponibles, error
        if (paloSeleccionado == null) {
            throw new IllegalArgumentException("No hay palos suficientes para repartir flor a todos los jugadores");
        }

        // 7b) Tomamos las 3 primeras cartas de ese palo
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
    Integer cartasEnLaBaraja = 40;       // Asumiendo baraja de 40 cartas
    Integer cartasPorJugador = 3;

    // 1) Verificamos que haya cartas suficientes
    if (numJugadores * cartasPorJugador > cartasEnLaBaraja) {
        throw new IllegalArgumentException("No hay suficientes cartas para todos los jugadores.");
    }

    // 2) Generamos la lista de IDs de 1..40 y la barajamos
    List<Integer> listaCartasId = IntStream.rangeClosed(1, cartasEnLaBaraja)
                                          .boxed()
                                          .collect(Collectors.toList());
    Collections.shuffle(listaCartasId);

    // 3) Cargamos todas las cartas en memoria, agrupadas por Palo
    Map<Palo, List<Carta>> cartasPorPalo = new HashMap<>();
    for (Palo p : Palo.values()) {
        cartasPorPalo.put(p, new ArrayList<>());
    }

    // Rellenamos el Map<Palo, List<Carta>> con los 40 IDs (barajeados)
    for (Integer id : listaCartasId) {
        Carta c = findCarta(id);
        if (c != null) {
            cartasPorPalo.get(c.getPalo()).add(c);
        }
    }

    // 4) Seleccionamos al "jugadorFlor": puede ser el primero (índice 0) 
    //    o uno aleatorio. Aquí, elegimos el primero por simplicidad.
    int jugadorFlor = 0;

    // 5) Elegimos un palo que tenga al menos 3 cartas
    //    (Por simplicidad, nos quedamos con el primero que encontremos)
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

    // 6) Extraemos 3 cartas de ese palo
    List<Carta> listaFlor = cartasPorPalo.get(paloFlor);
    // Barajamos internamente para tomar 3 aleatorias de ese palo
    Collections.shuffle(listaFlor);
    List<Carta> manoFlor = new ArrayList<>();
    for (int i = 0; i < cartasPorJugador; i++) {
        manoFlor.add(listaFlor.remove(0));
    }

    // 7) Para el resto de jugadores, repartimos 3 cartas de palos distintos.
    //    (Para asegurar que NO tengan flor.)
    //    Tomamos las cartas restantes en la baraja y las "barajamos".
    List<Carta> barajaRestante = new ArrayList<>();
    for (Palo p : Palo.values()) {
        barajaRestante.addAll(cartasPorPalo.get(p));
    }
    Collections.shuffle(barajaRestante);

    // Estructura final: 
    // Repartiremos [ [flor para jugadorFlor], [3 cartas sin flor], [3 cartas sin flor], ... ]
    List<List<Carta>> resultado = new ArrayList<>();
    // Ponemos primero la mano del jugadorFlor
    resultado.add(jugadorFlor, manoFlor);

    // 8) Para los demás jugadores, sacamos 3 cartas de la barajaRestante asegurando NO sean flor.
    //    => Si por casualidad salen 3 del mismo palo, "forzamos" un cambio.
    //    => Lo haremos de forma sencilla: 
    //       - Cogemos la barajaRestante en orden
    //       - Vamos dando cartas y verificamos que no formen flor.
    
    int jugador = 0; // índice de jugador
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
                            // Reemplazamos
                            manoJugador.set(2, cartaAlternativa);
                            barajaRestante.remove(idx);
                            // Regresamos la carta previa al final de la baraja
                            barajaRestante.add(siguienteCarta);
                            encontreDistinta = true;
                            break;
                        }
                    }
                    if (!encontreDistinta) {
                        // Si no encontré ninguna carta de distinto palo,
                        // es que la baraja no permite "no dar flor".
                        throw new IllegalArgumentException(
                            "No hay suficientes cartas para evitar flor al resto de jugadores."
                        );
                    }
                }
            }
        }
        // insertamos en la posición i
        if (resultado.size() <= i) {
            // Expandimos la lista con nulls si hace falta
            while (resultado.size() <= i) {
                resultado.add(null);
            }
        }
        resultado.set(i, manoJugador);
    }

    return resultado;
}


	public Carta findCarta(Integer cartaId){
		Carta res = cartaRepository.findById(cartaId).orElse(null);
		return res;
	}

	
	public Mano crearMano(Partida partida){
		Mano nuevaMano = new Mano();
		
		nuevaMano.setPartida(partida);
		nuevaMano.setJugadorTurno(partida.getJugadorMano());
		nuevaMano.setCartasDisp(repartirCartasSoloUnaFlor(partida)); //RECORDAR CAMBIAR ESTO

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

            if(canto.equals(Cantos.FLOR)){ //AQUI ESTA LA FLOOOOOOOOOOOOOOOOOOOOOOOOR
                    if (queFlorPuedeCantar!=1) {
                        throw new FlorException("No podés cantar más veces flor/No tenés flor");
                    }
                    
                    numCantosFlores=numCantosFlores+1;
                    manoActual.setEquipoCantor(null);
                    Integer envidosIniciales=0;
                    Integer tiposDeEnvido=3;
                    List<Integer> envidos = new ArrayList<>();
                    for(int i = 0; i<tiposDeEnvido;i++){
                        envidos.add(envidosIniciales);
                    }
                    manoActual.setEnvidosCantados(envidos);
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
        
        switch (respuesta) {
            case QUIERO:
                
                gestionarPuntosEnvido(false, codigo);
                manoActual = getMano(codigo);
                manoActual.setUltimoMensaje(Cantos.LISTA_ENVIDOS);
                manoActual.setJugadorTurno(jugadorIniciador);
                manoActual.setJugadorIniciadorDelCanto(null);
                manoActual.comprobarSiPuedeCantarTruco();
                
                break;
            case NO_QUIERO:
                
                
                gestionarPuntosEnvido(true, codigo);
                
                manoActual = getMano(codigo);
                manoActual.setUltimoMensaje(Cantos.NO_QUIERO);
                manoActual.setJugadorTurno(jugadorIniciador);
                manoActual.setJugadorIniciadorDelCanto(null);
                manoActual.comprobarSiPuedeCantarTruco();
                
                break;

            default:
                cantosEnvido(codigo, respuesta);
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

        switch (respuesta) {
            case QUIERO:

                gestionarPuntosFlor(false, codigo,null);
                manoActual.setUltimoMensaje(Cantos.LISTA_ENVIDOS_FLOR);
                manoActual.setJugadorTurno(jugadorIniciador);
                manoActual.setJugadorIniciadorDelCanto(null);
                manoActual.comprobarSiPuedeCantarTruco();
                break;

            case CON_FLOR_ME_ACHICO:
                gestionarPuntosFlor(true, codigo,puntosRechazoAchicarse);
                manoActual = getMano(codigo);
                manoActual.setUltimoMensaje(Cantos.CON_FLOR_ME_ACHICO);
                manoActual.setJugadorTurno(jugadorIniciador);
                manoActual.setJugadorIniciadorDelCanto(null);
                manoActual.comprobarSiPuedeCantarTruco();
                

                break;

            case NO_QUIERO:
                
                gestionarPuntosFlor(true, codigo,puntosRechazoNoQuiero);
                manoActual = getMano(codigo);
                manoActual.setUltimoMensaje(Cantos.NO_QUIERO);
                manoActual.setJugadorTurno(jugadorIniciador);
                manoActual.setJugadorIniciadorDelCanto(null);
                manoActual.comprobarSiPuedeCantarTruco();
                
                break;

             case CONTRAFLOR:
                    if(queFlorPuedeCantar!=2){
                        throw new EnvidoException("No podés cantar contraflor capo");
                    }
                    numCantosFlores=numCantosFlores+1;
                    manoActual.setFloresCantadas(numCantosFlores);
                    manoActual.setJugadorTurno(quienRespondeFlor);
                    manoActual.comprobarSiPuedeCantarFlor();
                    break;
          
            default:
                 throw new FlorException("Canto no valido");
        }
        manoActual.comprobarSiPuedeCantarTruco();
        actualizarMano(manoActual, codigo);
    }

    //TODO: PROBABLEMENTE SEA MÁS CONVENIENTE EN MANOSERVICE
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

}
