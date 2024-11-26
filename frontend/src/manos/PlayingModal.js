import { useState, forwardRef, useEffect, useRef } from 'react';
import tokenService from "frontend/src/services/token.service.js";
import useFetchState from "../util/useFetchState";
import CartasVolteadas from './CartasVolteadas';
const jwt = tokenService.getLocalAccessToken();

const PlayingModal = forwardRef((props, ref) => {
    const game = props.game
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [cartasJugador,setCartasJugador] = useState([])
    const [mano, setMano] = useState(null)


    const [draggedCarta, setDraggedCarta] = useState(null);
    const [positionCarta, setPositionCarta] = useState({ x: 0, y: 0 });

    const [tirarTrigger, setTirarTrigger] = useState(0);
    const [trucoTrigger, setTrucoTrigger] = useState(0);

    const [posicion, setPosicion] = useFetchState(
        {}, `/api/v1/partidajugador/miposicion/${game.id}`, jwt, setMessage, setVisible
      );
     function fetchMano() {
            
        fetch(
            '/api/v1/manos/'+game.codigo,
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
                let cartasActuales = data.cartasDisp[posicion]
                if(cartasJugador!==cartasActuales){
                    setCartasJugador(cartasActuales)
                }
            })
    }
    
    useEffect(() => {
        let intervalId;

        
        fetchMano();

        intervalId = setInterval(fetchMano, 1000);
        
        return () => clearInterval(intervalId)
    },[game.codigo, posicion])

    useEffect(() => {
        fetchMano();
    }, [tirarTrigger]);

    useEffect(() => {
        fetchMano();
    }, [trucoTrigger]);

    useEffect(() => {
        const styleSheet = document.createElement('style');
        styleSheet.type = 'text/css';
        styleSheet.innerText = `
          @keyframes holoGlow {
            0%, 100% { opacity: 1; }
            50% { opacity: 0.8; }
          }

          @keyframes slightSwirl {
            0% { transform: rotate(0deg); }
            25% { transform: rotate(2deg); }
            50% { transform: rotate(0deg); }
            75% { transform: rotate(-2deg); }
            100% { transform: rotate(0deg); }
          }
        `;
        document.head.appendChild(styleSheet);

        // Cleanup on unmount
        return () => {
            document.head.removeChild(styleSheet);
        };
    }, []);

    // Define the sunset-like overlay style
    const sunsetOverlay = {
        position: 'absolute',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        background: `
          radial-gradient(
            circle at 50% 50%, 
            rgba(255, 223, 186, 0.5) 0%, 
            rgba(255, 183, 76, 0.5) 40%, 
            rgba(255, 140, 0, 0.6) 70%
          )
        `,
        backgroundSize: '400% 400%',
        animation: 'holoGlow 3s ease-in-out infinite',
        mixBlendMode: 'overlay', // Blend mode to create a subtle glow
        //mix-blend-mode: normal | multiply | screen | overlay | darken | lighten 
        //| color-dodge | color-burn | hard-light | soft-light 
        //| difference | exclusion | hue | saturation | color |
        // luminosity | plus-darker | plus-lighter;
        pointerEvents: 'none', // Ensure overlay doesn't block interactions
        borderRadius: '8px', // Optional: match card's border radius if any
    };

    // Swirl animation style
    const swirlStyle = {
        animation: 'slightSwirl 5s ease-in-out infinite',
    };

    // Estilo base de las cartas
    const baseCardStyle = {
        width: '100px',
        height: '150px',
        backgroundColor: '#fff',
        border: '1px solid #ccc',
        boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.3)',
        transformOrigin: 'center',
        transition: 'transform 0.3s ease, box-shadow 0.3s ease, filter 0.3s ease',
        cursor: 'pointer',
        filter: 'none', // We'll handle filter based on esTurnoValido
        borderRadius: '8px', // Asegura que coincide con el overlay
        position: 'relative', // Para posicionar el overlay
    };

    // Estilo adicional al pasar el ratón
    const cardHoverStyle = {
        transform: 'rotateY(10deg)',
        boxShadow: '0px 8px 16px rgba(0, 0, 0, 0.5)',
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
                return <div>No hay cartas para mostrar.</div>;
            }

            // Estilo del contenedor con efecto 3D
            const containerStyle = {
                position: 'fixed',
                top: '80%',
                left: '50%',
                transform: 'translateX(-50%) rotateX(25deg)',
                display: 'flex',
                gap: '10px',
                perspective: '800px',
                transformOrigin: 'center bottom',
            };

            return (
                <div style={containerStyle}>
                    {cartasJugador.map((carta, index) => (
                        carta && (
                            <div
                                    key={index}
                                    style={{
                                        ...(esTurnoValido ? swirlStyle : {}),
                                        position: 'relative',
                                        width: '100px',
                                        height: '150px',
                                    }}
                                >
                                <div
                                    style={{
                                        ...baseCardStyle,
                                        transform: 'rotateY(-10deg)', // Rotación estática original
                                        cursor: esTurnoValido ? 'pointer' : 'not-allowed',
                                        filter: esTurnoValido ? 'none' : 'grayscale(80%) brightness(0.5)',
                                        // Aplicar hover styles mediante eventos
                                    }}
                                    onMouseEnter={(e) =>
                                        Object.assign(
                                            e.currentTarget.style,
                                            cardHoverStyle
                                        )
                                    }
                                    onMouseLeave={(e) =>
                                        Object.assign(
                                            e.currentTarget.style,
                                            {
                                                transform: 'rotateY(-10deg)', // Restaurar rotación original
                                                boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.3)',
                                            }
                                        )
                                    }
                                    onClick={() =>
                                        esTurnoValido && tirarCarta(carta.id)
                                    } // Desactiva clic si no es el turno
                                >
                                    <img
                                        src={carta.foto}
                                        alt={`Carta ${index + 1}`}
                                        style={{
                                            width: '100%',
                                            height: '100%',
                                            display: 'block',
                                            borderRadius: '8px', // Asegura que coincide con el overlay
                                        }}
                                        onError={(e) =>
                                            (e.target.style.display = 'none')
                                        }

                                    />
                                    {/* Overlay de resplandor holográfico */}
                                    <div style={sunsetOverlay}></div>
                                </div>
                            </div>
                        )
                    ))}
                </div>
            );
            return cartasJugador.map((carta, index) => (<>
                {carta &&<img
                    key={index}
                    src={carta.foto} 
                    alt={`Carta ${index + 1}`}
                    style={{ width: '50px', height: '75px', margin: '5px' }} 
                    onError={(e) => (e.target.style.display = 'none')} 
                    draggable 
                    onDragStart={(evento) => dragStart(evento, carta)} 
                    onDrag={(evento) => onDrag(evento)} 
                    onDragEnd={(evento) => onDragEnd(evento)} 
                />}
                </>
            ));
        }

        return <div>Cargando cartas...</div>;
    };

    
    
    
    const renderCartasMesa = () => {
        if (mano && mano.cartasLanzadasRonda) {
            const cartasLanzadas = mano.cartasLanzadasRonda;
    
            if (!cartasLanzadas || cartasLanzadas.length === 0) {
                return <div>No hay cartas para mostrar.</div>;
            }
    
            // Lo mismo que renderizar las cartas del jugador
            const containerStyle = {
                position: 'fixed',
                top: '39%',
                left: '49%',
                transform: 'translateX(-50%)',
                display: 'flex',
                gap: '10px' ,
                
                
            };
    
            const itemStyle = {
                width: '83px',
                height: '120px'
            };
    
            return (
                <div style={containerStyle}>
                    {cartasLanzadas.map((carta, index) => (
                        carta && (
                            <img
                                key={index}
                                src={carta.foto}
                                alt={`Carta ${index + 1}`}
                                style={itemStyle}
                                onError={(e) => (e.target.style.display = 'none')}
                            />
                        )
                    ))}
                </div>
            );
        }
    
        return <div>Cargando cartas lanzadas...</div>;
    };
    

    const dragStart = (evento, carta) => {
        if(mano  &&cartasJugador && Number(posicion) === mano.jugadorTurno){
        setDraggedCarta(carta);
        tirarCarta(carta.id)
        evento.dataTransfer.effectAllowed ='move';}
        
    }
    const onDrag = (evento) => { setPositionCarta({ x: evento.clientX, y: evento.clientY })};
    const onDragEnd = (evento) => { setPositionCarta({ x: evento.clientX, y: evento.clientY }) };

    const handleDrop = (evento) => {
        evento.preventDefault();
        // Aquí puedes manejar el caso de soltar la carta
        const offsetX = evento.clientX;
        const offsetY = evento.clientY;
        setPositionCarta({ x: offsetX, y: offsetY });
        
    };
    const allowDrop = (evento) => {
        evento.preventDefault();
    };

    


     function tirarCarta(cartaId) {
        
            fetch(`/api/v1/manos/${game.codigo}/tirarCarta/${cartaId}`, {
                method: "PATCH",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
            }).then((response) => response.text())
            .then((data) => {
                if(data){
                    console.log(data)
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
        }).then((response) => response.text())
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
        }).then((response) => response.text())
        .then((data) => {
            if(data){
                console.log(data)
                setTrucoTrigger((prev) => prev + 1);
            }
        })
        .catch((error) => alert(error.message));
    }

    return (<div style={{ backgroundImage: 'url(/fondos/fondoPlayingModal.jpg)',backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100vh', width: '100vw'}}>
            <div /* onDrop={handleDrop} onDragOver={allowDrop} */>
                <h3>Jugador: {Number(posicion)}</h3>

                {renderCartasJugador()} 
                {draggedCarta && 
                  <img
                  src={draggedCarta.foto} 
                  alt='Carta arrastrada'
                  style={{ position: 'absolute', left: `${positionCarta.x}px`, top: `${positionCarta.y}px`, width: '50px', height: '75px' }}
                  onError={(e) => (e.target.style.display = 'none')}
                  />  }
            </div>


            <div style={{left: "20px", position: "absolute"}}>
                {renderCartasMesa()}
            </div>
            {mano && cartasJugador && Number(posicion) === mano.jugadorTurno && !mano.esperandoRespuesta ? (
                <div
                    style={{
                        width: '150px',
                        height: '75px',
                        margin: '5px',
                        left: '70%',
                        position: 'fixed',
                        transform: 'translateX(-50%)',
                        top: '70%',
                    }}
                >
                    <h3>Es tu turno</h3>
                </div>
                ) : null}

            {mano  &&cartasJugador && Number(posicion) === mano.jugadorTurno &&
            <div style={{ width: '150px', height: '75px', margin: '5px', left: "80%", position: "fixed",transform: 'translateX(-50%)', top: "70%"}}> 
                {<button onClick={()=> cantarTruco("TRUCO")} >Cantar TRUCO</button>}
                {<button onClick={()=> cantarTruco("RETRUCO")} >Cantar RETRUCO</button>}
                {<button onClick={()=> cantarTruco("VALECUATRO")} >Cantar VALECUATRO</button>}
            </div>}
            {mano  &&cartasJugador && Number(posicion) === mano.jugadorTurno && mano.esperandoRespuesta &&
            <div style={{ width: '150px', height: '75px', margin: '5px', left: "80%", position: "fixed",transform: 'translateX(-50%)', top: "70%"}}> 
                {<button onClick={()=> responderTruco("QUIERO")} >Quiero</button>}
                {<button onClick={()=> responderTruco("NO_QUIERO")} >No quiero</button>}
                {<button onClick={()=> responderTruco("SUBIR")} >Subir la apuesta</button>}
            </div>}

             <div>
                {mano && <CartasVolteadas cartasDispo={mano.cartasDisp} posicionListaCartas={posicion} />}
            </div>
        {mano && console.log(mano)}
        
        {console.log(posicion)}
        </div>
    )
        
    

});
export default PlayingModal;