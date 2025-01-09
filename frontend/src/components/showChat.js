import React, { forwardRef, useState, useEffect, useRef } from "react";
import tokenService from "../services/token.service";
import { Client } from "@stomp/stompjs";
import "./Chat.css";
import MessageList from "./MessageList";
import { IoCloseCircle } from "react-icons/io5";
import InputContainer from "./InputContainer";
const Chat = forwardRef((props, ref) => {
  const jwt = tokenService.getLocalAccessToken();
  const user = tokenService.getUser();
  const [stompClient, setStompClient] = useState(null);

  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [mensajes, setMensajes] = useState([]);
  const [mensaje, setMensaje] = useState("");

  function fetchMensajesIniciales() {
    fetch('api/v1/chat/' + props.idChat, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.length > 0) {
            setMensajes(data);
        }
      })
      .catch((error) => {
        console.error(error);
        alert("Hubo un problema al cargar los mensajes.");
      });
  }

  function updateChatLastConnection(){
    fetch('api/v1/chat/' + props.idChat + '/updatetime', {
      method: 'PATCH',
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("No se pudo actualizar la conexión.");
        }else{
          console.log("Actualizada última fecha de conexión al chat");
        }
      })
      .catch((error) => {
        console.error(error);
        alert("Hubo un problema al actualizar la conexión.");
      });
  }

  useEffect(() => {
    fetchMensajesIniciales();
    updateChatLastConnection();
    const timer = setInterval(() => {
      updateChatLastConnection();
    }, 60000);
    return () => clearInterval(timer);
    
  }, []);


  // Ref para el contenedor de mensajes
  const messagesEndRef = useRef(null);
  const messagesContainerRef = useRef(null);

  // Efecto para desplazar automáticamente al final cuando los mensajes cambien
  function moveScroll() {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }


  useEffect(() => {
    const cliente = new Client({
      brokerURL: "ws://localhost:8080/ws",
      connectHeaders: {
        Authorization: `Bearer ${jwt}`,
      },
    });

    cliente.onConnect = () => {
      console.log("Conectado exitosamente a " + props.idChat);
      cliente.subscribe(`/topic/chat/${props.idChat}`, (mensaje) => {
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

  const handleRemoveFriend = (friendId) => {
    fetch(`/api/v1/jugador/isFriend/${friendId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("No se pudo eliminar al amigo.");
        }
        props.setChatVisible(false);

      })
      .catch((error) => {
        console.error(error);
        alert("Hubo un problema al eliminar al amigo.");

      });
  };


  return (
    <>
    <IoCloseCircle style={{ width: 30, height: 30, cursor: "pointer", position: 'absolute',top:10,left:10, zIndex:1000 }} onClick={() => props.setChatVisible(false)} />
      <h1 style={{ fontSize: 30, textAlign: "center", display: "flex", justifyContent: "center", alignItems: "center", gap: "10px",marginTop: "10px",position: "relative" }}>
        {props.player?.userName || "Cargando..."}
        {true && (
          <button
            style={{
              background: "#ff4d4f",
              color: "white",
              border: "none",
              borderRadius: "5px",
              padding: "5px 10px",
              cursor: "pointer",
              fontSize: "14px",
              position: "absolute",
              right: "0", 
              marginRight: "10px",
            }}
            onClick={() => setShowConfirmModal(true)}
          >
            Eliminar Amigo
          </button>
        )}
      </h1>
      <hr></hr>
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          alignItems: "stretch",
          height: "85vh",
          paddingBottom: "0px",
        }}
      >
     
        <MessageList mensajes={mensajes} userId={user.id} />
        <InputContainer mensaje={mensaje} setMensaje={setMensaje} evtEnviarMensaje={evtEnviarMensaje} />
      </div>
      {showConfirmModal && (
        <div
          style={{
            position: "fixed",
            top: "0",
            left: "0",
            width: "100%",
            height: "100%",
            backgroundColor: "rgba(0, 0, 0, 0.5)",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            zIndex: 1000,
          }}
        >
          <div
            style={{
              backgroundColor: "white",
              padding: "20px",
              borderRadius: "8px",
              textAlign: "center",
              boxShadow: "0px 4px 6px rgba(0, 0, 0, 0.1)",
            }}
          >
            <p>¿Estás seguro de que quieres eliminar este amigo?</p>
            <div style={{ display: "flex", justifyContent: "center", gap: "10px" }}>
              <button
                onClick={() => {
                  handleRemoveFriend(props.player.id);
                  setShowConfirmModal(false);
                }}
                style={{
                  background: "#ff4d4f",
                  color: "white",
                  border: "none",
                  padding: "10px 20px",
                  borderRadius: "5px",
                  cursor: "pointer",
                }}
              >
                Sí
              </button>
              <button
                onClick={() => setShowConfirmModal(false)}
                style={{
                  background: "#ccc",
                  color: "black",
                  border: "none",
                  padding: "10px 20px",
                  borderRadius: "5px",
                  cursor: "pointer",
                }}
              >
                No
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
});

export default Chat;
