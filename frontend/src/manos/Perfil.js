import useFetchState from "../util/useFetchState";
import React, { useState, forwardRef } from 'react';
import tokenService from "../services/token.service";
import "../static/css/mano/perfil.css";
import { useEffect } from "react";
import { Client } from "@stomp/stompjs";
import { MdEmojiEmotions } from "react-icons/md";
import GestosMenu from "./GestosMenu";

const jwt = tokenService.getLocalAccessToken();



const renderJugador = (persona, index) => {
    return (
        <div
            style={{
                position: 'relative',
                width: 80,
                height: 80,
                display: 'inline-block',
                textAlign: 'center'
            }}
            key={index}
        >
            <p className="swirl-glow-textFoto">
                {persona.userName}
            </p>
            <img
                style={{
                    height: 80,
                    width: 80,
                    borderRadius: 500,
                    objectFit: 'cover',
                    margin: 40,
                    left: '0%',
                    position: 'absolute',
                }}
                src={persona.foto}
                alt={`Foto perfil jugador ${index}`}
                onError={(e) => (e.target.style.display = 'none')}
            />
        </div>
    );
};

// Componente para cuando `posicionJugador === null`, con distribución fija
const DistribuirJugadoresFijos = ({ perfil, game }) => {
    
    return perfil.map((persona, index) => {
        let posicion= persona.posicion;
        let playerStyle = {};
        if (game.numJugadores === 2) {
            if (index === 0) {
                playerStyle = {
                    position: 'absolute',
                    top: '50%',
                    right: '10%', // A la derecha
                    transform: 'translateY(-50%)',
                    zIndex: 1000
                };
            } else {
                playerStyle = {
                    position: 'absolute',
                    top: '50%',
                    left: '10%',  // A la izquierda
                    transform: 'translateY(-50%)',
                    zIndex: 1000
                };
            }


        } else if (game.numJugadores === 4){
            if (index === 2) {
                playerStyle = {
                    position: 'absolute',
                    top: '7%',  // Arriba
                    left: '50%',
                    transform: 'translateX(-50%)',
                    zIndex: 1000
                };
            } else if (index === 3) {
                playerStyle = {
                    position: 'absolute',
                    top: '50%',
                    left: '10%',  // A la izquierda
                    transform: 'translateY(-50%)',
                    zIndex: 1000
                };
            } else if (index === 0) {
                playerStyle = {
                    position: 'absolute',
                    bottom: '7%', // Abajo
                    left: '50%',
                    transform: 'translateY(-50%)',
                    zIndex: 1000
                };
            } else if (index === 1) {
                playerStyle = {
                    position: 'absolute',
                    top: '50%',
                    right: '10%',  // A la derecha
                    transform: 'translateX(-50%)',
                    zIndex: 1000
                };
            }
        } else if (game.numJugadores === 6) { 
            if (posicion === 0) { 
                playerStyle = { 
                position: 'absolute', 
                bottom: '7%', // Abajo 
                left: '39%', 
                transform: 'translateX(-50%)', 
                zIndex: 1000 }; 
            } else if (posicion === 1) { 
                playerStyle = { 
                    position: 'absolute', 
                    bottom: '25%', // Arriba 
                    left: '83%', // Esquina inferior izquierda
                    transform: 'translateX(-50%)', 
                    zIndex: 1000 }; 
            } else if (posicion === 2) { 
                playerStyle = { 
                    position: 'absolute', 
                    top: '25%', // Arriba 
                    left: '85%', // Esquina superior derecha 
                    transform: 'translateX(-50%)', 
                    zIndex: 1000 }; 
            } else if (posicion === 3) { 
                    playerStyle = { position: 'absolute', 
                    top: '7%', 
                    left: '62%', // Esquina inferior izquierda 
                    transform: 'translateY(-50%)', 
                    zIndex: 1000 }; 
            } else if (posicion === 4) { 
                playerStyle = { 
                    position: 'absolute', 
                    top: '25%', 
                    left: '5%', // Esquina inferior derecha 
                    transform: 'translateY(-50%)', 
                    zIndex: 1000 }; 
            } else if (posicion === 5) { 
                playerStyle = { 
                    position: 'absolute', 
                    bottom: '25%', // Arriba 
                    left: '10%', // Enfrente 
                    transform: 'translateX(-50%)', 
                    zIndex: 1000 }; 
            } 
        }

        return (
            <div key={index} style={playerStyle}>
                {renderJugador(persona, index)}
            </div>
        );
    });
};

