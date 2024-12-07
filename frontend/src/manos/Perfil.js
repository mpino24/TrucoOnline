import useFetchState from "../util/useFetchState";
import React, { useState, forwardRef } from 'react';
import tokenService from "../services/token.service";

const jwt = tokenService.getLocalAccessToken();

const Perfil = forwardRef((props, ref) => { 
    const game = props.game; 
    const posicionJugador = props.posicion; 
    const [message, setMessage] = useState(null); 
    const [visible, setVisible] = useState(false); 
    const [perfil, setPerfil] = useFetchState( 
        [], 
        `/api/v1/partidajugador/jugadores/codigoPartida/${game.codigo}`, 
        jwt, 
        setMessage, 
        setVisible
    ); 

    console.log(perfil);

    const renderJugador = (persona, index) => { 
        return ( 
            <div key={index}> 
                <p style={{color:'red'}}>{persona.userName}</p> 
                <img 
                    style={{ height: 60, width: 60, borderRadius: 500 }} 
                    src={persona.foto} 
                    alt={`Foto perfil jugador ${index}`} 
                    onError={(e) => (e.target.style.display = 'none')} 
                /> 
            </div> 
        ); 
    };

    let posicionUsada = false;

    return ( 
        <> 
            {perfil.map((persona, index) => { 
                if (index !== posicionJugador) { 
                    let playerStyle = {};

                    // Para partidas de 2 jugadores
                    if (game.numJugadores === 2) {
                        if (posicionJugador === 0) {
                            // Si el jugador actual está en la posición 0, el contrario va a la derecha
                            if (index % 2 !== posicionJugador % 2) {
                                playerStyle = { 
                                    position: 'absolute', 
                                    top: '50%', 
                                    right: '10%', // A la derecha
                                    transform: 'translateY(-50%)',
                                    zIndex: 1000 
                                };
                            }
                        } else {
                            // Si el jugador actual está en la posición 1, el contrario va a la izquierda
                            if (index % 2 !== posicionJugador % 2) {
                                playerStyle = { 
                                    position: 'absolute', 
                                    top: '50%', 
                                    left: '10%',  // A la izquierda
                                    transform: 'translateY(-50%)',
                                    zIndex: 1000 
                                };
                            }
                        }
                    }

                    // Para partidas de 4 jugadores
                    else if (game.numJugadores === 4) {
                        if (index % 2 === posicionJugador % 2) {
                            // Si el jugador es de tu equipo, se coloca arriba
                            playerStyle = { 
                                position: 'absolute', 
                                top: '7%', // Jugador de tu equipo, arriba
                                left: '65%', // Centrado horizontalmente
                                transform: 'translateX(-50%)', // Asegurarse de que esté centrado
                                zIndex: 1000 
                            };
                        } else if (!posicionUsada) { 
                            // El primer jugador contrario va a la izquierda
                            posicionUsada = true;
                            playerStyle = { 
                                position: 'absolute',
                                top: '50%',
                                left: '10%',  // A la izquierda
                                transform: 'translateY(-50%)',
                                zIndex: 1000 
                            };
                        } else { 
                            // El segundo jugador contrario va a la derecha
                            playerStyle = { 
                                position: 'absolute', 
                                top: '50%', 
                                right: '10%',  // A la derecha
                                transform: 'translateY(-50%)',
                                zIndex: 1000 
                            };
                        }
                    }

                    return (
                        <div key={index} style={playerStyle}>
                            {renderJugador(persona, index)}
                        </div>
                    );
                } 
                return null; 
            })} 
        </> 
    );
});

export default Perfil;
