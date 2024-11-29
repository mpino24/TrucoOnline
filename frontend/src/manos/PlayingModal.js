// PlayingModal.jsx

import { useState, forwardRef, useEffect } from 'react';
import tokenService from "frontend/src/services/token.service.js";
import useFetchState from "../util/useFetchState";
import CartasVolteadas from './CartasVolteadas';
import './PlayingModal.css'; // Import the CSS file

const jwt = tokenService.getLocalAccessToken();

const PlayingModal = forwardRef((props, ref) => {
    const game = props.game;
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [cartasJugador, setCartasJugador] = useState([]);
    const [mano, setMano] = useState(null);
    const puntosSinTruco = 1;
    const puntosConTruco = 2;
    const puntosConRetruco = 3;
    const [puntosTrucoActuales, setPuntosTrucoActuales] = useState(puntosSinTruco);
    const [draggedCarta, setDraggedCarta] = useState(null);
    const [positionCarta, setPositionCarta] = useState({ x: 0, y: 0 });

    const [tirarTrigger, setTirarTrigger] = useState(0);
    const [trucoTrigger, setTrucoTrigger] = useState(0);

    const [posicion, setPosicion] = useFetchState(
        {}, `/api/v1/partidajugador/miposicion/${game.id}`, jwt, setMessage, setVisible
    );

    function fetchMano() {
        fetch(
            '/api/v1/manos/' + game.codigo,
            {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            }
        )
            .then((response) => response.json())
            .then((data) => {
                setMano(data);
                let cartasActuales = data.cartasDisp[posicion];
                if (cartasJugador !== cartasActuales) {
                    setCartasJugador(cartasActuales);
                    setPuntosTrucoActuales(data.puntosTruco); // Corrected to use data
                }
            })
            .catch((error) => {
                console.error("Error fetching mano:", error);
                setMessage("Error fetching mano.");
                setVisible(true);
            });
    }

    useEffect(() => {
        let intervalId;
        fetchMano();
        intervalId = setInterval(fetchMano, 1000);
        return () => clearInterval(intervalId);
    }, [game.codigo, posicion]);

    useEffect(() => {
        fetchMano();
    }, [tirarTrigger]);

    useEffect(() => {
        fetchMano();
    }, [trucoTrigger]);

    useEffect(() => {
        if (mano && puntosTrucoActuales) {
            setPuntosTrucoActuales(mano.puntosTruco);
        }
    }, [mano]);

    const renderCartasJugador = () => {
        if (mano && mano.cartasDisp && posicion !== null) {
            const cartasJugador = mano.cartasDisp[posicion];
            const esTurnoValido =
                mano &&
                cartasJugador &&
                Number(posicion) === mano.jugadorTurno &&
                !mano.esperandoRespuesta;

            if (!cartasJugador) {
                return <div>No hay cartas para mostrar.</div>;
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
                                        draggable 
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
        if (mano && mano.cartasLanzadasRonda) {
            const cartasLanzadas = mano.cartasLanzadasRonda;

            if (!cartasLanzadas || cartasLanzadas.length === 0) {
                return <div>No hay cartas para mostrar.</div>;
            }

            return (
                <div className="cartas-mesa-container">
                    {cartasLanzadas.map((carta, index) => (
                        carta && (
                            <div key={index} className="card-container mesa">
                                <img
                                    src={carta.foto}
                                    alt={`Carta ${index + 1}`}
                                    className="card-image"
                                    onError={(e) => (e.target.style.display = 'none')}
                                />
                                <div className="sunset-overlay"></div>
                            </div>
                        )
                    ))}
                </div>
            );
        }
        
        return <div>Cargando cartas lanzadas...</div>;
    }; 

    const dragStart = (evento, carta) => {
        if(mano  && cartasJugador && Number(posicion) === mano.jugadorTurno){
            setDraggedCarta(carta);
            evento.dataTransfer.effectAllowed = 'move';
        }
    }

    const onDrag = (evento) => { 
        setPositionCarta({ x: evento.clientX, y: evento.clientY });
    }

    const onDragEnd = (evento, carta) => { 
        setPositionCarta({ x: evento.clientX, y: evento.clientY }); 
        tirarCarta(carta.id); 
        setDraggedCarta(null);  
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
            if(data){
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
            if(data){
                console.log(data)
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
            if(data){
                console.log(data)
                setTrucoTrigger((prev) => prev + 1);
            }
        })
        .catch((error) => alert(error.message));
    }

    return (
        <>
        <div style={{ 
            backgroundImage: 'url(/fondos/fondoPlayingModal.jpg)',
            backgroundSize: 'cover', 
            backgroundRepeat: 'no-repeat', 
            backgroundPosition: 'center', 
            height: '100vh', 
            width: '100vw',
            position: 'relative', // To position the dragged card relative to this container
            zIndex:-1
        }}>
             <h3 className="player-heading">
                    Jugador: {Number(posicion)}
                </h3>
            </div>
           
            <div>
            <div>
                
                {renderCartasJugador()} 
                
                {/* Render the dragged card */}
                {draggedCarta && 
                    <div
                        className={`dragged-card ${mano && cartasJugador && Number(posicion) === mano.jugadorTurno ? 'swirl' : ''}`}
                        style={{ left: `${positionCarta.x}px`, top: `${positionCarta.y}px` }}
                    >
                        <img
                            src={draggedCarta.foto}
                            alt='Carta arrastrada'
                            className="card-image"
                            onError={(e) => (e.target.style.display = 'none')}
                        />
                        {/* Overlay de resplandor holográfico */}
                        <div className="sunset-overlay"></div>
                    </div>
                } 

                <div className="cartas-mesa-position">
                    {renderCartasMesa()}
                </div> 
            </div>
            
            {/* Truco Buttons */}
            {mano && cartasJugador && Number(posicion) === mano.jugadorTurno && !mano.esperandoRespuesta && mano.puedeCantarTruco && puntosTrucoActuales &&
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
            }

            {/* Responder Truco Buttons */}
            {mano && cartasJugador && Number(posicion) === mano.jugadorTurno && mano.esperandoRespuesta && puntosTrucoActuales &&
            <div>
                <div className="truco-button-container responder-truco-buttons"> 
                    <button onClick={() => responderTruco("QUIERO")} >Quiero</button>
                    <button onClick={() => responderTruco("NO_QUIERO")} >No quiero</button>
                    <button style={{animation: 'dropShadowGlowContainer 3s ease-in-out infinite'}}
                     onClick={() => responderTruco('SUBIR')}>
                        <span className="swirl-glow-text">
                            {puntosTrucoActuales === puntosSinTruco
                                ? '¡Retruco!'
                                : '¡Vale Cuatro!'}
                        </span>
                    </button>
                   </div> 
                </div>
            }

            <div>
                {mano && <CartasVolteadas cartasDispo={mano.cartasDisp} posicionListaCartas={posicion} jugadorMano={game.jugadorMano}  />}
            </div>
            {mano && console.log(mano)}
            {console.log(posicion)}
        </div>
        </>
    )
});

export default PlayingModal;
