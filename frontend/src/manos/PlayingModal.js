import React, { useState, forwardRef, useEffect, useRef } from 'react';
import tokenService from "../services/token.service.js";
import useFetchState from "../util/useFetchState";
import CartasVolteadas from './CartasVolteadas';
import Perfil from './Perfil';
import  "../static/css/mano/PlayingModal.css"

import backgroundMusic from '../static/audios/musicaPartida2.mp3';
import PuntosComponente from './PuntosComponente';

const jwt = tokenService.getLocalAccessToken();

const PlayingModal = forwardRef((props, ref) => {
    const game = props.game;
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [cartasJugador, setCartasJugador] = useState([]);
    const [mano, setMano] = useState(null);  // mano inicialmente es null

    //MENSAJES Y CUADRO DEL ENVIDO
    const [ultimoMensaje, setUltimoMensaje] = useState(null);
    const [resolucionEnvido, setResolucionEnvido] = useState(false);
    const [resolucionEnvidoFlor, setResolucionEnvidoFlor] = useState(false);

    const [cantoDicho, setCantoDicho] = useState(false);

    const [envidosJugadores, setEnvidosJugadores] = useState([]);
    const [envidosFlorJugadores, setEnvidosFlorJugadores] = useState([]);

    const puntosSinTruco = 1;
    const puntosConTruco = 2;
    const puntosConRetruco = 3;
    const [puntosTrucoActuales, setPuntosTrucoActuales] = useState(puntosSinTruco);
    const [tirarTrigger, setTirarTrigger] = useState(0);
    const [trucoTrigger, setTrucoTrigger] = useState(0);
    const [envidoTrigger, setEnvidoTrigger] = useState(0);
    const [florTrigger, setFlorTrigger] = useState(0);

    const [posicion, setPosicion] = useFetchState(
        {}, `/api/v1/partidajugador/miposicion/${game.id}`, jwt, setMessage, setVisible
    );

    const [nombresJugadores, setNombresJugadores] = useState([]);

    const audioRef = useRef(null);
    const dropAreaRef = useRef(null);
    const [isOverDropArea, setIsOverDropArea] = useState(false);
    const [draggedCarta, setDraggedCarta] = useState(null);
    const [positionCarta, setPositionCarta] = useState({ x: 0, y: 0 });

    const [isPlaying, setIsPlaying] = useState(false);
    const [volume, setVolume] = useState(50); // Default volume at 50%

    function fetchMano() {
        fetch('/api/v1/manos/' + game.codigo, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${jwt}`,
            },
        })
        .then((response) => response.json())
        .then((data) => {
            setMano(data);  // Asignamos 'data' a mano
            let cartasActuales = data.cartasDisp[posicion];

            if (JSON.stringify(cartasJugador) !== JSON.stringify(cartasActuales)) {
                setCartasJugador(cartasActuales);
                setPuntosTrucoActuales(data.puntosTruco);
            }
            
            if (data.envidosCadaJugador) {
                setEnvidosJugadores(
                    data.envidosCadaJugador.map((envido) => (envido === null ? 'Son buenas' : envido))
                );
            }
            if (data.envidosFlorCadaJugador) {
                setEnvidosFlorJugadores(
                    data.envidosFlorCadaJugador.map((envidoflor) => (envidoflor === null ? 'Son buenas' : envidoflor))
                );
            }

            // Actualizamos el ultimoMensaje si ha cambiado
            setUltimoMensaje((prevUltimoMensaje) => {
                if (data.ultimoMensaje !== prevUltimoMensaje) {
                    return data.ultimoMensaje;
                }
                return prevUltimoMensaje;
            });
        })
        .catch((error) => {
            console.error("Error fetching mano:", error);
            setMessage("Error fetching mano.");
            setVisible(true);
        });
    }

    function mostrarMensaje() {
        if (ultimoMensaje === "LISTA_ENVIDOS") {
            setResolucionEnvido(true);
            const timeoutId = setTimeout(() => setResolucionEnvido(false), 7000);
            return () => clearTimeout(timeoutId);
        } 
        if (ultimoMensaje === "LISTA_ENVIDOS_FLOR") {
            setResolucionEnvidoFlor(true);
            const timeoutId = setTimeout(() => setResolucionEnvidoFlor(false), 7000);
            return () => clearTimeout(timeoutId);
        }
        else if (ultimoMensaje !== null) {
            setCantoDicho(true);
            const timeoutId = setTimeout(() => setCantoDicho(false), 5000);
            return () => clearTimeout(timeoutId);
        }
    }

    useEffect(() => {
        mostrarMensaje();
    }, [ultimoMensaje]);

    useEffect(() => {
        let intervalId;
        mostrarMensaje();
        fetchMano();
        intervalId = setInterval(fetchMano, 1000);

        function fetchPlayerNames() {
            fetch('/api/v1/partidajugador/players?partidaCode='+game.codigo, {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            })
            .then((response) => response.json())
            .then((data) => {
                const nombres = data.map(
                    (partidaJugador) => partidaJugador.player.userName
                );
                setNombresJugadores(nombres);
            });
        }
        fetchPlayerNames();
        
        return () => clearInterval(intervalId);
    }, [game.codigo, posicion]);

    useEffect(() => {
        fetchMano();
    }, [tirarTrigger, trucoTrigger, envidoTrigger, florTrigger]);

    useEffect(() => {
        // Cuando mano cambie y no sea null, actualizamos puntosTrucoActuales
        if (mano && mano.puntosTruco) {
            setPuntosTrucoActuales(mano.puntosTruco);
        }
    }, [mano]);

    // Set initial volume
    useEffect(() => {
        if (audioRef.current) {
            audioRef.current.volume = volume / 100;
        }
    }, [volume]);

    const handlePlayMusic = () => {
        if (audioRef.current) {
            audioRef.current.play()
            .then(() => {
                setIsPlaying(true);
            })
            .catch((error) => {
                console.error("Error playing audio:", error);
            });
        }
    };

    const handlePauseMusic = () => {
        if (audioRef.current) {
            audioRef.current.pause();
            setIsPlaying(false);
        }
    };

    const handleVolumeChange = (event) => {
        const sliderValue = event.target.value;
        const minLog = 0.01; // Volumen más bajo
        const maxLog = 1;    
        const logVolume = Math.pow(10, (sliderValue / 100) * (Math.log10(maxLog) - Math.log10(minLog)) + Math.log10(minLog));
        setVolume(sliderValue); 
        if (audioRef.current) {
            audioRef.current.volume = logVolume; 
        }
    };

    const dragStart = (evento, carta) => {
        // Chequeamos que mano no sea null
        if (mano && cartasJugador && Number(posicion) === mano.jugadorTurno) {
            setDraggedCarta(carta);
            evento.dataTransfer.effectAllowed = 'move';
        }
    };

    const onDrag = (evento) => {
        const x = evento.clientX;
        const y = evento.clientY;
        setPositionCarta({ x, y });

        if (dropAreaRef.current) {
            const dropAreaRect = dropAreaRef.current.getBoundingClientRect();
            const isOver = (
                x >= dropAreaRect.left &&
                x <= dropAreaRect.right &&
                y >= dropAreaRect.top &&
                y <= dropAreaRect.bottom
            );
            setIsOverDropArea(isOver);
        }
    };

    const onDragEnd = (evento, carta) => {
        const x = evento.clientX;
        const y = evento.clientY;
    
        if (dropAreaRef.current) {
            const dropAreaRect = dropAreaRef.current.getBoundingClientRect();
            const isOver = (
                x >= dropAreaRect.left &&
                x <= dropAreaRect.right &&
                y >= dropAreaRect.top &&
                y <= dropAreaRect.bottom
            );
            if (isOver) {
                tirarCarta(carta.id); 
            }
        }
        setDraggedCarta(null);
        setIsOverDropArea(false);
    };

    function tirarCarta(cartaId) {
        fetch(`/api/v1/manos/${game.codigo}/tirarCarta/${cartaId}`, {
            method: "PATCH",
            headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: "application/json",
                "Content-Type": "application/json",
            },
        })
        .then((response) => response.text())
        .then((data) => {
            if (data) {
                setTirarTrigger((prev) => prev + 1);
            }
        })
        .catch((error) => alert(error.message));
    }

    function cantarTruco(truco) {
        fetch(`/api/v1/manos/${game.codigo}/cantarTruco/${truco}`, {
            method: "PATCH",
            headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: "application/json",
                "Content-Type": "application/json",
            },
        })
        .then((response) => response.text())
        .then((data) => {
            if (data) {
                setTrucoTrigger((prev) => prev + 1);
            }
        })
        .catch((error) => alert(error.message));
    }

    function responderTruco(respuesta) {
        fetch(`/api/v1/manos/${game.codigo}/responderTruco/${respuesta}`, {
            method: "PATCH",
            headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: "application/json",
                "Content-Type": "application/json",
            },
        })
        .then((response) => response.text())
        .then((data) => {
            if (data) {
                setTrucoTrigger((prev) => prev + 1);
            }
        })
        .catch((error) => alert(error.message));
    }

    function cantarEnvido(respuesta) {
        fetch(`/api/v1/manos/${game.codigo}/cantarEnvido/${respuesta}`, {
            method: "PATCH",
            headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: "application/json",
                "Content-Type": "application/json",
            },
        })
        .then((response) => response.text())
        .then((data) => {
            if (data) {
                setEnvidoTrigger((prev) => prev + 1);
            }
        })
        .catch((error) => alert(error.message));
    }

    function cantarFlor(respuesta) {
        fetch(`/api/v1/manos/${game.codigo}/cantarFlor/${respuesta}`, {
            method: "PATCH",
            headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: "application/json",
                "Content-Type": "application/json",
            },
        })
        .then((response) => response.text())
        .then((data) => {
            if (data) {
                setFlorTrigger((prev) => prev + 1);
            }
        })
        .catch((error) => alert(error.message));
    }

    function responderEnvido(respuesta) {
        fetch(`/api/v1/manos/${game.codigo}/responderEnvido/${respuesta}`, {
            method: "PATCH",
            headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: "application/json",
                "Content-Type": "application/json",
            },
        })
        .then((response) => response.text())
        .then((data) => {
            if (data) {
                setEnvidoTrigger((prev) => prev + 1);
            }
        })
        .catch((error) => alert(error.message));
    }

    function responderFlor(respuesta) {
        fetch(`/api/v1/manos/${game.codigo}/responderFlor/${respuesta}`, {
            method: "PATCH",
            headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: "application/json",
                "Content-Type": "application/json",
            },
        })
        .then((response) => response.text())
        .then((data) => {
            if (data) {
                setFlorTrigger((prev) => prev + 1);
            }
        })
        .catch((error) => alert(error.message));
    }

    // NUEVO: Función para renderizar Cartas del Jugador
    const renderCartasJugador = () => {
        if (!mano) {  // Si mano es null, no retorna nada
            return <div>Cargando mano...</div>;
        }
        if (!mano.cartasDisp) {
            return <div>Cargando cartas del jugador...</div>;
        }
        if (posicion === null) {
            return <div>Posición no establecida...</div>;
        }

        const cartasJugador = mano.cartasDisp[posicion];
        const esTurnoValido =
            mano &&
            cartasJugador &&
            Number(posicion) === mano.jugadorTurno &&
            !mano.esperandoRespuesta;

        if (!cartasJugador) {
            return <div></div>;
        }

        return (
            <div className="cartas-jugador-container">
                {cartasJugador.map((carta, index) => (
                    carta && (
                        <div
                            key={index}
                            className={`card-container ${esTurnoValido ? 'swirl' : ''}`}
                        >
                            <div
                                className={`base-card ${!esTurnoValido ? 'invalid-turn' : ''}`}
                                draggable={esTurnoValido}
                                onDragStart={(evento) => dragStart(evento, carta)}
                                onDrag={(evento) => onDrag(evento)}
                                onDragEnd={(evento) => onDragEnd(evento, carta)}
                            >
                                <button 
                                    onClick={() => tirarCarta(carta.id)}
                                    style={{
                                        border: 'none',
                                        outline: 'none',
                                        background: 'none',
                                        padding: 0,
                                        margin: 0,
                                        cursor: 'pointer'
                                    }}
                                >
                                    <img
                                        src={carta.foto}
                                        alt={`Carta ${index + 1}`}
                                        className="card-image"
                                        onError={(e) => (e.target.style.display = 'none')}
                                    />
                                </button>
                                {/* Overlay de resplandor holográfico */}
                                <div className="sunset-overlay"></div>
                            </div>
                        </div>
                    )
                ))}
            </div>
        );
    };

    // NUEVO: Función para renderizar cartas en mesa
    const renderCartasMesa = () => {
        if (!mano) {
            return <div>Cargando mano...</div>;
        }
        if (!mano.cartasLanzadasTotales) {
            return <div>No hay cartas para mostrar.</div>;
        }

        const cartasLanzadasTotales = mano.cartasLanzadasTotales;
        if (cartasLanzadasTotales.length === 0) {
            return <div>No hay cartas para mostrar.</div>;
        }

        return (
            <div className="cartas-mesa-container">
                {cartasLanzadasTotales.map((cartasJugador, jugadorIndex) => (
                    <div key={jugadorIndex} className="jugador-cartas">
                        {cartasJugador.map((carta, rondaIndex) => (
                            carta && (
                                <div
                                    key={rondaIndex}
                                    className="card-container mesa"
                                    style={{
                                        transform: `translateY(-${rondaIndex * -15}px)`,
                                        position: 'relative',
                                        zIndex: 10 + rondaIndex,
                                    }}
                                >
                                    <img
                                        src={carta.foto}
                                        alt={`Carta Ronda ${rondaIndex + 1}`}
                                        className="card-image"
                                        onError={(e) => (e.target.style.display = 'none')}
                                    />
                                    <div className="sunset-overlay"></div>
                                </div>
                            )
                        ))}
                    </div>
                ))}
            </div>
        );
    };

    // AQUÍ empieza el **return** principal del componente
    return (
        <>
            { /* Evitamos rompernos si 'mano' es null usando optional chaining o condicionales */ }
            <div className="playing-modal-container">
                
                {/* Si mano es null, mostramos un loading y retornamos */}
                {!mano && (
                    <div style={{ color: 'white' }}>
                        Cargando mano...  
                    </div>
                )}

                {console.log("TresMismoPalo?-----------------------------"+mano?.tresMismoPalo)}
                {console.log("------------------------PuntosEnvido?-----------------------------"+mano?.puntosEnvido)}
                {console.log("------------------------RondaActual?-----------------------------"+mano?.rondaActual)}
                {console.log("------------------------FloresCantadas?-----------------------------"+mano?.floresCantadas)}
                {console.log("------------------------PuntosTruco?-----------------------------"+mano?.puntosTruco)}



                {/* Si mano existe, seguimos */}
                {mano && (
                    <>
                        <div
                            style={{ 
                                backgroundImage: 'url(/fondos/fondoPlayingModal.jpg)',
                                backgroundSize: 'cover', 
                                backgroundRepeat: 'no-repeat', 
                                backgroundPosition: 'center', 
                                height: '19000vh',
                                width: '100vw',
                                position: 'relative',
                                zIndex: -1
                            }}
                        >
                            <h3 className="player-heading">
                                Jugador: {Number(posicion)}
                            </h3>
                        </div>

                        <h4 className={"puntos-nuestros"}>Nos:</h4>
                        <h4 className={"puntos-ellos"}>Ellos:</h4>

                        {puntosTrucoActuales && (
                            <div style={{overflow:'hidden'}}> 
                                <PuntosComponente  
                                    estiloFotoPunto={"puntaje-EquipoNuestro"} 
                                    posicion={posicion} 
                                    puntosEquipo1={game.puntosEquipo1} 
                                    puntosEquipo2={game.puntosEquipo2}
                                />
                                <PuntosComponente 
                                    estiloFotoPunto={"puntaje-EquipoEllos"} 
                                    posicion={posicion} 
                                    puntosEquipo1={game.puntosEquipo2} 
                                    puntosEquipo2={game.puntosEquipo1}
                                />
                            </div>
                        )}

                        {/* Drop Area */}
                        <div ref={dropAreaRef} className="drop-area" />

                        {/* Botón Play Music */}
                        {!isPlaying && (
                            <button onClick={handlePlayMusic} className="play-music-button">
                                <span className="swirl-glow-textMusica"> 🎵 </span>                 
                            </button>
                        )}

                        {/* Botón Pause y Slider Volumen */}
                        {isPlaying && (
                            <>
                                <button onClick={handlePauseMusic} className="play-music-button">
                                    ⏸
                                </button>
                                <div className="volume-slider-container">
                                    <label htmlFor="volume-slider">Volume:</label>
                                    <input
                                        type="range"
                                        id="volume-slider"
                                        min="0"
                                        max="100"
                                        value={volume}
                                        onChange={handleVolumeChange}
                                    />
                                    <span>{volume}%</span>
                                </div>
                            </>
                        )}

                        <audio ref={audioRef} src={backgroundMusic} loop /> 

                        {/* Cartas del Jugador */}
                        {renderCartasJugador()}

                        {/* Carta Arrastrada */}
                        {draggedCarta && (
                            <div
                                className={`dragged-card ${Number(posicion) === mano.jugadorTurno ? 'swirl' : ''}`}
                                style={{ 
                                    left: `${positionCarta.x - 50}px`,
                                    top: `${positionCarta.y - 75}px`,
                                    opacity: isOverDropArea ? 1 : 0.5,
                                    pointerEvents: 'none',
                                    position: 'fixed',
                                    zIndex: 1000
                                }}
                            >
                                <img
                                    src={draggedCarta.foto}
                                    alt="Carta arrastrada"
                                    className="card-image"
                                    onError={(e) => (e.target.style.display = 'none')}
                                />
                                <div className="sunset-overlay"></div>
                            </div>
                        )}

                        {/* Cartas en la Mesa */}
                        <div className="cartas-mesa-position">
                            {renderCartasMesa()}
                        </div>

                        {/* Cuadro del Envido */}
                        <div style={{ position: 'absolute', left: '52%', top: '30%', transform: 'translateX(-50%)', zIndex: 1000 }}>
                            {resolucionEnvido && (
                                <div className="res-envido-container">
                                    <h3 className='swirl-glow-text2' style={{marginTop:12}}>Resolución de Envido</h3>
                                    <h5
                                        style={{
                                            color: mano.equipoGanadorEnvido === posicion % 2 ? 'white' : 'black',
                                            margin: '3px 0',
                                            fontStyle: 'italic'
                                        }}
                                    >
                                        {mano.equipoGanadorEnvido === posicion % 2 ? '¡Ganaste!' : 'Perdiste...'}
                                    </h5>                               
                                    {envidosJugadores.map((envido, index) => (
                                        <p style={{ margin: '9px 0' }} key={index}>
                                            {index === posicion ? "Vos" : nombresJugadores[index]}: {envido}
                                        </p>
                                    ))}
                                </div>
                            )}
                        </div>

                        {/* Cuadro de la contraflor */}
                        <div style={{ position: 'absolute', left: '52%', top: '30%', transform: 'translateX(-50%)', zIndex: 1000 }}>
                            {resolucionEnvidoFlor && (
                                <div className="res-envido-container">
                                    <h3 className='swirl-glow-text2' style={{marginTop:12}}>Resolución de Envido</h3>
                                    <h5
                                        style={{
                                            color: mano.equipoGanadorEnvido === posicion % 2 ? 'white' : 'black',
                                            margin: '3px 0',
                                            fontStyle: 'italic'
                                        }}
                                    >
                                        {mano.equipoGanadorEnvido === posicion % 2 ? '¡Ganaste!' : 'Perdiste...'}
                                    </h5>                               
                                    {envidosFlorJugadores.map((envidoflor, index) => (
                                        <p style={{ margin: '9px 0' }} key={index}>
                                            {index === posicion ? "Vos" : nombresJugadores[index]}: {envidoflor}
                                        </p>
                                    ))}
                                </div>
                            )}
                        </div>

                        {/* Cuadro Canto */}
                        <div style={{ position: 'absolute', left: '50%', top: '39%', transform: 'translateX(-50%)', zIndex: 1000 }}>
                            {cantoDicho && (
                                <div className="cuadros-canto">
                                    {ultimoMensaje && (
                                        <h3 style={{ color: "darkorange" }}>
                                            {ultimoMensaje.replace("_", " ").replace("2","")}
                                        </h3>
                                    )}
                                </div>
                            )}
                        </div>

                        {/* Botones Truco */}
                        <div style={{ display:'flex', flexDirection: 'row' }}>
                            {/* Evitamos error: mano?.puedeCantarTruco en lugar de mano.puedeCantarTruco */}
                            {mano?.cartasDisp && Number(posicion) === mano?.jugadorTurno && !mano?.esperandoRespuesta && mano?.puedeCantarTruco && puntosTrucoActuales && (
                                <div className="truco-button-container"> 
                                    {puntosTrucoActuales === puntosSinTruco && (
                                        <button onClick={() => cantarTruco('TRUCO')}>
                                            <span className="swirl-glow-text">¡Truco!</span>
                                        </button>
                                    )}
                                    {puntosTrucoActuales === puntosConTruco && (
                                        <button onClick={() => cantarTruco('RETRUCO')}>
                                            <span className="swirl-glow-text">¡Retruco!</span>
                                        </button>
                                    )}
                                    {puntosTrucoActuales === puntosConRetruco && (
                                        <button onClick={() => cantarTruco('VALECUATRO')}>
                                            <span className="swirl-glow-text">¡Vale cuatro!</span>
                                        </button>
                                    )}
                                </div>
                            )}
                            
                            {/* Cantar envido */}
                            {mano?.cartasDisp && Number(posicion) === mano?.jugadorTurno && !mano?.esperandoRespuesta && mano?.puedeCantarEnvido && (
                                <div className="envido-button-container">
                                    {mano?.queEnvidoPuedeCantar >= 3 && (
                                        <button onClick={() => cantarEnvido('ENVIDO')}>
                                            <span>Envido</span>
                                        </button>
                                    )}
                                    {mano?.queEnvidoPuedeCantar >= 2 && (
                                        <button onClick={() => cantarEnvido('REAL_ENVIDO')}>
                                            <span>Real Envido</span>
                                        </button>
                                    )}
                                    {mano?.queEnvidoPuedeCantar >= 1 && (
                                        <button style={{animation:'dropShadowGlowContainer 3s ease-in-out infinite'}} onClick={() => cantarEnvido('FALTA_ENVIDO')}>
                                            <span className="swirl-glow-text">Falta Envido</span>
                                        </button>
                                    )}
                                </div>
                            )}

                            {/* Cantar Flor */}
                            {mano?.cartasDisp && Number(posicion) === mano?.jugadorTurno && !mano?.esperandoRespuesta && mano?.puedeCantarFlor && (
                                <div className="envido-button-container" style={{ top: "30%", position: "absolute" }}>
                                    {mano?.queFlorPuedeCantar === 1 && (
                                        <button onClick={() => cantarFlor('FLOR')}>
                                            <span>¡Flor!</span>
                                        </button>
                                    )}
                                    {mano?.queFlorPuedeCantar === 2 && (
                                        <div>
                                            <button 
                                                style={{ animation:'dropShadowGlowContainer 3s ease-in-out infinite' }}
                                                onClick={() => cantarFlor('CONTRAFLOR')}
                                            >
                                                <span className="swirl-glow-text">¡Contraflor!</span>
                                            </button>
                                            <button 
                                                style={{ animation:'dropShadowGlowContainer 3s ease-in-out infinite' }}
                                                onClick={() => responderFlor('CON_FLOR_ME_ACHICO')}
                                            >
                                                <span className="swirl-glow-text">Con flor me achico...</span>
                                            </button>
                                        </div>
                                    )}
                                </div>
                            )}
                        </div>

                        {/* Responder Truco Buttons */}
                        {mano?.cartasDisp && Number(posicion) === mano?.jugadorTurno && mano?.esperandoRespuesta && puntosTrucoActuales && mano?.esTrucoEnvidoFlor === 0 && (
                            <div className="truco-button-container responder-truco-buttons">
                                {puntosTrucoActuales !== puntosConRetruco && 
                                    <button onClick={() => responderTruco("QUIERO")}>Quiero</button>
                                }
                                <button onClick={() => responderTruco("NO_QUIERO")}>No quiero</button>
                                {puntosTrucoActuales === puntosConRetruco && 
                                    <button style={{ animation: 'dropShadowGlowContainer 3s ease-in-out infinite' }} 
                                        onClick={() => responderTruco("QUIERO")}
                                    >
                                        <span className="swirl-glow-text">¡Quiero!</span>
                                    </button>
                                }
                                {puntosTrucoActuales !== puntosConRetruco && mano?.puedeCantarTruco && (
                                    <button
                                        style={{ animation:'dropShadowGlowContainer 3s ease-in-out infinite' }}
                                        onClick={() => responderTruco('SUBIR')}
                                    >
                                        <span className="swirl-glow-text">
                                            {puntosTrucoActuales === puntosSinTruco ? '¡Retruco!' : '¡Vale Cuatro!'}
                                        </span>
                                    </button>
                                )}
                                
                                {mano?.puedeCantarEnvido && (
                                    <div className='envido-button-container' style={{top:'-120%', position:'absolute', left:'60%'}}>
                                        {mano?.queEnvidoPuedeCantar >= 3 && (
                                            <button onClick={() => responderEnvido('ENVIDO')}>
                                                <span>Envido</span>
                                            </button>
                                        )}
                                        {mano?.queEnvidoPuedeCantar >= 2 && (
                                            <button onClick={() => responderEnvido('REAL_ENVIDO')}>
                                                <span>Real Envido</span>
                                            </button>
                                        )}
                                        {mano?.queEnvidoPuedeCantar >= 1 && (
                                            <button style={{animation:'dropShadowGlowContainer 3s ease-in-out infinite'}} onClick={() => responderEnvido('FALTA_ENVIDO')}>
                                                <span className="swirl-glow-text">Falta Envido</span>
                                            </button>
                                        )}
                                    </div>
                                )}
                            </div>
                        )}

                        {/* Responder Envido */}
                        {mano?.cartasDisp && Number(posicion) === mano?.jugadorTurno && mano?.esperandoRespuesta && mano?.esTrucoEnvidoFlor === 1 && (
                            <div className="envido-button-container" style={{ left:'70%', position:'fixed'}}>
                                {/* Responder Envido */}
                                <button onClick={() => responderEnvido("QUIERO")}>Quiero</button>
                                <button onClick={() => responderEnvido("NO_QUIERO")}>No quiero</button>
                                
                                {mano?.queEnvidoPuedeCantar >= 3 && mano?.puedeCantarEnvido && (
                                    <button onClick={() => responderEnvido('ENVIDO')}>
                                        <span>Envido</span>
                                    </button>
                                )}
                                {mano?.queEnvidoPuedeCantar >= 2 && mano?.puedeCantarEnvido && (
                                    <button onClick={() => responderEnvido('REAL_ENVIDO')}>
                                        <span>Real Envido</span>
                                    </button>
                                )}
                                {mano?.queEnvidoPuedeCantar >= 1 && mano?.puedeCantarEnvido && (
                                    <button style={{animation:'dropShadowGlowContainer 3s ease-in-out infinite'}} onClick={() => responderEnvido('FALTA_ENVIDO')}>
                                        <span className="swirl-glow-text">Falta Envido</span>
                                    </button>
                                )}
                       
                       
                        {/* Responder Flor */}
                        {mano?.cartasDisp && Number(posicion) === mano?.jugadorTurno && mano?.esperandoRespuesta && mano?.esTrucoEnvidoFlor === 1 && (
                            <div className="envido-button-container" style={{ left:'70%', position:'fixed'}}>
                                <button onClick={() => responderFlor("QUIERO")}>Quiero</button>
                                <button onClick={() => responderFlor("NO_QUIERO")}>No quiero</button>
                            </div>
                        )}
                            </div>
                        )}

                        {/* Cartas Volteadas */}
                        <CartasVolteadas
                            cartasDispo={mano?.cartasDisp}
                            posicionListaCartas={posicion}
                            jugadorMano={game.jugadorMano}
                        />
                    </>
                )}
            </div>

            <div style={{ zIndex: 100000000000000 }}>
                <Perfil game={game} posicion={posicion} />
            </div>
        </>
    );
});

export default PlayingModal;
