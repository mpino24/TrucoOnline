package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorRepository;

import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaRepository;

import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Visibilidad;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorRepository;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.AuthoritiesRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserRepository;


import static org.junit.jupiter.api.Assertions.assertEquals;


import java.time.Duration;
import java.time.LocalDateTime;


import org.junit.jupiter.api.Test;

@DataJpaTest
public class EstadisticasRepositoryTest {

    @Autowired
    private EstadisticasRepository estadisticasRepository;
    @Autowired
    private PartidaJugadorRepository partidaJugadorRepository;
    @Autowired
    private JugadorRepository jugadorRepository;   
    @Autowired
    private PartidaRepository partidaRepository;
    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    @Autowired
    private UserRepository  userRepository;
    
        private Jugador jugador = new Jugador();
        private User user = new User();
        private Partida partida = new Partida();
    
        private PartidaJugador partidaJugador= new PartidaJugador();
    
       
        public void crearYGuardarPartida(String codigo,Integer numJugadores, Integer puntosMaximos, Integer puntosEquipo1, Integer puntosEquipo2, Boolean conFlor, Boolean terminada) {
            partida = new Partida();
            
            partida.setCodigo("TESTS");
            partida.setNumJugadores(numJugadores);
            partida.setInstanteInicio(LocalDateTime.now().minusHours(1));
            partida.setConFlor(conFlor);
            partida.setPuntosMaximos(puntosMaximos);
            partida.setPuntosEquipo1(puntosEquipo1);
            partida.setPuntosEquipo2(puntosEquipo2);
            if(terminada){
                partida.setInstanteFin(LocalDateTime.now());
            }
            partida.setJugadorMano(1);
            partida.setVisibilidad(Visibilidad.PUBLICA);
            partida = partidaRepository.save(partida);
        }
    
        public void crearYGuardarPartidaJugador(Integer posicion, Integer floresCantadas, Integer quierosCantados, Integer noQuierosCantados) {
           partidaJugador = new PartidaJugador();
    
            partidaJugador.setPlayer(jugadorRepository.findById(jugador.getId()).get());
            partidaJugador.setGame(partida);
            partidaJugador.setPosicion(posicion);
            partidaJugador.setIsCreator(true);
            partidaJugador.setFloresCantadas(floresCantadas);
            partidaJugador.setQuierosCantados(quierosCantados);	
            partidaJugador.setNoQuierosCantados(noQuierosCantados);
            partidaJugador.setEnganos(2);
            partidaJugador = partidaJugadorRepository.save(partidaJugador);
        }
    
    
    

        public void setUp() {

            borrarTodo();
            // usuario
            
            user.setAuthority(authoritiesRepository.findByName("PLAYER").get());
            user.setPassword("CONTRASEÑA PRUEBA");
            user.setUsername("USUARIO PRUEBA");
            user = userRepository.save(user);  
            
            // jugador
            
            jugador.setEmail("mail");
            jugador.setFirstName("nombre");
            jugador.setLastName("apellido");
            jugador.setPhoto("foto");
            jugador.setUser(user);
            jugador = jugadorRepository.save(jugador);  


            
        }
        
    
        
        public void borrarTodo() {
            if (user != null && user.getId() != null && userRepository.existsById(user.getId())) {
                userRepository.delete(user);
            }
            if (jugador != null && jugador.getId() != null && jugadorRepository.existsById(jugador.getId())) {
                jugadorRepository.delete(jugador);
            }
            if (partida != null && partida.getId() != null && partidaRepository.existsById(partida.getId())) {
                partidaRepository.delete(partida);
            }
            if (partidaJugador != null && partidaJugador.getId() != null && partidaJugadorRepository.existsById(partidaJugador.getId())) {
                partidaJugadorRepository.delete(partidaJugador);
            }
        }
        


    @Test
    public void testFindAllPartidasJugadasDebenSer0() {
        setUp();

        
        Integer partidasJugadas = estadisticasRepository.findAllPartidasJugadas(jugador.getId());
        Integer partidasDeA2 = estadisticasRepository.findPartidasA2(jugador.getId());
        Integer partidasDeA4 = estadisticasRepository.findPartidasA4(jugador.getId());
        Integer partidasDeA6 = estadisticasRepository.findPartidasA6(jugador.getId());
        assertEquals(0, partidasJugadas);
        assertEquals(0, partidasDeA2);
        assertEquals(0, partidasDeA4);
        assertEquals(0, partidasDeA6);
    }

