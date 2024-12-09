import React, { useState, forwardRef, useEffect, useRef } from 'react';
import tokenService from "../services/token.service.js";
import useFetchState from "../util/useFetchState";
import CartasVolteadas from './CartasVolteadas';
import Perfil from './Perfil';
import './PlayingModal.css';

import backgroundMusic from 'frontend/src/static/audios/musicaPartida2.mp3';
import PuntosComponente from './PuntosComponente';

const jwt = tokenService.getLocalAccessToken();

const PlayingModal = forwardRef((props, ref) => {
    const game = props.game;
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [cartasJugador, setCartasJugador] = useState([]);
    const [mano, setMano] = useState(null);

    //MENSAJES Y CUADRO DEL ENVIDO
    const [ultimoMensaje, setUltimoMensaje] = useState(null)
    const[resolucionEnvido, setResolucionEnvido] = useState(false)
    const [cantoDicho, setCantoDicho] = useState(false)

    const [envidosJugadores, setEnvidosJugadores] = useState([]);
    


    const puntosSinTruco = 1;
    const puntosConTruco = 2;
    const puntosConRetruco = 3;
    const [puntosTrucoActuales, setPuntosTrucoActuales] = useState(puntosSinTruco);
    const [tirarTrigger, setTirarTrigger] = useState(0);
    const [trucoTrigger, setTrucoTrigger] = useState(0);
    const [envidoTrigger, setEnvidoTrigger] = useState(0);
    const [posicion, setPosicion] = useFetchState(
        {}, `/api/v1/partidajugador/miposicion/${game.id}`, jwt, setMessage, setVisible
    );

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
            setMano(data);
            let cartasActuales = data.cartasDisp[posicion];
            console.log("-----------------------------------")
            console.log("EL FIN SE ACERCA:"+game.instanteFin)
            console.log("-----------------------------------")

            if (JSON.stringify(cartasJugador) !== JSON.stringify(cartasActuales)) {
                setCartasJugador(cartasActuales);
                setPuntosTrucoActuales(data.puntosTruco);
            }
            
            if (data.envidosCadaJugador) {
                setEnvidosJugadores(data.envidosCadaJugador.map((envido) => envido === null ? 'Son buenas' : envido));
            }

            //NECESARIO PARA FORZAR QUE SE ACTUALICE, SINO SOLO LE APARECE AL DEL QUIERO
            setUltimoMensaje((prevUltimoMensaje) => {
                if (data.ultimoMensaje !== prevUltimoMensaje) {
                    return data.ultimoMensaje;
                }
                return prevUltimoMensaje;
            })
            
            
        })
        .catch((error) => {
            console.error("Error fetching mano:", error);
            setMessage("Error fetching mano.");
            setVisible(true);
        });
    }

    function mostrarMensaje() {
        if(ultimoMensaje=== "LISTA_ENVIDOS"){
            setResolucionEnvido(true)
            const timeoutId = setTimeout(() => setResolucionEnvido(false), 7000);

            return () => clearTimeout(timeoutId);
        } else if(ultimoMensaje!==null ){
            setCantoDicho(true)
            const timeoutId = setTimeout(() => setCantoDicho(false), 5000);

            return () => clearTimeout(timeoutId);
        }
    }

    useEffect(() => {
        mostrarMensaje()
    }, [ultimoMensaje])

    useEffect(() => {
        let intervalId;
        
        mostrarMensaje();
        fetchMano();
        intervalId = setInterval(fetchMano, 1000);
        
        return () => clearInterval(intervalId);
    }, [game.codigo, posicion]);

    useEffect(() => {
        fetchMano();
        
    }, [tirarTrigger, trucoTrigger, envidoTrigger]);

    useEffect(() => {
        if (mano && puntosTrucoActuales) {
            setPuntosTrucoActuales(mano.puntosTruco);
        }
    }, [mano]);

    // Set initial volume when component mounts or volume changes
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
        const minLog = 0.01; // Volumen más bajo (casi mudo)
        const maxLog = 1;    // Volumen más alto (máximo)
        const logVolume = Math.pow(10, (sliderValue / 100) * (Math.log10(maxLog) - Math.log10(minLog)) + Math.log10(minLog));
        setVolume(sliderValue); 
        if (audioRef.current) {
            audioRef.current.volume = logVolume; 
        }
    };

    const renderCartasJugador = () => {
        if (mano && mano.cartasDisp && posicion !== null) {
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
                                    <img
                                        src={carta.foto}
                                        alt={`Carta ${index + 1}`}
                                        className="card-image"
                                        onError={(e) => (e.target.style.display = 'none')}
                                    />
                                    {/* Overlay de resplandor holográfico */}
                                    <div className="sunset-overlay"></div>
                                </div>
                            </div>
                        )
                    ))}
                </div>
            );
        }

        return <div>Cargando cartas...</div>;
    };

    const renderCartasMesa = () => {
        if (mano && mano.cartasLanzadasTotales) {
            const cartasLanzadasTotales = mano.cartasLanzadasTotales;
    
            if (!cartasLanzadasTotales || cartasLanzadasTotales.length === 0) {
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
                                            transform: `translateY(-${rondaIndex * -15}px)`, // Mueve cada carta más arriba según la ronda
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
        }
    
        return <div>Cargando cartas lanzadas...</div>;
    };
    
    


    const dragStart = (evento, carta) => {
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

    return (
        <>
        <div className="playing-modal-container">
            {console.log(mano)}
            {/* Background */}
            <div
                style={{ 
                    backgroundImage: 'url(/fondos/fondoPlayingModal.jpg)',
                    backgroundSize: 'cover', 
                    backgroundRepeat: 'no-repeat', 
                    backgroundPosition: 'center', 
                    height: '19000vh', // Adjusted to cover the viewport
                    width: '100vw',
                    position: 'relative', // To position the dragged card relative to this container
                    zIndex: -1
                }}
            >
                <h3 className="player-heading">
                    Jugador: {Number(posicion)}
                </h3>
            </div>
            <h4 className={"puntos-nuestros"}>Nos:</h4>
            <h4 className={"puntos-ellos"}>Ellos:</h4>
            {mano && puntosTrucoActuales && (
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
            <div
                ref={dropAreaRef}
                className={`drop-area`}
            >
                {/* Visual indicator for drop area (optional) */}
            </div>

            {/* Play Music Button */}
            {!isPlaying && (
                <button onClick={handlePlayMusic} className="play-music-button">
                    <span className="swirl-glow-textMusica" > 🎵 </span>                 
                </button>
            )}

            {/* Volume Slider */}
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

            {/* Player's Cards */}
            {renderCartasJugador()}

            {/* Render the Dragged Card */}
            {draggedCarta && (
                <div
                    className={`dragged-card ${Number(posicion) === mano.jugadorTurno ? 'swirl' : ''}`}
                    style={{ 
                        left: `${positionCarta.x - 50}px`,  // Subtract half the card's width
                        top: `${positionCarta.y - 75}px`,   // Subtract half the card's height
                        opacity: isOverDropArea ? 1 : 0.5,
                        pointerEvents: 'none',
                        position: 'fixed', // Use fixed position
                        zIndex: 1000
                    }}
                >
                    <img
                        src={draggedCarta.foto}
                        alt="Carta arrastrada"
                        className="card-image"
                        onError={(e) => (e.target.style.display = 'none')}
                    />
                    {/* Holographic glow overlay */}
                    <div className="sunset-overlay"></div>
                </div>
            )}

            {/* Table Cards */}
            <div className="cartas-mesa-position">
                {renderCartasMesa()}
            </div>

            {/* Cuadto del envido*/}
            <div  style={{position: 'absolute', left: '52%', top: '30%', transform: 'translateX(-50%)', zIndex: '1000'}}>
            {mano && resolucionEnvido && (
                            <div 
                                className="res-envido-container"
                               
                            >
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
                                    <p style={{ margin: '9px 0' }} key={index}>Jugador {index }: {envido}</p>
                                ))}
                            </div>
                        )}
            </div>
            {/*Cuadro cantos */}
            <div  style={{position: 'absolute', left: '50%', top: '39%', transform: 'translateX(-50%)', zIndex: '1000'}}>
            {mano && cantoDicho && (
                            <div 
                                className="cuadros-canto"
                            >
                                {ultimoMensaje && <h3 style={{ color: "darkorange" }}>{ultimoMensaje.replace("_", " ").replace("2","")}</h3>}
                                
                            </div>
                        )}
            </div>

            {/* Truco Buttons */}
            <div style={{display:'flex', flexDirection: 'row'}}>
                {mano && cartasJugador && Number(posicion) === mano.jugadorTurno && !mano.esperandoRespuesta && mano.puedeCantarTruco && puntosTrucoActuales && (
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
                {mano && cartasJugador && Number(posicion) === mano.jugadorTurno && !mano.esperandoRespuesta && mano.puedeCantarEnvido && (
                    <div className="envido-button-container"> 
                        {mano.queEnvidoPuedeCantar >= 3 && (
                            <button onClick={() => cantarEnvido('ENVIDO')}>
                                <span>Envido</span>
                            </button>
                        )}
                        {mano.queEnvidoPuedeCantar >= 2 && (
                            <button onClick={() => cantarEnvido('REAL_ENVIDO')}>
                                <span>Real Envido</span>
                            </button>
                        )}
                        {mano.queEnvidoPuedeCantar >= 1 && (
                            <button style={{animation:'dropShadowGlowContainer 3s ease-in-out infinite' }} onClick={() => cantarEnvido('FALTA_ENVIDO')}>
                                <span className="swirl-glow-text">Falta Envido</span>
                            </button>
                        )}
                    </div>
                )}
            </div>

            {/* Responder Truco Buttons */}
            {mano && cartasJugador && Number(posicion) === mano.jugadorTurno && mano.esperandoRespuesta && puntosTrucoActuales && mano.esTrucoEnvidoFlor === 0 && (
                <div className="truco-button-container responder-truco-buttons"> 
                    {puntosTrucoActuales !== puntosConRetruco && 
                        <button onClick={() => responderTruco("QUIERO")}>Quiero</button>}
                    <button onClick={() => responderTruco("NO_QUIERO")}>No quiero</button>
                    {puntosTrucoActuales === puntosConRetruco && 
                        <button style={{ animation: 'dropShadowGlowContainer 3s ease-in-out infinite' }} 
                            onClick={() => responderTruco("QUIERO")}>
                            <span className="swirl-glow-text"> ¡Quiero!</span>
                        </button>}
                    {puntosTrucoActuales !== puntosConRetruco && mano.puedeCantarTruco && 
                        <button
                            style={{ animation: 'dropShadowGlowContainer 3s ease-in-out infinite' }}
                            onClick={() => responderTruco('SUBIR')}
                        >
                            <span className="swirl-glow-text">
                                {puntosTrucoActuales === puntosSinTruco ? '¡Retruco!' : '¡Vale Cuatro!'}
                            </span>
                        </button>}
                    {mano.puedeCantarEnvido && (  
                        <div className='envido-button-container' style={{top:'-120%', position:'absolute', left:'60%'}}>
                            {mano.queEnvidoPuedeCantar >= 3 && mano.puedeCantarEnvido && (
                                <button onClick={() => responderEnvido('ENVIDO')}>
                                    <span>Envido</span>
                                </button>
                            )}
                            {mano.queEnvidoPuedeCantar >= 2 && mano.puedeCantarEnvido && (
                                <button onClick={() => responderEnvido('REAL_ENVIDO')}>
                                    <span>Real Envido</span>
                                </button>
                            )}
                            {mano.queEnvidoPuedeCantar >= 1 && mano.puedeCantarEnvido && (
                                <button style={{animation:'dropShadowGlowContainer 3s ease-in-out infinite'}} onClick={() => responderEnvido('FALTA_ENVIDO')}>
                                    <span className="swirl-glow-text">Falta Envido</span>
                                </button>
                            )}
                        </div>
                    )}
                </div>
            )}
            
            {/* Responder Envido */}
            {mano && cartasJugador && Number(posicion) === mano.jugadorTurno && mano.esperandoRespuesta && mano.esTrucoEnvidoFlor === 1 && (
                <div className="envido-button-container" style={{left:'70%', position:'fixed'}}> 
                    <button onClick={() => responderEnvido("QUIERO")}>Quiero</button>
                    <button onClick={() => responderEnvido("NO_QUIERO")}>No quiero</button>
                    {mano.queEnvidoPuedeCantar >= 3 && mano.puedeCantarEnvido && (
                        <button onClick={() => responderEnvido('ENVIDO')}>
                            <span>Envido</span>
                        </button>
                    )}
                    {mano.queEnvidoPuedeCantar >= 2 && mano.puedeCantarEnvido && (
                        <button onClick={() => responderEnvido('REAL_ENVIDO')}>
                            <span>Real Envido</span>
                        </button>
                    )}
                    {mano.queEnvidoPuedeCantar >= 1 && mano.puedeCantarEnvido && (
                        <button style={{animation:'dropShadowGlowContainer 3s ease-in-out infinite'}} onClick={() => responderEnvido('FALTA_ENVIDO')}>
                            <span className="swirl-glow-text">Falta Envido</span>
                        </button>
                    )}
                </div>
            )}

            {/* Cartas Volteadas */}
            {mano && (
                <CartasVolteadas
                    cartasDispo={mano.cartasDisp}
                    posicionListaCartas={posicion}
                    jugadorMano={game.jugadorMano}
                />
            )}
            
                           
        </div>
        <div style={{zIndex:100000000000000}}>
        <Perfil game={game} posicion={posicion}/>
        </div>
        </>
    );

});

export default PlayingModal;
