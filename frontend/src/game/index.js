import { useState, useEffect } from "react";
import tokenService from "../services/token.service.js";
import WaitingModal from "../game/WaitingModal.js"
import PlayingModal from "../manos/PlayingModal.js"
import FinishedModal from "./FinishedModal";
import { Client } from "@stomp/stompjs";
import MessageList from "../components/MessageList.js";
import InputContainer from "../components/InputContainer.js";

const jwt = tokenService.getLocalAccessToken();

export default function Game() {
    const currentUrl = window.location.href;
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const codigo = currentUrl.split('partidaCode=')[1].substring(0, 5);
    const [game, setGame] = useState(null);

    const [mensajes, setMensajes] = useState([]);
    const [stompClient, setStompClient] = useState(null);
    const [mensaje,setMensaje] = useState("");


    useEffect(() => {
        let intervalId;
        function fetchGame() {
            fetch(
                '/api/v1/partida/search?codigo=' + codigo,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                    },
                }
            )
                .then((response) => response.json())
                .then((data) => {
                    setGame(data);
                    if (data.estado === 'FINISHED') {
                        clearInterval(intervalId)
                    }
                })
        }

        fetchGame();
        intervalId = setInterval(fetchGame, 1000);

        return () => clearInterval(intervalId);
    }, [codigo])

    /*Código para el chat en la partida*/

    useEffect(() => {
        connectToChat();
    }, []);


    function connectToChat(){
        const cliente = new Client({
            brokerURL: "ws://localhost:8080/ws",
            connectHeaders: {
                Authorization: `Bearer ${jwt}`,
            },
        });

        cliente.onConnect = () => {
            console.log("Conectado exitosamente al chat de partida " + codigo);
            cliente.subscribe(`/topic/gamechat/${codigo}`, (mensaje) => {
                console.log("Mensaje recibido: ", mensaje.body);
                console.log("Mensaje recibido: ", mensaje.body);
                const nuevoMensaje = JSON.parse(mensaje.body);
                setMensajes((prevMensajes) => [...prevMensajes, nuevoMensaje]);
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
    }

    const evtEnviarMensaje = () => {
        if (stompClient && stompClient.connected && mensaje.trim() !== "") {
            stompClient.publish({
                destination: "/app/mensajepartida",
                body: JSON.stringify({
                    contenido: mensaje,
                    chat: { id: game.id,
                            partida: {
                                id: game.id,
                                codigo: codigo
                            }
                     },
                    remitente: { id: tokenService.getUser().id, username: tokenService.getUser().username,lastConnection: new Date() },
                    username: { username: tokenService.getUser().username },
                }),
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            });
            console.log("Mensaje enviado");

            setMensaje("");

        } else {
            console.error("STOMP aún no está listo o no está conectado");
        }
    };




    return (
        <div>
            {game && game.estado === 'WAITING' &&

                <WaitingModal game={game} />}

            {game && game.estado === 'ACTIVE' &&
                <PlayingModal game={game} />
            }

            {game && game.estado === 'FINISHED' &&
                <FinishedModal game={game} />
            }

            {game && game.estado !== 'FINISHED' &&
                <div>
                    <h1>Chat de la partida</h1>
                    <MessageList mensajes={mensajes} userId={tokenService.getUser().id} />
                    <InputContainer
                        mensaje={mensaje}
                        setMensaje={setMensaje}
                        evtEnviarMensaje={evtEnviarMensaje}
                    />

                </div>
            }



        </div>


    );
}