import React, { forwardRef, useState, useEffect } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import { Client } from "@stomp/stompjs";
import "./Chat.css";

const Chat = forwardRef((props, ref) => {
  const jwt = tokenService.getLocalAccessToken();
  const user = tokenService.getUser();
  const [stompClient, setStompClient] = useState(null);
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [mensajes, setMensajes] = useFetchState(
    [],
    `/api/v1/chat/${props.idChat}`,
    jwt,
    setMessage,
    setVisible
  );


  const [mensaje, setMensaje] = useState('');

  useEffect(() => {
    const cliente = new Client({
      brokerURL: "ws://localhost:8080/ws",
      connectHeaders: {
        Authorization: `Bearer ${jwt}`,
      },
    });

    cliente.onConnect = () => {
      console.log("Conectado exitosamente");
      cliente.subscribe(`/topic/chat/${props.idChat}`, (mensaje) => {
        const nuevoMensaje = JSON.parse(mensaje.body);
        setMensajes((prevMensajes) => [...prevMensajes, nuevoMensaje]);
      });
    };

    cliente.onDisconnect = () => {
      console.log("Desconectado del servidor STOMP");
    };

    cliente.onStompError = (frame) => {
      console.error('Error de STOMP: ', frame.headers['message']);
      console.error('Detalles: ', frame.body);
    };

    cliente.activate();
    setStompClient(cliente);

    return () => {
      if (cliente) {
        cliente.deactivate();
      }
    };
  }, [jwt, props.idChat]);

  const evtEnviarMensaje = () => {
    if (stompClient && stompClient.connected && mensaje.trim() !== "") {
      stompClient.publish({
        destination: "/app/mensaje",
        body: JSON.stringify({
          contenido: mensaje,
          chat: { id: props.idChat },
          remitente: {id: tokenService.getUser().id},
        }),
        headers: {
          Authorization: `Bearer ${jwt}`
        }
      });
      console.log("Mensaje enviado");
      setMensaje("");
    } else {
      console.error("STOMP aún no está listo o no está conectado");
    }
  };

  const handleEnviar = () => {
    evtEnviarMensaje();
  };

  return (
    <>
    <div className="messages-container">
    {mensajes.map((msg, i) =>
    msg.remitente.id === user.id ? (
      <div key={i} className="own-message">
        {msg.contenido}
      </div>
    ) : (
      <>
      <div key={i} >{msg.remitente.username}</div>
      <div key={i} className="other-message">
        {msg.contenido}
      </div>
      </>
    )
    )}
  </div>
    <div className="input-container">
      <input
        type="text"
        value={mensaje}
        onChange={(e) => setMensaje(e.target.value)} 
        placeholder="Escribe un mensaje..."
        className="input-text"
      />

      <button onClick={handleEnviar} className="btn-send" >Enviar</button>
    </div>
    </>

    
  );
});

export default Chat;
