import React, { useRef, useEffect } from "react";
import PropTypes from "prop-types";
import RenderContent from "./RenderContent";
import "./Chat.css";

const MessageList = ({ mensajes, userId }) => {
  const messagesEndRef = useRef(null);

  // Desplazar automáticamente al final cuando los mensajes cambien
  const moveScroll = () => {
    const container = document.querySelector(".messages-container"); // Selecciona el contenedor
    if (container) {
      container.scrollTop = container.scrollHeight; // Ajusta el scroll manualmente
    }
  };
  // Ejecutar moveScroll cuando los mensajes cambien
  useEffect(() => {
    console.log("Mensajes recibidos en MessageList:", mensajes);
    moveScroll(); // Asegura el scroll cuando los mensajes cambien
  }, [mensajes]);

  const formatFecha = (fecha) => {
    const date = new Date(fecha);
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
    })}`;
  };

  return (
    <div
      className="messages-container"
      style={{ flexGrow: 1, overflowY: "auto", padding: "10px" }}
    >
      {mensajes.map((msg, i) => (
        msg.remitente.id === userId ? (
          <div key={i} className="own-message">
            <RenderContent contenido={msg.contenido} />
            <p style={{ fontSize: 10, color: "gray" }}>{formatFecha(msg.fechaEnvio)}</p>
          </div>
        ) : (
          <div key={i}>
            <div>{msg.remitente.username}</div>
            <div className="other-message">
              <RenderContent contenido={msg.contenido} />
              <p style={{ fontSize: 10, color: "gray" }}>{formatFecha(msg.fechaEnvio)}</p>
            </div>
          </div>
        )
      ))}
      {/* Referencia para el desplazamiento automático */}
      <div ref={messagesEndRef} />
    </div>
  );
};

MessageList.propTypes = {
  mensajes: PropTypes.arrayOf(
    PropTypes.shape({
      remitente: PropTypes.shape({
        id: PropTypes.number.isRequired,
        username: PropTypes.string.isRequired,
      }).isRequired,
      contenido: PropTypes.string.isRequired,
      fechaEnvio: PropTypes.string.isRequired,
    }).isRequired
  ).isRequired,
  userId: PropTypes.number.isRequired,
};

export default MessageList;
