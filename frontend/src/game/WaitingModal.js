import { useState, forwardRef, useEffect } from 'react';
import { IoCloseCircle } from "react-icons/io5";

import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import EquipoView from '../game/EquipoView.js'
import LeavingGameModal from '../components/LeavingGameModal';
import tokenService from "../services/token.service.js";
import { useNavigate } from 'react-router-dom'
import ExpeledModal from './ExpeledModal';
import { FaUserFriends } from "react-icons/fa";
import { IoIosSend } from "react-icons/io";
import { Client } from "@stomp/stompjs";
import Modal from 'react-modal';
import JugadorView from '../components/JugadorView.js';

const WaitingModal = forwardRef((props, ref) => {
    const game = props.game;

    const [connectedUsers, setConnectedUsers] = useState(0);
    const [jugadores, setJugadores] = useState([]);
    const [leavingModal, setLeavingModal] = useState(false);
    const usuario = tokenService.getUser();
    const jwt = tokenService.getLocalAccessToken();
    const [connected, setConnected] = useState(null);
    const [expeledView, setExpeledView] = useState(false);
    const [friendList, showFriendList] = useState(false);
    const [friends, setFriends] = useState([]);
    const navigate = useNavigate();
    const [stompClient, setStompClient] = useState(null);
    const [numDesconectados, setNumDesconectados] = useState(0);


    useEffect(() => {
        function fetchPlayers() {
            fetch(
                `/api/v1/partidajugador/players?partidaCode=` + game.codigo,
                {
                    method: "GET"
                }
            )
                .then((response) => response.json())
                .then((data) => {
                    setJugadores(data)
                    setConnectedUsers(data.length)
                    const inTheGame = (jugadores.find(pj => pj.player.id === usuario.id) ? true : false);
                    if (connected && !inTheGame) {
                        setExpeledView(true);
                    }
                    setConnected(inTheGame);
                    setNumDesconectados(data.filter(jugador => jugador.player.isConnected===false).length);
                })
        }
        fetchPlayers();

        const timer = setInterval(() => {
            fetchPlayers();
        }, 2000);
        return () => clearInterval(timer);

    }, [game.codigo, navigate, usuario.id])

    function getGameCreator() {
        if (jugadores.length > 0) {
            const currentCreator = jugadores.find(pj => pj.isCreator === true)
            return currentCreator.player;
        } else {
            return null;
        }


    }

    function getJugadoresEquipo(equipo) {
        if (jugadores.length > 0) {

            return jugadores.filter(jugadorPartida => jugadorPartida.equipo === "EQUIPO" + equipo);
        } else {
            return [];
        }

    }

    function startGame() {
        if (numDesconectados > 0) {
            alert("No se puede comenzar la partida, hay jugadores desconectados. Considere expulsarlos: " + numDesconectados);
        } else {
            if (getJugadoresEquipo(1).length === game.numJugadores / 2 && getJugadoresEquipo(2).length === game.numJugadores / 2) {
                fetch(
                    `/api/v1/partida/${game.codigo}/start`,
                    {
                        method: "PATCH",
                        headers: {
                            Authorization: `Bearer ${jwt}`,
                        },
                    }
                )
                    .then((response) => {
                        if (!response.ok) {
                            return response.text().then((errorMessage) => {
                                throw new Error("Error al empezar la partida: " + errorMessage);
                            });
                        }
                    })
                    .then(() => {

                    })
                    .catch((error) => {
                        alert(error.message);
                    });

            } else {
                alert(`Faltan jugadores para comenzar la partida.`)
            }
        }

    }
    function getFriends() {
        fetch(
            `/api/v1/jugador/amigos?userId=` + usuario.id,
            {
                method: "GET"
            }
        )
            .then((response) => response.text())
            .then((data) => {
                const amigos = JSON.parse(data);
                if (amigos.length !== 0) {
                    const jugadoresEnPartida = jugadores.map(j => j.player.id);
                    const amigosFiltrados = amigos.filter(amigo => !jugadoresEnPartida.includes(amigo.id) && amigo.isConnected);
                    setFriends(amigosFiltrados)
                } else {
                    setFriends([]);
                }

            })
            .catch((message) => alert(message));

        showFriendList(!friendList);

    }

    async function findChatId(friendId) {
        try {
            const response = await fetch(`/api/v1/chat/with/` + friendId, {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                throw new Error("Error al buscar el chat: " + errorMessage);
            }

            const data = await response.json();
            return data.id; // Devuelve el ID del chat
        } catch (error) {
            alert("Error al buscar el chat: " + error.message);
            throw error; // Lanza el error para que la función llamante pueda manejarlo
        }
    }

    async function sendInvitation(friendId) {
        try {
            const chatId = await findChatId(friendId);
            const cliente = new Client({
                brokerURL: "ws://localhost:8080/ws",
                connectHeaders: {
                    Authorization: `Bearer ${jwt}`,
                },
            });

            // Crea una promesa para esperar la conexión STOMP
            const connectPromise = new Promise((resolve, reject) => {
                cliente.onConnect = () => {
                    console.log("Conectado exitosamente");
                    cliente.subscribe(`/topic/chat/${chatId}`);
                    setStompClient(cliente); // Asigna el cliente STOMP una vez conectado
                    resolve(cliente); // Resuelve la promesa con el cliente STOMP
                };

                cliente.onDisconnect = () => {
                    console.log("Desconectado del servidor STOMP");
                };

                cliente.onStompError = (frame) => {
                    console.error("Error de STOMP: ", frame.headers["message"]);
                    console.error("Detalles: ", frame.body);
                    reject(new Error("Error de STOMP: " + frame.headers["message"]));
                };
            });

            cliente.activate(); // Activa el cliente STOMP

            const connectedClient = await connectPromise; // Espera a que el cliente esté completamente conectado

            evtEnviarMensaje(chatId, connectedClient); // Pasa el cliente conectado a evtEnviarMensaje
        } catch (error) {
            console.error("Error en sendInvitation: ", error.message);
        }
    }

    const evtEnviarMensaje = (chatId, connectedClient) => {
        if (connectedClient && connectedClient.connected) {
            connectedClient.publish({
                destination: "/app/mensaje",
                body: JSON.stringify({
                    contenido: `Te invito a la partida {${game.codigo}}`,
                    chat: { id: chatId },
                    remitente: { id: tokenService.getUser().id, username: tokenService.getUser().username },
                    username: { username: tokenService.getUser().username },
                }),
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            });
            console.log("Mensaje enviado");

            connectedClient.deactivate(); // Desactiva el cliente después de enviar el mensaje
            alert("Invitación enviada");
        } else {
            console.error("STOMP aún no está listo o no está conectado");
        }
    };




    return (
        <>
            <div className='cuadro-union'>
                <FaUserFriends
                    style={{ position: 'absolute', right: '10px', width: '30px', height: '30px', cursor: 'pointer' }} onClick={() => { getFriends() }} />
                {friendList && (
                    <div
                        style={{ backgroundColor: 'gray', position: 'absolute', right: '10px', top: '165px', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', borderRadius: '10px', padding: '10px', zIndex: 1000 }}  >
                        <div
                            style={{ overflowY: 'auto', height: '200px', width: '200px' }}
                        >
                            <p style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>Invitar amigos</p>
                            {friends.map((friend, index) => (
                                <div
                                    key={friend.id}
                                    style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '10px' }}
                                >
                                    <img
                                        style={{
                                            height: '40px',
                                            width: '40px',
                                            borderRadius: '50%',
                                            marginRight: '10px',
                                        }}
                                        src={friend.photo}
                                        alt="Foto de perfil del usuario"
                                    />
                                    <p style={{ margin: 0, flex: 1, textAlign: 'left' }}>
                                        {friend.userName}
                                    </p>

                                    <IoIosSend
                                        style={{
                                            width: '20px',
                                            height: '20px',
                                            cursor: 'pointer',
                                            marginLeft: '10px',
                                            color: 'black',
                                        }}
                                        onClick={() => sendInvitation(friend.id)}
                                    />
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                <IoCloseCircle style={{ width: 30, height: 30, cursor: "pointer", position: 'absolute' }} onClick={() => setLeavingModal(true)} />
                <div style={{ textAlign: 'center' }}>
                    <h1>Partida {game.codigo}</h1>
                    <h2>{connectedUsers}/{game.numJugadores} Jugadores conectados</h2>
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', marginRight: 10 }}>
                        <p style={{ margin: 0 }}>{game.puntosMaximos} puntos</p>
                        {game.conFlor && <TbFlower style={{ verticalAlign: 'middle', marginLeft: 5 }} />}
                        {!game.conFlor && <TbFlowerOff style={{ marginLeft: 5 }} />}
                    </div>
                    <div style={{ columnCount: 2, textAlign: 'center' }}>
                        <div style={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            textAlign: 'center'
                        }}>
                            <h2 style={{ color: 'blue' }}>Equipo 1</h2>
                            <EquipoView
                                partida={game}
                                jugadores={getJugadoresEquipo(1)}
                                gameCreator={getGameCreator()}
                            />
                        </div>

                        <div style={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            textAlign: 'center'
                        }}>
                            <h2 style={{ color: 'red' }}>Equipo 2</h2>
                            <EquipoView
                                partida={game}
                                jugadores={getJugadoresEquipo(2)}
                                gameCreator={getGameCreator()}
                            />

                        </div>

                    </div>
                    {getGameCreator() && getGameCreator().id === usuario.id && (
                        <div style={{
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            marginTop: 20,
                            width: '100%',
                        }}>
                            <button
                                onClick={() => startGame()}
                                className="button"
                                style={{ color: 'darkgreen', width: '20%', left: 0 }}
                            >
                                Comenzar partida
                            </button>
                        </div>
                    )}
                </div>
            </div>
            <LeavingGameModal
                modalIsOpen={leavingModal}
                setIsOpen={setLeavingModal}
            />
            <ExpeledModal
                modalIsOpen={expeledView} />
        </>
    );




});
export default WaitingModal;