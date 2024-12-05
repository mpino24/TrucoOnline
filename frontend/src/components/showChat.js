import React, { forwardRef,useState, useEffect } from "react";
import tokenService from "../services/token.service";
import { Client } from '@stomp/stompjs';

const Chat= forwardRef((props, ref) => {

  const [stompClient,setStompClient] = useState(null);
  const [mensajes,setMensajes]= useState([]);
  const [mensaje,setMensaje]= useState('');

  useEffect(() => {
    setMensaje('');
    const cliente = new Client({
      brokerURL :'ws://localhost:8080'
    })

    cliente.onConnect = () =>{
      cliente.subscribe(`/topic/chat/${props.idChat}`,(mensaje) =>{ //¿?
        const nuevoMensaje = JSON.parse(mensaje.body)
        setMensajes((p) => [...p,nuevoMensaje])
      }) 
    }

    cliente.activate()
    setStompClient(cliente);

    return () =>{
      if(cliente){
        cliente.deactivate();
      }
    }



  }, []);


  const evtEnviarMensaje = () =>{
    if(stompClient!==null && mensaje !==''){
      stompClient.publish({
        destination:'/app/mensaje', //¿?¿?¿?
        body: JSON.stringify({
          contenido:mensaje,
          chat: { id: props.idChat }
        })

      })
      setMensaje('')
    }

  }

  const handleEnviar = () => {
    if (mensaje.trim() !== "") {
      evtEnviarMensaje(mensaje); // Llama a la función pasada como prop
      setMensajes((prev) => [...prev, { contenido: mensaje }]); // Añade el mensaje localmente
      setMensaje(""); // Limpia el input
    }
  };

  return (
    <>
    {mensajes.map((msg,i) => (
      <p key={i}>
        {msg.contenido}
      </p>


    ))}

          <input
        type="text"
        value={mensaje}
        onChange={(e) => setMensaje(e.target.value)} // Actualiza el estado del mensaje
        placeholder="Escribe un mensaje..."
      />

      {/* Botón para enviar el mensaje */}
      <button onClick={handleEnviar}>Enviar</button>
    </>
  );
});

export default Chat;
