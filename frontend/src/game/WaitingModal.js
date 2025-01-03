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
    const user = tokenService.getUser();
    const navigate = useNavigate();
    const [stompClient, setStompClient] = useState(null);
    const [mensaje, setMensaje] = useState("");
    const [chatId, setChatId] = useState(null);


    useEffect(() => {
        function fetchPlayers() {
            fetch(
                `/api/v1/partidajugador/players?partidaCode=` + game.codigo,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                        Accept: "application/json",
                        "Content-Type": "application/json",
                      }
                }
            )
                .then((response) => response.json())
                .then((data) => {
                    setJugadores(data)
                    setConnectedUsers(data.length)
                    const isConnected = (jugadores.find(pj => pj.player.id === usuario.id) ? true : false);
                    if (connected && !isConnected) {
                        setExpeledView(true);
                    }
                    setConnected(isConnected);
                })
        }
        fetchPlayers();

        const intervalId = setInterval(fetchPlayers, 500);

        return () => clearInterval(intervalId)
    }, [connected, game.codigo, jugadores, navigate, usuario.id])

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
            alert(`Faltan jugadores para comenzar la partida. Equipo 1:${getJugadoresEquipo(1).length}. Equipo 2:${getJugadoresEquipo(2).length}`)
        }

    }
    function getFriends() {
        fetch(
            `/api/v1/jugador/amigos?userId=` + user.id,
            {
                method: "GET"
            }
        )
            .then((response) => response.text())
            .then((data) => {
                const amigos = JSON.parse(data);
                if (amigos.length !== 0) {
                    const jugadoresEnPartida = jugadores.map(j => j.player.id);
                    const amigosFiltrados = amigos.filter(amigo => !jugadoresEnPartida.includes(amigo.id));
                    setFriends(amigosFiltrados)
                }else{
                    setFriends([]);
                }

            })
            .catch((message) => alert(message));

        showFriendList(!friendList);

    }

    function findChatId(friendId) {
        fetch(
            `/api/v1/chat/with/` + friendId,
            {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            }
        ).then((response) => {
            if (!response.ok) {
                return response.text().then((errorMessage) => {
                    throw new Error("Error al buscar el chat: " + errorMessage);
                });
            } else {
                return response.json();
            }
        }).then((data) => {
            setChatId(data.id);
        }).catch((message) => alert("Error al buscar el chat: " + message));
    }


    function sendInvitation(friendId) {
        findChatId(friendId);

        const cliente = new Client({
            brokerURL: "ws://localhost:8080/ws",
            connectHeaders: {
                Authorization: `Bearer ${jwt}`,
            },
        });

        cliente.onConnect = () => {
            console.log("Conectado exitosamente");
            cliente.subscribe(`/topic/chat/${chatId}`);
            setStompClient(cliente);

            evtEnviarMensaje();
        };

        cliente.onDisconnect = () => {
            console.log("Desconectado del servidor STOMP");
        };

        cliente.onStompError = (frame) => {
            console.error("Error de STOMP: ", frame.headers["message"]);
            console.error("Detalles: ", frame.body);
        };

        cliente.activate();
        

        

    }

    const evtEnviarMensaje = () => {
        setMensaje("Te invito a la partida {" + game.codigo + "}");
        if (stompClient && stompClient.connected) {
            stompClient.publish({
                destination: "/app/mensaje",
                body: JSON.stringify({
                    contenido: mensaje,
                    chat: { id: chatId },
                    remitente: { id: tokenService.getUser().id, username: tokenService.getUser().username },
                    username: { username: tokenService.getUser().username },
                }),
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            });
            console.log("Mensaje enviado");

            stompClient.deactivate();

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
                            {friends.map((friend) => (
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