import React, { useState, useEffect } from 'react';
import PartidaView from './PartidaView'; // Asegúrate de importar PartidaView
import tokenService from '../services/token.service';

const jwt = tokenService.getLocalAccessToken()
function RenderContent({ contenido }) {
  const [res, setRes] = useState(contenido);
  const urlActual = window.location.href;

  useEffect(() => {
    if (contenido.search(/{[A-Z0-9]{5}}/) !== -1 && !urlActual.includes("partidaCode=")) {
      let codigo = contenido.match(/{[A-Z0-9]{5}}/)[0].slice(1, -1);
      fetch("/api/v1/partida/search?codigo=" + codigo, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        }
      })
        .then((response) => response.json())
        .then((data) => {
          if(data.statusCode===404){
            setRes(<p>Partida no encontrada</p>);
          }else{
            setRes(<PartidaView game={data} interfaz='chat' />);
          }
          
        })
        .catch((message) => {
          setRes(<p>Error al cargar la invitación a la partida {codigo}</p>);
        });
    } else {
      setRes(<p>{contenido}</p>);
    }
  }, [contenido]);

  return (
    <div>{res}</div>
  );
}

export default RenderContent;