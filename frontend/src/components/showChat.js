import React, { forwardRef, useState, useEffect, useRef, useLayoutEffect } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import { Client } from "@stomp/stompjs";
import "./Chat.css";
import RenderContent from "./RenderContent";

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

  const [mensaje, setMensaje] = useState("");

  // Ref para el contenedor de mensajes
  const messagesEndRef = useRef(null);
  const messagesContainerRef = useRef(null);

  // Efecto para desplazar automáticamente al final cuando los mensajes cambien
  function moveScroll() {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }

  useEffect(() => {
    const observer = new MutationObserver(() => {
      moveScroll();
    });

    if (messagesContainerRef.current) {
      observer.observe(messagesContainerRef.current, { childList: true, subtree: true });
    }

    return () => {
      if (messagesContainerRef.current) {
        observer.disconnect();
      }
    };
  }, [mensajes]);


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
  }, [jwt, props.idChat]);

  const evtEnviarMensaje = () => {
    if (stompClient && stompClient.connected && mensaje.trim() !== "") {
      stompClient.publish({
        destination: "/app/mensaje",
        body: JSON.stringify({
          contenido: mensaje,
          chat: { id: props.idChat },
          remitente: { id: tokenService.getUser().id, username: tokenService.getUser().username },
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

  const handleEnviar = () => {
    evtEnviarMensaje();
  };

  const formatFecha = (fecha) => {
    const date = new Date(fecha);
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
        alignItems: "stretch",
        height: "85vh",
      }}
    >
      <div
        className="messages-container"
        style={{
          flexGrow: 1,
          overflowY: "auto",
          padding: "10px",
          
        }}
        ref={messagesContainerRef}
      >
        {mensajes.map((msg, i) => {
          return (
            msg.remitente.id === user.id ? (
              <div key={i} className="own-message" style={{ display: 'flex', flexDirection: 'column'}}>
                <RenderContent contenido={msg.contenido} />
                <p style={{fontSize:10 ,color:'gray'}}>{formatFecha(msg.fechaEnvio)}</p>
              </div>
            ) : (
              <>
                <div key={i}>{msg.remitente.username}</div>
                <div key={i} className="other-message">
                  <RenderContent contenido={msg.contenido} style={{ display: 'flex', flexDirection: 'column' }} />
                  <p style={{fontSize:10 ,color:'gray'}}>{formatFecha(msg.fechaEnvio)}</p>
                </div>
              </>
            )
          );
        }
        )}
        <div ref={messagesEndRef} />


      </div>

      <div className="input-container">
        <input
          type="text"
          value={mensaje}
          onChange={(e) => setMensaje(e.target.value)}
          placeholder="Escribe un mensaje..."
          className="input-text"
          onKeyPress={(e) => {
            if (e.key === 'Enter') {
              handleEnviar();
            }
          }}
        />
        <button onClick={handleEnviar} className="btn-send">
          Enviar
        </button>
      </div>
    </div>
  );
});

export default Chat;
