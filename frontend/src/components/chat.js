import React, { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

const Chat = () => {
  // Estados para manejar mensajes, conexión y cliente STOMP
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState("");
  const [stompClient, setStompClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {
    // Configuración del cliente STOMP con SockJS
    const socket = new SockJS("http://localhost:8080/"); // Cambia la URL si tu backend usa otro puerto/contexto
    const client = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str), // Habilitar logs para depuración
      onConnect: () => {
        console.log("Conectado al WebSocket");
        setIsConnected(true); // Cambiar estado a conectado

        // Suscripción a mensajes
        client.subscribe("/user/jugador1/queue/mensajes", (message) => {
          const body = JSON.parse(message.body);
          setMessages((prev) => [...prev, body]); // Agregar nuevo mensaje a la lista
        });
      },
      onStompError: (frame) => {
        console.error("Error STOMP: ", frame.headers["message"]);
      },
    });

    client.activate(); // Activar cliente STOMP
    setStompClient(client);

    return () => {
      if (client) client.deactivate(); // Limpiar conexión al desmontar componente
    };
  }, []);

  // Manejar envío de mensajes
  const sendMessage = () => {
    if (isConnected && stompClient) {
      stompClient.publish({
        destination: "/app/mensaje", // Endpoint configurado en tu backend
        body: JSON.stringify({
          remitente: "jugador1", // Ajusta el remitente dinámicamente si es necesario
          contenido: message,
        }),
      });
      setMessages((prev) => [...prev, { remitente: "Tú", contenido: message }]); // Añadir mensaje al chat local
      setMessage(""); // Limpiar input
    } else {
      console.error("El cliente STOMP no está conectado. No se puede enviar el mensaje.");
    }
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <h2>Chat</h2>
      <div
        style={{
          border: "1px solid #ccc",
          borderRadius: "5px",
          padding: "10px",
          height: "300px",
          overflowY: "scroll",
          marginBottom: "10px",
        }}
      >
        {messages.map((msg, idx) => (
          <div key={idx}>
            <strong>{msg.remitente}:</strong> {msg.contenido}
          </div>
        ))}
      </div>
      <input
        type="text"
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        placeholder="Escribe un mensaje..."
        style={{
          width: "80%",
          padding: "10px",
          borderRadius: "5px",
          border: "1px solid #ccc",
          marginRight: "10px",
        }}
      />
      <button
        onClick={sendMessage}
        style={{
          padding: "10px 15px",
          borderRadius: "5px",
          border: "none",
          backgroundColor: "#007bff",
          color: "white",
          cursor: "pointer",
        }}
      >
        Enviar
      </button>
    </div>
  );
};

export default Chat;