    @Test
    public void testFindAllPartidasJugadasDebenSer1() {
        setUp();
        crearYGuardarPartida("TESTS",2, 15, 15, 1, true, true);
        
        crearYGuardarPartidaJugador(1, 1, 1, 1);

        Integer partidasJugadas = estadisticasRepository.findAllPartidasJugadas(jugador.getId());
        assertEquals(1, partidasJugadas);

        borrarTodo();
       
    }

    @Test
    public void testFindAllPartidasJugadasDebenSer2() {
        setUp();
        crearYGuardarPartida("TESTS1",2, 15, 15, 1, true, true);
        crearYGuardarPartidaJugador(1, 1, 1, 1);
        crearYGuardarPartida("TESTS2",2, 15, 15, 1, true, true);
        crearYGuardarPartidaJugador(1, 1, 1, 1);
        crearYGuardarPartida("TESTS3",2, 15, 14, 1, true, false); // Una no terminada entonces no cuenta
        crearYGuardarPartidaJugador(1, 1, 1, 1);

  

        Integer partidasJugadas = estadisticasRepository.findAllPartidasJugadas(jugador.getId());
        assertEquals(2, partidasJugadas);

        borrarTodo();
       
    }

    @Test
    public void testFindTiempoJugadorDebeSerNull() {
        setUp();

        
        Integer tiempoJugador = estadisticasRepository.findTiempoJugado(jugador.getId());
        assertEquals(null, tiempoJugador);
        borrarTodo();
    }
    @Test
    public void testFindTiempoJugadorDebeSer1hora() {
        setUp();
        crearYGuardarPartida("TESTS",2, 15, 15, 1, true, true);
        crearYGuardarPartidaJugador(1, 1, 1, 1);
        Duration duracion = Duration.ofSeconds(3600);   
        
        Integer tiempoJugador = estadisticasRepository.findTiempoJugado(jugador.getId());
        assertEquals(duracion, Duration.ofSeconds(tiempoJugador));
        borrarTodo();
    }
    @Test
    public void testFindTiempoJugadorDebeSer2horas() {
        setUp();
        crearYGuardarPartida("TESTS1",2, 15, 15, 1, true, true);
        crearYGuardarPartidaJugador(1, 1, 1, 1);
        crearYGuardarPartida("TESTS2",2, 15, 15, 1, true, true);
        crearYGuardarPartidaJugador(1, 1, 1, 1);
        crearYGuardarPartida("TESTS3",2, 15, 15, 1, true, false); // Una no terminada entonces no cuenta
        crearYGuardarPartidaJugador(1, 1, 1, 1);
        Duration duracion = Duration.ofSeconds(3600*2);   
        
        Integer tiempoJugador = estadisticasRepository.findTiempoJugado(jugador.getId());
        assertEquals(duracion, Duration.ofSeconds(tiempoJugador));
        borrarTodo();
    }


    @Test
    public void testFindVictoriasDebenSer1YDerrotasDebenSer2() {
        setUp();
        crearYGuardarPartida("TESTS1",2, 15, 15, 1, true, true);
        crearYGuardarPartidaJugador(0, 1, 1, 1);
        crearYGuardarPartida("TESTS2",2, 15, 1, 15, true, true);
        crearYGuardarPartidaJugador(0, 1, 1, 1);
        crearYGuardarPartida("TESTS3",2, 15, 1, 15, true, true);
        crearYGuardarPartidaJugador(0, 1, 1, 1);
        crearYGuardarPartida("TESTS4",2, 15, 1, 15, true, false); // Una no terminada entonces no cuenta
        crearYGuardarPartidaJugador(0, 1, 1, 1);
        
        Integer victorias = estadisticasRepository.findVictorias(jugador.getId());
        Integer derrotas = estadisticasRepository.findAllPartidasJugadas(jugador.getId()) - victorias;
        assertEquals(1, victorias);
        assertEquals(2, derrotas);
        borrarTodo();
    }
    @Test
    public void testFindVictoriasHabiendoJugadoEnDistintosEquipos() {
        setUp();
        crearYGuardarPartida("TESTS1",2, 15, 15, 1, true, true);
        crearYGuardarPartidaJugador(0, 1, 1, 1);
        crearYGuardarPartida("TESTS2",2, 15, 1, 15, true, true);
        crearYGuardarPartidaJugador(1, 1, 1, 1);
        crearYGuardarPartida("TESTS3",2, 15, 1, 15, true, true);
        crearYGuardarPartidaJugador(1, 1, 1, 1);
     
        
        Integer victorias = estadisticasRepository.findVictorias(jugador.getId());
        assertEquals(3, victorias);

        borrarTodo();
    }