// Componente para cuando `posicionJugador !== null`, con distribución dinámica
const DistribuirJugadoresDinamicos = ({ perfil, game, posicionJugador }) => {
    let posicionUsada = false;
    let posicionUsadaInferior = false;

    return perfil.map((persona, index) => {
        let posicionJug = persona.posicion;
        
        // Evitar que el jugador con la misma posición que el actual se asignen a sí mismo
        if (posicionJug !== posicionJugador) {
            let playerStyle = {};

            // Para partidas de 2 jugadores
            if (game.numJugadores === 2) {
                if (posicionJugador === 0) {
                    if (index % 2 !== posicionJugador % 2) {
                        playerStyle = {
                            position: 'absolute',
                            top: '50%',
                            right: '10%', // A la derecha
                            transform: 'translateY(-50%)',
                            zIndex: 1000
                        };
                    }
                } else if (posicionJugador === 1) {
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
            if (game.numJugadores === 4) {
                if (index % 2 === posicionJugador % 2) {
                    playerStyle = {
                        position: 'absolute',
                        top: '7%', // Jugador de tu equipo, arriba
                        left: '60%', // Centrado horizontalmente
                        transform: 'translateX(-50%)',
                        zIndex: 1000
                    };
                } else if (!posicionUsada) {
                    posicionUsada = true;
                    playerStyle = {
                        position: 'absolute',
                        top: '45%',
                        left: '5%',  // A la izquierda
                        transform: 'translateY(-50%)',
                        zIndex: 1000
                    };
                } else {
                    playerStyle = {
                        position: 'absolute',
                        top: '42%',
                        right: '10%',  // A la derecha
                        transform: 'translateY(-50%)',
                        zIndex: 1000
                    };
                }
            }

            // Para partidas de 6 jugadores
            if (game.numJugadores === 6) {
                if (posicionJug % 2 === posicionJugador % 2) {
                    if (posicionJug === obtenerPosicionEquipo(posicionJugador, 2, 6)) {
                        playerStyle = {
                            position: 'absolute',
                            top: '20%',
                            left: '85%',
                            zIndex: 1000
                        };
                    } else if (posicionJug === obtenerPosicionEquipo(posicionJugador, 4, 6)) {
                        playerStyle = {
                            position: 'absolute',
                            top: '20%',
                            left: '2%',
                            zIndex: 1000
                        };
                    }
                } else {
                    if (!posicionUsadaInferior) {
                        posicionUsadaInferior = true;
                        playerStyle = {
                            position: 'absolute',
                            top: '-2%',
                            left: '81%',
                            transform: 'translate(50%, 600%)',
                            zIndex: 1000
                        };
                    } else if (!posicionUsada) {
                        posicionUsada = true;
                        playerStyle = {
                            position: 'absolute',
                            top: '-70%',
                            right: '40%',
                            transform: 'translate(50%, 600%)',
                            zIndex: 1000
                        };
                    } else {
                        playerStyle = {
                            position: 'absolute',
                            top: '65%',
                            left: '5%',
                            zIndex: 1000
                        };
                    }
                }
            }

            return (
                <div key={index} style={playerStyle}>
                    {renderJugador(persona, index)}
                </div>
            );
        }
        return null;
    });
};


function obtenerPosicionEquipo(posicion, incremento, longitud) { 
    return (posicion + incremento) % longitud; 
}


// Componente principal de Perfil
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



    /*Código para los gestos*/
    const [gestosVisible, setGestosVisible] = useState(false);
    const [stompClient, setStompClient] = useState(null);
    const [imagenOriginal, setImagenOriginal] = useState(null);
    const toggleGestos = () => {
        setGestosVisible(!gestosVisible);
    };

    useEffect(() => {
        const cliente = new Client({
            brokerURL: "ws://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/ws",
            connectHeaders: {
                Authorization: `Bearer ${jwt}`,
            },
        });

        cliente.onConnect = () => {
            console.log("Conectado exitosamente");
            cliente.subscribe(`/topic/partida/${props.game.id}`, (partJugador) => {
                const partJugadorParse = JSON.parse(partJugador.body);
                setPerfil(perfil.map(partJugador => partJugador.userName === partJugadorParse.userName ? partJugadorParse : partJugador));
                console.log("Gesto recibido");
                console.log(partJugadorParse);
            });
        };

        cliente.onDisconnect = () => {
            console.log("Desconectado del servidor STOMP");
        };

        cliente.onStompError = (frame) => {
            console.error("Error de STOMP: ", frame.headers["message"]);
            console.error("Detalles: ", frame.body);
        };

        cliente.activate();
        setStompClient(cliente);

        return () => {
            if (cliente) {
                cliente.deactivate();
            }
        };
    }, [perfil, props.game.id, setPerfil]);

    const enviarGesto = (gestoUrl) => {
        if (!imagenOriginal) {
            setImagenOriginal(perfil[posicionJugador].foto);
        }
        const partJugador = perfil[posicionJugador];
        partJugador.foto = gestoUrl;
        console.log(partJugador);

        if (stompClient && stompClient.connected && partJugador) {
            stompClient.publish({
                destination: "/app/partjugador",
                body: JSON.stringify(partJugador),
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            });
            console.log("Gesto enviado");
        } else {
            console.error("STOMP aún no está listo o no está conectado");
        }
    };



    const gestosDisp = [
        { nombre: "1 Espada", photo: "/gestos/unoespada.png", codigo: "UNOESPADA" },
        { nombre: "1 Basto", photo: "/gestos/unobasto.png", codigo: "UNOBASTO" },
        { nombre: "7 Espada", photo: "/gestos/sieteespada.png", codigo: "SIETEESPADA" },
        { nombre: "7 Oro", photo: "/gestos/sieteoro.png", codigo: "SIETEORO" },
        { nombre: "Los 3", photo: "/gestos/tres.png", codigo: "TRES" },
        { nombre: "Los 2", photo: "/gestos/dos.png", codigo: "DOS" },
        { nombre: "1 Copa o 1 Oro", photo: "/gestos/unocopaoro.png", codigo: "UNOCOPAORO" },
        { nombre: "Son malas", photo: "/gestos/malas.png", codigo: "MALAS" },
        { nombre: "Corazón", photo: "/gestos/corazon.png", codigo: "CORAZON" },
        { nombre: "Calavera", photo: "/gestos/calavera.gif", codigo: "CALAVERA" },
        { nombre: "Tiempo", photo: "/gestos/tiempo.png", codigo: "TIEMPO" },
        { nombre: "Estrella", photo: "/gestos/estrella.png", codigo: "ESTRELLA" }
    ];



    /*Fin del código para los gestos*/

    return (
        <>
            {posicionJugador < 6 ? (
                <DistribuirJugadoresDinamicos
                    perfil={perfil}
                    game={game}
                    posicionJugador={posicionJugador}
                />

            ) : (
                <DistribuirJugadoresFijos perfil={perfil} game={game} />
            )}
            <MdEmojiEmotions
                style={{
                    fontSize: '50px',
                    color: 'yellow',
                    position: 'fixed',
                    bottom: '5',
                    left: '5',
                    cursor: 'pointer'
                }}
                onClick={toggleGestos} 
            />
                {perfil[posicionJugador] && gestosVisible && (
                    <img
                        src={perfil[posicionJugador].foto}
                        alt="Foto del usuario"
                        style={{
                            width: '50px',
                            height: '50px',
                            borderRadius: '50%',
                            marginLeft: '10px',
                            position: 'fixed',
                            bottom: '5px',
                            left: '60px',
                            zIndex: 1000

                        }}
                    />

                )}
            {gestosVisible && (
                <div style={{
                    position: 'fixed',
                    bottom: '60px',
                    left: '5px',
                    zIndex: 1000
                }}>
                    <GestosMenu
                        partJugador={perfil[posicionJugador]}
                        enviarGesto={enviarGesto}
                        gestosDisp={gestosDisp}
                        imagenOriginal={imagenOriginal}
                    />
                </div>
            )}

        </>
    );
});

export default Perfil;