    @Test
    public void testFindAllPartidasA2DebenSer2A4DebenSer2A6DebenSer1() {
        setUp();
        crearYGuardarPartida("TESTS1",2, 15, 15, 1, true, true);
        crearYGuardarPartidaJugador(0, 1, 1, 1);
        crearYGuardarPartida("TESTS2",2, 15, 1, 15, true, true);
        crearYGuardarPartidaJugador(0, 1, 1, 1);
        crearYGuardarPartida("TESTS3",4, 15, 1, 15, true, true);
        crearYGuardarPartidaJugador(0, 1, 1, 1);
        crearYGuardarPartida("TESTS4",4, 15, 1, 15, true, true);
        crearYGuardarPartidaJugador(0, 1, 1, 1);
        crearYGuardarPartida("TESTS5",4, 15, 1, 15, true, false); // Una no terminada entonces no cuenta
        crearYGuardarPartidaJugador(0, 1, 1, 1);
        crearYGuardarPartida("TESTS6",6, 15, 1, 15, true, true);
        crearYGuardarPartidaJugador(0, 1, 1, 1);
        crearYGuardarPartida("TESTS7",6, 15, 1, 15, true, false); // Una no terminada entonces no cuenta
        crearYGuardarPartidaJugador(0, 1, 1, 1);
        
        Integer partidasA2 = estadisticasRepository.findPartidasA2(jugador.getId());
        Integer partidasA4 = estadisticasRepository.findPartidasA4(jugador.getId());
        Integer partidasA6 = estadisticasRepository.findPartidasA6(jugador.getId());
        assertEquals(2, partidasA2);
        assertEquals(2, partidasA4);
        assertEquals(1, partidasA6);
        borrarTodo();
    }

    @Test
    public void testFindAllFloresQuierosNoQuieros1Partida() {
        setUp();
        crearYGuardarPartida("TESTS1",2, 15, 15, 1, true, true);
        crearYGuardarPartidaJugador(0, 2, 4, 2);

        assertEquals(2, estadisticasRepository.findNumeroFlores(jugador.getId()));
        assertEquals(4, estadisticasRepository.findQuieros(jugador.getId()));
        assertEquals(2, estadisticasRepository.findNoQuieros(jugador.getId()));
        assertEquals(2, estadisticasRepository.findNumeroEnganos(jugador.getId()));
        borrarTodo();
    }
    @Test
    public void testFindAllFloresQuierosNoQuierosVariasPartidas() {
        setUp();
        crearYGuardarPartida("TESTS1",2, 15, 15, 1, true, true);
        crearYGuardarPartidaJugador(0, 0, 1, 1);
        crearYGuardarPartida("TESTS2",2, 15, 15, 1, false, true); //Si no tiene flor, ignorarla para el calculo de esta
        crearYGuardarPartidaJugador(0, 100000, 4, 2);
        crearYGuardarPartida("TESTS3",2, 15, 15, 1, true, false); // Una no terminada entonces no cuenta
        crearYGuardarPartidaJugador(0, 2, 2, 1);
        crearYGuardarPartida("TESTS4",2, 15, 15, 1, true, true);
        crearYGuardarPartidaJugador(0, 1, 0, 1);

        assertEquals(1, estadisticasRepository.findNumeroFlores(jugador.getId()));
        assertEquals(5, estadisticasRepository.findQuieros(jugador.getId()));
        assertEquals(4, estadisticasRepository.findNoQuieros(jugador.getId()));
        assertEquals(6, estadisticasRepository.findNumeroEnganos(jugador.getId())); // en total 6 engaños porque en el constructor puse por defecto 2, porque me acorde al final 
        borrarTodo();
    }
}
